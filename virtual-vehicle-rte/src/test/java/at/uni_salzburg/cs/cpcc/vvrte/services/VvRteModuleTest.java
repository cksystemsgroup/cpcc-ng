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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBindingOptions;
import org.testng.annotations.Test;

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
 * VvRteModuleTest
 */
public class VvRteModuleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<VvRteModule> cnt = VvRteModule.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldBindServices()
    {
        ServiceBindingOptions options = mock(ServiceBindingOptions.class);
        ServiceBinder binder = mock(ServiceBinder.class);
        
        when(binder.bind(BuiltInFunctions.class, BuiltInFunctionsImpl.class)).thenReturn(options);
        when(binder.bind(MessageConverter.class, MessageConverterImpl.class)).thenReturn(options);
        when(binder.bind(VirtualVehicleLauncher.class, VirtualVehicleLauncherImpl.class)).thenReturn(options);
        when(binder.bind(VirtualVehicleMapper.class, VirtualVehicleMapperImpl.class)).thenReturn(options);
        when(binder.bind(VvRteRepository.class, VvRteRepositoryImpl.class)).thenReturn(options);
        when(binder.bind(JavascriptService.class, JavascriptServiceImpl.class)).thenReturn(options);
        when(binder.bind(TaskAnalyzer.class, TaskAnalyzerImpl.class)).thenReturn(options);
        when(binder.bind(TaskExecutionService.class, TaskExecutionServiceImpl.class)).thenReturn(options);
        when(binder.bind(TaskSchedulerService.class, TaskSchedulerServiceImpl.class)).thenReturn(options);
        when(binder.bind(TimerService.class, TimerServiceImpl.class)).thenReturn(options);
        when(binder.bind(VirtualVehicleMigrator.class, VirtualVehicleMigratorImpl.class)).thenReturn(options);
        
        VvRteModule.bind(binder);
        
        verify(binder).bind(BuiltInFunctions.class, BuiltInFunctionsImpl.class);
        verify(binder).bind(MessageConverter.class, MessageConverterImpl.class);
        verify(binder).bind(VirtualVehicleLauncher.class, VirtualVehicleLauncherImpl.class);
        verify(binder).bind(VirtualVehicleMapper.class, VirtualVehicleMapperImpl.class);
        verify(binder).bind(VvRteRepository.class, VvRteRepositoryImpl.class);
        verify(binder).bind(JavascriptService.class, JavascriptServiceImpl.class);
        verify(binder).bind(TaskAnalyzer.class, TaskAnalyzerImpl.class);
        verify(binder).bind(TaskExecutionService.class, TaskExecutionServiceImpl.class);
        verify(binder).bind(TaskSchedulerService.class, TaskSchedulerServiceImpl.class);
        verify(binder).bind(TimerService.class, TimerServiceImpl.class);
        verify(binder).bind(VirtualVehicleMigrator.class, VirtualVehicleMigratorImpl.class);
        verify(options, times(11)).eagerLoad();
    }

    @Test
    public void shouldContributeToHibernateEntityPackageManager()
    {
        @SuppressWarnings("unchecked")
        Configuration<String> configuration = mock(Configuration.class);

        VvRteModule.contributeHibernateEntityPackageManager(configuration);

        verify(configuration).add("at.uni_salzburg.cs.cpcc.vvrte.entities");
    }
}
