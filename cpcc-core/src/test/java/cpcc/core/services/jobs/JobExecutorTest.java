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

package cpcc.core.services.jobs;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.slf4j.Logger;

import cpcc.core.entities.Job;
import cpcc.core.entities.JobStatus;

class JobExecutorTest
{
    private static final Integer SUCCEEDING_JOB_ID = 1001;
    private static final Integer FAILING_JOB_ID = 2002;
    private static final Integer HAS_NO_FACTORY_JOB_ID = 3003;
    private static final Integer CRASHING_JOB_ID = 4004;

    private Date startDate;
    private Date endDate;
    private TimeService timeService;
    private ServiceResources serviceResources;
    private PerthreadManager perthreadManager;
    private HibernateSessionManager sessionManager;
    private JobRepository jobRepository;
    private Session session;
    private Job succeedingJob;
    private Job failingJob;
    private Job hasNoFactoryJob;
    private Job crashingJob;
    private JobRunnable succeedingRunnable;
    private JobRunnable failingRunnable;
    private JobRunnable crashingRunnable;
    private JobRunnableFactory factory;
    private Logger logger;
    private JobQueueCallback callBack;

    @BeforeEach
    void setUp() throws Exception
    {
        logger = mock(Logger.class);
        callBack = mock(JobQueueCallback.class);
        startDate = mock(Date.class);
        endDate = mock(Date.class);

        timeService = mock(TimeService.class);
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dates = {startDate, endDate};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return dates[counter++];
            }
        });

        session = mock(Session.class);

        succeedingJob = mock(Job.class);
        when(succeedingJob.getId()).thenReturn(SUCCEEDING_JOB_ID);

        failingJob = mock(Job.class);
        when(failingJob.getId()).thenReturn(FAILING_JOB_ID);

        hasNoFactoryJob = mock(Job.class);
        when(hasNoFactoryJob.getId()).thenReturn(HAS_NO_FACTORY_JOB_ID);

        crashingJob = mock(Job.class);
        when(crashingJob.getId()).thenReturn(CRASHING_JOB_ID);

        succeedingRunnable = mock(JobRunnable.class);

        failingRunnable = mock(JobRunnable.class);
        doThrow(JobExecutionException.class).when(failingRunnable).run();

        crashingRunnable = mock(JobRunnable.class);
        doThrow(IllegalArgumentException.class).when(crashingRunnable).run();

        perthreadManager = mock(PerthreadManager.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        jobRepository = mock(JobRepository.class);
        when(jobRepository.findJobById(SUCCEEDING_JOB_ID)).thenReturn(succeedingJob);
        when(jobRepository.findJobById(FAILING_JOB_ID)).thenReturn(failingJob);
        when(jobRepository.findJobById(HAS_NO_FACTORY_JOB_ID)).thenReturn(hasNoFactoryJob);
        when(jobRepository.findJobById(CRASHING_JOB_ID)).thenReturn(crashingJob);

        serviceResources = mock(ServiceResources.class);
        when(serviceResources.getService(PerthreadManager.class)).thenReturn(perthreadManager);
        when(serviceResources.getService(HibernateSessionManager.class)).thenReturn(sessionManager);
        when(serviceResources.getService(TimeService.class)).thenReturn(timeService);
        when(serviceResources.getService(JobRepository.class)).thenReturn(jobRepository);

        factory = mock(JobRunnableFactory.class);
        when(factory.createRunnable(serviceResources, succeedingJob)).thenReturn(succeedingRunnable);
        when(factory.createRunnable(serviceResources, failingJob)).thenReturn(failingRunnable);
        when(factory.createRunnable(serviceResources, hasNoFactoryJob)).thenReturn(null);
        when(factory.createRunnable(serviceResources, crashingJob)).thenReturn(crashingRunnable);
    }

    @Test
    void shouldExecuteSucceedingRunnable() throws Exception
    {
        JobExecutor sut = new JobExecutor(serviceResources, Arrays.asList(factory), SUCCEEDING_JOB_ID, callBack);

        sut.run();

        final InOrder inOrder =
            Mockito.inOrder(jobRepository, session, sessionManager, factory, succeedingJob, succeedingRunnable);

        inOrder.verify(jobRepository).findJobById(SUCCEEDING_JOB_ID);

        inOrder.verify(succeedingJob).setStart(startDate);
        inOrder.verify(succeedingJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(succeedingJob);
        inOrder.verify(sessionManager).commit();

        inOrder.verify(succeedingJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(serviceResources, succeedingJob);
        inOrder.verify(succeedingRunnable).run();
        inOrder.verify(succeedingJob).setStatus(JobStatus.OK);

        inOrder.verify(succeedingJob).setEnd(endDate);
        inOrder.verify(session).update(succeedingJob);
        inOrder.verify(sessionManager).commit();
    }

    @Test
    void shouldExecuteFailingRunnable() throws Exception
    {
        JobExecutor sut = new JobExecutor(serviceResources, Arrays.asList(factory), FAILING_JOB_ID, callBack);

        sut.run();

        final InOrder inOrder =
            Mockito.inOrder(logger, session, sessionManager, factory, failingJob, failingRunnable);
        inOrder.verify(failingJob).setStart(startDate);
        inOrder.verify(failingJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(failingJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(failingJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(serviceResources, failingJob);
        inOrder.verify(failingRunnable).run();
        inOrder.verify(sessionManager).abort();
        inOrder.verify(failingJob).setStatus(JobStatus.FAILED);
        inOrder.verify(failingJob).setEnd(endDate);
        inOrder.verify(session).update(failingJob);
        inOrder.verify(sessionManager).commit();
    }

    @Test
    void shouldExecuteCrashingRunnable() throws Exception
    {
        JobExecutor sut = new JobExecutor(serviceResources, Arrays.asList(factory), CRASHING_JOB_ID, callBack);

        sut.run();

        final InOrder inOrder =
            Mockito.inOrder(logger, session, sessionManager, factory, crashingJob, crashingRunnable);
        inOrder.verify(crashingJob).setStart(startDate);
        inOrder.verify(crashingJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(crashingJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(crashingJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(serviceResources, crashingJob);
        inOrder.verify(crashingRunnable).run();
        inOrder.verify(sessionManager).abort();
        inOrder.verify(crashingJob).setStatus(JobStatus.FAILED);
        inOrder.verify(crashingJob).setEnd(endDate);
        inOrder.verify(session).update(crashingJob);
        inOrder.verify(sessionManager).commit();
    }

    @Test
    void shouldHandleFactoriesThatDoNotCreateJob() throws Exception
    {
        JobExecutor sut = new JobExecutor(serviceResources, Arrays.asList(factory), HAS_NO_FACTORY_JOB_ID, callBack);

        sut.run();

        final InOrder inOrder = Mockito.inOrder(session, sessionManager, factory, hasNoFactoryJob);
        inOrder.verify(hasNoFactoryJob).setStart(startDate);
        inOrder.verify(hasNoFactoryJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(hasNoFactoryJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(hasNoFactoryJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(serviceResources, hasNoFactoryJob);

        inOrder.verify(hasNoFactoryJob).setEnd(endDate);
        inOrder.verify(session).update(hasNoFactoryJob);
        inOrder.verify(sessionManager).commit();
    }

}
