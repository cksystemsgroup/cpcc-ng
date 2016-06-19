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

package cpcc.core.services;

import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.OrderedConfiguration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.slf4j.Logger;

import cpcc.core.base.CoreConstants;
import cpcc.core.services.jobs.JobExecutionException;
import cpcc.core.services.jobs.JobRepository;
import cpcc.core.services.jobs.JobRepositoryImpl;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.JobServiceImpl;
import cpcc.core.services.jobs.TimeService;
import cpcc.core.services.jobs.TimeServiceImpl;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.OptionsParserServiceImpl;

/**
 * CoreModule
 */
public final class CoreModule
{
    private CoreModule()
    {
        // Intentionally empty.
    }

    /**
     * @param binder the service binder
     */
    public static void bind(ServiceBinder binder)
    {
        binder.bind(LiquibaseService.class, LiquibaseServiceImpl.class);
        binder.bind(QueryManager.class, QueryManagerImpl.class).eagerLoad();
        binder.bind(CoreJsonConverter.class, CoreJsonConverterImpl.class);
        binder.bind(OptionsParserService.class, OptionsParserServiceImpl.class);
        binder.bind(JobService.class, JobServiceImpl.class);
        binder.bind(JobRepository.class, JobRepositoryImpl.class);
        binder.bind(TimeService.class, TimeServiceImpl.class);
        binder.bind(RealVehicleRepository.class, RealVehicleRepositoryImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeApplicationDefaults(MappedConfiguration<String, String> configuration)
    {
        configuration.add(
            CoreConstants.PROP_MAX_JOB_AGE, System.getProperty(
                CoreConstants.PROP_MAX_JOB_AGE,
                CoreConstants.DEFAULT_MAX_JOB_AGE));

        configuration.add(
            CoreConstants.PROP_LIQUIBASE_CHANGE_LOG_FILE, System.getProperty(
                CoreConstants.PROP_LIQUIBASE_CHANGE_LOG_FILE,
                CoreConstants.DEFAULT_LIQUIBASE_CHANGE_LOG_FILE));

        configuration.add(
            CoreConstants.PROP_LIQUIBASE_DATABASE_URL, System.getProperty(
                CoreConstants.PROP_LIQUIBASE_DATABASE_URL,
                CoreConstants.DEFAULT_LIQUIBASE_DATABASE_URL));
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeComponentMessagesSource(OrderedConfiguration<String> configuration)
    {
        configuration.add("cpccCoreMessages", "cpcc/core/CoreMessages");
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("cpcc.core.entities");
    }

    /**
     * @param config the current configuration.
     * @param logger the current logger.
     * @param liquibaseService the Liquibase service.
     */
    public static void contributeHibernateSessionSource(OrderedConfiguration<HibernateConfigurer> config
        , final Logger logger, final LiquibaseService liquibaseService)
    {
        config.add("EventListener", new HibernateConfigurer()
        {
            @Override
            public void configure(org.hibernate.cfg.Configuration configuration)
            {
                logger.info("Updating database by liquibase service...");
                liquibaseService.update();
                logger.info("Updating database done.");
            }
        });
    }

    /**
     * @param advisor the transaction adviser.
     * @param receiver the advice receiver.
     */
    @Match({"*Repository", "*Service", "*Synchronizer"})
    public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
    {
        advisor.addTransactionCommitAdvice(receiver);
    }

    /**
     * @param executor the periodic executor service.
     * @param logger the application logger.
     * @param jobService the job service.
     * @param sessionManager the database session manager.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final Logger logger, final JobService jobService
        , HibernateSessionManager sessionManager)
    {
        jobService.resetJobs();
        jobService.removeOldJobs();

        executor.addJob(new CronSchedule("* * * * * ?"), "JobService periodical execution", new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    jobService.executeJobs();
                }
                catch (JobExecutionException e)
                {
                    logger.error("Job execution failed.", e);
                }
            }
        });

        executor.addJob(new CronSchedule("0 0 * * * ?"), "Cleanup job history periodical execution", new Runnable()
        {
            @Override
            public void run()
            {
                jobService.removeOldJobs();
            }
        });
    }

}
