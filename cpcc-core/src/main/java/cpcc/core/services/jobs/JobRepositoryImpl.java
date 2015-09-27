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

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;

import cpcc.core.base.CoreConstants;
import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

/**
 * JobRepository implementation.
 */
public class JobRepositoryImpl implements JobRepository
{
    private static final JobStatus[] ACTIVE_JOB_STATES = {JobStatus.CREATED, JobStatus.QUEUED, JobStatus.RUNNING};

    private Logger logger;
    private Session session;
    private long maxJobAge;

    /**
     * @param logger the application logger.
     * @param session the database session.
     * @param maxJobAgeString the maximum job age as a string.
     */
    public JobRepositoryImpl(Logger logger, Session session
        , @Symbol(CoreConstants.PROP_MAX_JOB_AGE) String maxJobAgeString)
    {
        this.logger = logger;
        this.session = session;
        this.maxJobAge = Long.parseLong(maxJobAgeString);
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findAllJobs()
    {
        return (List<Job>) session.createCriteria(Job.class)
            .addOrder(Order.desc("created"))
            .setMaxResults(100)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Job findJobById(int id)
    {
        return (Job) session.createCriteria(Job.class)
            .add(Restrictions.eq("id", id))
            .uniqueResult();
    }

    /**
     * @param queueName the
     * @param parameters
     * @return
     */
    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findOtherRunningJob(String queueName, String parameters)
    {
        return (List<Job>) session.createCriteria(Job.class)
            .add(Restrictions.in("status", ACTIVE_JOB_STATES))
            .add(Restrictions.eq("queueName", queueName))
            .add(Restrictions.eq("parameters", parameters))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findNextScheduledJobs()
    {
        return (List<Job>) session.createCriteria(Job.class)
            .add(Restrictions.eq("status", JobStatus.CREATED))
            .addOrder(Order.asc("created"))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void resetJobs()
    {
        List<Job> activeJobs = (List<Job>) session.createCriteria(Job.class)
            .add(Restrictions.in("status", Arrays.asList(JobStatus.QUEUED, JobStatus.RUNNING)))
            .list();

        for (Job job : activeJobs)
        {
            logger.info("Resetting job " + job.getId()
                + " " + job.getQueued()
                + " " + job.getQueueName() + " " + job.getParameters());

            job.setStatus(JobStatus.CREATED);
            job.setStart(null);
            job.setEnd(null);
            job.setResultText(null);
            session.update(job);
        }
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void removeOldJobs()
    {
        List<Job> oldJobs = (List<Job>) session.createCriteria(Job.class)
            .add(Restrictions.le("end", new Date(System.currentTimeMillis() - maxJobAge)))
            .list();

        for (Job job : oldJobs)
        {
            logger.debug("Removing old job " + job.getId()
                + " " + job.getQueued()
                + " " + job.getQueueName() + " " + job.getParameters());

            session.delete(job);
        }
    }
}
