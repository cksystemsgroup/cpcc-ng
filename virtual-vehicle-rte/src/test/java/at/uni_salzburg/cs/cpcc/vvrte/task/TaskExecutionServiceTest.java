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
package at.uni_salzburg.cs.cpcc.vvrte.task;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.anyList;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ros.node.NodeConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import sensor_msgs.NavSatFix;
import std_msgs.Float32;
import at.uni_salzburg.cs.cpcc.ros.actuators.ActuatorType;
import at.uni_salzburg.cs.cpcc.ros.actuators.SimpleWayPointControllerAdapter;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AbstractGpsSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AltimeterAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.SensorType;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;

/**
 * TaskExecutionServiceTest
 */
public class TaskExecutionServiceTest
{
    private Task taskA;
    private Task taskB;
    private Task taskC;
    private Task taskD;
    private TaskExecutionService executor;
    private TaskSchedulerService scheduler;
    private RosNodeService rosNodeService;
    private TimerService timerService;
    private Map<String, List<AbstractRosAdapter>> adapterNodes;
    private SimpleWayPointControllerAdapter wpc;
    private AbstractGpsSensorAdapter gps;
    private AltimeterAdapter altimeter;
    private AbstractRosAdapter unknownAdapterA;
    private AbstractRosAdapter unknownAdapterB;

    private NavSatFix position;
    private Float32 float32altitude;
    

