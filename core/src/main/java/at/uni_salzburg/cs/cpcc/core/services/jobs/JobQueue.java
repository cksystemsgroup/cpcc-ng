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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.tapestry5.hibernate.HibernateSessionManager;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

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

    private HibernateSessionManager sessionManager;
    private TimeService timeService;
    private List<JobRunnableFactory> factoryList;
    private ExecutorService executorService;

    private Map<Job, Runnable> taskMap = new HashMap<Job, Runnable>();

    /**
     * @param sessionManager the Hibernate session manager.
     * @param timeService the time service.
     * @param factoryList the factory list.
     * @param numberOfPoolThreads the number of pool threads to be used.
     */
    public JobQueue(HibernateSessionManager sessionManager, TimeService timeService
        , List<JobRunnableFactory> factoryList, int numberOfPoolThreads)
    {
        this.sessionManager = sessionManager;
        this.timeService = timeService;
        this.factoryList = factoryList;

        executorService = Executors.newFixedThreadPool(numberOfPoolThreads);
    }

    /**
     * @param job the job to execute.
     * @throws JobExecutionException on errors.
     */
    public void execute(Job job) throws JobExecutionException
    {
        cleanupTaskMap();

        if (taskMap.containsKey(job))
        {
            throw new JobExecutionException("Job " + job.getId() + " is already executing! "
                + "Status is " + job.getStatus());
        }

        job.setQueued(timeService.newDate());
        job.setStatus(JobStatus.QUEUED);
        sessionManager.getSession().update(job);
        sessionManager.commit();

        JobExecutor executor = new JobExecutor(sessionManager, timeService, factoryList, job);

        synchronized (taskMap)
        {
            taskMap.put(job, executor);
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

            for (Job job : taskMap.keySet())
            {
                if (ENDED_JOB_STATUS.contains(job.getStatus()))
                {
                    toBeDeleted.add(job);
                }
            }

            for (Job job : toBeDeleted)
            {
                taskMap.remove(job);
            }
        }
    }

}
