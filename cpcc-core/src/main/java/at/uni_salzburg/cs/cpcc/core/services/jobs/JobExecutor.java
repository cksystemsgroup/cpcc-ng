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

package at.uni_salzburg.cs.cpcc.core.services.jobs;

import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

/**
 * JobExecutor
 */
public class JobExecutor implements Runnable
{
    private Logger logger;
    private ServiceResources serviceResources;
    private List<JobRunnableFactory> factoryList;
    private int jobNumber;

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param factoryList the factory list.
     * @param jobNumber the id of the job to be executed.
     */
    public JobExecutor(Logger logger, ServiceResources serviceResources, List<JobRunnableFactory> factoryList
        , int jobNumber)
    {
        this.logger = logger;
        this.serviceResources = serviceResources;
        this.factoryList = factoryList;
        this.jobNumber = jobNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        PerthreadManager tm = serviceResources.getService(PerthreadManager.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        TimeService timeService = serviceResources.getService(TimeService.class);
        JobRepository jobRepository = serviceResources.getService(JobRepository.class);

        Job job = jobRepository.findJobById(jobNumber);
        job.setStart(timeService.newDate());
        job.setStatus(JobStatus.RUNNING);
        sessionManager.getSession().update(job);
        sessionManager.commit();

        job.setStatus(JobStatus.NO_FACTORY);

        for (JobRunnableFactory factory : factoryList)
        {
            JobRunnable runnable = factory.createRunnable(logger, serviceResources, job);
            if (runnable != null)
            {
                try
                {
                    runnable.run();
                    job.setStatus(JobStatus.OK);
                }
                catch (Throwable e)
                {
                    sessionManager.abort();
                    job.setResultText(e.getMessage());
                    job.setStatus(JobStatus.FAILED);
                }
                break;
            }
        }

        job.setEnd(timeService.newDate());
        sessionManager.getSession().update(job);
        sessionManager.commit();

        tm.cleanup();
    }

}
