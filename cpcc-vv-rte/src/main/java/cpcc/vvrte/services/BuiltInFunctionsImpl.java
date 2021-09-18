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

package cpcc.vvrte.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.Option;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.ParseException;
import cpcc.core.services.opts.Token;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.TaskState;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.js.ApplicationState;
import cpcc.vvrte.services.js.BuiltInFunctions;
import cpcc.vvrte.services.task.TaskAnalyzer;

/**
 * BuiltInFunctionsImpl
 */
public class BuiltInFunctionsImpl implements BuiltInFunctions
{
    private static final String VALID = "valid";

    private static final String SENSOR_VALUES = "sensorValues";

    private static final Logger LOG = LoggerFactory.getLogger(BuiltInFunctionsImpl.class);

    private static final String SENSORS = "sensors";
    private static final String PARAMS = "params";
    private static final String ID = "id";
    private static final String VISIBILITY = "visibility";
    private static final String TYPE = "type";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String DESCRIPTION = "description";
    private static final String SEQUENCE = "sequence";
    private static final String REPEAT = "repeat";

    private OptionsParserService opts;
    private VirtualVehicleMapper mapper;
    private TaskAnalyzer taskAnalyzer;
    private VvRteRepository vvRteRepo;
    private QueryManager qm;
    private HibernateSessionManager sessionManager;

