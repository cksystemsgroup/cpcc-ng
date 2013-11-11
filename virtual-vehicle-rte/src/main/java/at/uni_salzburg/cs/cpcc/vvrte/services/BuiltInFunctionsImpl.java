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
import java.util.List;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.persistence.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.utilities.opts.Option;
import at.uni_salzburg.cs.cpcc.utilities.opts.OptionsParserService;
import at.uni_salzburg.cs.cpcc.utilities.opts.ParseException;
import at.uni_salzburg.cs.cpcc.utilities.opts.Token;
import at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions;

/**
 * VvRteFunctions
 */
public class BuiltInFunctionsImpl implements BuiltInFunctions
{
    private static final Logger LOG = LoggerFactory.getLogger(BuiltInFunctionsImpl.class);

    private boolean migrate = false;

    private QueryManager qm;
    private RosNodeService rns;
    private OptionsParserService opts;
    private MessageConverter conv;

    /**
     * @param migrate the migrate to set
     */
    public void setMigrate(boolean migrate)
    {
        this.migrate = migrate;
    }

    /**
     * @param qm the query manager
     * @param rns the ROS node service.
     * @param opts the options parser service-
     * @param conv the message converter service.
     */
    public BuiltInFunctionsImpl(QueryManager qm, RosNodeService rns, OptionsParserService opts, MessageConverter conv)
    {
        this.qm = qm;
        this.rns = rns;
        this.opts = opts;
        this.conv = conv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listSensors()
    {
        System.out.println("listSensors start");
        List<SensorDefinition> asd = qm.findAllSensorDefinitions();
        return converToScriptableObjectList(asd);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public List<ScriptableObject> listActiveSensors()
    {
        System.out.println("listActiveSensors start");
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
        // TODO a lot!
        System.out.println("executeTask1");
        if (!verifyTaskParameters(taskParameters))
        {
            managementParameters.put("repeat", managementParameters, Boolean.FALSE);
            return;
        }

        System.out.println("executeTask2");

        Number sequence = (Number) managementParameters.get("sequence");
        if (sequence.intValue() == 0)
        {
            // TODO decide for migration or not.
            // TODO migration: initiate migration by throwing CP-Exception.

            if (migrate)
            {
                System.out.println("migration");
                Context cx = Context.enter();
                try
                {
                    ContinuationPending cp = cx.captureContinuation();
                    cp.setApplicationState("migration");
                    throw cp;
                }
                finally
                {
                    Context.exit();
                }
            }

            System.out.println("no migration");

            // TODO no migration: schedule task and wait for completion.

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
            managementParameters.put("repeat", managementParameters, Boolean.TRUE);
            return;
        }

        // String type = (String) taskParameters.get("type");
        Number tolerance = (Number) taskParameters.get("tolerance");
        tolerance.doubleValue();

        // NativeArray sensors = (NativeArray) taskParameters.get("sensors");

        // TODO Auto-generated method stub
        managementParameters.put("repeat", managementParameters, Boolean.FALSE);
        return;
    }

    private boolean verifyTaskParameters(ScriptableObject taskParameters)
    {
        // TODO Auto-generated method stub
        Object sensors = taskParameters.get("sensors");
        Object type = taskParameters.get("type");

        if (sensors == null || !(sensors instanceof NativeArray) || ((NativeArray) sensors).getLength() == 0)
        {
            return false;
        }

        if (type == null || !(type instanceof String) || !"point".equalsIgnoreCase((String) type))
        {
            return false;
        }

        // !('sensors' in taskParams) || !('length' in taskParams.sensors) 
        // || taskParams.sensors.length == 0 || taskParams.type)

        return true;
    }
}
