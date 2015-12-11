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
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.json.JsonParser;
import org.slf4j.Logger;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.core.services.opts.Option;
import cpcc.core.services.opts.OptionsParserService;
import cpcc.core.services.opts.ParseException;
import cpcc.core.services.opts.Token;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.TaskState;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleStorage;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.js.ApplicationState;
import cpcc.vvrte.services.js.BuiltInFunctions;
import cpcc.vvrte.services.ros.MessageConverter;
import cpcc.vvrte.task.TaskAnalyzer;

/**
 * BuiltInFunctionsImpl
 */
public class BuiltInFunctionsImpl implements BuiltInFunctions
{
    private static final String SENSORS = "sensors";
    private static final String PARAMS = "params";
    private static final String ID = "id";
    private static final String VISIBILITY = "visibility";
    private static final String TYPE = "type";
    private static final String MESSAGE_TYPE = "messageType";
    private static final String DESCRIPTION = "description";
    private static final String SEQUENCE = "sequence";
    private static final String REPEAT = "repeat";

    private RosNodeService rns;
    private OptionsParserService opts;
    private MessageConverter conv;
    private VirtualVehicleMapper mapper;
    private TaskAnalyzer taskAnalyzer;
    private VvRteRepository vvRteRepo;
    private QueryManager qm;
    private HibernateSessionManager sessionManager;
    private Logger logger;

    /**
     * @param rns the ROS node service.
     * @param opts the options parser service-
     * @param conv the message converter service.
     * @param mapper the virtual vehicle mapper.
     * @param taskAnalyzer the task analyzer.
     * @param vvRteRepo the virtual vehicle repository.
     * @param qm the query manager.
     * @param sessionManager the Hibernate session manager.
     * @param logger the application logger.
     */
    public BuiltInFunctionsImpl(RosNodeService rns, OptionsParserService opts, MessageConverter conv
        , VirtualVehicleMapper mapper, TaskAnalyzer taskAnalyzer, VvRteRepository vvRteRepo, QueryManager qm
        , HibernateSessionManager sessionManager, Logger logger)
    {
        this.rns = rns;
        this.opts = opts;
        this.conv = conv;
        this.mapper = mapper;
        this.taskAnalyzer = taskAnalyzer;
        this.vvRteRepo = vvRteRepo;
        this.qm = qm;
        this.sessionManager = sessionManager;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listSensors()
    {
        logger.info("listSensors start");
        List<SensorDefinition> asd = qm.findAllVisibleSensorDefinitions();
        return converToScriptableObjectList(asd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listActiveSensors()
    {
        logger.info("listActiveSensors start");
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

        List<ScriptableObject> sensorList = new ArrayList<ScriptableObject>();

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
        sensor.put(TYPE, sensor, sd.getType());
        sensor.put(VISIBILITY, sensor, sd.getVisibility());

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
                logger.error("Parsing parameters of sensor definition " + sd.getId() + " failed!", e);
            }
        }
        return sensor;
    }

    /**
     * @param tokenList the token list.
     * @return the converted token list.
     */
    private Object convertTokenList(List<Token> tokenList)
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
        for (Token token : tokenList)
        {
            a.add(token.getValue());
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
    public ScriptableObject getSensorValue(ScriptableObject sensor)
    {
        SensorDefinition sd = qm.findSensorDefinitionByDescription((String) sensor.get(DESCRIPTION));

        if (sd == null || sd.getVisibility() == SensorVisibility.NO_VV)
        {
            return null;
        }

        AbstractRosAdapter node = rns.findAdapterNodeBySensorDefinitionId(sd.getId());
        return conv.convertMessageToJS(node.getValue());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void executeTask(ScriptableObject managementParameters, ScriptableObject taskParameters)
    {
        logger.info("*** executeTask");

        String vehicleUUID = (String) managementParameters.get("vehicleUUID");
        VirtualVehicle vehicle = vvRteRepo.findVirtualVehicleByUUID(vehicleUUID);

        if (vehicle == null)
        {
            logger.error("Can not find Virtual Vehicle for UUID " + vehicleUUID);
            return;
        }

        Task task = vehicle.getTask();
        if (task != null && task.getTaskState() == TaskState.EXECUTED)
        {
            handleExecutedTask(vehicle, managementParameters, taskParameters);
            return;
        }

        managementParameters.put(REPEAT, managementParameters, Boolean.FALSE);

        Number sequence = (Number) managementParameters.get(SEQUENCE);
        task = taskAnalyzer.analyzeTaskParameters(taskParameters, sequence.intValue());
        if (task == null)
        {
            return;
        }

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        task.setTaskState(TaskState.PENDING);
        vehicle.setTask(task);
        task.setVehicle(vehicle);
        sessionManager.getSession().saveOrUpdate(vehicle);
        sessionManager.getSession().saveOrUpdate(task);

        if (decision.isMigration())
        {
            initiateMigration(managementParameters, decision);
        }
        else
        {
            initiateTaskExecution(managementParameters, taskParameters, task);
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
        logger.info("*** handleExecutedTask");

        Task task = vehicle.getTask();

        Context cx = Context.getCurrentContext();
        Scriptable sc = cx.initStandardObjects();

        NativeObject sensorValues2;
        try
        {
            sensorValues2 = (NativeObject) new JsonParser(cx, sc).parseValue(task.getSensorValues());
        }
        catch (org.mozilla.javascript.json.JsonParser.ParseException e)
        {
            sensorValues2 = new NativeObject();
        }

        Number sequence = (Number) managementParameters.get(SEQUENCE);

        managementParameters.put("valid", managementParameters, Boolean.TRUE);
        managementParameters.put(SEQUENCE, managementParameters, Integer.valueOf(sequence.intValue() + 1));

        NativeArray sensors = (NativeArray) taskParameters.get(SENSORS);
        NativeArray sensorValues = new NativeArray(sensors.getLength());

        for (int k = 0; k < sensors.getLength(); ++k)
        {
            NativeObject s = (NativeObject) sensors.get(k);
            String description = (String) s.get(DESCRIPTION);
            sensorValues.put(k, sensorValues, sensorValues2.get(description));
            // sensorValues.put(k, sensorValues, getSensorValue(node, s));
        }

        managementParameters.put("sensorValues", managementParameters, sensorValues);
        managementParameters.put(REPEAT, managementParameters, Boolean.FALSE);

        task.setTaskState(TaskState.COMPLETED);
        // TODO check: task.setCompletedTime();

        sessionManager.getSession().saveOrUpdate(task);
        sessionManager.commit();
    }

    /**
     * @param vehicle the virtual vehicle.
     * @param managementParameters the management parameters.
     * @param taskParameters the task parameters.
     */
    private void initiateTaskExecution(ScriptableObject managementParameters, ScriptableObject taskParameters
        , Task task)
    {
        logger.info("*** no migration (execute task).");

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
        logger.info("*** initiateMigration");

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
        List<String> result = new ArrayList<String>();
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
        // String vvName = Thread.currentThread().getName().replace("VV-", "");
        // return vvRteRepo.findVirtualVehicleByName(vvName);
        String[] vv = Thread.currentThread().getName().split("-");
        return vvRteRepo.findVirtualVehicleById(Integer.parseInt(vv[1]));
    }

}