    /**
     * @param opts the options parser service-
     * @param mapper the virtual vehicle mapper.
     * @param taskAnalyzer the task analyzer.
     * @param vvRteRepo the virtual vehicle repository.
     * @param qm the query manager.
     * @param sessionManager the Hibernate session manager.
     */
    public BuiltInFunctionsImpl(OptionsParserService opts, VirtualVehicleMapper mapper, TaskAnalyzer taskAnalyzer,
        VvRteRepository vvRteRepo, QueryManager qm, HibernateSessionManager sessionManager)
    {
        this.opts = opts;
        this.mapper = mapper;
        this.taskAnalyzer = taskAnalyzer;
        this.vvRteRepo = vvRteRepo;
        this.qm = qm;
        this.sessionManager = sessionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listSensors()
    {
        LOG.debug("listSensors start");
        List<SensorDefinition> asd = qm.findAllVisibleSensorDefinitions();
        return converToScriptableObjectList(asd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listActiveSensors()
    {
        LOG.debug("listActiveSensors start");
        List<SensorDefinition> asd = qm.findAllActiveSensorDefinitions();
        return converToScriptableObjectList(asd);
    }

    /**
     * @param sensorDefinitionList the list of sensor definitions.
     * @return the converted sensor definitions as list of scriptable objects.
     */
    private List<ScriptableObject> converToScriptableObjectList(List<SensorDefinition> sensorDefinitionList)
    {
        if (sensorDefinitionList == null)
        {
            return Collections.emptyList();
        }

        List<ScriptableObject> sensorList = new ArrayList<>();

        for (SensorDefinition sd : sensorDefinitionList)
        {
            sensorList.add(convertSensorDefinition(sd));
        }

        return sensorList;
    }

    /**
     * @param sd the sensor definition.
     * @return the sensor definition as NativeObject.
     */
    private NativeObject convertSensorDefinition(SensorDefinition sd)
    {
        NativeObject sensor = new NativeObject();
        sensor.put(ID, sensor, sd.getId());
        sensor.put(DESCRIPTION, sensor, sd.getDescription());
        sensor.put(MESSAGE_TYPE, sensor, sd.getMessageType());
        sensor.put(TYPE, sensor, sd.getType().name());
        sensor.put(VISIBILITY, sensor, sd.getVisibility().name());

        if (sd.getParameters() != null)
        {
            try
            {
                NativeObject params = new NativeObject();
                for (Option option : opts.parse(sd.getParameters()))
                {
                    params.put(option.getKey(), params, convertTokenList(option.getValue()));
                }
                sensor.put(PARAMS, sensor, params);
            }
            catch (IOException | ParseException e)
            {
                LOG.error("Parsing parameters of sensor definition " + sd.getId() + " failed!", e);
            }
        }
        return sensor;
    }

    /**
     * @param tokenList the token list.
     * @return the converted token list.
     */
    static Object convertTokenList(List<Token> tokenList)
    {
        if (tokenList.isEmpty())
        {
            return null;
        }

        if (tokenList.size() == 1)
        {
            return tokenList.get(0).getValue();
        }

        NativeArray a = new NativeArray(tokenList.size());
        for (int k = 0; k < tokenList.size(); ++k)
        {
            a.put(k, a, tokenList.get(k).getValue());
        }

        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScriptableObject getSensor(String description)
    {
        SensorDefinition sd = qm.findSensorDefinitionByDescription(description);

        if (sd == null || sd.getVisibility() == SensorVisibility.NO_VV)
        {
            return null;
        }

        return convertSensorDefinition(sd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeTask(ScriptableObject managementParameters, ScriptableObject taskParameters)
    {
        LOG.debug("*** executeTask");

        String vehicleUUID = (String) managementParameters.get("vehicleUUID");
        VirtualVehicle vehicle = vvRteRepo.findVirtualVehicleByUUID(vehicleUUID);

        if (vehicle == null)
        {
            LOG.error("Can not find Virtual Vehicle for UUID {}", vehicleUUID);
            managementParameters.put(REPEAT, managementParameters, Boolean.FALSE);
            return;
        }

        Task task = vehicle.getTask();
        if (task != null && task.getTaskState() == TaskState.EXECUTED)
        {
            handleExecutedTask(vehicle, managementParameters, taskParameters);
            return;
        }

        Number sequence = (Number) managementParameters.get(SEQUENCE);
        task = taskAnalyzer.analyzeTaskParameters(taskParameters, sequence.intValue());
        if (task == null)
        {
            LOG.debug("*** nullTask");
            managementParameters.put(REPEAT, managementParameters, Boolean.FALSE);
            return;
        }

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        if (decision.isMigration())
        {
            initiateMigration(managementParameters, decision);
        }
        else
        {
            task.setTaskState(TaskState.PENDING);
            vehicle.setTask(task);
            task.setVehicle(vehicle);
            sessionManager.getSession().saveOrUpdate(vehicle);
            sessionManager.getSession().saveOrUpdate(task);
            initiateTaskExecution(taskParameters, task);
        }
    }

    /**
     * @param vehicle the virtual vehicle.
     * @param managementParameters the management parameters.
     * @param taskParameters the task parameters.
     */
    private void handleExecutedTask(VirtualVehicle vehicle, ScriptableObject managementParameters,
        ScriptableObject taskParameters)
    {
        LOG.debug("*** handleExecutedTask");

        Task task = vehicle.getTask();

        ScriptableObject sensorValues2 = task.getSensorValues();

        Number sequence = (Number) managementParameters.get(SEQUENCE);

        managementParameters.put(VALID, managementParameters, Boolean.TRUE);
        managementParameters.put(SEQUENCE, managementParameters, Integer.valueOf(sequence.intValue() + 1));

        NativeArray sensors = (NativeArray) taskParameters.get(SENSORS);
        NativeArray sensorValues = new NativeArray(sensors.getLength());

        for (int k = 0; k < sensors.getLength(); ++k)
        {
            NativeObject s = (NativeObject) sensors.get(k);
            String description = (String) s.get(DESCRIPTION);
            sensorValues.put(k, sensorValues, sensorValues2.get(description));
        }

        managementParameters.put(SENSOR_VALUES, managementParameters, sensorValues);
        managementParameters.put(REPEAT, managementParameters, Boolean.FALSE);

        task.setTaskState(TaskState.COMPLETED);
        // TODO check: task.setCompletedTime();

        sessionManager.getSession().saveOrUpdate(task);
        sessionManager.commit();
    }

    /**
     * @param managementParameters the management parameters.
     * @param task the task.
     */
    private void initiateTaskExecution(ScriptableObject managementParameters, Task task)
    {
        LOG.debug("*** no migration (execute task).");

        Context cx = Context.enter();
        try
        {
            managementParameters.put(REPEAT, managementParameters, Boolean.TRUE);
            ContinuationPending cp = cx.captureContinuation();
            cp.setApplicationState(new ApplicationState(task));
            // handover the task! -> no, call execute again with same sequence number!
            throw cp;
        }
        finally
        {
            Context.exit();
        }
    }

    private void initiateMigration(ScriptableObject managementParameters, VirtualVehicleMappingDecision decision)
    {
        LOG.debug("*** initiateMigration");

        Context cx = Context.enter();
        try
        {
            managementParameters.put(REPEAT, managementParameters, Boolean.TRUE);
            ContinuationPending cp = cx.captureContinuation();
            cp.setApplicationState(new ApplicationState(decision));
            // handover the task! -> no, call execute again with same sequence number!
            throw cp;
        }
        finally
        {
            Context.exit();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> listObjects(String pattern)
    {
        List<String> result = new ArrayList<>();
        List<String> itemNames = vvRteRepo.findAllStorageItemNames();
        for (String name : itemNames)
        {
            if (name.matches(pattern))
            {
                result.add(name);
            }
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ScriptableObject loadObject(String name)
    {
        VirtualVehicle virtualVehicle = findVirtualVehicle();
        VirtualVehicleStorage item = vvRteRepo.findStorageItemByVirtualVehicleAndName(virtualVehicle, name);
        return item.getContent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void storeObject(String name, ScriptableObject obj)
    {
        VirtualVehicle virtualVehicle = findVirtualVehicle();
        VirtualVehicleStorage item = vvRteRepo.findStorageItemByVirtualVehicleAndName(virtualVehicle, name);
        if (item == null)
        {
            item = new VirtualVehicleStorage();
            item.setName(name);
            item.setVirtualVehicle(virtualVehicle);
        }

        item.setModificationTime(new Date());
        item.setContent(obj);

        sessionManager.getSession().saveOrUpdate(item);
        sessionManager.commit();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeObject(String name)
    {
        VirtualVehicle virtualVehicle = findVirtualVehicle();
        VirtualVehicleStorage item = vvRteRepo.findStorageItemByVirtualVehicleAndName(virtualVehicle, name);
        if (item == null)
        {
            return;
        }

        sessionManager.getSession().delete(item);
        sessionManager.commit();
    }

    /**
     * @return the virtual vehicle associated to the current thread.
     */
    private VirtualVehicle findVirtualVehicle()
    {
        String vv = Thread.currentThread().getName().replaceAll("^.*\\((\\d+)\\).*$", "$1");
        return vvRteRepo.findVirtualVehicleById(Integer.parseInt(vv));
    }

}
