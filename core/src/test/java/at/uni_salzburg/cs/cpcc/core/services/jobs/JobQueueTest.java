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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
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

public class JobQueueTest
{
    private HibernateSessionManager sessionManager;
    private Job job;
    private Job slowJob;
    private boolean jobEnded;
    private JobQueue sut;
    private Date queuedDate;
    private Date startDate;
    private Date endDate;
    private TimeService timeService;
    private Session session;
    private JobRunnableFactory factory;
    private JobRunnable jobRunnable;
    private JobRunnable slowJobRunnable;
    private int numberOfPoolThreads;

    @BeforeMethod
    public void setUp() throws Exception
    {
        jobEnded = false;
        numberOfPoolThreads = 3;

        queuedDate = mock(Date.class);
        startDate = mock(Date.class);
        endDate = mock(Date.class);

        timeService = mock(TimeService.class);
        when(timeService.newDate()).thenAnswer(new Answer<Date>()
        {
            int counter = 0;
            Date[] dates = {queuedDate, startDate, endDate, queuedDate, startDate, endDate};

            @Override
            public Date answer(InvocationOnMock invocation) throws Throwable
            {
                return counter < dates.length ? dates[counter++] : null;
            }
        });

        session = mock(Session.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        job = mock(Job.class);
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                jobEnded = true;
                return null;
            }
        }).when(job).setEnd(any(Date.class));
        slowJob = mock(Job.class);

        jobRunnable = mock(JobRunnable.class);
        slowJobRunnable = mock(JobRunnable.class);
        doAnswer(new Answer<Void>()
        {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable
            {
                Thread.sleep(1000);
                return null;
            }
        }).when(slowJobRunnable).run();

        factory = mock(JobRunnableFactory.class);
        when(factory.createRunnable(job)).thenReturn(jobRunnable);
        when(factory.createRunnable(slowJob)).thenReturn(slowJobRunnable);

        sut = new JobQueue(sessionManager, timeService, Arrays.asList(factory), numberOfPoolThreads);
    }

    @Test
    public void shouldExecuteJob() throws JobExecutionException, InterruptedException
    {
        sut.execute(job);

        int counter = 0;
        while (counter++ < 10 && !jobEnded)
        {
            Thread.sleep(100);
        }

        assertThat(jobEnded)
            .overridingErrorMessage("Job did not terminate within %.1f seconds!", counter / 10.0)
            .isTrue();

        final InOrder inOrder = Mockito.inOrder(session, sessionManager, factory, job);
        inOrder.verify(job).setQueued(queuedDate);
        inOrder.verify(job).setStatus(JobStatus.QUEUED);
        inOrder.verify(session).update(job);
        inOrder.verify(sessionManager).commit();

        inOrder.verify(job).setStart(startDate);
        inOrder.verify(job).setStatus(JobStatus.RUNNING);
        inOrder.verify(session).update(job);
        inOrder.verify(sessionManager).commit();

        inOrder.verify(job).setStatus(JobStatus.OK);
        inOrder.verify(job).setEnd(endDate);
        inOrder.verify(session).update(job);
        inOrder.verify(sessionManager).commit();
    }

    @Test
    public void shouldThrowExceptionIfJobAlreadyRuns() throws JobExecutionException
    {
        sut.execute(slowJob);

        catchException(sut).execute(slowJob);

        assertThat(caughtException())
            .overridingErrorMessage("Second invocation of execute() does not throw an exception!")
            .isNotNull()
            .isInstanceOf(JobExecutionException.class);
    }

    @Test
    public void shouldReExecuteAnAlreadyFinishedJob() throws Exception
    {
        Job reusableJob = new Job();
        reusableJob.setId(31337);

        JobRunnable reusableJobRunnable = mock(JobRunnable.class);

        when(factory.createRunnable(reusableJob)).thenReturn(reusableJobRunnable);

        sut.execute(reusableJob);

        int counter = 0;
        while (counter++ < 10 && reusableJob.getEnd() == null)
        {
            Thread.sleep(100);
        }

        assertThat(reusableJob.getEnd())
            .overridingErrorMessage("Job did not terminate within %.1f seconds!", counter / 10.0)
            .isNotNull();

        sut.execute(reusableJob);

        counter = 0;
        while (counter++ < 10 && reusableJob.getEnd() == null)
        {
            Thread.sleep(100);
        }
        Thread.sleep(100);

        assertThat(reusableJob.getEnd())
            .overridingErrorMessage("Job did not terminate within %.1f seconds!", counter / 10.0)
            .isNotNull();

        verify(reusableJobRunnable, times(2)).run();

        verify(session, times(6)).update(reusableJob);
        verify(sessionManager, times(6)).commit();

        assertThat(reusableJob.getQueued()).isEqualTo(queuedDate);
        assertThat(reusableJob.getStart()).isEqualTo(startDate);
        assertThat(reusableJob.getEnd()).isEqualTo(endDate);
        assertThat(reusableJob.getStatus()).isEqualTo(JobStatus.OK);
    }
}
