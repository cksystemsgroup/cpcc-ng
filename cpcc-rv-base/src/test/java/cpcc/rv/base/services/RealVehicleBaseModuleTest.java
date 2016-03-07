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

package cpcc.rv.base.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import java.lang.reflect.Constructor;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.ScopeConstants;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.ServiceBindingOptions;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.LibraryMapping;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.testng.annotations.Test;

import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobExecutionException;
import cpcc.core.services.jobs.JobQueue;
import cpcc.core.services.jobs.JobRepository;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;

public class RealVehicleBaseModuleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RealVehicleBaseModule> cnt = RealVehicleBaseModule.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    public void shouldBindServices()
    {
        ServiceBindingOptions options = mock(ServiceBindingOptions.class);
        ServiceBinder binder = mock(ServiceBinder.class);

        RealVehicleBaseModule.bind(binder);

        verify(binder).bind(StateSynchronizer.class, StateSynchronizerImpl.class);
        verify(binder).bind(StateService.class, StateServiceImpl.class);
    }

    @Test
    public void shouldContributeComponentClassResolver()
    {
        @SuppressWarnings("unchecked")
        Configuration<LibraryMapping> configuration = mock(Configuration.class);

        RealVehicleBaseModule.contributeComponentClassResolver(configuration);

        verify(configuration).add(any(LibraryMapping.class));
    }

    @Test
    public void shouldScheduleJobs() throws JobExecutionException
    {
        PeriodicExecutor executor = mock(PeriodicExecutor.class);
        Logger logger = mock(Logger.class);

        StateSynchronizer stateSyncService = mock(StateSynchronizer.class);

        RealVehicleBaseModule.scheduleJobs(executor, stateSyncService);

        InOrder inOrder = Mockito.inOrder(executor, stateSyncService, logger);

        ArgumentCaptor<Runnable> argument1 = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Runnable> argument2 = ArgumentCaptor.forClass(Runnable.class);

        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*status update.*"), argument1.capture());
        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*Push Config.*"), argument2.capture());

        argument1.getValue().run();
        inOrder.verify(stateSyncService).realVehicleStatusUpdate();

        argument2.getValue().run();
        inOrder.verify(stateSyncService).pushConfiguration();
        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldSetupCommunicationService()
    {
        CommunicationService communicationService = mock(CommunicationService.class);

        RealVehicleBaseModule.setupCommunicationService(communicationService);

        verify(communicationService)
            .addConnector(eq(RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR), anyString());
        verify(communicationService)
            .addConnector(eq(RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR), anyString());
    }

    @Test
    public void shouldSetupJobQueues()
    {
        Logger logger = mock(Logger.class);
        JobService jobService = mock(JobService.class);
        HibernateSessionManager sessionManager = mock(HibernateSessionManager.class);
        TimeService timeService = mock(TimeService.class);
        JobRepository jobRepository = mock(JobRepository.class);

        RealVehicleBaseModule.setupJobQueues(logger, jobService, sessionManager, timeService, jobRepository);

        verify(jobService).addJobQueue(eq(RealVehicleBaseConstants.JOB_QUEUE_NAME), any(JobQueue.class));
        verify(jobService).addJobIfNotExists(RealVehicleBaseConstants.JOB_QUEUE_NAME, "mode=init");
    }
}
