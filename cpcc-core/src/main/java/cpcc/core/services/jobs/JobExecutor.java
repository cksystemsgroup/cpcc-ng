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

package cpcc.core.services.jobs;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

/**
 * JobExecutor
 */
public class JobExecutor implements Runnable
{
    private static final Logger LOG = LoggerFactory.getLogger(JobExecutor.class);

    private ServiceResources serviceResources;
    private List<JobRunnableFactory> factoryList;
    private int jobNumber;
    private JobQueueCallback callBack;

    /**
     * @param serviceResources the service resources.
     * @param factoryList the factory list.
     * @param jobNumber the id of the job to be executed.
     * @param callBack the job queue callback.
     */
    public JobExecutor(ServiceResources serviceResources, List<JobRunnableFactory> factoryList, int jobNumber,
        JobQueueCallback callBack)
    {
        this.serviceResources = serviceResources;
        this.factoryList = factoryList;
        this.jobNumber = jobNumber;
        this.callBack = callBack;
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

        LOG.debug("Executing job {} {} parameters={}", jobNumber, job.getQueueName(), job.getParameters());

        job.setStatus(JobStatus.NO_FACTORY);

        for (JobRunnableFactory factory : factoryList)
        {
            JobRunnable runnable = factory.createRunnable(serviceResources, job);
            if (runnable != null)
            {
                try
                {
                    runnable.run();
                    job.setStatus(JobStatus.OK);
                }
                catch (IOException e)
                {
                    sessionManager.abort();
                    job.setResultText(e.getMessage());
                    job.setStatus(JobStatus.FAILED);

                    LOG.error("Job failed: {} {} parameters={}. {}",
                        jobNumber, job.getQueueName(), job.getParameters(), e.getMessage());
                }
                catch (Throwable e)
                {
                    sessionManager.abort();
                    job.setResultText(e.getMessage());
                    job.setStatus(JobStatus.FAILED);

                    LOG.error("Job failed: {} {} parameters={}.",
                        jobNumber, job.getQueueName(), job.getParameters(), e);
                }

                break;
            }
        }

        job.setEnd(timeService.newDate());
        sessionManager.getSession().update(job);
        sessionManager.commit();

        tm.cleanup();

        callBack.executed(jobNumber);
    }

}
