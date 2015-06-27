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

import org.hibernate.Session;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnable;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnableFactory;
import at.uni_salzburg.cs.cpcc.core.services.jobs.TimeService;

/**
 * RealVehicleJobRunnableFactory
 */
public class RealVehicleJobRunnableFactory implements JobRunnableFactory
{
    private Session session;
    private QueryManager qm;
    private CommunicationService com;
    private TimeService timeService;
    private Logger logger;

    /**
     * @param session the HIbernate session.
     * @param qm the query manager.
     * @param com the communication service.
     * @param timeService the time service.
     * @param logger the application logger.
     */
    public RealVehicleJobRunnableFactory(Session session, QueryManager qm, CommunicationService com
        , TimeService timeService, Logger logger)
    {
        this.session = session;
        this.qm = qm;
        this.com = com;
        this.timeService = timeService;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JobRunnable createRunnable(Job job)
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
            return new ConfigSyncJobRunnable(parameters, session, qm, com, timeService, logger);

        }
        else if ("status".equals(mode))
        {
            return new RealVehicleStateJobRunnable(parameters, session);
        }

        return null;
    }

}
