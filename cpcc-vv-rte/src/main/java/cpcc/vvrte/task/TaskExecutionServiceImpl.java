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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.NativeObject;
import org.slf4j.Logger;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.jobs.TimeService;
import cpcc.core.utils.GeodeticSystem;
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
import cpcc.vvrte.services.db.TaskRepository;
import cpcc.vvrte.services.ros.MessageConverter;
import sensor_msgs.NavSatFix;

/**
 * TaskExecutionServiceImpl
 */
public class TaskExecutionServiceImpl implements TaskExecutionService
{
    private Task currentRunningTask = null;

    private ServiceResources serviceResources;
    private Logger logger;
    private TaskSchedulerService scheduler;
    private RosNodeService rosNodeService;
    private MessageConverter conv;
    private TimeService timeService;
    private Map<String, List<AbstractRosAdapter>> adapterNodes;
    private SimpleWayPointControllerAdapter wayPointController = null;
    private AbstractGpsSensorAdapter gpsReceiver;
    private AltimeterAdapter altimeter;
    private GeodeticSystem gs = new WGS84();
    private Set<TaskCompletionListener> listeners = new HashSet<>();

    /**
     * @param logger the application logger.
     * @param serviceResources the service resources.
     * @param scheduler the scheduler instance.
     * @param rosNodeService the ROS node service instance.
     * @param conv the ROS message converter instance.
     * @param timeService the time service instance.
     */
    public TaskExecutionServiceImpl(Logger logger, ServiceResources serviceResources, TaskSchedulerService scheduler
        , RosNodeService rosNodeService, MessageConverter conv, TimeService timeService)
    {
        this.serviceResources = serviceResources;
        this.logger = logger;
        this.scheduler = scheduler;
        this.rosNodeService = rosNodeService;
        this.conv = conv;
        this.timeService = timeService;
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
        PerthreadManager tm = serviceResources.getService(PerthreadManager.class);
        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);
        TaskRepository taskRepository = serviceResources.getService(TaskRepository.class);

        if (currentRunningTask == null)
        {
            currentRunningTask = taskRepository.getCurrentRunningTask();
        }

        if (currentRunningTask == null)
        {
            currentRunningTask = scheduler.schedule();
        }

        if (currentRunningTask == null)
        {
            // TODO after some idle time return to the depot/loiter position.
            sessionManager.commit();
            tm.cleanup();
            return;
        }

        boolean completed = false;

        wayPointController.setPosition(currentRunningTask.getPosition());

        NavSatFix pos = gpsReceiver.getPosition();
        if (pos != null)
        {
            PolarCoordinate vehiclePosition = new PolarCoordinate(pos.getLatitude(), pos.getLongitude(),
                altimeter != null ? altimeter.getValue().getData() : pos.getAltitude());

            double distance = gs.calculateDistance(currentRunningTask.getPosition(), vehiclePosition);
            currentRunningTask.setDistanceToTarget(distance);

            if (distance < currentRunningTask.getTolerance())
            {
                logger.info("Task executed: " + currentRunningTask.getPosition() + " distance=" + distance);
                completeTask(currentRunningTask);
                completed = true;
            }
            else if (currentRunningTask.getExecutionStart() == null)
            {
                currentRunningTask.setExecutionStart(timeService.newDate());
            }

            sessionManager.getSession().saveOrUpdate(currentRunningTask);
        }

        sessionManager.commit();

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

        tm.cleanup();
    }

    /**
     * @param task the task to be completed.
     */
    private void completeTask(Task task)
    {
        task.setTaskState(TaskState.EXECUTED);
        task.setExecutionEnd(timeService.newDate());

        NativeObject o = new NativeObject();

        for (SensorDefinition sd : task.getSensors())
        {
            if (sd == null || sd.getVisibility() == SensorVisibility.NO_VV)
            {
                continue;
            }

            AbstractRosAdapter node = rosNodeService.findAdapterNodeBySensorDefinitionId(sd.getId());
            o.put(sd.getDescription(), o, conv.convertMessageToJS(node.getValue()));
        }

        Context cx = Context.enter();
        try
        {
            Object sensorValues = NativeJSON.stringify(cx, cx.initStandardObjects(), o, null, null);
            task.setSensorValues((String) sensorValues);
        }
        finally
        {
            Context.exit();
        }
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
