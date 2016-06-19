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
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.LibraryMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobQueue;
import cpcc.core.services.jobs.JobRepository;
import cpcc.core.services.jobs.JobRunnableFactory;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;

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
        binder.bind(StateSynchronizer.class, StateSynchronizerImpl.class);
        binder.bind(StateService.class, StateServiceImpl.class);
        binder.bind(SetupService.class, SetupServiceImpl.class);
        binder.bind(SystemMonitor.class, SystemMonitorImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(
            RealVehicleBaseConstants.RV_BASE_JOB_POOL_THREADS, System.getProperty(
                RealVehicleBaseConstants.RV_BASE_JOB_POOL_THREADS,
                RealVehicleBaseConstants.RV_BASE_JOB_POOL_THREADS_DEFAULT));
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentClassResolver(Configuration<LibraryMapping> configuration)
    {
        configuration.add(new LibraryMapping("rvbase", "cpcc.rv.base"));
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeSystemMonitor(MappedConfiguration<String, Object> configuration)
    {
        configuration.add("logger", LoggerFactory.getLogger("SystemMonitorLogger"));
    }

    /**
     * @param executor the periodic executor service.
     * @param stateSync the state synchronization service.
     * @param monitor the system monitor.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final StateSynchronizer stateSync
        , final SystemMonitor monitor)
    {
        // TODO check cycle time!
        executor.addJob(new CronSchedule("0 * * * * ?"), "Push Configuration", new Runnable()
        {
            @Override
            public void run()
            {
                stateSync.pushConfiguration();
            }
        });

        // TODO check cycle time!
        executor.addJob(new CronSchedule("*/15 * * * * ?"), "System Monitor", new Runnable()
        {
            @Override
            public void run()
            {
                monitor.writeLogEntry();
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
     * @param logger the application logger.
     * @param jobService the job service instance.
     * @param sessionManager the session manager instance.
     * @param timeService the time service.
     * @param jobRepository the job repository
     * @param numberOfPoolThreads the number of job queue pool threads.
     */
    @Startup
    public static void setupJobQueues(Logger logger, JobService jobService, HibernateSessionManager sessionManager
        , TimeService timeService, JobRepository jobRepository
        , @Symbol(RealVehicleBaseConstants.RV_BASE_JOB_POOL_THREADS) int numberOfPoolThreads)
    {
        logger.info("Creating job queue '" + RealVehicleBaseConstants.JOB_QUEUE_NAME + "' having "
            + numberOfPoolThreads + " pool threads.");

        JobRunnableFactory factory = new RealVehicleJobRunnableFactory();

        jobService.addJobQueue(RealVehicleBaseConstants.JOB_QUEUE_NAME
            , new JobQueue(logger, sessionManager, timeService, Arrays.asList(factory), numberOfPoolThreads));

        jobService.addJobIfNotExists(RealVehicleBaseConstants.JOB_QUEUE_NAME
            , "mode=" + RealVehicleBaseConstants.JOB_MODE_INIT);
    }
}
