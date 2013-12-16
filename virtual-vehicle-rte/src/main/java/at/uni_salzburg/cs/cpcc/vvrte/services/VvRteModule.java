/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;

import at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptService;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptServiceImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskAnalyzer;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskAnalyzerImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionService;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionServiceImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskSchedulerService;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskSchedulerServiceImpl;
import at.uni_salzburg.cs.cpcc.vvrte.task.TimerService;
import at.uni_salzburg.cs.cpcc.vvrte.task.TimerServiceImpl;

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
        binder.bind(TimerService.class, TimerServiceImpl.class).eagerLoad();
        binder.bind(VirtualVehicleMigrator.class, VirtualVehicleMigratorImpl.class).eagerLoad();
    }

    /**
     * @param configuration the IoC configuration.
     */
    public static void contributeHibernateEntityPackageManager(Configuration<String> configuration)
    {
        configuration.add("at.uni_salzburg.cs.cpcc.vvrte.entities");
    }
}
