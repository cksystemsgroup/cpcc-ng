// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationService;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobRunnable;

/**
 * Real vehicle state job runnable.
 */
public class RealVehicleStateJobRunnable implements JobRunnable
{
    private int id;
    private Logger logger;
    private ServiceResources serviceResources;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     */
    public RealVehicleStateJobRunnable(Logger logger, ServiceResources serviceResources, Map<String, String> parameters)
    {
        this.logger = logger;
        this.serviceResources = serviceResources;

        id = Integer.parseInt(parameters.get("rv"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        RealVehicleRepository rvRepo = serviceResources.getService(RealVehicleRepository.class);

        RealVehicle target = rvRepo.findRealVehicleById(id);

        try
        {
            CommunicationResponse result = com
                .transfer(target, RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR, ArrayUtils.EMPTY_BYTE_ARRAY);

            RealVehicleState rvState = rvRepo.findRealVehicleStateById(id);
            if (rvState == null)
            {
                rvState = new RealVehicleState();
                rvState.setId(id);
            }

            String stateString = org.apache.commons.codec.binary.StringUtils.newStringUtf8(result.getContent());
            rvState.setLastUpdate(new Date());
            rvState.setRealVehicleName(target.getName());
            rvState.setState(stateString);

            logger.info("RealVehicleState: ;" + target.getName() + ";" + stateString + ";");

            sessionManager.getSession().saveOrUpdate(rvState);
        }
        catch (IOException e)
        {
            logger.debug("Real vehicle state query to " + target.getName() + " did not work. " + e.getMessage());
        }
    }
}
