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

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
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
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * MigrationSendJobRunnable implementation.
 */
public class MigrationSendJobRunnable implements JobRunnable
{
    private Logger logger;
    private ServiceResources serviceResources;
    private VirtualVehicleMigrator migrator;
    private HibernateSessionManager sessionManager;
    private TimeService timeService;
    private Map<String, String> parameters;
    private int chunkNumber = 0;
    private String dataString = null;
    private String uuid = null;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     * @param data the optional job data.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public MigrationSendJobRunnable(Logger logger, ServiceResources serviceResources, Map<String, String> parameters,
        byte[] data)
    {
        this.logger = logger;
        this.serviceResources = serviceResources;
        this.parameters = parameters;

        if (ArrayUtils.isNotEmpty(data))
        {
            dataString = org.apache.commons.codec.binary.StringUtils.newStringUtf8(data);

            if (dataString.length() == 0 || dataString.charAt(0) != '{')
            {
                logger.error("Can not migrate VV because of data: parameters={} data='{}'", parameters, dataString);
            }

            JSONObject obj = new JSONObject(dataString);
            uuid = obj.getString("uuid");
            chunkNumber = obj.getInt("chunk");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        PerthreadManager perthreadManager = serviceResources.getService(PerthreadManager.class);

        String name = Thread.currentThread().getName();

        try
        {
            runMigration();
        }
        finally
        {
            perthreadManager.cleanup();
            Thread.currentThread().setName(name);
        }
    }

    /**
     * Run the migration job.
     */
    private void runMigration()
    {
        VvRteRepository vvRepository = serviceResources.getService(VvRteRepository.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        migrator = serviceResources.getService(VirtualVehicleMigrator.class);
        sessionManager = serviceResources.getService(HibernateSessionManager.class);
        timeService = serviceResources.getService(TimeService.class);

        String id = parameters.get("id");
        if (StringUtils.isBlank(id))
        {
            logger.error("Can not migrate virtual vehicle, parameters={}", parameters);
            return;
        }

        int vvId = Integer.parseInt(id);
        VirtualVehicle vehicle = vvId != 0
            ? vvRepository.findVirtualVehicleById(vvId)
            : vvRepository.findVirtualVehicleByUUID(uuid);

        if (vehicle == null)
        {
            logger.error("Can not find VV for ID {} UUID {}", vvId, uuid);
            return;
        }

        if (vvId == 0 && vehicle.getState() == VirtualVehicleState.MIGRATION_COMPLETED_SND)
        {
            logger.info("VV Migration completed for ID {} {} UUID {}", vvId, vehicle.getName(), uuid);
            vvRepository.deleteVirtualVehicleById(vehicle);
            sessionManager.commit();
            return;
        }

        if (!verifyVehicleStatus(vehicle))
        {
            return;
        }

        Thread.currentThread().setName(
            "MIG-SND-" + vehicle.getName() + "-" + vehicle.getMigrationDestination().getName());

        try
        {
            byte[] chunk = findVvChunk(vehicle);

            CommunicationResponse response = com.transfer(
                vehicle.getMigrationDestination(), VvRteConstants.MIGRATION_CONNECTOR, chunk);

            setVehicleState(vehicle, response);

            String content = org.apache.commons.codec.binary.StringUtils.newStringUtf8(response.getContent());

            logger.info("Migration done! Virtual vehicle: {} ({}) {} {}",
                vehicle.getName(), vehicle.getUuid(), response.getStatus(), content);
        }
        catch (IOException | ArchiveException e)
        {
            logger.error("Migration aborted! Virtual vehicle: {} ({}) {}",
                vehicle.getName(), vehicle.getUuid(), e.getMessage());
            sessionManager.abort();

            vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
            vehicle.setStateInfo(ExceptionUtils.getStackTrace(e));
        }

        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.commit();
    }

    /**
     * @param vehicle the virtual vehicle.
     * @param response the communication response.
     */
    private void setVehicleState(VirtualVehicle vehicle, CommunicationResponse response)
    {
        String stateInfo = org.apache.commons.codec.binary.StringUtils.newStringUtf8(response.getContent());

        if (response.getStatus() == Status.OK)
        {
            vehicle.setChunkNumber(chunkNumber);
            vehicle.setStateInfo(stateInfo);
        }
        else
        {
            logger.error("Migration failed! Virtual vehicle: {} ({}) {}",
                vehicle.getName(), vehicle.getUuid(), stateInfo);
            vehicle.setState(VirtualVehicleState.MIGRATION_INTERRUPTED_SND);
            vehicle.setStateInfo(stateInfo);
        }
    }

    /**
     * @param vehicle the virtual vehicle.
     * @return the chunk.
     * @throws IOException in case of errors.
     * @throws ArchiveException in case of errors.
     */
    private byte[] findVvChunk(VirtualVehicle vehicle) throws IOException, ArchiveException
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
            chunk = migrator.findChunk(vehicle, dataString, chunkNumber);

            String md5sum = DigestUtils.md5Hex(chunk);
            logger.info("Initiating migration of virtual vehicle {}, chunk={}, parameters={}, len={}, md5={}",
                vehicle.getName(), chunkNumber, parameters, chunk.length, md5sum);
        }
        else
        {
            // Migration continues.
            chunkNumber = vehicle.getChunkNumber() + 1;
            chunk = migrator.findChunk(vehicle, dataString, chunkNumber);

            String md5sum = DigestUtils.md5Hex(chunk);
            logger.info("Continuing migration of virtual vehicle {}, chunk={}, parameters={}, len={}, md5={}",
                vehicle.getName(), chunkNumber, parameters, chunk.length, md5sum);
        }

        return chunk;
    }

    /**
     * @return true if a migration may take place, false otherwise.
     */
    private boolean verifyVehicleStatus(VirtualVehicle vehicle)
    {
        if (vehicle.getMigrationSource() == null)
        {
            logger.error("Can not migrate vehicle {} ({}) because of missing source.",
                vehicle.getName(), vehicle.getUuid());
            return false;
        }

        if (vehicle.getMigrationDestination() == null)
        {
            logger.error("Can not migrate vehicle {} ({}) because of missing destination.",
                vehicle.getName(), vehicle.getUuid());
            return false;
        }

        if (VirtualVehicleState.MIGRATING_SND == vehicle.getState())
        {
            return true;
        }

        if (!VirtualVehicleState.VV_STATES_FOR_RESTART_MIGRATION_FROM_RV.contains(vehicle.getState()))
        {
            logger.error("Can not migrate vehicle {} because of wrong state {}",
                vehicle.getName(), vehicle.getState());
            return false;
        }

        return true;
    }
}
