/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.http.client.ClientProtocolException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;

public class VvMigrationWorkerTest
{
    private static final byte[][] chunks = {
        {1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {2, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9},
        {3, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
        {4, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 9, 8, 7, 6, 5, 4, 3, 2, 1, 0},
    };

    private List<byte[]> transferredChunks;
    private VvMigrationWorker worker;
    private VirtualVehicle virtualVehicle;
    private Transaction transaction;
    private Session session;
    private VvRteRepository vvRepository;
    private CommunicationService com;
    private VirtualVehicleMigrator migrator;
    private RealVehicle realVehicle;

    @BeforeMethod
    public void setUp() throws ClientProtocolException, IOException, ArchiveException
    {
        transferredChunks = new ArrayList<byte[]>();

        realVehicle = mock(RealVehicle.class);
        when(realVehicle.getName()).thenReturn("rv01");

        virtualVehicle = mock(VirtualVehicle.class);
        when(virtualVehicle.getName()).thenReturn("vv01");
        when(virtualVehicle.getMigrationDestination()).thenReturn(realVehicle);
        when(virtualVehicle.getState()).thenReturn(VirtualVehicleState.MIGRATION_AWAITED);

        transaction = mock(Transaction.class);

        session = mock(Session.class);
        when(session.beginTransaction()).thenReturn(transaction);

        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.getSessionFactory()).thenReturn(sessionFactory);

        vvRepository = mock(VvRteRepository.class);
        // when(vvRepository.getSession()).thenReturn(session);

        com = mock(CommunicationService.class);
        when(com.transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class))).thenAnswer(
            new Answer<CommunicationResponse>()
            {
                @Override
                public CommunicationResponse answer(InvocationOnMock invocation) throws Throwable
                {
                    Object[] args = invocation.getArguments();
                    // RealVehicle rv = (RealVehicle) args[0];
                    // Connector conn = (Connector) args[1];
                    byte[] data = (byte[]) args[2];
                    transferredChunks.add(data);

                    CommunicationResponse response = new CommunicationResponse();
                    response.setContent(("storage" + data[0]).getBytes());
                    response.setStatus(Status.OK);
                    return response;
                }
            });

        migrator = mock(VirtualVehicleMigrator.class);
        when(migrator.findFirstChunk(virtualVehicle)).thenReturn(chunks[0]);
        when(migrator.findChunk(any(VirtualVehicle.class), any(String.class), anyInt())).thenAnswer(
            new Answer<byte[]>()
            {
                @Override
                public byte[] answer(InvocationOnMock invocation) throws Throwable
                {
                    Object[] args = invocation.getArguments();
                    VirtualVehicle vv = (VirtualVehicle) args[0];
                    String storageName = (String) args[1];
                    Integer chunkNumber = (Integer) args[2];

                    if (vv == null || storageName == null || !storageName.startsWith("storage") || chunkNumber == null)
                    {
                        throw new IOException("Invalid chunk specification found!");
                    }

                    int storageIndex = Integer.parseInt(storageName.substring(7));
                    return storageIndex > chunks.length - 1 ? null : chunks[storageIndex];
                }
            });

