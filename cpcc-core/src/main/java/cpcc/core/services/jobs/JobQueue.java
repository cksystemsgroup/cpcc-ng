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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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
public class JobQueue
{
    @SuppressWarnings("serial")
    private static final Set<JobStatus> ENDED_JOB_STATUS = new HashSet<JobStatus>()
    {
        {
            add(JobStatus.OK);
            add(JobStatus.FAILED);
            add(JobStatus.NO_FACTORY);
        }
    };

    private Logger logger;
    private HibernateSessionManager sessionManager;
    private TimeService timeService;
    private JobRepository jobRepository;
    private List<JobRunnableFactory> factoryList;
    private ExecutorService executorService;
    private ServiceResources serviceResources;

    private Map<Integer, Runnable> taskMap = Collections.synchronizedMap(new HashMap<Integer, Runnable>());

    /**
     * @param logger the application logger.
     * @param sessionManager the Hibernate session manager.
     * @param timeService the time service.
     * @param jobRepository the job repository.
     * @param factoryList the factory list.
     * @param numberOfPoolThreads the number of pool threads to be used.
     */
    public JobQueue(Logger logger, HibernateSessionManager sessionManager, TimeService timeService
        , JobRepository jobRepository, List<JobRunnableFactory> factoryList, int numberOfPoolThreads)
    {
        this.logger = logger;
        this.sessionManager = sessionManager;
        this.timeService = timeService;
        this.jobRepository = jobRepository;
        this.factoryList = factoryList;

        executorService = Executors.newFixedThreadPool(numberOfPoolThreads);
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

        JobExecutor executor = new JobExecutor(logger, serviceResources, factoryList, job.getId());

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
        synchronized (taskMap)
        {
            List<Job> toBeDeleted = new ArrayList<>();

            for (Integer jobNumber : taskMap.keySet())
            {
                Job job = jobRepository.findJobById(jobNumber);

                if (ENDED_JOB_STATUS.contains(job.getStatus()))
                {
                    toBeDeleted.add(job);
                }
            }

            for (Job job : toBeDeleted)
            {
                taskMap.remove(job.getId());
            }
        }
    }

}
