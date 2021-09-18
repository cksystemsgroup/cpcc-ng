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

package cpcc.vvrte.services.task;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.ros.node.NodeConfiguration;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.ros.actuators.AbstractActuatorAdapter;
import cpcc.ros.actuators.ActuatorType;
import cpcc.ros.actuators.SimpleWayPointControllerAdapter;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.sensors.AbstractSensorAdapter;
import cpcc.ros.sensors.AltimeterAdapter;
import cpcc.ros.sensors.SensorType;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import sensor_msgs.NavSatFix;
import std_msgs.Float32;

/**
 * TaskExecutionServiceTest
 */
class TaskExecutionServiceTest
{
    private Task taskA;
    private Task taskB;
    private Task taskC;
    private Task taskD;
    private TaskExecutionService sut;
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
    private PolarCoordinate posVehicle;
    private PolarCoordinate posA;
    private PolarCoordinate posB;
    private PolarCoordinate posC;
    private PolarCoordinate posD;
    private ServiceResources serviceResources;
    private PerthreadManager threadManager;
    private HibernateSessionManager sessionManager;
    private Session session;
    private MessageConverter conv;
    private TimeService timeService;
    private RealVehicleRepository rvRepo;
    private TaskRepository taskRepository;
    private RealVehicle hostingRv;

    /**
     * Test setup.
     */
    @BeforeEach
    void setUp()
    {
        posVehicle = mock(PolarCoordinate.class);
        when(posVehicle.toString()).thenReturn("posVehicle");

        posA = mock(PolarCoordinate.class);
        when(posA.getLatitude()).thenReturn(47.1234);
        when(posA.getLongitude()).thenReturn(13.7897);
        when(posA.getAltitude()).thenReturn(8.0);
        when(posA.toString()).thenReturn("posA");

        taskA = mock(Task.class);
        when(taskA.getPosition()).thenReturn(posA);
        when(taskA.getCreationTime()).thenReturn(new Date(1L));
        when(taskA.getTolerance()).thenReturn(10.0);
        when(taskA.toString()).thenReturn("taskA (47.1234, 13.7897, 8), time=1");

        posB = mock(PolarCoordinate.class);
        when(posB.getLatitude()).thenReturn(47.2345);
        when(posB.getLongitude()).thenReturn(13.1234);
        when(posB.getAltitude()).thenReturn(23.0);
        when(posB.toString()).thenReturn("posB");

        taskB = mock(Task.class);
        when(taskB.getPosition()).thenReturn(posB);
        when(taskB.getCreationTime()).thenReturn(new Date(2L));
        when(taskB.getTolerance()).thenReturn(10.0);
        when(taskB.toString()).thenReturn("taskB (47.2345, 13.1234, 23), time=2");

        posC = mock(PolarCoordinate.class);
        when(posC.getLatitude()).thenReturn(47.3345);
        when(posC.getLongitude()).thenReturn(13.5234);
        when(posC.getAltitude()).thenReturn(13.0);
        when(posC.toString()).thenReturn("posC");

        taskC = mock(Task.class);
        when(taskC.getPosition()).thenReturn(posC);
        when(taskC.getCreationTime()).thenReturn(new Date(3L));
        when(taskC.getTolerance()).thenReturn(10.0);
        when(taskC.toString()).thenReturn("taskC (47.3345, 13.5234, 13), time=3");

        posD = mock(PolarCoordinate.class);
        when(posD.getLatitude()).thenReturn(47.4345);
        when(posD.getLongitude()).thenReturn(13.3234);
        when(posD.getAltitude()).thenReturn(18.0);
        when(posD.toString()).thenReturn("posD");

        taskD = mock(Task.class);
        when(taskD.getPosition()).thenReturn(posD);
        when(taskD.getCreationTime()).thenReturn(new Date(4L));
        when(taskD.getTolerance()).thenReturn(10.0);
        when(taskD.toString()).thenReturn("taskD (47.4345, 13.3234, 18), time=4");

        float32altitude = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(Float32._TYPE);
        float32altitude.setData((float) posA.getAltitude());

        position = NodeConfiguration.newPrivate().getTopicMessageFactory().newFromType(NavSatFix._TYPE);
        position.setLatitude(posA.getLatitude());
        position.setLongitude(posA.getLongitude());
        position.setAltitude(posA.getAltitude());

        wpc = mock(SimpleWayPointControllerAdapter.class);
        when(wpc.getType()).thenReturn(ActuatorType.SIMPLE_WAYPOINT_CONTROLLER);
        when(wpc.isConnectedToAutopilot()).thenReturn(true);

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

        conv = mock(MessageConverter.class);

        timeService = mock(TimeService.class);

        scheduler = mock(TaskSchedulerService.class);

        threadManager = mock(PerthreadManager.class);

        session = mock(Session.class);

        sessionManager = mock(HibernateSessionManager.class);
        when(sessionManager.getSession()).thenReturn(session);

        hostingRv = mock(RealVehicle.class);
        when(hostingRv.getName()).thenReturn("RV0001");
        when(hostingRv.getId()).thenReturn(1);

        rvRepo = mock(RealVehicleRepository.class);
        when(rvRepo.findOwnRealVehicle()).thenReturn(hostingRv);

        taskRepository = mock(TaskRepository.class);
        when(taskRepository.countAllIncompleteTasks()).thenReturn(0L);

        serviceResources = mock(ServiceResources.class);
        when(serviceResources.getService(PerthreadManager.class)).thenReturn(threadManager);
        when(serviceResources.getService(HibernateSessionManager.class)).thenReturn(sessionManager);
        when(serviceResources.getService(TaskRepository.class)).thenReturn(taskRepository);

        sut = new TaskExecutionServiceImpl(serviceResources, scheduler, rosNodeService, conv, timeService, rvRepo);
    }