        worker = new VvMigrationWorker(virtualVehicle, vvRepository, com, migrator, session);
    }

    @Test
    public void shouldRunMigration() throws ClientProtocolException, IOException, InterruptedException
    {
        worker.start();
        worker.join();
        assertThat(worker.getName()).isEqualTo(
            "MIG-" + virtualVehicle.getName() + "-" + virtualVehicle.getMigrationDestination().getName());

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, times(4)).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));

        assertThat(transferredChunks.size())
            .overridingErrorMessage("The number of transferred chunks must be %d but is %d",
                chunks.length, transferredChunks.size())
            .isEqualTo(chunks.length);

        for (int k = 0; k < chunks.length; ++k)
        {
            assertThat(transferredChunks.get(k))
                .overridingErrorMessage("Verifying transferred chunk number %d", k)
                .isNotNull().isEqualTo(chunks[k]);
        }

        verify(virtualVehicle, times(1)).setState(VirtualVehicleState.MIGRATING);
        verify(session, times(2)).beginTransaction();
        verify(transaction, times(2)).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    public void shouldContinueInterruptedMigration() throws ClientProtocolException, IOException, InterruptedException
    {
        reset(virtualVehicle);
        when(virtualVehicle.getName()).thenReturn("vv01");
        when(virtualVehicle.getMigrationDestination()).thenReturn(realVehicle);
        when(virtualVehicle.getState()).thenReturn(VirtualVehicleState.MIGRATION_INTERRUPTED);

        worker.start();
        worker.join();
        assertThat(worker.getName()).isEqualTo(
            "MIG-" + virtualVehicle.getName() + "-" + virtualVehicle.getMigrationDestination().getName());

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, times(4)).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));

        assertThat(transferredChunks.size())
            .overridingErrorMessage("The number of transferred chunks must be %d but is %d",
                chunks.length, transferredChunks.size())
            .isEqualTo(chunks.length);

        for (int k = 0; k < chunks.length; ++k)
        {
            assertThat(transferredChunks.get(k))
                .overridingErrorMessage("Verifying transferred chunk number %d", k)
                .isNotNull().isEqualTo(chunks[k]);
        }

        verify(virtualVehicle, times(1)).setState(VirtualVehicleState.MIGRATING);
        verify(session, times(2)).beginTransaction();
        verify(transaction, times(2)).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    public void shouldRunMigrationAndAwaitCompletion()
        throws ClientProtocolException, IOException, InterruptedException
    {
        worker.start();
        worker.awaitCompetion();

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, times(4)).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));

        assertThat(transferredChunks.size())
            .overridingErrorMessage("The number of transferred chunks must be %d but is %d",
                chunks.length, transferredChunks.size())
            .isEqualTo(chunks.length);

        for (int k = 0; k < chunks.length; ++k)
        {
            assertThat(transferredChunks.get(k))
                .overridingErrorMessage("Verifying transferred chunk number %d", k)
                .isNotNull().isEqualTo(chunks[k]);
        }

        verify(virtualVehicle).setState(VirtualVehicleState.MIGRATING);
        //TODO verify(virtualVehicle).setState(VirtualVehicleState.MIGRATION_COMPLETED);
        verify(session, times(2)).beginTransaction();
        verify(transaction, times(2)).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    public void shouldAbortMigrationOnMissingFirstChunk()
        throws ClientProtocolException, IOException, InterruptedException, ArchiveException
    {
        reset(migrator);
        when(migrator.findFirstChunk(virtualVehicle)).thenReturn(null);

        worker.start();
        worker.awaitCompetion();

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, never()).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));
        verify(session, times(2)).beginTransaction();
        verify(transaction).commit();
        verify(transaction).rollback();
    }

    @Test
    public void shouldAbortMigrationOnMissingSecondChunk()
        throws ClientProtocolException, IOException, InterruptedException, ArchiveException
    {
        reset(migrator);
        when(migrator.findFirstChunk(virtualVehicle)).thenReturn(chunks[0]);
        when(migrator.findChunk(any(VirtualVehicle.class), any(String.class), anyInt())).thenReturn(null);

        worker.start();
        worker.awaitCompetion();

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, times(1)).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));
        verify(session, times(2)).beginTransaction();
        verify(transaction).commit();
        verify(transaction).rollback();
    }

    @Test
    public void shouldAbortMigrationOnMissingMigrationDestination()
        throws ClientProtocolException, IOException, InterruptedException, ArchiveException
    {
        reset(virtualVehicle);
        when(virtualVehicle.getName()).thenReturn("vv01");
        when(virtualVehicle.getState()).thenReturn(VirtualVehicleState.MIGRATION_AWAITED);

        worker.run();

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, never()).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));
        verify(session, never()).beginTransaction();
        verify(transaction, never()).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    public void shouldSetVvStateToInterruptedIfCommunicationFails()
        throws ClientProtocolException, IOException, InterruptedException, ArchiveException
    {
        CommunicationResponse response = mock(CommunicationResponse.class);
        when(response.getStatus()).thenReturn(Status.NOT_OK);

        reset(com);
        when(com.transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class))).thenReturn(response);

        worker.start();
        worker.awaitCompetion();

        verify(virtualVehicle, times(1)).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, times(1)).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));
        verify(session, times(2)).beginTransaction();
        verify(transaction, times(2)).commit();
        verify(transaction, never()).rollback();
    }

    @Test
    public void shouldAbortMigrationOnWrongVirtualVehicleState()
        throws ClientProtocolException, IOException, InterruptedException, ArchiveException
    {
        reset(virtualVehicle);
        when(virtualVehicle.getName()).thenReturn("vv01");
        when(virtualVehicle.getMigrationDestination()).thenReturn(realVehicle);
        when(virtualVehicle.getState()).thenReturn(VirtualVehicleState.INIT);

        worker.run();

        verify(virtualVehicle, never()).setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
        verify(com, never()).transfer(any(RealVehicle.class), any(Connector.class), any(byte[].class));
        verify(session, never()).beginTransaction();
        verify(transaction, never()).commit();
        verify(transaction, never()).rollback();
    }
}
