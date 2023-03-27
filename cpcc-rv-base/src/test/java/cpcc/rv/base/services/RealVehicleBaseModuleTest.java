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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import org.apache.tapestry5.commons.Configuration;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceBinder;
import org.apache.tapestry5.ioc.services.cron.CronSchedule;
import org.apache.tapestry5.ioc.services.cron.PeriodicExecutor;
import org.apache.tapestry5.services.LibraryMapping;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.slf4j.Logger;

import cpcc.com.services.CommunicationService;
import cpcc.core.services.jobs.JobExecutionException;
import cpcc.core.services.jobs.JobQueue;
import cpcc.core.services.jobs.JobRepository;
import cpcc.core.services.jobs.JobService;
import cpcc.core.services.jobs.TimeService;

class RealVehicleBaseModuleTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<RealVehicleBaseModule> cnt = RealVehicleBaseModule.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @Test
    void shouldBindServices()
    {
        ServiceBinder binder = mock(ServiceBinder.class);

        RealVehicleBaseModule.bind(binder);

        verify(binder).bind(StateSynchronizer.class, StateSynchronizerImpl.class);
        verify(binder).bind(StateService.class, StateServiceImpl.class);
    }

    @Test
    void shouldContributeComponentClassResolver()
    {
        @SuppressWarnings("unchecked")
        Configuration<LibraryMapping> configuration = mock(Configuration.class);

        RealVehicleBaseModule.contributeComponentClassResolver(configuration);

        verify(configuration).add(any(LibraryMapping.class));
    }

    @Test
    void shouldScheduleJobs() throws JobExecutionException
    {
        PeriodicExecutor executor = mock(PeriodicExecutor.class);
        Logger logger = mock(Logger.class);
        SystemMonitor monitor = mock(SystemMonitor.class);

        StateSynchronizer stateSyncService = mock(StateSynchronizer.class);

        RealVehicleBaseModule.scheduleJobs(executor, stateSyncService, monitor);

        InOrder inOrder = Mockito.inOrder(executor, stateSyncService, monitor, logger);

        ArgumentCaptor<Runnable> argument1 = ArgumentCaptor.forClass(Runnable.class);
        ArgumentCaptor<Runnable> argument2 = ArgumentCaptor.forClass(Runnable.class);

        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*Push Config.*"), argument1.capture());
        inOrder.verify(executor).addJob(any(CronSchedule.class), matches(".*System Monitor.*"), argument2.capture());

        argument1.getValue().run();
        inOrder.verify(stateSyncService).pushConfiguration();

        argument2.getValue().run();
        inOrder.verify(monitor).writeLogEntry();

        verifyNoInteractions(logger);
    }

    @Test
    void shouldSetupCommunicationService()
    {
        CommunicationService communicationService = mock(CommunicationService.class);

        RealVehicleBaseModule.setupCommunicationService(communicationService);

        verify(communicationService)
            .addConnector(eq(RealVehicleBaseConstants.CONFIGURATION_UPDATE_CONNECTOR), anyString());
        verify(communicationService)
            .addConnector(eq(RealVehicleBaseConstants.REAL_VEHICLE_STATUS_CONNECTOR), anyString());
    }

    @Test
    void shouldSetupJobQueues()
    {
        JobService jobService = mock(JobService.class);
        HibernateSessionManager sessionManager = mock(HibernateSessionManager.class);
        TimeService timeService = mock(TimeService.class);
        JobRepository jobRepository = mock(JobRepository.class);

        RealVehicleBaseModule.setupJobQueues(jobService, sessionManager, timeService, jobRepository, 10);

        verify(jobService).addJobQueue(eq(RealVehicleBaseConstants.JOB_QUEUE_NAME), any(JobQueue.class));
        verify(jobService).addJobIfNotExists(RealVehicleBaseConstants.JOB_QUEUE_NAME, "mode=init");
    }
}
