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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.Job;
import cpcc.core.services.jobs.JobRunnable;
import cpcc.core.services.jobs.JobRunnableFactory;

/**
 * RealVehicleJobRunnableFactory
 */
public class RealVehicleJobRunnableFactory implements JobRunnableFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public JobRunnable createRunnable(Logger logger, ServiceResources serviceResources, Job job)
    {
        Map<String, String> parameters = new HashMap<>();

        for (String param : job.getParameters().trim().split("\\s*,\\s*"))
        {
            String[] kv = param.split("=", 2);
            parameters.put(kv[0], kv[1]);
        }

        String mode = parameters.get("mode");

        if (RealVehicleBaseConstants.JOB_MODE_CONFIG.equals(mode))
        {
            return new ConfigPushJobRunnable(serviceResources, parameters);
        }

        if (RealVehicleBaseConstants.JOB_MODE_IMPORT.equals(mode))
        {
            return new ConfigImportJobRunnable(logger, serviceResources, job.getData());
        }

        if (RealVehicleBaseConstants.JOB_MODE_STATUS.equals(mode))
        {
            Logger rvStateJobLogger = LoggerFactory.getLogger("RealVehicleStateJobLogger");
            return new RealVehicleStateJobRunnable(rvStateJobLogger, serviceResources, parameters);
        }

        if (RealVehicleBaseConstants.JOB_MODE_INIT.equals(mode))
        {
            return new RealVehicleInitJobRunnable(logger, serviceResources);
        }

        logger.error("RealVehicleJobRunnableFactory: Can not create a runnable for mode {} parameters are {}",
            mode, parameters);
        return null;
    }
}
