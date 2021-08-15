// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//
// This program is free software; you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation; either version 2 of the License, or
// (at your option) any later version.
//
// This program is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with this program; if not, write to the Free Software Foundation,
// Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.

package cpcc.vvrte.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.commons.Messages;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import cpcc.core.entities.RealVehicle;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.js.JavascriptService;
import cpcc.vvrte.services.js.JavascriptWorker;
import cpcc.vvrte.services.js.JavascriptWorkerStateListener;

/**
 * VehicleLauncherTest
 */
public class VehicleLauncherTest
{
    private static final int INVALID_VV_ID = 31337;

    private Date currentDate;
    private JavascriptWorkerStateListener jobListener = null;
    private VirtualVehicle vehicle;
    private VirtualVehicleLauncherImpl launcher;
    private Session session;
    private HibernateSessionManager sessionManager;
    private Logger logger;
    private TimeService timeService;
    private VirtualVehicle vehicle2;
    private RealVehicle groundStation;
    private JobService jobService;

    @BeforeEach
    public void setUp() throws IOException
    {
        currentDate = new Date();

        timeService = mock(TimeService.class);
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return currentDate;
            }
        });

        logger = mock(Logger.class);
        jobService = mock(JobService.class);

        String vvProgramFileName = "simple-vv.js";
        InputStream scriptStream = VehicleLauncherTest.class.getResourceAsStream(vvProgramFileName);
        String program = IOUtils.toString(scriptStream, "UTF-8");

        vehicle = spy(new VirtualVehicle());
        vehicle.setId(1001);
        vehicle.setApiVersion(1);
        vehicle.setCode(program);
        vehicle.setState(VirtualVehicleState.INIT);
        vehicle.setName("rv001");

        vehicle2 = mock(VirtualVehicle.class);
        when(vehicle2.getId()).thenReturn(1234567);
        when(vehicle2.getState()).thenReturn(VirtualVehicleState.DEFECTIVE);

        groundStation = mock(RealVehicle.class);

        final JavascriptWorker worker = mock(JavascriptWorker.class);
        when(worker.getApplicationState()).thenReturn(null);
        worker.setName("Worker 01");

        doAnswer(new Answer<Object>()
        {
            public Object answer(InvocationOnMock invocation)
            {
                Object[] args = invocation.getArguments();
                jobListener = (JavascriptWorkerStateListener) args[0];
                return null;
            }
        }).when(worker).addStateListener(any(JavascriptWorkerStateListener.class));

        doAnswer(new Answer<Object>()
        {
            public Object answer(InvocationOnMock invocation)
            {
                jobListener.notify(worker, VirtualVehicleState.RUNNING);
                return null;
            }
        }).when(worker).start();

        session = mock(Session.class);
        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        JavascriptService jss = mock(JavascriptService.class);
        when(jss.createWorker(any(VirtualVehicle.class), any(Boolean.class))).thenReturn(worker);

        VirtualVehicleMigrator migrator = mock(VirtualVehicleMigrator.class);

        VvRteRepository vvRteRepository = mock(VvRteRepository.class);
        when(vvRteRepository.findVirtualVehicleById(1001)).thenReturn(vehicle);
        when(vvRteRepository.findVirtualVehicleById(1234567)).thenReturn(vehicle2);

        RealVehicleRepository rvRepository = mock(RealVehicleRepository.class);
        when(rvRepository.findAllGroundStations()).thenReturn(Arrays.asList(groundStation));

        Messages messages = mock(Messages.class);

        TaskRepository taskRepository = mock(TaskRepository.class);

        ServiceResources serviceResources = mock(ServiceResources.class);
        when(serviceResources.getService(Logger.class)).thenReturn(logger);
        when(serviceResources.getService(HibernateSessionManager.class)).thenReturn(sessionManager);
        when(serviceResources.getService(JavascriptService.class)).thenReturn(jss);
        when(serviceResources.getService(VirtualVehicleMigrator.class)).thenReturn(migrator);
        when(serviceResources.getService(VvRteRepository.class)).thenReturn(vvRteRepository);
        when(serviceResources.getService(RealVehicleRepository.class)).thenReturn(rvRepository);
        when(serviceResources.getService(TimeService.class)).thenReturn(timeService);
        when(serviceResources.getService(Messages.class)).thenReturn(messages);
        when(serviceResources.getService(TaskRepository.class)).thenReturn(taskRepository);
        when(serviceResources.getService(JobService.class)).thenReturn(jobService);

        launcher = new VirtualVehicleLauncherImpl(serviceResources);
    }

    @Test
    public void shouldLaunchSimpleVirtualVehicle() throws Exception
    {
        launcher.start(vehicle.getId());

        verify(vehicle).setState(VirtualVehicleState.RUNNING);
        assertThat(vehicle.getState()).isNotNull().isEqualTo(VirtualVehicleState.RUNNING);

        verify(session, never()).beginTransaction();
        verify(sessionManager, times(2)).commit();
    }

    @Test
    public void shouldStopSimpleVirtualVehicle() throws Exception
    {
        launcher.stop(vehicle2.getId(), VirtualVehicleState.INIT);

        verify(vehicle2).setEndTime(currentDate);
        verify(vehicle2).setState(VirtualVehicleState.INIT);
        verify(vehicle2).setStateInfo("Vehicle has been stopped manually.");
        verify(vehicle2).setContinuation(null);
        verify(sessionManager.getSession()).saveOrUpdate(vehicle2);
        verify(sessionManager).commit();
    }

    @Test()
    public void shouldThrowVVLEOnInvalidVvId() throws IOException
    {
        try
        {
            launcher.stop(INVALID_VV_ID, VirtualVehicleState.INIT);
            failBecauseExceptionWasNotThrown(VirtualVehicleLaunchException.class);
        }
        catch (VirtualVehicleLaunchException e)
        {
            assertThat(e).hasMessage("Invalid virtual vehicle 'null'");
        }
    }

    @Test
    public void shouldThrowVVLEOnInvalidVvState() throws IOException
    {
        try
        {
            launcher.stop(vehicle.getId(), VirtualVehicleState.INIT);
            failBecauseExceptionWasNotThrown(VirtualVehicleLaunchException.class);
        }
        catch (VirtualVehicleLaunchException e)
        {
            assertThat(e).hasMessageMatching("Got vehicle in state INIT, but expected \\[[^]]*\\]");
        }
    }

    public void shouldThrowVLEIfVirtualVehicleIsNull() throws IOException
    {
        try
        {
            launcher.start(-1);
            failBecauseExceptionWasNotThrown(VirtualVehicleLaunchException.class);
        }
        catch (VirtualVehicleLaunchException e)
        {
            assertThat(e).hasMessage("Invalid virtual vehicle 'null'");
        }
    }

    public void shouldThrowVLEIfVirtualVehicleHasWrongState() throws IOException
    {
        try
        {
            vehicle.setState(VirtualVehicleState.RUNNING);
            launcher.start(vehicle.getId());
            failBecauseExceptionWasNotThrown(VirtualVehicleLaunchException.class);
        }
        catch (VirtualVehicleLaunchException e)
        {
            assertThat(e).hasMessage("Expected vehicle in state INIT, but got RUNNING");
        }
    }
}
