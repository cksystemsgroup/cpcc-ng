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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;

import cpcc.core.entities.Job;
import cpcc.core.services.jobs.JobRunnable;
import cpcc.core.services.jobs.JobRunnableFactory;
import cpcc.vvrte.base.VvRteConstants;

/**
 * VvRteJobRunnableFactory implementation.
 */
public class VvRteJobRunnableFactory implements JobRunnableFactory
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

        if (VvRteConstants.MIGRATION_MODE_SEND.equals(mode))
        {
            return new MigrationSendJobRunnable(logger, serviceResources, parameters, job.getData());
        }

        if (VvRteConstants.MIGRATION_MODE_SEND_ACK.equals(mode))
        {
            return new MigrationSendAckJobRunnable(logger, serviceResources, parameters, job.getData());
        }

        if (VvRteConstants.MIGRATION_MODE_RECEIVE.equals(mode))
        {
            return new MigrationReceiveJobRunnable(logger, serviceResources, job.getData());
        }

        if (VvRteConstants.STUCK_MIGRATIONS_MODE.equals(mode))
        {
            return new StuckMigrationsJobRunnable(serviceResources);
        }

        logger.error("VvRteJobRunnableFactory: Can not create a runnable for mode {} parameters are {}",
            mode, parameters);
        return null;
    }
}
