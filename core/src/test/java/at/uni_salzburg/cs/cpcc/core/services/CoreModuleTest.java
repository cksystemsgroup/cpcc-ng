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
package at.uni_salzburg.cs.cpcc.core.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBindingOptions;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserServiceImpl;

/**
 * PersistenceModuleTest
 */
public class CoreModuleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<CoreModule> cnt = CoreModule.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldBindQueryManager()
    {
        ServiceBindingOptions options = mock(ServiceBindingOptions.class);
        ServiceBinder binder = mock(ServiceBinder.class);
        when(binder.bind(QueryManager.class, QueryManagerImpl.class)).thenReturn(options);
        when(binder.bind(CoreJsonConverter.class, CoreJsonConverterImpl.class)).thenReturn(options);
        when(binder.bind(CoreGeoJsonConverter.class, CoreGeoJsonConverterImpl.class)).thenReturn(options);
        when(binder.bind(OptionsParserService.class, OptionsParserServiceImpl.class)).thenReturn(options);
        when(binder.bind(TimerService.class, TimerServiceImpl.class)).thenReturn(options);

        CoreModule.bind(binder);

        verify(binder).bind(QueryManager.class, QueryManagerImpl.class);
        verify(binder).bind(CoreJsonConverter.class, CoreJsonConverterImpl.class);
        verify(binder).bind(CoreGeoJsonConverter.class, CoreGeoJsonConverterImpl.class);
        verify(binder).bind(OptionsParserService.class, OptionsParserServiceImpl.class);
        verify(binder).bind(TimerService.class, TimerServiceImpl.class);
        verify(options, times(1)).eagerLoad();
    }

    @Test
    public void shouldContributeToHibernateEntityPackageManager()
    {
        @SuppressWarnings("unchecked")
        Configuration<String> configuration = mock(Configuration.class);

        CoreModule.contributeHibernateEntityPackageManager(configuration);

        verify(configuration).add("at.uni_salzburg.cs.cpcc.core.entities");
    }
}
