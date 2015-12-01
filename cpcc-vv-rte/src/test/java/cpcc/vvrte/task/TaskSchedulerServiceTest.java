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

package cpcc.vvrte.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.vvrte.base.VvRteConstants;

/**
 * TaskSchedulerServiceTest
 */
public class TaskSchedulerServiceTest
{
    private Task taskA;
    private Task taskB;
    private Task taskC;
    private Task taskD;

    private ArrayList<Task> scheduledTasks;
    private ArrayList<Task> pendingTasks;

    private TaskSchedulerServiceImpl scheduler;
    private Logger logger;

    @BeforeMethod
    public void setUp()
    {
        logger = mock(Logger.class);

        taskA = mock(Task.class);
        when(taskA.getLatitude()).thenReturn(47.1234);
        when(taskA.getLongitude()).thenReturn(13.7897);
        when(taskA.getAltitude()).thenReturn(8.0);
        when(taskA.getCreationTime()).thenReturn(1L);
        when(taskA.toString()).thenReturn("taskA (47.1234, 13.7897, 8), time=1");

        taskB = mock(Task.class);
        when(taskB.getLatitude()).thenReturn(47.2345);
        when(taskB.getLongitude()).thenReturn(13.1234);
        when(taskB.getAltitude()).thenReturn(23.0);
        when(taskB.getCreationTime()).thenReturn(2L);
        when(taskB.toString()).thenReturn("taskB (47.2345, 13.1234, 23), time=2");

        taskC = mock(Task.class);
        when(taskC.getLatitude()).thenReturn(47.3345);
        when(taskC.getLongitude()).thenReturn(13.5234);
        when(taskC.getAltitude()).thenReturn(13.0);
        when(taskC.getCreationTime()).thenReturn(3L);
        when(taskC.toString()).thenReturn("taskC (47.3345, 13.5234, 13), time=3");

        taskD = mock(Task.class);
        when(taskD.getLatitude()).thenReturn(47.4345);
        when(taskD.getLongitude()).thenReturn(13.3234);
        when(taskD.getAltitude()).thenReturn(18.0);
        when(taskD.getCreationTime()).thenReturn(4L);
        when(taskD.toString()).thenReturn("taskD (47.4345, 13.3234, 18), time=4");

        scheduledTasks = new ArrayList<Task>();
        assertThat(scheduledTasks).isNotNull().isEmpty();

        pendingTasks = new ArrayList<Task>();
        assertThat(pendingTasks).isNotNull().isEmpty();

        scheduler = new TaskSchedulerServiceImpl(VvRteConstants.PROP_DEFAULT_SCHEDULER_CLASS_NAME, logger);
        assertThat(scheduler).isNotNull();
    }

    @Test
    public void shouldHaveDefaultSchedulingAlgorithm()
    {
        pendingTasks.addAll(Arrays.asList(taskA, taskB, taskC, taskD));

        scheduler.schedule(scheduledTasks, pendingTasks);

        assertThat(scheduledTasks).isNotEmpty().containsExactly(taskA, taskB, taskC, taskD);
        assertThat(pendingTasks).isEmpty();
    }

    @Test
    public void shouldLoadSchedulingAlgorithm() throws Exception
    {
        pendingTasks.addAll(Arrays.asList(taskA, taskB, taskC, taskD));

        scheduler.setAlgorithm(ReverseScheduler.class.getName());

        scheduler.schedule(scheduledTasks, pendingTasks);

        assertThat(scheduledTasks).isNotEmpty().containsExactly(taskD, taskC, taskB, taskA);
        assertThat(pendingTasks).isEmpty();
    }

    @Test
    public void shouldLogNotExistingSchedulingAlgorithm() throws Exception
    {
        TaskSchedulerServiceImpl scheduler2 = new TaskSchedulerServiceImpl("cpcc.notExistingAlgorithmImpl", logger);

        verify(logger).error(anyString(), any(ClassNotFoundException.class));

        pendingTasks.addAll(Arrays.asList(taskA, taskB, taskC, taskD));

        scheduler2.schedule(scheduledTasks, pendingTasks);

        assertThat(scheduledTasks).isNotEmpty().containsExactly(taskA, taskB, taskC, taskD);
        assertThat(pendingTasks).isEmpty();
    }

    /**
     * ReverseScheduler
     */
    public static class ReverseScheduler implements TaskSchedulingAlgorithm
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public void schedule(List<Task> scheduledTasks, List<Task> pendingTasks)
        {
            for (int k = pendingTasks.size(); k > 0; --k)
            {
                scheduledTasks.add(pendingTasks.get(k - 1));
            }
            pendingTasks.clear();
        }
    }
}
