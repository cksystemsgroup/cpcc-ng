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

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;

import cpcc.com.services.CommunicationResponse;
import cpcc.com.services.CommunicationService;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.services.QueryManager;
import cpcc.core.services.jobs.JobRunnable;

/**
 * Real vehicle state job runnable.
 */
public class RealVehicleStateJobRunnable implements JobRunnable
{
    private int id;
    private ServiceResources serviceResources;

    /**
     * @param serviceResources the service resources.
     * @param parameters the job parameters.
     */
    public RealVehicleStateJobRunnable(ServiceResources serviceResources, Map<String, String> parameters)
    {
        this.serviceResources = serviceResources;

        id = Integer.parseInt(parameters.get("rv"));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() throws Exception
    {
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        QueryManager qm = serviceResources.getService(QueryManager.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);

        RealVehicle target = qm.findRealVehicleById(id);

        CommunicationResponse result = com
            .transfer(target, RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR, ArrayUtils.EMPTY_BYTE_ARRAY);

        RealVehicleState rvState = qm.findRealVehicleStateById(id);
        if (rvState == null)
        {
            rvState = new RealVehicleState();
            rvState.setId(id);
        }

        rvState.setLastUpdate(new Date());
        rvState.setRealVehicleName(target.getName());
        rvState.setState(new String(result.getContent(), "UTF-8"));

        sessionManager.getSession().saveOrUpdate(rvState);
    }
}
