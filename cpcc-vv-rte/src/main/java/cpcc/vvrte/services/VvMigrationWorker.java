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

import java.io.IOException;
import java.util.Date;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationResponse.Status;
import cpcc.com.services.CommunicationService;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;

/**
 * VvMigrationWorker
 */
public class VvMigrationWorker extends Thread
{
    private static final String LOG_MIG_WRONG_STATE =
        "Can not migrate vehicle %s because of wrong state %s";
    private static final String LOG_MIG_NO_DESTINATION =
        "Can not migrate vehicle %s (%s) because of missing destination.";

    private boolean running = true;
    private Thread waiter = null;
    private ServiceResources serviceResources;
    private Logger logger;
    private int vvId;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param vvId the virtual vehicle's identification.
     */
    public VvMigrationWorker(Logger logger, ServiceResources serviceResources, int vvId)
    {
        this.serviceResources = serviceResources;
        this.logger = logger;
        this.vvId = vvId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        PerthreadManager perthreadManager = serviceResources.getService(PerthreadManager.class);
        VvRteRepository vvRepository = serviceResources.getService(VvRteRepository.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        VirtualVehicleMigrator migrator = serviceResources.getService(VirtualVehicleMigrator.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);

        VirtualVehicle vehicle = vvRepository.findVirtualVehicleById(vvId);

        if (!verifyVehicleStatus(vehicle))
        {
            return;
        }

        setName("MIG-" + vehicle.getName() + "-" + vehicle.getMigrationDestination().getName());

        // TODO remember old state and transfer it to the migration destination!
        // TODO Finished VVs should not be started automatically after migration.

        vehicle.setPreMigrationState(vehicle.getState());
        vehicle.setState(VirtualVehicleState.MIGRATING_SND);
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

                if (vehicle.getState() == VirtualVehicleState.MIGRATION_COMPLETED_SND)
                {
                    break;
                }

                ++chunkNumber;
            }

            if (response.getStatus() == Status.OK)
            {
                vvRepository.deleteVirtualVehicleById(vehicle);
            }
            else
            {
                vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
                sessionManager.getSession().saveOrUpdate(vehicle);
            }

            sessionManager.commit();
        }
        catch (IOException | ArchiveException e)
        {
            logger.error("Migration aborted! Virtual vehicle: " + vehicle.getName()
                + " (" + vehicle.getUuid() + ")", e);
            sessionManager.abort();

            vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
            sessionManager.getSession().saveOrUpdate(vehicle);
            sessionManager.commit();
        }
        finally
        {
            perthreadManager.cleanup();
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
    private boolean verifyVehicleStatus(VirtualVehicle vehicle)
    {
        if (vehicle.getMigrationDestination() == null)
        {
            logger.error(String.format(LOG_MIG_NO_DESTINATION, vehicle.getName(), vehicle.getUuid()));
            return false;
        }

        if (!VirtualVehicleState.VV_STATES_FOR_RESTART_MIGRATION_FROM_RV.contains(vehicle.getState()))
        {
            logger.error(String.format(LOG_MIG_WRONG_STATE, vehicle.getName(), vehicle.getState().name()));
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
