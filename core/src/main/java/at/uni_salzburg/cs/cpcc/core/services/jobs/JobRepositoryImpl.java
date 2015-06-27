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

import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

/**
 * JobRepository implementation.
 */
public class JobRepositoryImpl implements JobRepository
{
    private static final String[] ACTIVE_JOB_STATES = {
        JobStatus.CREATED.name(), JobStatus.QUEUED.name(), JobStatus.RUNNING.name()
    };

    private Session session;

    /**
     * @param session the database session.
     */
    public JobRepositoryImpl(Session session)
    {
        this.session = session;
    }

    /**
     * @param queueName
     * @param parameters
     * @return
     */
    @Override
    public Job findOtherRunningJob(String queueName, String parameters)
    {
        return (Job) session.createCriteria(Job.class)
            .add(Restrictions.not(Restrictions.in("status", ACTIVE_JOB_STATES)))
            .add(Restrictions.eq("queueName", queueName))
            .add(Restrictions.ne("parameters", parameters))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Job> findNextScheduledJobs()
    {
        throw new IllegalArgumentException("not implemented!");
    }
}
