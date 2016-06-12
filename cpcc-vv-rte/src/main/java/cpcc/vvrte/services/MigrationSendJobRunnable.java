// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationResponse.Status;
import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobRunnable;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;

/**
 * MigrationSendJobRunnable implementation.
 */
public class MigrationSendJobRunnable implements JobRunnable
{
    private static final String LOG_MIG_WRONG_STATE =
        "Can not migrate vehicle %s because of wrong state %s";
    private static final String LOG_MIG_NO_DESTINATION =
        "Can not migrate vehicle %s (%s) because of missing destination.";
    private static final String LOG_MIG_NO_SOURCE =
        "Can not migrate vehicle %s (%s) because of missing source.";

    private Logger logger;
    private ServiceResources serviceResources;
    private Map<String, String> parameters;
    private byte[] data;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     * @param data the optional job data.
     * @throws IOException in case of errors.
     */
    public MigrationSendJobRunnable(Logger logger, ServiceResources serviceResources
        , Map<String, String> parameters, byte[] data)
    {
        this.logger = logger;
        this.serviceResources = serviceResources;
        this.parameters = parameters;
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() throws Exception
    {
        String id = parameters.get("id");
        if (StringUtils.isBlank(id))
        {
            logger.error("Can not migrate virtual vehicle, parameters=" + parameters);
            return;
        }

        int vvId = Integer.parseInt(id);
        String dataString = null;
        String uuid = null;
        int chunkNumber = 0;

        if (ArrayUtils.isNotEmpty(data))
        {
            dataString = new String(data, "UTF-8");

            if (dataString.length() == 0 || dataString.charAt(0) != '{')
            {
                logger.error("Can not migrate VV because of data: parameters=" + parameters
                    + " data='" + dataString + "'");
            }

            JSONObject obj = new JSONObject(dataString);
            uuid = obj.getString("uuid");
            chunkNumber = obj.getInt("chunk");
        }

        PerthreadManager perthreadManager = serviceResources.getService(PerthreadManager.class);
        VvRteRepository vvRepository = serviceResources.getService(VvRteRepository.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        VirtualVehicleMigrator migrator = serviceResources.getService(VirtualVehicleMigrator.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        TimeService timeService = serviceResources.getService(TimeService.class);

        VirtualVehicle vehicle = vvId != 0
            ? vvRepository.findVirtualVehicleById(vvId)
            : vvRepository.findVirtualVehicleByUUID(uuid);

        if (vehicle == null)
        {
            logger.error("Can not find VV for ID " + vvId + " UUID " + uuid);
            return;
        }

        if (vvId == 0 && vehicle.getState() == VirtualVehicleState.MIGRATION_COMPLETED_SND)
        {
            vvRepository.deleteVirtualVehicleById(vehicle);
            logger.error("VV Migration completed for ID " + vvId + " UUID " + uuid);
            return;
        }

        if (!verifyVehicleStatus(vehicle))
        {
            return;
        }

        String name = Thread.currentThread().getName();
        Thread.currentThread().setName(
            "MIG-SND-" + vehicle.getName() + "-" + vehicle.getMigrationDestination().getName());

        try
        {
            byte[] chunk;

            if (VirtualVehicleState.MIGRATING_SND != vehicle.getState())
            {
                // Migration start.
                vehicle.setPreMigrationState(vehicle.getState());
                vehicle.setState(VirtualVehicleState.MIGRATING_SND);
                vehicle.setMigrationStartTime(timeService.newDate());
                vehicle.setChunkNumber(chunkNumber);
                sessionManager.getSession().saveOrUpdate(vehicle);
                sessionManager.commit();
                chunk = migrator.findFirstChunk(vehicle);

                logger.info("Initiating migration of virtual vehicle " + vehicle.getName() + ", chunk=" + chunkNumber
                    + ", parameters=" + parameters);
            }
            else
            {
                // Migration continues.
                chunkNumber = vehicle.getChunkNumber() + 1;
                chunk = migrator.findChunk(vehicle, dataString, chunkNumber);

                logger.info("Continuing migration of virtual vehicle " + vehicle.getName() + ", chunk=" + chunkNumber
                    + ", parameters=" + parameters);
            }

            CommunicationResponse response = com.transfer(
                vehicle.getMigrationDestination(), VvRteConstants.MIGRATION_CONNECTOR, chunk);

            if (response.getStatus() == Status.OK)
            {
                vehicle.setChunkNumber(chunkNumber);
                vehicle.setStateInfo(null);
                sessionManager.getSession().saveOrUpdate(vehicle);
                sessionManager.commit();
            }
            else
            {
                logger.error("Migration failed! Virtual vehicle: " + vehicle.getName()
                    + " (" + vehicle.getUuid() + ") " + new String(response.getContent(), "UTF-8"));
                vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
                vehicle.setStateInfo(new String(response.getContent(), "UTF-8"));
                sessionManager.getSession().saveOrUpdate(vehicle);
                sessionManager.commit();
            }
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
            Thread.currentThread().setName(name);
        }
    }

    /**
     * @return true if a migration may take place, false otherwise.
     */
    private boolean verifyVehicleStatus(VirtualVehicle vehicle)
    {
        if (vehicle.getMigrationSource() == null)
        {
            logger.error(String.format(LOG_MIG_NO_SOURCE, vehicle.getName(), vehicle.getUuid()));
            return false;
        }

        if (vehicle.getMigrationDestination() == null)
        {
            logger.error(String.format(LOG_MIG_NO_DESTINATION, vehicle.getName(), vehicle.getUuid()));
            return false;
        }

        if (VirtualVehicleState.MIGRATING_SND == vehicle.getState())
        {
            return true;
        }

        if (!VirtualVehicleState.VV_STATES_FOR_RESTART_MIGRATION_FROM_RV.contains(vehicle.getState()))
        {
            logger.error(String.format(LOG_MIG_WRONG_STATE, vehicle.getName(), vehicle.getState().name()));
            return false;
        }

        return true;
    }
}
