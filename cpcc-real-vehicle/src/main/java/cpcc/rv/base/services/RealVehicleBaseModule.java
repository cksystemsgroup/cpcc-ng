// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.services;

import java.util.Arrays;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.LibraryMapping;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobQueue;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnable;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobRunnableFactory;
import at.uni_salzburg.cs.cpcc.core.services.jobs.JobService;
import at.uni_salzburg.cs.cpcc.core.services.jobs.TimeService;

/**
 * CommonsModule
 */
public final class RealVehicleBaseModule
{
    private RealVehicleBaseModule()
    {
        // Intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(StateSynchronizer.class, StateSynchronizerImpl.class).eagerLoad();

    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("rvbase", "cpcc.rv.base"));
    }

    /**
     * @param advisor the transaction adviser.
     * @param receiver the advice receiver.
     */
    @Match("*Service")
    public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
    {
        advisor.addTransactionCommitAdvice(receiver);
    }

    /**
     * @param executor the periodic executor service.
     * @param stateSyncService the state synchronization service.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final StateSynchronizer stateSyncService)
    {
        executor.addJob(new CronSchedule("0 0/2 * * * ?"), "Real Vehicle status update", new Runnable()
        {
            @Override
            public void run()
            {
                stateSyncService.realVehicleStatusUpdate();
            }
        });

        // TODO check cycle time!
        executor.addJob(new CronSchedule("0 * * * * ?"), "Synchronize Configuration", new Runnable()
        {
            @Override
            public void run()
            {
                stateSyncService.synchronizeConfiguration();
            }
        });
    }

    /**
     * @param communicationService the communication service.
     */
    @Startup
    public static void setupCommunicationService(CommunicationService communicationService)
    {
        communicationService.addConnector(
            RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR,
            RealVehicleBaseConstants.CONFIGURATION_UPDATE_PATH);

        communicationService.addConnector(
            RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR,
            RealVehicleBaseConstants.REAL_VEHICLE_STATUS_PATH);
    }

    /**
     * @param jobService the job service instance.
     * @param sessionManager the session manager instance.
     * @param timeService the time service.
     */
    @Startup
    public static void setupJobQueues(JobService jobService, HibernateSessionManager sessionManager
        , TimeService timeService)
    {
        JobRunnableFactory factory = new JobRunnableFactory()
        {
            @Override
            public JobRunnable createRunnable(Job job)
            {
                return null;
            }
        };

        jobService.addJobQueue(RealVehicleBaseConstants.JOB_QUEUE_NAME
            , new JobQueue(sessionManager
                , timeService
                , Arrays.asList(factory)
                , RealVehicleBaseConstants.NUMBER_OF_POOL_THREADS));
    }
}
