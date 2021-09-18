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
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationResponse.Status;
import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobRunnable;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * MigrationSendAckJobRunnable implementation.
 */
public class MigrationSendAckJobRunnable implements JobRunnable
{
    private static final Logger LOG = LoggerFactory.getLogger(MigrationSendAckJobRunnable.class);

    private ServiceResources serviceResources;
    private Map<String, String> parameters;
    private byte[] data;
    private boolean succeeded = false;

    /**
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     * @param data the optional job data.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public MigrationSendAckJobRunnable(ServiceResources serviceResources, Map<String, String> parameters, byte[] data)
    {
        this.serviceResources = serviceResources;
        this.parameters = parameters;
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() throws IOException
    {
        String id = parameters.get("id");
        if (StringUtils.isBlank(id))
        {
            LOG.error("Can not acknowledge virtual vehicle migration , parameters={}", parameters);
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
            LOG.error("Can not find VV for ID {} (ACK).", vvId);
            return;
        }

        if (vehicle.getState() != VirtualVehicleState.MIGRATION_COMPLETED_SND)
        {
            LOG.error("Can not acknowledge vehicle {} ({}) because of wrong state {} instead of {}",
                vehicle.getName(), vehicle.getId(), vehicle.getState(), VirtualVehicleState.MIGRATION_COMPLETED_SND);
            return;
        }

        String name = Thread.currentThread().getName();
        Thread.currentThread().setName("MIG-ACK-" + vehicle.getName());

        try
        {
            CommunicationResponse response = com.transfer(
                vehicle.getMigrationSource(), VvRteConstants.MIGRATION_ACK_CONNECTOR, data);

            String content = new String(response.getContent(), StandardCharsets.UTF_8);

            if (response.getStatus() == Status.OK)
            {
                LOG.info("ACK virtual vehicle migration , parameters={} {}", parameters, content);
                vvRepository.deleteVirtualVehicleById(vehicle);
                succeeded = true;
            }
            else
            {
                LOG.error("Can not ACK VV {} to RV {} reason: {}",
                    vehicle.getName(), vehicle.getMigrationSource().getName(), content);
            }

            sessionManager.commit();
        }
        catch (IOException e)
        {
            LOG.error("Migration ACK aborted again! Virtual vehicle: {} ({})",
                vehicle.getName(), vehicle.getUuid(), e);
            sessionManager.abort();
        }
        finally
        {
            perthreadManager.cleanup();
            Thread.currentThread().setName(name);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executionSucceeded()
    {
        return succeeded;
    }
}
