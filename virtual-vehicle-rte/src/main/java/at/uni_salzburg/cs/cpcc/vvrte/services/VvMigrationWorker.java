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

import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;

/**
 * VvMigrationWorker
 */
public class VvMigrationWorker extends Thread
{
    private static final Logger LOG = LoggerFactory.getLogger(VvMigrationWorker.class);

    private boolean running = true;
    private Thread waiter = null;
    private VirtualVehicle vehicle;
    private VvRteRepository vvRepository;
    private CommunicationService com;
    private VirtualVehicleMigrator migrator;

    /**
     * @param vehicle the virtual vehicle to be migrated.
     * @param vvRepository the virtual vehicle repository.
     * @param com the communication service.
     * @param migrator the migration service.
     */
    public VvMigrationWorker(VirtualVehicle vehicle, VvRteRepository vvRepository, CommunicationService com,
        VirtualVehicleMigrator migrator)
    {
        this.vehicle = vehicle;
        this.vvRepository = vvRepository;
        this.com = com;
        this.migrator = migrator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        setName("MIG-" + vehicle.getName() + "-" + vehicle.getMigrationDestination().getName());

        Transaction transaction = vvRepository.getSession().beginTransaction();

        try
        {
            byte[] chunk = migrator.findFirstChunk(vehicle);
            if (chunk == null)
            {
                throw new IOException("Can not find first chunk of virtual vehicle");
            }

            CommunicationResponse response = com.transfer(vehicle.getMigrationDestination(), Connector.MIGRATE, chunk);

            int chunkNumber = 1;
            while (response.getStatus() == Status.OK)
            {
                chunk = migrator.findChunk(vehicle, response.getContent(), chunkNumber);
                if (chunk == null)
                {
                    if (chunkNumber > 1)
                    {
                        break;
                    }
                    else
                    {
                        throw new IOException("Can not find data chunks of virtual vehicle");
                    }
                }
                response = com.transfer(vehicle.getMigrationDestination(), Connector.MIGRATE, chunk);
                ++chunkNumber;
            }

            vehicle.setState(
                response.getStatus() == Status.OK
                    ? VirtualVehicleState.MIGRATION_COMPLETED
                    : VirtualVehicleState.MIGRATION_INTERRUPTED);
            transaction.commit();
        }
        catch (IOException | ArchiveException e)
        {
            LOG.error("Migration aborted of virtual vehicle " + vehicle.getName() + " (" + vehicle.getUuid() + ")", e);
            transaction.rollback();
        }

        running = false;

        if (waiter != null)
        {
            waiter.interrupt();
        }
    }

    /**
     * Await the completion of this worker instance.
     */
    public void awaitCompetion()
    {
        waiter = Thread.currentThread();

        while (running)
        {
            try
            {
                Thread.sleep(60000);
            }
            catch (InterruptedException e)
            {
                LOG.debug("Worker thread has ended.", e);
            }
        }
    }
}
