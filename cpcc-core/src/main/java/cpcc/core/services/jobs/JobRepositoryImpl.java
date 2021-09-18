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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.base.CoreConstants;
import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

/**
 * JobRepository implementation.
 */
public class JobRepositoryImpl implements JobRepository
{
    private static final Logger LOG = LoggerFactory.getLogger(JobRepositoryImpl.class);

    private static final String PARAMETERS = "parameters";
    private static final String STATUS = "status";
    private static final String QUEUE_NAME = "queueName";
    private static final String ID = "id";

    private static final JobStatus[] ACTIVE_JOB_STATES = {JobStatus.CREATED, JobStatus.QUEUED, JobStatus.RUNNING};

    private HibernateSessionManager sessionManager;
    private long maxJobAge;

    /**
     * @param sessionManager the database session manager.
     * @param maxJobAge the maximum job age in milliseconds.
     */
    public JobRepositoryImpl(HibernateSessionManager sessionManager,
        @Symbol(CoreConstants.PROP_MAX_JOB_AGE) long maxJobAge)
    {
        this.sessionManager = sessionManager;
        this.maxJobAge = maxJobAge;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> findAllJobs()
    {
        return sessionManager.getSession()
            .createQuery("FROM Job ORDER BY created", Job.class)
            .setMaxResults(100)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job findJobById(int id)
    {
        return sessionManager.getSession()
            .createQuery("FROM Job WHERE id = :id", Job.class)
            .setParameter(ID, id)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> findOtherRunningJob(String queueName, String parameters)
    {
        return sessionManager.getSession()
            .createQuery("FROM Job "
                + "WHERE queueName = :queueName "
                + "AND parameters = :parameters "
                + "AND status IN (:status)", Job.class)
            .setParameter(QUEUE_NAME, queueName)
            .setParameter(PARAMETERS, parameters)
            .setParameterList(STATUS, ACTIVE_JOB_STATES)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> findNextScheduledJobs()
    {
        return sessionManager.getSession()
            .createQuery("FROM Job WHERE status = :status ORDER BY created", Job.class)
            .setParameter(STATUS, JobStatus.CREATED)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetJobs()
    {
        List<Job> activeJobs = sessionManager.getSession()
            .createQuery("FROM Job WHERE status IN (:status)", Job.class)
            .setParameterList(STATUS, Arrays.asList(JobStatus.QUEUED, JobStatus.RUNNING))
            .list();

        for (Job job : activeJobs)
        {
            LOG.debug("Resetting job {} {} {} {}",
                job.getId(), job.getQueued(), job.getQueueName(), job.getParameters());

            job.setStatus(JobStatus.CREATED);
            job.setStart(null);
            job.setEnd(null);
            job.setResultText(null);
            sessionManager.getSession().update(job);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeOldJobs()
    {
        Date jobAgeLimit = new Date(System.currentTimeMillis() - maxJobAge);
        Date halfMinuteLimit = new Date(System.currentTimeMillis() - 30000);

        List<Job> oldJobs = sessionManager.getSession()
            .createQuery("FROM Job j "
                + "WHERE j.end <= :jobAgeLimit "
                + "OR (j.end <= :halfMinuteLimit AND j.status in (:endedStatus)) "
                + "OR (j.start <= :jobAgeLimit AND j.end IS NULL)", Job.class)
            .setParameter("jobAgeLimit", jobAgeLimit)
            .setParameter("halfMinuteLimit", halfMinuteLimit)
            .setParameterList("endedStatus", new Object[]{JobStatus.OK, JobStatus.FAILED, JobStatus.NO_FACTORY})
            .list();

        for (Job job : oldJobs)
        {
            LOG.debug("Removing old job {} {} {} {}",
                job.getId(), job.getQueued(), job.getQueueName(), job.getParameters());

            sessionManager.getSession().delete(job);
        }
    }
}
