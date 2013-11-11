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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;

import sensor_msgs.NavSatFix;
import at.uni_salzburg.cs.cpcc.ros.actuators.AbstractActuatorAdapter;
import at.uni_salzburg.cs.cpcc.ros.actuators.ActuatorType;
import at.uni_salzburg.cs.cpcc.ros.actuators.SimpleWayPointControllerAdapter;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AbstractGpsSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AbstractSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AltimeterAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.SensorType;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.utilities.GeodeticSystem;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.utilities.WGS84;

/**
 * TaskExecutionServiceImpl
 */
public class TaskExecutionServiceImpl extends TimerTask implements TaskExecutionService
{
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
    private double toleranceRadius;

    /**
     * @param scheduler the task scheduler.
     * @param rosNodeService the ROS node service.
     * @param timerService the timer service.
     */
    public TaskExecutionServiceImpl(TaskSchedulerService scheduler, RosNodeService rosNodeService,
        TimerService timerService)
    {
        this.scheduler = scheduler;
        this.rosNodeService = rosNodeService;
        init();
        timerService.periodicSchedule(this, 1000);
    }

    /**
     * Initialize the task execution service.
     */
    private void init()
    {
        // TODO make toleranceRadius configurable.
        toleranceRadius = 3;

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
    public void run()
    {
        if (currentRunningTask == null && scheduledTasks.isEmpty())
        {
            return;
        }
        
        if (currentRunningTask == null)
        {
            currentRunningTask = scheduledTasks.get(0);
            wayPointController.setPosition(currentRunningTask.getPosition());
            scheduledTasks.remove(0);
        }

        NavSatFix pos = gpsReceiver.getPosition();
        if (pos == null)
        {
            return;
        }
        
        PolarCoordinate vehiclePosition = new PolarCoordinate(pos.getLatitude(), pos.getLongitude(),
            altimeter != null ? altimeter.getValue().getData() : pos.getAltitude());

        double distance = gs.calculateDistance(currentRunningTask.getPosition(), vehiclePosition);

        if (distance < toleranceRadius)
        {
            currentRunningTask = null;
        }
        
        // TODO now the action points of the tasks are in the trajectory, but no "action" takes place yet. 
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
        pendingTasks.add(task);
        scheduler.schedule(scheduledTasks, pendingTasks);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPendingTasks()
    {
        return pendingTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getScheduledTasks()
    {
        return scheduledTasks;
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
