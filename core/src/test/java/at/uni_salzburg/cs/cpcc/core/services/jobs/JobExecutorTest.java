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

package at.uni_salzburg.cs.cpcc.core.services.jobs;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.hibernate.Session;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.Job;
import at.uni_salzburg.cs.cpcc.core.entities.JobStatus;

public class JobExecutorTest
{
    private Date startDate;
    private Date endDate;
    private TimeService timeService;
    private Session session;
    private HibernateSessionManager sessionManager;
    private Job succeedingJob;
    private Job failingJob;
    private Job hasNoFactoryJob;
    private JobRunnable succeedingRunnable;
    private JobRunnable failingRunnable;
    private JobRunnableFactory factory;

    @BeforeMethod
    public void setUp() throws Exception
    {
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

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        succeedingJob = mock(Job.class);
        failingJob = mock(Job.class);
        hasNoFactoryJob = mock(Job.class);

        succeedingRunnable = mock(JobRunnable.class);

        failingRunnable = mock(JobRunnable.class);
        doThrow(JobExecutionException.class).when(failingRunnable).run();

        factory = mock(JobRunnableFactory.class);
        when(factory.createRunnable(succeedingJob)).thenReturn(succeedingRunnable);
        when(factory.createRunnable(failingJob)).thenReturn(failingRunnable);
        when(factory.createRunnable(hasNoFactoryJob)).thenReturn(null);
    }

    @Test
    public void shouldExecuteSucceedingRunnable() throws Exception
    {
        JobExecutor sut = new JobExecutor(sessionManager, timeService, Arrays.asList(factory), succeedingJob);

        sut.run();

        final InOrder inOrder = Mockito.inOrder(session, sessionManager, factory, succeedingJob, succeedingRunnable);
        inOrder.verify(succeedingJob).setStart(startDate);
        inOrder.verify(succeedingJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(succeedingJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(succeedingJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(succeedingJob);
        inOrder.verify(succeedingRunnable).run();

        inOrder.verify(succeedingJob).setStatus(JobStatus.OK);
        inOrder.verify(succeedingJob).setEnd(endDate);
        inOrder.verify(session).update(succeedingJob);
        inOrder.verify(sessionManager).commit();

        verify(sessionManager, times(2)).getSession();
    }

    @Test
    public void shouldExecuteFailingRunnable() throws Exception
    {
        JobExecutor sut = new JobExecutor(sessionManager, timeService, Arrays.asList(factory), failingJob);

        sut.run();

        final InOrder inOrder = Mockito.inOrder(session, sessionManager, factory, failingJob, failingRunnable);
        inOrder.verify(failingJob).setStart(startDate);
        inOrder.verify(failingJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(failingJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(failingJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(failingJob);
        inOrder.verify(failingRunnable).run();

        inOrder.verify(sessionManager).abort();

        inOrder.verify(failingJob).setStatus(JobStatus.FAILED);
        inOrder.verify(failingJob).setEnd(endDate);
        inOrder.verify(session).update(failingJob);
        inOrder.verify(sessionManager).commit();

        verify(sessionManager, times(2)).getSession();
    }

    @Test
    public void shouldHandleFactoriesThatDoNotCreateJob() throws Exception
    {
        JobExecutor sut = new JobExecutor(sessionManager, timeService, Arrays.asList(factory), hasNoFactoryJob);

        sut.run();

        final InOrder inOrder = Mockito.inOrder(session, sessionManager, factory, hasNoFactoryJob);
        inOrder.verify(hasNoFactoryJob).setStart(startDate);
        inOrder.verify(hasNoFactoryJob).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(hasNoFactoryJob);
        inOrder.verify(sessionManager).commit();
        inOrder.verify(hasNoFactoryJob).setStatus(JobStatus.NO_FACTORY);

        inOrder.verify(factory).createRunnable(hasNoFactoryJob);

        inOrder.verify(hasNoFactoryJob).setEnd(endDate);
        inOrder.verify(session).update(hasNoFactoryJob);
        inOrder.verify(sessionManager).commit();

        verify(sessionManager, times(2)).getSession();
    }

}