    /**
     * Test setup.
     */
    @SuppressWarnings("unchecked")
    @BeforeMethod
    public void setUp()
    {
        taskA = mock(Task.class);
        when(taskA.getPosition()).thenReturn(new PolarCoordinate(47.1234, 13.7897, 8));
        when(taskA.getCreationTime()).thenReturn(1L);
        when(taskA.toString()).thenReturn("taskA (47.1234, 13.7897, 8), time=1");

        taskB = mock(Task.class);
        when(taskB.getPosition()).thenReturn(new PolarCoordinate(47.2345, 13.1234, 23));
        when(taskB.getCreationTime()).thenReturn(2L);
        when(taskB.toString()).thenReturn("taskB (47.2345, 13.1234, 23), time=2");

        taskC = mock(Task.class);
        when(taskC.getPosition()).thenReturn(new PolarCoordinate(47.3345, 13.5234, 13));
        when(taskC.getCreationTime()).thenReturn(3L);
        when(taskC.toString()).thenReturn("taskC (47.3345, 13.5234, 13), time=3");

        taskD = mock(Task.class);
        when(taskD.getPosition()).thenReturn(new PolarCoordinate(47.4345, 13.3234, 18));
        when(taskD.getCreationTime()).thenReturn(4L);
        when(taskD.toString()).thenReturn("taskD (47.4345, 13.3234, 18), time=4");

        float32altitude = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(Float32._TYPE);
        position = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(NavSatFix._TYPE);

        wpc = mock(SimpleWayPointControllerAdapter.class);
        when(wpc.getType()).thenReturn(ActuatorType.SIMPLE_WAYPOINT_CONTROLLER);
        when(wpc.getConnectedToAutopilot()).thenReturn(true);

        doAnswer(new Answer<Object>()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                PolarCoordinate pos = (PolarCoordinate) args[0];
                position.setLatitude(pos.getLatitude());
                position.setLongitude(pos.getLongitude());
                position.setAltitude(pos.getAltitude());
                float32altitude.setData((float) pos.getAltitude());
                return null;
            }
        }).when(wpc).setPosition((PolarCoordinate) anyObject());

        gps = mock(AbstractGpsSensorAdapter.class);
        when(gps.getPosition()).thenReturn(position);
        when(gps.getType()).thenReturn(SensorType.GPS_RECEIVER);
        when(gps.getConnectedToAutopilot()).thenReturn(true);

        altimeter = mock(AltimeterAdapter.class);
        when(altimeter.getType()).thenReturn(SensorType.ALTIMETER);
        when(altimeter.getConnectedToAutopilot()).thenReturn(true);
        when(altimeter.getValue()).thenReturn(float32altitude);

        unknownAdapterA = mock(AbstractRosAdapter.class);
        when(unknownAdapterA.getConnectedToAutopilot()).thenReturn(true);

        unknownAdapterB = mock(AbstractRosAdapter.class);
        when(unknownAdapterB.getConnectedToAutopilot()).thenReturn(true);

        adapterNodes = new HashMap<String, List<AbstractRosAdapter>>();
        adapterNodes.put("/mav01", Arrays.asList(wpc, gps, altimeter));

        rosNodeService = mock(RosNodeService.class);
        when(rosNodeService.getAdapterNodes()).thenReturn(adapterNodes);

        timerService = mock(TimerService.class);
        // when(timerService.periodicSchedule(timerTask, cycleTime));

        scheduler = mock(TaskSchedulerService.class);

        doAnswer(new Answer<Object>()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                List<Task> a = (List<Task>) args[0];
                List<Task> b = (List<Task>) args[1];
                a.addAll(b);
                b.clear();
                return null;
            }
        }).when(scheduler).schedule(anyList(), anyList());

        executor = new TaskExecutionServiceImpl(scheduler, rosNodeService, timerService);
    }

    /**
     * When initializing the task executor should query the ROS node services for active adapter nodes.
     */
    @Test
    public void shouldRetrieveAdapterInformationOnStartUp()
    {
        verify(rosNodeService).getAdapterNodes();
    }

    /**
     * When initializing the task executor should have found a way point controller.
     */
    @Test
    public void shouldRetrieveWayPointControllerOnStartUp()
    {
        assertThat(executor.getWayPointController()).isEqualTo(wpc);
        assertThat(executor.getWayPointController().getType()).isEqualTo(ActuatorType.SIMPLE_WAYPOINT_CONTROLLER);
    }

    /**
     * When initializing the task executor should have found a GPS receiver. It knows the position in WGS84 coordinates.
     */
    @Test
    public void shouldHaveDetectedGpsReceiverOnStartUp()
    {
        assertThat(executor.getGpsReceiver()).isEqualTo(gps);
        assertThat(executor.getGpsReceiver().getType()).isEqualTo(SensorType.GPS_RECEIVER);
    }

    /**
     * When initializing the task executor should have found an altimeter. It knows the altitude above ground.
     */
    @Test
    public void shouldHaveDetectedAltimeterOnStartUp()
    {
        assertThat(executor.getAltimeter()).isEqualTo(altimeter);
        assertThat(executor.getAltimeter().getType()).isEqualTo(SensorType.ALTIMETER);
    }

    /**
     * The task executor should accept a single task for processing.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldAcceptTasksForProcessing()
    {
        executor.addTask(taskA);
        List<Task> taskList = executor.getScheduledTasks();
        verify(scheduler).schedule(anyList(), anyList());
        assertThat(taskList.size()).isEqualTo(1);
        assertThat(taskList.get(0)).overridingErrorMessage("did not return expected task A").isEqualTo(taskA);
    }

    /**
     * The task executor should accept multiple tasks for processing.
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldAcceptMultipleTasksForProcessing()
    {
        executor.addTask(taskA);
        executor.addTask(taskB);
        verify(scheduler, times(2)).schedule(anyList(), anyList());

        List<Task> taskList = executor.getScheduledTasks();
        assertThat(taskList.size()).isEqualTo(2);
        assertThat(taskList.get(0)).overridingErrorMessage("did not return expected task A").isEqualTo(taskA);
        assertThat(taskList.get(1)).overridingErrorMessage("did not return expected task B").isEqualTo(taskB);
    }

    /**
     * The task executor should execute a single task.
     */
    @Test
    public void shouldExecuteASingleTask()
    {
        verify(timerService).periodicSchedule((TimerTask) anyObject(), anyLong());

        executor.addTask(taskA);

        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("did not return expected task A")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskA.getPosition());
        
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("returned an unexpected task")
            .isNull();
    }
    
    /**
     * The task executor should execute a single task.
     */
    @Test
    public void shouldExecuteASingleTaskWithoutAltimeter()
    {
        adapterNodes.put("/mav01", Arrays.asList(wpc, gps));
        executor = new TaskExecutionServiceImpl(scheduler, rosNodeService, timerService);
        assertThat(executor.getAltimeter()).isNull();

        executor.addTask(taskA);

        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("did not return expected task A")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskA.getPosition());
        
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("returned an unexpected task")
            .isNull();
    }

    /**
     * The task executor should execute multiple tasks
     */
    @Test
    public void shouldExecuteMultipleTask()
    {
        verify(timerService).periodicSchedule((TimerTask) anyObject(), anyLong());

        executor.addTask(taskA);
        executor.addTask(taskB);

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA, taskB);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskA.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskB);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskB.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should have processed all tasks")
            .isNull();
    }

    /**
     * The task executor should execute multiple tasks in the correct order
     */
    @SuppressWarnings("unchecked")
    @Test
    public void shouldExecuteMultipleTasksInCorrectOrder()
    {
        doAnswer(new MultipleTasksOrderedByCreationTime()).when(scheduler).schedule(anyList(), anyList());

        executor.addTask(taskA);
        executor.addTask(taskB);

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskB, taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskB.getPosition());

        executor.addTask(taskC);
        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskC, taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskC.getPosition());

        executor.addTask(taskD);
        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskD, taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskD.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        ((TimerTask) executor).run();

        verify(wpc).setPosition(taskA.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should have processed all tasks")
            .isNull();
    }

    /**
     * MultipleTasksOrderedByCreationTime
     */
    private static class MultipleTasksOrderedByCreationTime implements Answer<Object>
    {
        /**
         * {@inheritDoc}
         */
        @SuppressWarnings("unchecked")
        @Override
        public Object answer(InvocationOnMock invocation) throws Throwable
        {
            Object[] args = invocation.getArguments();
            List<Task> a = (List<Task>) args[0];
            List<Task> b = (List<Task>) args[1];
            a.addAll(b);
            b.clear();
            Collections.sort(a, new TaskCreationTimeComparator());
            return null;
        }
    }

    /**
     * TaskCreationTimeComparator
     */
    private static class TaskCreationTimeComparator implements Comparator<Task>
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public int compare(Task a, Task b)
        {
            return (int) (b.getCreationTime() - a.getCreationTime());
        }

    };
}
