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

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.mozilla.javascript.NativeObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.RealVehicleUtils;
import cpcc.core.utils.WGS84;
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
import cpcc.vvrte.entities.TaskState;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import sensor_msgs.NavSatFix;

/**
 * TaskExecutionServiceImpl
 */
public class TaskExecutionServiceImpl implements TaskExecutionService
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskExecutionServiceImpl.class);

    private Task currentRunningTask = null;

    private ServiceResources serviceResources;
    private TaskSchedulerService scheduler;
    private RosNodeService rosNodeService;
    private MessageConverter conv;
    private TimeService timeService;
    private SimpleWayPointControllerAdapter wayPointController = null;
    private AbstractGpsSensorAdapter gpsReceiver;
    private AltimeterAdapter altimeter;
    private GeodeticSystem gs = new WGS84();
    private Set<TaskCompletionListener> listeners = new HashSet<>();
    private RealVehicleRepository rvRepo;
    private RealVehicle myself;

    private List<PolarCoordinate> depotPositions;

    /**
     * @param serviceResources the service resources.
     * @param scheduler the scheduler instance.
     * @param rosNodeService the ROS node service instance.
     * @param conv the ROS message converter instance.
     * @param timeService the time service instance.
     * @param rvRepo the Real Vehicle repository instance.
     */
    public TaskExecutionServiceImpl(ServiceResources serviceResources, TaskSchedulerService scheduler,
        RosNodeService rosNodeService, MessageConverter conv, TimeService timeService, RealVehicleRepository rvRepo)
    {
        this.serviceResources = serviceResources;
        this.scheduler = scheduler;
        this.rosNodeService = rosNodeService;
        this.conv = conv;
        this.timeService = timeService;
        this.rvRepo = rvRepo;

        init();
    }

    /**
     * Initialize the task execution service.
     */
    private void init()
    {
        Map<String, List<AbstractRosAdapter>> adapterNodes = rosNodeService.getAdapterNodes();

        for (Map.Entry<String, List<AbstractRosAdapter>> entry : adapterNodes.entrySet())
        {
            List<AbstractRosAdapter> adapterList = entry.getValue();
            for (AbstractRosAdapter adapter : adapterList)
            {
                if (!adapter.isConnectedToAutopilot())
                {
                    continue;
                }

                if (adapter instanceof AbstractActuatorAdapter)
                {
                    initActuators(adapter);
                }
                else if (adapter instanceof AbstractSensorAdapter)
                {
                    initSensors(adapter);
                }
            }
        }

        myself = rvRepo.findOwnRealVehicle();

        String areaOfOperation = myself != null ? myself.getAreaOfOperation() : "";

        try
        {
            depotPositions = RealVehicleUtils
                .getDepotPositions(areaOfOperation)
                .stream().map(x -> new PolarCoordinate(x.getLatitude(), x.getLongitude(), 0.0))
                .collect(Collectors.toList());
        }
        catch (IOException e)
        {
            depotPositions = Collections.emptyList();
        }
    }

    /**
     * @param adapter the ROS adapter.
     */
    private void initSensors(AbstractRosAdapter adapter)
    {
        AbstractSensorAdapter sensor = (AbstractSensorAdapter) adapter;
        if (sensor.getType() == SensorType.GPS_RECEIVER)
        {
            gpsReceiver = (AbstractGpsSensorAdapter) sensor;
        }
        else if (sensor.getType() == SensorType.ALTIMETER)
        {
            altimeter = (AltimeterAdapter) sensor;
        }
    }

    /**
     * @param adapter the ROS adapter.
     */
    private void initActuators(AbstractRosAdapter adapter)
    {
        AbstractActuatorAdapter actuator = (AbstractActuatorAdapter) adapter;
        if (actuator.getType() == ActuatorType.SIMPLE_WAYPOINT_CONTROLLER)
        {
            wayPointController = (SimpleWayPointControllerAdapter) actuator;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeTasks()
    {
        PerthreadManager tm = serviceResources.getService(PerthreadManager.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);

        logUnfinishedTasks();

        PolarCoordinate vehiclePosition = getCurrentVehiclePosition();

        if (currentRunningTask == null)
        {
            currentRunningTask = scheduler.schedule(vehiclePosition, depotPositions);
        }

        if (currentRunningTask == null)
        {
            // TODO after some idle time return to the depot/loiter position.
            sessionManager.commit();
            tm.cleanup();
            return;
        }

        wayPointController.setPosition(currentRunningTask.getPosition());

        if (vehiclePosition != null)
        {
            if (currentRunningTask.getExecutionStart() == null)
            {
                currentRunningTask.setExecutionStart(timeService.newDate());
            }

            double distance = gs.calculateDistance(currentRunningTask.getPosition(), vehiclePosition);
            currentRunningTask.setDistanceToTarget(distance);

            boolean completed = false;
            if (distance < currentRunningTask.getTolerance())
            {
                completeTask(currentRunningTask);
                logExecutionCompleted(currentRunningTask, distance);
                completed = true;
            }

            sessionManager.getSession().update(currentRunningTask);

            try
            {
                sessionManager.commit();
            }
            catch (RuntimeException e)
            {
                LOG.error("Buggerit!", e);
            }

            if (completed)
            {
                try
                {
                    notify(currentRunningTask);
                }
                finally
                {
                    currentRunningTask = null;
                }
            }
        }

        tm.cleanup();
    }

    private void logUnfinishedTasks()
    {
        TaskRepository repo = serviceResources.getService(TaskRepository.class);
        LOG.info("Unfinished tasks: ;time;{};name;{};id;{};incompleteTasks;{}",
            System.currentTimeMillis(), myself.getName(), myself.getId(), repo.countAllIncompleteTasks());
    }

    private void logExecutionCompleted(Task task, double distance)
    {
        String vv = vvToString(task.getVehicle());
        String creationTimeStr = dateFormatter(task.getCreationTime());
        String executionStartStr = dateFormatter(task.getExecutionStart());
        String executionEndStr = dateFormatter(task.getExecutionEnd());
        String positionStr = positionToString(task.getPosition());

        LOG.info("Task executed: ;{};{};{};{};{};{};",
            vv, creationTimeStr, executionStartStr, executionEndStr, positionStr, distance);
    }

    private static String dateFormatter(Date date)
    {
        return date != null ? new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date) : "";
    }

    private static String vvToString(VirtualVehicle vv)
    {
        return vv != null ? vv.getName() + ";" + vv.getUuid() : ";";
    }

    private static String positionToString(PolarCoordinate pos)
    {
        return pos != null ? pos.getLatitude() + ";" + pos.getLongitude() + ";" + pos.getAltitude() : ";;";
    }

    /**
     * @return the current vehicle position or null
     */
    private PolarCoordinate getCurrentVehiclePosition()
    {
        NavSatFix pos = gpsReceiver.getPosition();

        if (pos == null)
        {
            return null;
        }

        return new PolarCoordinate(pos.getLatitude(), pos.getLongitude(),
            altimeter != null ? altimeter.getValue().getData() : pos.getAltitude());
    }

    /**
     * @param task the task to be completed.
     */
    private void completeTask(Task task)
    {
        task.setTaskState(TaskState.EXECUTED);
        task.setExecutionEnd(timeService.newDate());

        NativeObject sensorValues = new NativeObject();

        task.getSensors().stream()
            .filter(Objects::nonNull)
            .filter(sd -> sd.getVisibility() != SensorVisibility.NO_VV)
            .forEach(sd -> sensorValues.put(sd.getDescription(), sensorValues,
                conv.convertMessageToJS(rosNodeService.findAdapterNodeBySensorDefinitionId(sd.getId()).getValue())));

        // SensorDefinition a = task.getSensors().get(0);
        // AbstractRosAdapter b = rosNodeService.findAdapterNodeBySensorDefinitionId(a.getId());
        // Message v = b.getValue();
        // TODO check me!

        task.setSensorValues(sensorValues);
    }

    /**
     * @param task the completed task.
     */
    private void notify(Task task)
    {
        listeners.stream().forEach(listener -> listener.notify(task));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addListener(TaskCompletionListener listener)
    {
        listeners.add(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractActuatorAdapter getWayPointController()
    {
        return wayPointController;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractSensorAdapter getGpsReceiver()
    {
        return gpsReceiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public AbstractSensorAdapter getAltimeter()
    {
        return altimeter;
    }

}
