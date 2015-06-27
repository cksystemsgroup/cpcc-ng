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

import org.apache.tapestry5.hibernate.HibernateSessionManager;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

/**
 * JobExecutor
 */
public class JobExecutor implements Runnable
{
    private HibernateSessionManager sessionManager;
    private TimeService timeService;
    private List<JobRunnableFactory> factoryList;
    private Job job;

    /**
     * @param sessionManager the Hibernate session manager.
     * @param timeService the time service.
     * @param factoryList the factory list.
     * @param job the job to be executed.
     */
    public JobExecutor(HibernateSessionManager sessionManager, TimeService timeService
        , List<JobRunnableFactory> factoryList, Job job)
    {
        this.sessionManager = sessionManager;
        this.timeService = timeService;
        this.factoryList = factoryList;
        this.job = job;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        job.setStart(timeService.newDate());
        job.setStatus(JobStatus.RUNNING);
        sessionManager.getSession().update(job);
        sessionManager.commit();

        job.setStatus(JobStatus.NO_FACTORY);

        for (JobRunnableFactory factory : factoryList)
        {
            JobRunnable runnable = factory.createRunnable(job);
            if (runnable != null)
            {
                try
                {
                    runnable.run();
                    job.setStatus(JobStatus.OK);
                }
                catch (Exception e)
                {
                    sessionManager.abort();
                    job.setStatus(JobStatus.FAILED);
                }
                break;
            }
        }

        job.setEnd(timeService.newDate());
        sessionManager.getSession().update(job);
        sessionManager.commit();
    }

}
