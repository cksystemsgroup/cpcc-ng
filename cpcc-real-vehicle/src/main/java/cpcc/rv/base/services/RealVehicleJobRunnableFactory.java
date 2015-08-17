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

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnable;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnableFactory;

/**
 * RealVehicleJobRunnableFactory
 */
public class RealVehicleJobRunnableFactory implements JobRunnableFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public JobRunnable createRunnable(ServiceResources serviceResources, Job job)
    {
        Map<String, String> parameters = new HashMap<>();

        for (String param : job.getParameters().trim().split("\\s*,\\s*"))
        {
            String[] kv = param.split("=", 2);
            parameters.put(kv[0], kv[1]);
        }

        String mode = parameters.get("mode");

        if ("config".equals(mode))
        {
            return new ConfigPushJobRunnable(serviceResources, parameters);

        }
        else if ("import".equals(mode))
        {
            return new ConfigImportJobRunnable(serviceResources, job.getData());
        }
        else if ("status".equals(mode))
        {
            return new RealVehicleStateJobRunnable(serviceResources, parameters);
        }
        else if ("init".equals(mode))
        {
            return new RealVehicleInitJobRunnable(serviceResources);
        }

        return null;
    }
}
