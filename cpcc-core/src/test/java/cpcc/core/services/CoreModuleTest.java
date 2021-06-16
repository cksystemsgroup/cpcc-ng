// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.commons.MappedConfiguration;
import org.apache.tapestry5.commons.OrderedConfiguration;
import org.apache.tapestry5.hibernate.HibernateConfigurer;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.hibernate.HibernateTransactionAdvisor;
import org.apache.tapestry5.ioc.MethodAdviceReceiver;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBindingOptions;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import cpcc.core.base.CoreConstants;
import cpcc.core.services.jobs.JobExecutionException;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.OptionsParserServiceImpl;

/**
 * CoreModuleTest
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
        when(binder.bind(OptionsParserService.class, OptionsParserServiceImpl.class)).thenReturn(options);

        CoreModule.bind(binder);

        verify(binder).bind(QueryManager.class, QueryManagerImpl.class);
        verify(binder).bind(CoreJsonConverter.class, CoreJsonConverterImpl.class);
        verify(binder).bind(OptionsParserService.class, OptionsParserServiceImpl.class);
        verify(options, times(1)).eagerLoad();
    }

    @Test
    public void shouldContributeApplicationDefaults()
    {
        @SuppressWarnings("unchecked")
        MappedConfiguration<String, String> configuration = mock(MappedConfiguration.class);

        CoreModule.contributeApplicationDefaults(configuration);

        verify(configuration).add(eq(CoreConstants.PROP_MAX_JOB_AGE), anyString());
    }

    @Test
    public void shouldContributeComponentMessagesSource()
    {
        @SuppressWarnings("unchecked")
        OrderedConfiguration<String> configuration = mock(OrderedConfiguration.class);

        CoreModule.contributeComponentMessagesSource(configuration);

        verify(configuration).add(eq("cpccCoreMessages"), eq("cpcc/core/CoreMessages"));
    }

    @Test
    public void shouldContributeToHibernateEntityPackageManager()
    {
        @SuppressWarnings("unchecked")
        Configuration<String> configuration = mock(Configuration.class);

        CoreModule.contributeHibernateEntityPackageManager(configuration);

        verify(configuration).add("cpcc.core.entities");
    }

    @Test
    public void shouldContributeHibernateSessionSource()
    {
        @SuppressWarnings("unchecked")
        OrderedConfiguration<HibernateConfigurer> configuration = mock(OrderedConfiguration.class);
        Logger logger = mock(Logger.class);
        LiquibaseService liquibaseService = mock(LiquibaseService.class);

        CoreModule.contributeHibernateSessionSource(configuration, logger, liquibaseService);

        ArgumentCaptor<HibernateConfigurer> argument = ArgumentCaptor.forClass(HibernateConfigurer.class);

        verify(configuration).add(eq("EventListener"), argument.capture());

        org.hibernate.cfg.Configuration confMock = mock(org.hibernate.cfg.Configuration.class);

        argument.getValue().configure(confMock);

        verify(liquibaseService).update();
    }

    @Test
    public void shouldAdviseTransactions()
    {
        HibernateTransactionAdvisor advisor = mock(HibernateTransactionAdvisor.class);
        MethodAdviceReceiver receiver = mock(MethodAdviceReceiver.class);

        CoreModule.adviseTransactions(advisor, receiver);

        verify(advisor).addTransactionCommitAdvice(receiver);
    }

    @Test
    public void shouldScheduleJobs() throws JobExecutionException
    {
        PeriodicExecutor executor = mock(PeriodicExecutor.class);
        Logger logger = mock(Logger.class);
        JobService jobService = mock(JobService.class);
        HibernateSessionManager sessionManager = mock(HibernateSessionManager.class);

        CoreModule.scheduleJobs(executor, logger, jobService, sessionManager);

        InOrder inOrder = Mockito.inOrder(executor, logger, jobService, sessionManager);

        inOrder.verify(jobService).resetJobs();
        inOrder.verify(jobService).removeOldJobs();

        ArgumentCaptor<Runnable> argument1 = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Runnable> argument2 = ArgumentCaptor.forClass(Runnable.class);

        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*JobService.*"), argument1.capture());
        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*Cleanup.*"), argument2.capture());

        argument1.getValue().run();
        inOrder.verify(jobService).executeJobs();

        doThrow(JobExecutionException.class).when(jobService).executeJobs();
        argument1.getValue().run();
        inOrder.verify(jobService).executeJobs();
        inOrder.verify(logger).error(anyString(), any(JobExecutionException.class));

        argument2.getValue().run();
        inOrder.verify(jobService).removeOldJobs();
    }
}
