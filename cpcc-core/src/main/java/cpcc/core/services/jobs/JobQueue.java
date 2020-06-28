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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;

import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

/**
 * JobQueue implementation.
 */
public class JobQueue implements JobQueueCallback
{
    private Logger logger;
    private HibernateSessionManager sessionManager;
    private TimeService timeService;
    private List<JobRunnableFactory> factoryList;
    private ExecutorService executorService;
    private ServiceResources serviceResources;

    private Map<Integer, Runnable> taskMap = Collections.synchronizedMap(new HashMap<>());
    private List<Integer> doneList = new ArrayList<>();

    /**
     * @param queueName the (unique) name of this queue.
     * @param logger the application logger.
     * @param sessionManager the Hibernate session manager.
     * @param timeService the time service.
     * @param factoryList the factory list.
     * @param numberOfPoolThreads the number of pool threads to be used.
     */
    public JobQueue(String queueName, Logger logger, HibernateSessionManager sessionManager, TimeService timeService,
        List<JobRunnableFactory> factoryList, int numberOfPoolThreads)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.timeService = timeService;
        this.factoryList = factoryList;

        executorService = Executors.newFixedThreadPool(numberOfPoolThreads, new JobQueueThreadFactory(queueName));
    }

    /**
     * @param serviceResources the services resources instance.
     */
    public void setServiceResources(ServiceResources serviceResources)
    {
        this.serviceResources = serviceResources;
    }

    /**
     * @param job the job to execute.
     * @throws JobExecutionException on errors.
     */
    public void execute(Job job) throws JobExecutionException
    {
        cleanupTaskMap();

        if (taskMap.containsKey(job.getId()))
        {
            throw new JobExecutionException("Job " + job.getId() + " is already executing! "
                + "Status is " + job.getStatus());
        }

        job.setQueued(timeService.newDate());
        job.setStatus(JobStatus.QUEUED);
        sessionManager.getSession().update(job);
        sessionManager.commit();

        JobExecutor executor = new JobExecutor(logger, serviceResources, factoryList, job.getId(), this);

        synchronized (taskMap)
        {
            taskMap.put(job.getId(), executor);
        }

        executorService.execute(executor);
    }

    /**
     * Cleanup Task Map.
     */
    private void cleanupTaskMap()
    {
        synchronized (doneList)
        {
            synchronized (taskMap)
            {
                for (Integer jobId : doneList)
                {
                    taskMap.remove(jobId);
                }
            }

            doneList.clear();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executed(int jobId)
    {
        synchronized (doneList)
        {
            doneList.add(jobId);
        }
    }
}
