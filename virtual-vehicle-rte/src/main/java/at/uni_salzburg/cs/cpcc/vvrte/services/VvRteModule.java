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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.annotations.Match;
import org.apache.tapestry5.ioc.annotations.Startup;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptServiceImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskAnalyzer;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskAnalyzerImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionService;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionServiceImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskSchedulerService;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskSchedulerServiceImpl;

/**
 * VvRteModule
 */
public final class VvRteModule
{
    private VvRteModule()
    {
        // intentionally empty.
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
        binder.bind(VirtualVehicleMigrator.class, VirtualVehicleMigratorImpl.class).eagerLoad();
        binder.bind(VvJsonConverter.class, VvJsonConverterImpl.class);
        binder.bind(VvGeoJsonConverter.class, VvGeoJsonConverterImpl.class);
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("at.uni_salzburg.cs.cpcc.vvrte.entities");
    }

    /**
     * @param advisor the Hibernate transaction advisor.
     * @param receiver the method adviser reveiver.
     */
    @Match({"*Repository", "*Service"})
    public static void adviseTransactions(HibernateTransactionAdvisor advisor, MethodAdviceReceiver receiver)
    {
        advisor.addTransactionCommitAdvice(receiver);
    }

    /**
     * @param executor the periodic executor service.
     * @param taskExecutionService the task executor service.
     */
    @Startup
    public static void scheduleJobs(PeriodicExecutor executor, final TaskExecutionService taskExecutionService)
    {
        executor.addJob(new CronSchedule("1 * * * * ?"), "Real Vehicle status update", new Runnable()
        {
            @Override
            public void run()
            {
                taskExecutionService.executeTasks();
            }
        });
    }

    /**
     * @param vvRteRepo the virtual vehicle repository.
     */
    @Startup
    public static void resetVirtualVehicleStates(VvRteRepository vvRteRepo)
    {
        vvRteRepo.resetVirtualVehicleStates();
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
    }
}
