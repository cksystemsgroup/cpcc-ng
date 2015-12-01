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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.PolarCoordinate;
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
import sensor_msgs.NavSatFix;

/**
 * TaskExecutionServiceImpl
 */
public class TaskExecutionServiceImpl implements TaskExecutionService
{
    private static final Logger LOG = LoggerFactory.getLogger(TaskExecutionServiceImpl.class);

    private List<Task> pendingTasks = Collections.synchronizedList(new ArrayList<Task>());
    private List<Task> scheduledTasks = Collections.synchronizedList(new ArrayList<Task>());
    private Task currentRunningTask = null;

    private TaskSchedulerService scheduler;
    private RosNodeService rosNodeService;
    private Map<String, List<AbstractRosAdapter>> adapterNodes;
    private SimpleWayPointControllerAdapter wayPointController = null;
    private AbstractGpsSensorAdapter gpsReceiver;
    private AltimeterAdapter altimeter;
    private GeodeticSystem gs = new WGS84();

    /**
     * @param scheduler the task scheduler.
     * @param rosNodeService the ROS node service.
     */
    public TaskExecutionServiceImpl(TaskSchedulerService scheduler, RosNodeService rosNodeService)
    {
        this.scheduler = scheduler;
        this.rosNodeService = rosNodeService;
        init();
    }

    /**
     * Initialize the task execution service.
     */
    private void init()
    {
        adapterNodes = rosNodeService.getAdapterNodes();

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
        if (currentRunningTask == null)
        {
            if (scheduledTasks.isEmpty() && !pendingTasks.isEmpty())
            {
                scheduler.schedule(scheduledTasks, pendingTasks);
            }

            if (scheduledTasks.isEmpty())
            {
                return;
            }

            currentRunningTask = scheduledTasks.get(0);
            scheduledTasks.remove(0);
        }

        wayPointController.setPosition(currentRunningTask);

        NavSatFix pos = gpsReceiver.getPosition();
        if (pos == null)
        {
            return;
        }

        PolarCoordinate vehiclePosition = new PolarCoordinate(pos.getLatitude(), pos.getLongitude(),
            altimeter != null ? altimeter.getValue().getData() : pos.getAltitude());

        double distance = gs.calculateDistance(currentRunningTask, vehiclePosition);
        currentRunningTask.setDistanceToTarget(distance);

        if (distance < currentRunningTask.getTolerance())
        {
            LOG.info("Task completed: " + ((PolarCoordinate) currentRunningTask) + " distance=" + distance);
            currentRunningTask.setCompleted();
            currentRunningTask = null;
        }
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void addTask(Task task)
    {
        synchronized (pendingTasks)
        {
            pendingTasks.add(task);
        }

        scheduler.schedule(scheduledTasks, pendingTasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPendingTasks()
    {
        return Collections.unmodifiableList(pendingTasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getScheduledTasks()
    {
        return Collections.unmodifiableList(scheduledTasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getCurrentRunningTask()
    {
        return currentRunningTask;
    }
}