    /**
     * When initializing the task executor should query the ROS node services for active adapter nodes.
     */
    @Test
    void shouldRetrieveAdapterInformationOnStartUp()
    {
        verify(rosNodeService).getAdapterNodes();
    }

    /**
     * When initializing the task executor should have found a way point controller.
     */
    @Test
    void shouldRetrieveWayPointControllerOnStartUp()
    {
        assertThat(sut.getWayPointController()).isSameAs(wpc);
        assertThat(sut.getWayPointController().getType()).isEqualTo(ActuatorType.SIMPLE_WAYPOINT_CONTROLLER);
    }

    /**
     * When initializing the task executor should have found a GPS receiver. It knows the position in WGS84 coordinates.
     */
    @Test
    void shouldHaveDetectedGpsReceiverOnStartUp()
    {
        assertThat(sut.getGpsReceiver()).isSameAs(gps);
        assertThat(sut.getGpsReceiver().getType()).isEqualTo(SensorType.GPS_RECEIVER);
    }

    /**
     * When initializing the task executor should have found an altimeter. It knows the altitude above ground.
     */
    @Test
    void shouldHaveDetectedAltimeterOnStartUp()
    {
        assertThat(sut.getAltimeter()).isSameAs(altimeter);
        assertThat(sut.getAltimeter().getType()).isEqualTo(SensorType.ALTIMETER);
    }

    @Test
    void shouldLoadNewTaskFromScheduler()
    {
        // TODO check
        when(scheduler.schedule(any(), any())).thenReturn(taskB);

        sut.executeTasks();

        verify(wpc).setPosition(posB);
        verify(gps).getPosition();

        verify(sessionManager).commit();
        verify(threadManager).cleanup();
    }

    @Test
    void shouldLoadCompletedTaskFromScheduler()
    {
        when(altimeter.getValue()).thenReturn(float32altitude);
        when(scheduler.schedule(any(), any())).thenReturn(taskA);

        TaskCompletionListener listener = mock(TaskCompletionListener.class);

        sut.addListener(listener);

        sut.executeTasks();

        verify(wpc).setPosition(posA);
        verify(gps).getPosition();
        verify(listener).notify(any());

        verify(sessionManager).commit();
        verify(threadManager).cleanup();
    }

    @Test
    void shouldDoNothingOnMissingTasks()
    {
        sut.executeTasks();

        ArgumentCaptor<PolarCoordinate> pos = ArgumentCaptor.forClass(PolarCoordinate.class);

        verify(scheduler).schedule(pos.capture(), any());

        PolarCoordinate actual = pos.getValue();
        assertThat(actual.getLatitude()).isEqualTo(position.getLatitude(), offset(1E-8));
        assertThat(actual.getLongitude()).isEqualTo(position.getLongitude(), offset(1E-8));
        assertThat(actual.getAltitude()).isEqualTo(position.getAltitude(), offset(1E-8));

        verify(sessionManager).commit();
        verify(threadManager).cleanup();
    }
}
