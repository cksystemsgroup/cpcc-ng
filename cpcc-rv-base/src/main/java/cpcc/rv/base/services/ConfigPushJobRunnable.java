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
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.ioc.ServiceResources;

import com.owlike.genson.Genson;

import cpcc.com.services.CommunicationService;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.QueryManager;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.JobRunnable;

/**
 * Configuration synchronization job runnable.
 */
public class ConfigPushJobRunnable implements JobRunnable
{
    private int id;
    private Genson genson;
    private ServiceResources serviceResources;

    /**
     * @param serviceResources the service resources.
     * @param parameters the parameter map.
     */
    public ConfigPushJobRunnable(ServiceResources serviceResources, Map<String, String> parameters)
    {
        this.serviceResources = serviceResources;

        id = Integer.parseInt(parameters.get("rv"));
        genson = new Genson();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() throws IOException
    {
        QueryManager queryManager = serviceResources.getService(QueryManager.class);
        CommunicationService com = serviceResources.getService(CommunicationService.class);
        RealVehicleRepository realVehicleRepository = serviceResources.getService(RealVehicleRepository.class);

        RealVehicle target = realVehicleRepository.findRealVehicleById(id);

        List<SensorDefinition> sds = queryManager.findAllSensorDefinitions();
        List<RealVehicle> rvs = realVehicleRepository.findAllRealVehicles();
        ConfigSyncData syncData = new ConfigSyncData(sds, rvs);
        byte[] data = genson.serializeBytes(syncData);

        com.transfer(target, RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR, data);
    }
    

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean executionSucceeded()
    {
        return true;
    }
}
