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
    private static final String PARAMETERS = "parameters";
    private static final String START = "start";
    private static final String END = "end";
    private static final String STATUS = "status";
    private static final String QUEUE_NAME = "queueName";
    private static final String ID = "id";
    private static final String CREATED = "created";

    private static final JobStatus[] ACTIVE_JOB_STATES = {JobStatus.CREATED, JobStatus.QUEUED, JobStatus.RUNNING};

    private Logger logger;
    private Session session;
    private long maxJobAge;

    /**
     * @param logger the application logger.
     * @param session the database session.
     * @param maxJobAge the maximum job age in milliseconds.
     */
    public JobRepositoryImpl(Logger logger, Session session, @Symbol(CoreConstants.PROP_MAX_JOB_AGE) long maxJobAge)
    {
        this.logger = logger;
        this.session = session;
        this.maxJobAge = maxJobAge;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findAllJobs()
    {
        return session.createCriteria(Job.class)
            .addOrder(Order.desc(CREATED))
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
            .add(Restrictions.eq(ID, id))
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
        return session.createCriteria(Job.class)
            .add(Restrictions.eq(QUEUE_NAME, queueName))
            .add(Restrictions.eq(PARAMETERS, parameters))
            .add(Restrictions.in(STATUS, ACTIVE_JOB_STATES))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Job> findNextScheduledJobs()
    {
        return session.createCriteria(Job.class)
            .add(Restrictions.eq(STATUS, JobStatus.CREATED))
            .addOrder(Order.asc(CREATED))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public void resetJobs()
    {
        List<Job> activeJobs = session.createCriteria(Job.class)
            .add(Restrictions.in(STATUS, Arrays.asList(JobStatus.QUEUED, JobStatus.RUNNING)))
            .list();

        for (Job job : activeJobs)
        {
            logger.debug("Resetting job {} {} {} {}",
                job.getId(), job.getQueued(), job.getQueueName(), job.getParameters());

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
        List<Job> oldJobs = session.createCriteria(Job.class)
            .add(Restrictions.or(
                Restrictions.le(END, new Date(System.currentTimeMillis() - maxJobAge)),
                Restrictions.and(
                    Restrictions.le(END, new Date(System.currentTimeMillis() - 30000)),
                    Restrictions.in(STATUS, new JobStatus[]{JobStatus.OK, JobStatus.FAILED, JobStatus.NO_FACTORY})),
                Restrictions.and(
                    Restrictions.le(START, new Date(System.currentTimeMillis() - maxJobAge)),
                    Restrictions.isNull(END),
                    Restrictions.eq(STATUS, JobStatus.NO_FACTORY))))
            .list();

        for (Job job : oldJobs)
        {
            logger.debug("Removing old job {} {} {} {}",
                job.getId(), job.getQueued(), job.getQueueName(), job.getParameters());

            session.delete(job);
        }
    }
}
