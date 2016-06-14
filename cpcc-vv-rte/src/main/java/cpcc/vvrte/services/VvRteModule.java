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

package cpcc.vvrte.services;

import java.util.Arrays;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobQueue;
import cpcc.core.services.jobs.JobRepository;
import cpcc.core.services.jobs.JobRunnableFactory;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.services.db.DownloadService;
import cpcc.vvrte.services.db.DownloadServiceImpl;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.db.TaskRepositoryImpl;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.db.VvRteRepositoryImpl;
import cpcc.vvrte.services.js.BuiltInFunctions;
import cpcc.vvrte.services.js.JavascriptService;
import cpcc.vvrte.services.js.JavascriptServiceImpl;
import cpcc.vvrte.services.json.VvGeoJsonConverter;
import cpcc.vvrte.services.json.VvGeoJsonConverterImpl;
import cpcc.vvrte.services.ros.MessageConverter;
import cpcc.vvrte.services.ros.MessageConverterImpl;
import cpcc.vvrte.services.task.TaskAnalyzer;
import cpcc.vvrte.services.task.TaskAnalyzerImpl;
import cpcc.vvrte.services.task.TaskExecutionService;
import cpcc.vvrte.services.task.TaskExecutionServiceImpl;
import cpcc.vvrte.services.task.TaskSchedulerService;
import cpcc.vvrte.services.task.TaskSchedulerServiceImpl;

/**
 * VvRteModule
 */
public final class VvRteModule
{
    private VvRteModule()
    {
        // Intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(BuiltInFunctions.class, BuiltInFunctionsImpl.class).eagerLoad();
        binder.bind(MessageConverter.class, MessageConverterImpl.class).eagerLoad();
        binder.bind(VirtualVehicleLauncher.class, VirtualVehicleLauncherImpl.class).eagerLoad();
        binder.bind(VirtualVehicleMapper.class, VirtualVehicleMapperImpl.class).eagerLoad();
        binder.bind(VvRteRepository.class, VvRteRepositoryImpl.class).eagerLoad();
        binder.bind(JavascriptService.class, JavascriptServiceImpl.class).eagerLoad();
        binder.bind(TaskAnalyzer.class, TaskAnalyzerImpl.class).eagerLoad();
        binder.bind(TaskExecutionService.class, TaskExecutionServiceImpl.class).eagerLoad();
        binder.bind(TaskSchedulerService.class, TaskSchedulerServiceImpl.class).eagerLoad();
        binder.bind(VirtualVehicleMigrator.class, VirtualVehicleMigratorImpl.class).scope(ScopeConstants.PERTHREAD);
        binder.bind(VvGeoJsonConverter.class, VvGeoJsonConverterImpl.class);
        binder.bind(TaskRepository.class, TaskRepositoryImpl.class);
        binder.bind(DownloadService.class, DownloadServiceImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("cpcc.vvrte.entities");
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(
            VvRteConstants.PROP_DEFAULT_SCHEDULER, System.getProperty(
                VvRteConstants.PROP_DEFAULT_SCHEDULER,
                VvRteConstants.PROP_DEFAULT_SCHEDULER_CLASS_NAME));
        configuration.add(
            VvRteConstants.PROP_MIN_TOLERANCE_DISTANCE, System.getProperty(
                VvRteConstants.PROP_MIN_TOLERANCE_DISTANCE,
                VvRteConstants.PROP_MIN_TOLERANCE_DISTANCE_DEFAULT));
        configuration.add(
            VvRteConstants.NUMBER_OF_MIGRATION_POOL_THREADS, System.getProperty(
                VvRteConstants.NUMBER_OF_MIGRATION_POOL_THREADS,
                VvRteConstants.NUMBER_OF_MIGRATION_POOL_THREADS_DEFAULT));
        configuration.add(
            VvRteConstants.MIGRATION_CHUNK_SIZE, System.getProperty(
                VvRteConstants.MIGRATION_CHUNK_SIZE,
                VvRteConstants.MIGRATION_CHUNK_SIZE_EDFAULT));
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentMessagesSource(OrderedConfiguration<String> configuration)
    {
        configuration.add("cpccVvRteMessages", "cpcc/vvrte/VvRteMessages");
    }

    /**
     * @param vvRteRepo the Virtual Vehicle repository.
     * @param executor the periodic executor service.
     * @param taskExecutionService the task executor service.
     * @param jobService the job service.
     * @param logger the application logger.
     */
    @Startup
    public static void scheduleJobs(VvRteRepository vvRteRepo, PeriodicExecutor executor
        , final TaskExecutionService taskExecutionService, final JobService jobService, final Logger logger)
    {
        vvRteRepo.resetVirtualVehicleStates();

        // TODO check cycle
        executor.addJob(new CronSchedule("* * * * * ?"), "VvRte Task execution.", new Runnable()
        {
            @Override
            public void run()
            {
                taskExecutionService.executeTasks();
            }
        });

        // TODO check cycle
        executor.addJob(new CronSchedule("0,30 * * * * ?"), "VvRte handle stuck migrations.", new Runnable()
        {
            @Override
            public void run()
            {
                logger.debug("### Add job for stuck migrations.");
                jobService.addJobIfNotExists(
                    VvRteConstants.MIGRATION_JOB_QUEUE_NAME, VvRteConstants.STUCK_MIGRATIONS);
            }
        });
    }

    /**
     * @param communicationService the communication service instance.
     */
    @Startup
    public static void setupCommunicationService(CommunicationService communicationService)
    {
        communicationService.addConnector(
            VvRteConstants.MIGRATION_CONNECTOR,
            VvRteConstants.MIGRATION_PATH);

        communicationService.addConnector(
            VvRteConstants.MIGRATION_ACK_CONNECTOR,
            VvRteConstants.MIGRATION_ACK_PATH);
    }

    /**
     * @param tes the task execution service instance.
     * @param vvl the virtual vehicle launcher instance.
     */
    @Startup
    public static void setupTaskExecutionService(TaskExecutionService tes, VirtualVehicleLauncher vvl)
    {
        tes.addListener(vvl);
    }

    /**
     * @param logger the application logger.
     * @param jobService the job service instance.
     * @param sessionManager the session manager instance.
     * @param timeService the time service.
     * @param jobRepository the job repository
     * @param numberOfPoolThreads the number of migration job queue pool threads.
     */
    @Startup
    public static void setupJobQueues(Logger logger, JobService jobService, HibernateSessionManager sessionManager
        , TimeService timeService, JobRepository jobRepository
        , @Symbol(VvRteConstants.NUMBER_OF_MIGRATION_POOL_THREADS) int numberOfPoolThreads)
    {
        JobRunnableFactory factory = new VvRteJobRunnableFactory();

        jobService.addJobQueue(VvRteConstants.MIGRATION_JOB_QUEUE_NAME
            , new JobQueue(logger, sessionManager, timeService, Arrays.asList(factory), numberOfPoolThreads));
    }
}
