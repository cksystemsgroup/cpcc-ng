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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.slf4j.Logger;

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
    private boolean running = true;
    private Thread waiter = null;
    private VirtualVehicle vehicle;
    private VvRteRepository vvRepository;
    private CommunicationService com;
    private VirtualVehicleMigrator migrator;
    private HibernateSessionManager sessionManager;
    private Logger logger;

    /**
     * @param vehicle the virtual vehicle to be migrated.
     * @param vvRepository the virtual vehicle repository.
     * @param com the communication service.
     * @param migrator the migration service.
     * @param sessionManager the Hibernate session manager.
     * @param logger the application logger.
     */
    public VvMigrationWorker(VirtualVehicle vehicle, VvRteRepository vvRepository, CommunicationService com,
        VirtualVehicleMigrator migrator, HibernateSessionManager sessionManager, Logger logger)
    {
        this.vehicle = vehicle;
        this.vvRepository = vvRepository;
        this.com = com;
        this.migrator = migrator;
        this.sessionManager = sessionManager;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        if (!verifyVehicleStatus())
        {
            return;
        }

        setName("MIG-" + vehicle.getName() + "-" + vehicle.getMigrationDestination().getName());

        vehicle.setState(VirtualVehicleState.MIGRATING);
        vehicle.setMigrationStartTime(new Date());
        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();

        try
        {
            byte[] chunk = migrator.findFirstChunk(vehicle);
            if (chunk == null)
            {
                throw new IOException("Can not find first chunk of virtual vehicle");
            }

            CommunicationResponse response = com.transfer(
                vehicle.getMigrationDestination(), VvRteConstants.MIGRATION_CONNECTOR, chunk);

            int chunkNumber = 1;
            while (response.getStatus() == Status.OK)
            {
                chunk = migrator.findChunk(vehicle, new String(response.getContent(), "UTF-8"), chunkNumber);
                if (chunk == null)
                {
                    if (chunkNumber == 1)
                    {
                        throw new IOException("Second chunk is empty, which is not allowed.");
                    }
                    break;
                }

                response = com.transfer(
                    vehicle.getMigrationDestination(), VvRteConstants.MIGRATION_CONNECTOR, chunk);

                ++chunkNumber;
            }

            if (response.getStatus() == Status.OK)
            {
                // vehicle.setState(VirtualVehicleState.MIGRATION_COMPLETED);
                // vehicle.setMigrationStartTime(null);
                vvRepository.deleteVirtualVehicleById(vehicle);
            }
            else
            {
                vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED);
                sessionManager.getSession().saveOrUpdate(vehicle);
            }

            sessionManager.commit();
        }
        catch (IOException | ArchiveException e)
        {
            logger.error("Migration aborted! Virtual vehicle: " + vehicle.getName()
                + " (" + vehicle.getUuid() + ")", e);
            sessionManager.abort();
        }

        running = false;

        if (waiter != null)
        {
            waiter.interrupt();
        }
    }

    /**
     * @return true if a migration may take place, false otherwise.
     */
    private boolean verifyVehicleStatus()
    {
        if (vehicle.getMigrationDestination() == null)
        {
            logger.error("Can not migrate vehicle " + vehicle.getName()
                + " (" + vehicle.getUuid() + ") because of missing destination.");
            return false;
        }

        if (vehicle.getState() != VirtualVehicleState.MIGRATION_AWAITED
            && vehicle.getState() != VirtualVehicleState.MIGRATION_INTERRUPTED)
        {
            logger.error("Can not migrate vehicle " + vehicle.getName()
                + " because of wrong state " + vehicle.getState());
            return false;
        }

        return true;
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
                logger.debug("Worker thread has ended.", e);
            }
        }
    }
}
