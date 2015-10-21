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
import static org.mockito.Matchers.anyList;
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

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.ros.node.NodeConfiguration;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import cpcc.core.utils.PolarCoordinate;
import cpcc.ros.actuators.AbstractActuatorAdapter;
import cpcc.ros.actuators.ActuatorType;
import cpcc.ros.actuators.SimpleWayPointControllerAdapter;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.sensors.AbstractSensorAdapter;
import cpcc.ros.sensors.AltimeterAdapter;
import cpcc.ros.sensors.SensorType;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.task.Task;
import cpcc.vvrte.task.TaskExecutionService;
import cpcc.vvrte.task.TaskExecutionServiceImpl;
import cpcc.vvrte.task.TaskSchedulerService;
import sensor_msgs.NavSatFix;
import std_msgs.Float32;

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
    private Map<String, List<AbstractRosAdapter>> adapterNodes;
    private SimpleWayPointControllerAdapter wpc;
    private AbstractGpsSensorAdapter gps;
    private AltimeterAdapter altimeter;
    private AbstractRosAdapter unknownAdapterA;
    private AbstractRosAdapter unknownAdapterB;
    private AbstractRosAdapter unknownAdapterC;
    private AbstractRosAdapter unknownAdapterD;

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
        when(taskA.getTolerance()).thenReturn(10.0);
        when(taskA.toString()).thenReturn("taskA (47.1234, 13.7897, 8), time=1");

        taskB = mock(Task.class);
        when(taskB.getPosition()).thenReturn(new PolarCoordinate(47.2345, 13.1234, 23));
        when(taskB.getCreationTime()).thenReturn(2L);
        when(taskB.getTolerance()).thenReturn(10.0);
        when(taskB.toString()).thenReturn("taskB (47.2345, 13.1234, 23), time=2");

        taskC = mock(Task.class);
        when(taskC.getPosition()).thenReturn(new PolarCoordinate(47.3345, 13.5234, 13));
        when(taskC.getCreationTime()).thenReturn(3L);
        when(taskC.getTolerance()).thenReturn(10.0);
        when(taskC.toString()).thenReturn("taskC (47.3345, 13.5234, 13), time=3");

        taskD = mock(Task.class);
        when(taskD.getPosition()).thenReturn(new PolarCoordinate(47.4345, 13.3234, 18));
        when(taskD.getCreationTime()).thenReturn(4L);
        when(taskD.getTolerance()).thenReturn(10.0);
        when(taskD.toString()).thenReturn("taskD (47.4345, 13.3234, 18), time=4");

        float32altitude = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(Float32._TYPE);
        position = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(NavSatFix._TYPE);

        wpc = mock(SimpleWayPointControllerAdapter.class);
        when(wpc.getType()).thenReturn(ActuatorType.SIMPLE_WAYPOINT_CONTROLLER);
        when(wpc.isConnectedToAutopilot()).thenReturn(true);

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
        when(gps.isConnectedToAutopilot()).thenReturn(true);

        altimeter = mock(AltimeterAdapter.class);
        when(altimeter.getType()).thenReturn(SensorType.ALTIMETER);
        when(altimeter.isConnectedToAutopilot()).thenReturn(true);
        when(altimeter.getValue()).thenReturn(float32altitude);

        unknownAdapterA = mock(AbstractSensorAdapter.class);
        when(unknownAdapterA.isConnectedToAutopilot()).thenReturn(true);

        unknownAdapterB = mock(AbstractActuatorAdapter.class);
        when(unknownAdapterB.isConnectedToAutopilot()).thenReturn(true);

        unknownAdapterC = mock(AbstractRosAdapter.class);
        when(unknownAdapterC.isConnectedToAutopilot()).thenReturn(false);

        unknownAdapterD = mock(AbstractRosAdapter.class);
        when(unknownAdapterD.isConnectedToAutopilot()).thenReturn(true);

        adapterNodes = new HashMap<String, List<AbstractRosAdapter>>();
        adapterNodes.put("/mav01",
            Arrays.asList(wpc, gps, altimeter, unknownAdapterA, unknownAdapterB, unknownAdapterC, unknownAdapterD));

        rosNodeService = mock(RosNodeService.class);
        when(rosNodeService.getAdapterNodes()).thenReturn(adapterNodes);

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

        executor = new TaskExecutionServiceImpl(scheduler, rosNodeService);
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
        assertThat(taskList).isNotEmpty().containsExactly(taskA);
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
        assertThat(taskList).isNotEmpty().containsExactly(taskA, taskB);
    }

    /**
     * The task executor should execute a single task.
     */
    @Test
    public void shouldExecuteASingleTask()
    {
        executor.addTask(taskA);

        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("did not return expected task A")
            .isNull();

        executor.executeTasks();

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
        executor = new TaskExecutionServiceImpl(scheduler, rosNodeService);
        assertThat(executor.getAltimeter()).isNull();

        executor.addTask(taskA);

        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("did not return expected task A")
            .isNull();

        executor.executeTasks();

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
        executor.addTask(taskA);
        executor.addTask(taskB);

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA, taskB);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

        verify(wpc).setPosition(taskA.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskB);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

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

        executor.executeTasks();

        verify(wpc).setPosition(taskB.getPosition());

        executor.addTask(taskC);
        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskC, taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

        verify(wpc).setPosition(taskC.getPosition());

        executor.addTask(taskD);
        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskD, taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

        verify(wpc).setPosition(taskD.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

        verify(wpc).setPosition(taskA.getPosition());

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should have processed all tasks")
            .isNull();
    }

    /**
     * The task executor should do nothing, if there is nothing to do.
     */
    @Test
    public void shouldHandleEmptyTaskListCorrectly()
    {
        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();

        executor.executeTasks();

        assertThat(executor.getPendingTasks()).isEmpty();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getCurrentRunningTask())
            .overridingErrorMessage("should not return a current task")
            .isNull();
    }

    @Test
    public void shouldWaitForEndOfTravelling()
    {
        assertThat(executor.getCurrentRunningTask()).isNull();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getPendingTasks()).isEmpty();

        NavSatFix position2 = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(NavSatFix._TYPE);
        position2.setLatitude(taskA.getPosition().getLatitude());
        position2.setLongitude(taskA.getPosition().getLongitude() + 22.0);
        position2.setAltitude(taskA.getPosition().getAltitude());
        when(gps.getPosition()).thenReturn(position2);

        executor.addTask(taskA);
        assertThat(executor.getCurrentRunningTask()).isNull();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getPendingTasks()).isEmpty();

        executor.executeTasks();
        assertThat(executor.getCurrentRunningTask()).isNotNull();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getPendingTasks()).isEmpty();

        when(gps.getPosition()).thenReturn(position);

        executor.executeTasks();
        assertThat(executor.getCurrentRunningTask()).isNull();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getPendingTasks()).isEmpty();
    }

    @Test
    public void shouldDoNothingIfGpsIsUnavailable()
    {
        assertThat(executor.getCurrentRunningTask()).isNull();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getPendingTasks()).isEmpty();

        when(gps.getPosition()).thenReturn(null);

        executor.addTask(taskA);
        assertThat(executor.getCurrentRunningTask()).isNull();
        assertThat(executor.getScheduledTasks()).containsExactly(taskA);
        assertThat(executor.getPendingTasks()).isEmpty();

        executor.executeTasks();

        assertThat(executor.getCurrentRunningTask()).isNotNull();
        assertThat(executor.getScheduledTasks()).isEmpty();
        assertThat(executor.getPendingTasks()).isEmpty();
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
