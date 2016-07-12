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

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.slf4j.Logger;

import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

/**
 * JobService implementation.
 */
public class JobServiceImpl implements JobService
{
    private ServiceResources serviceResources;
    private HibernateSessionManager sessionManager;
    private JobRepository jobRepository;
    private TimeService timeService;
    private Map<String, JobQueue> queueMap = new HashMap<>();
    private Logger logger;

    /**
     * @param serviceResources the service resources.
     * @param sessionManager the Hibernate session manager.
     * @param jobRepository the job repository.
     * @param timeService the time service.
     * @param logger the application logger.
     * @throws NoSuchAlgorithmException in case of errors.
     */
    public JobServiceImpl(ServiceResources serviceResources, HibernateSessionManager sessionManager,
        JobRepository jobRepository, TimeService timeService, Logger logger) throws NoSuchAlgorithmException
    {
        this.serviceResources = serviceResources;
        this.sessionManager = sessionManager;
        this.jobRepository = jobRepository;
        this.timeService = timeService;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobQueue(String name, JobQueue jobQueue)
    {
        jobQueue.setServiceResources(serviceResources);
        queueMap.put(name, jobQueue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJob(String queueName, String parameters) throws JobCreationException
    {
        addJob(queueName, parameters, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobIfNotExists(String jobQueueName, String parameters)
    {
        try
        {
            addJob(jobQueueName, parameters);
        }
        catch (JobCreationException e)
        {
            logger.info(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJobIfNotExists(String jobQueueName, String parameters, byte[] data)
    {
        try
        {
            addJob(jobQueueName, parameters, data);
        }
        catch (JobCreationException e)
        {
            logger.info(e.getMessage());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addJob(String queueName, String params, byte[] data) throws JobCreationException
    {
        if (!queueMap.containsKey(queueName))
        {
            throw new JobCreationException("Queue " + queueName + " not registered!");
        }

        String parameters = ArrayUtils.isEmpty(data)
            ? params
            : params + ",len=" + data.length + ",md5=" + DigestUtils.md5Hex(data);

        List<Job> jobList = jobRepository.findOtherRunningJob(queueName, parameters);
        if (jobList.size() > 0)
        {
            throw new JobCreationException("Job already executing in queue='" + queueName + "', parameters='"
                + parameters + "'");
        }

        Job job = new Job();
        job.setStatus(JobStatus.CREATED);
        job.setCreated(timeService.newDate());
        job.setQueueName(queueName);
        job.setParameters(parameters);
        job.setData(data);

        sessionManager.getSession().save(job);
        sessionManager.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeJobs() throws JobExecutionException
    {
        synchronized (JobService.class)
        {
            for (Job job : jobRepository.findNextScheduledJobs())
            {
                try
                {
                    queueMap.get(job.getQueueName()).execute(job);
                    logger.debug("Executed job: " + job.getId() + ", queue: " + job.getQueueName() + ", params: "
                        + job.getParameters() + ", result: " + job.getResultText());
                }
                catch (JobExecutionException e)
                {
                    logger.error("Buggerit!", e);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetJobs()
    {
        jobRepository.resetJobs();
        sessionManager.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOldJobs()
    {
        jobRepository.removeOldJobs();
        sessionManager.commit();
    }
}
