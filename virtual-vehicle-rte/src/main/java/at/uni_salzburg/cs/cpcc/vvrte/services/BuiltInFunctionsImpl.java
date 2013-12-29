/*
 * This code is part of the CPCC-NG project. Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com> This program
 * is free software; you can redistribute it and/or modify it under the terms of the GNU General Public License as
 * published by the Free Software Foundation; either version 2 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.Transaction;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.services.opts.Option;
import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.core.services.opts.Token;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskAnalyzer;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionService;

/**
 * BuiltInFunctionsImpl
 */
public class BuiltInFunctionsImpl implements BuiltInFunctions
{
    private static final Logger LOG = LoggerFactory.getLogger(BuiltInFunctionsImpl.class);

    private RosNodeService rns;
    private OptionsParserService opts;
    private MessageConverter conv;
    private VirtualVehicleMapper mapper;
    private TaskExecutionService taskExecutor;
    private TaskAnalyzer taskAnalyzer;
    private VvRteRepository vvRteRepo;
    private QueryManager qm;
    
    /**
     * @param rns the ROS node service.
     * @param opts the options parser service-
     * @param conv the message converter service.
     * @param mapper the virtual vehicle mapper.
     * @param taskExecutor the task executor.
     * @param taskAnalyzer the task analyzer.
     * @param vvRteRepo the virtual vehicle repository.
     */
    public BuiltInFunctionsImpl(RosNodeService rns, OptionsParserService opts, MessageConverter conv,
        VirtualVehicleMapper mapper, TaskExecutionService taskExecutor, TaskAnalyzer taskAnalyzer,
        VvRteRepository vvRteRepo)
    {
        this.rns = rns;
        this.opts = opts;
        this.conv = conv;
        this.mapper = mapper;
        this.taskExecutor = taskExecutor;
        this.taskAnalyzer = taskAnalyzer;
        this.vvRteRepo = vvRteRepo;
        this.qm = vvRteRepo.getQueryManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listSensors()
    {
        LOG.info("listSensors start");
        List<SensorDefinition> asd = qm.findAllVisibleSensorDefinitions();
        return converToScriptableObjectList(asd);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listActiveSensors()
    {
        LOG.info("listActiveSensors start");
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
            return null;
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
        sensor.put("id", sensor, sd.getId());
        sensor.put("description", sensor, sd.getDescription());
        sensor.put("messageType", sensor, sd.getMessageType());
        sensor.put("type", sensor, sd.getType());
        sensor.put("visibility", sensor, sd.getVisibility());

        if (sd.getParameters() != null)
        {
            try
            {
                NativeObject params = new NativeObject();
                for (Option option : opts.parse(sd.getParameters()))
                {
                    params.put(option.getKey(), params, convertTokenList(option.getValue()));
                }
                sensor.put("params", sensor, params);
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
        SensorDefinition sd = qm.findSensorDefinitionByDescription((String) sensor.get("description"));

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
        LOG.info("executeTask1");

        managementParameters.put("repeat", managementParameters, Boolean.FALSE);

        Number sequence = (Number) managementParameters.get("sequence");

        Task task = taskAnalyzer.analyzeTaskParameters(taskParameters, sequence.intValue());
        if (task == null)
        {
            return;
        }

        VirtualVehicleMappingDecision decision = mapper.findMappingDecision(task);

        if (decision.isMigration())
        {
            LOG.info("migration");
            Context cx = Context.enter();
            try
            {
                managementParameters.put("repeat", managementParameters, Boolean.TRUE);
                ContinuationPending cp = cx.captureContinuation();
                cp.setApplicationState(decision);
                // handover the task! -> no, call execute again with same sequence number!
                throw cp;
            }
            finally
            {
                Context.exit();
            }
        }

        LOG.info("no migration");

        taskExecutor.addTask(task);
        task.awaitCompletion();

        managementParameters.put("valid", managementParameters, Boolean.TRUE);
        managementParameters.put("sequence", managementParameters, Integer.valueOf(sequence.intValue() + 1));

        NativeArray sensors = (NativeArray) taskParameters.get("sensors");
        NativeArray sensorValues = new NativeArray(sensors.getLength());

        for (int k = 0; k < sensors.getLength(); ++k)
        {
            NativeObject s = (NativeObject) sensors.get(k);
            sensorValues.put(k, sensorValues, getSensorValue(s));
        }

        managementParameters.put("sensorValues", managementParameters, sensorValues);
        managementParameters.put("repeat", managementParameters, Boolean.valueOf(!task.isLastInTaskGroup()));
        return;
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

        Transaction t = vvRteRepo.getSession().beginTransaction();
        vvRteRepo.saveOrUpdate(item);
        t.commit();
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

        Transaction t = vvRteRepo.getSession().beginTransaction();
        vvRteRepo.delete(item);
        t.commit();
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
