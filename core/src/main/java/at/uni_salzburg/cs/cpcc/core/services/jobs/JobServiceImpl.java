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

import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.hibernate.HibernateSessionManager;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

/**
 * JobService implementation.
 */
public class JobServiceImpl implements JobService
{
    private HibernateSessionManager sessionManager;
    private JobRepository jobRepository;
    private TimeService timeService;
    private Map<String, JobQueue> queueMap = new HashMap<>();

    /**
     * @param sessionManager the Hibernate session manager.
     * @param jobRepository the job repository.
     * @param timeService the time service.
     */
    public JobServiceImpl(HibernateSessionManager sessionManager, JobRepository jobRepository, TimeService timeService)
    {
        this.sessionManager = sessionManager;
        this.jobRepository = jobRepository;
        this.timeService = timeService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobQueue(String name, JobQueue jobQueue)
    {
        queueMap.put(name, jobQueue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJob(String queueName, String parameters) throws JobCreationException
    {
        if (!queueMap.containsKey(queueName))
        {
            throw new JobCreationException("Queue " + queueName + " not registered!");
        }

        Job job = jobRepository.findOtherRunningJob(queueName, parameters);
        if (job != null)
        {
            throw new JobCreationException("Job already executing: queue=" + queueName + "  params=" + parameters);
        }

        job = new Job();
        job.setStatus(JobStatus.CREATED);
        job.setCreated(timeService.newDate());
        job.setQueueName(queueName);
        job.setParameters(parameters);
        sessionManager.getSession().save(job);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeJobs() throws JobExecutionException
    {
        for (Job job : jobRepository.findNextScheduledJobs())
        {
            queueMap.get(job.getQueueName()).execute(job);
        }
    }
}
