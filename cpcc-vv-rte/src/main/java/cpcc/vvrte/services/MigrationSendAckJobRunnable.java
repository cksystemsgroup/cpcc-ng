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

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationResponse.Status;
import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobRunnable;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;

/**
 * MigrationSendAckJobRunnable implementation.
 */
public class MigrationSendAckJobRunnable implements JobRunnable
{
    private static final String LOG_MIG_WRONG_STATE =
        "Can not acknowledge vehicle %s (%d) because of wrong state %s instead of %s";

    private Logger logger;
    private ServiceResources serviceResources;
    private Map<String, String> parameters;
    private byte[] data;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     * @param data the optional job data.
     */
    public MigrationSendAckJobRunnable(Logger logger, ServiceResources serviceResources
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
            logger.error("Can not acknowledge virtual vehicle migration , parameters=" + parameters);
            return;
        }

        int vvId = Integer.parseInt(id);

        PerthreadManager perthreadManager = serviceResources.getService(PerthreadManager.class);
        VvRteRepository vvRepository = serviceResources.getService(VvRteRepository.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);

        VirtualVehicle vehicle = vvRepository.findVirtualVehicleById(vvId);

        if (vehicle == null)
        {
            logger.error("Can not find VV for ID " + vvId + " (ACK).");
            return;
        }

        if (vehicle.getState() != VirtualVehicleState.MIGRATION_COMPLETED_SND)
        {
            logger.error(String.format(LOG_MIG_WRONG_STATE, vehicle.getName(), vehicle.getId()
                , vehicle.getState().name()), VirtualVehicleState.MIGRATION_COMPLETED_SND.name());
            return;
        }

        String name = Thread.currentThread().getName();
        Thread.currentThread().setName("MIG-ACK-" + vehicle.getName());

        try
        {
            CommunicationResponse response = com.transfer(
                vehicle.getMigrationSource(), VvRteConstants.MIGRATION_ACK_CONNECTOR, data);

            if (response.getStatus() == Status.OK)
            {
                vvRepository.deleteVirtualVehicleById(vehicle);
                logger.info("ACK virtual vehicle migration , parameters="
                    + parameters + " " + new String(response.getContent(), "UTF-8"));
            }
            else
            {
                logger.error("Can not ACK VV " + vehicle.getName()
                    + " to RV " + vehicle.getMigrationSource().getName()
                    + " reason: " + new String(response.getContent(), "UTF-8"));
            }

            sessionManager.commit();
        }
        catch (IOException e)
        {
            logger.error("Migration ACK aborted again! Virtual vehicle: " + vehicle.getName()
                + " (" + vehicle.getUuid() + ")", e);
            sessionManager.abort();
        }
        finally
        {
            perthreadManager.cleanup();
            Thread.currentThread().setName(name);
        }
    }

}
