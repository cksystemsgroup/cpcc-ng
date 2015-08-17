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

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

/**
 * JobService interface.
 */
public interface JobService
{
    /**
     * @param queueName the name of the queue for executing the job.
     * @param parameters the job parameters.
     * @throws JobCreationException in case of errors.
     */
    void addJob(String queueName, String parameters) throws JobCreationException;

    /**
     * Add a job if it is not already queued.
     * 
     * @param queueName the name of the queue for executing the job.
     * @param parameters the job parameters.
     */
    void addJobIfNotExists(String queueName, String parameters);

    /**
     * @param queueName the name of the queue for executing the job.
     * @param parameters the job parameters.
     * @param data the job data.
     * @throws JobCreationException in case of errors.
     */
    void addJob(String queueName, String parameters, byte[] data) throws JobCreationException;

    /**
     * Execute pending jobs. This method should be invoked periodically by the {@code PeriodicExecutor}.
     * 
     * @throws JobExecutionException in case of errors.
     */
    @CommitAfter
    void executeJobs() throws JobExecutionException;

    /**
     * @param name the name of the job queue to add.
     * @param jobQueue the job queue to add.
     */
    void addJobQueue(String name, JobQueue jobQueue);

    /**
     * Reset all jobs when starting up the service.
     * 
     * @see {@code JobRepository}
     */
    @CommitAfter
    void resetJobs();

    /**
     * Remove inactive jobs from the history.
     * 
     * @see {@code JobRepository}
     */
    @CommitAfter
    void removeOldJobs();

}
