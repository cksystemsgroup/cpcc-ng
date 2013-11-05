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
package at.uni_salzburg.cs.cpcc.vvrte.services.js;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.Test;

/**
 * JavascriptServiceTest
 */
public class JavascriptServiceTest
{
    @Test
    public void shouldExecuteSimpleJS() throws InterruptedException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JsWorker x = jss.execute("function f(x){return x+1} f(7)", 1);
        x.awaitCopmletion();

        assertThat(x.getResult()).isNotNull().isEqualTo("undefined");
        assertThat(x.isDefective()).isFalse();
    }

    @Test
    public void shouldNotExecuteNaughtyScript() throws InterruptedException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JsWorker x = jss.execute("java.lang.System.currentTimeMillis()", 1);
        x.awaitCopmletion();

        assertThat(x.isDefective()).isTrue();
        assertThat(x.getResult()).isNotNull().startsWith("TypeError: Cannot call property currentTimeMillis in object");
    }

    @Test
    public void shouldDenyWrongApiVersion() throws InterruptedException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JsWorker x = jss.execute("function f(x){return x+1} f(7)", 1000);
        x.awaitCopmletion();

        assertThat(x.isDefective()).overridingErrorMessage("VV should be defective.").isTrue();
        assertThat(x.getResult()).isNotNull().isEqualTo("Can not handle API version 1000");
    }

    @Test
    public void shouldHandleVvRte() throws IOException, InterruptedException
    {
        // BuiltInFunctions functions = mock(BuiltInFunctions.class);

        MyBuiltInFunctions functions = new MyBuiltInFunctions();
        JavascriptService jss = new JavascriptServiceImpl();
        jss.setVvRteFunctions(functions);
        jss.addAllowedClass("org.apache.maven.surefire.report.ConsoleOutputCapture$ForwardingPrintStream");

        InputStream scriptStream = this.getClass().getResourceAsStream("simple-vv.js");
        String script = IOUtils.toString(scriptStream, "UTF-8");
        assertThat(script).isNotNull().isNotEmpty();

        functions.setMigrate(true);
        JsWorker x = jss.execute(script, 1);
        x.awaitCopmletion();
        System.out.println("shouldHandleVvRte() result1: '" + x.getResult() + "'");
        assertThat(x.isInterrupted()).isTrue();

        functions.setMigrate(false);
        byte[] snapshot = x.getSnapshot();
        x = jss.execute(snapshot);
        x.awaitCopmletion();
        assertThat(x.isInterrupted()).isFalse();

        System.out.println("shouldHandleVvRte() result2: '" + x.getResult() + "'");
        assertThat(x.isDefective()).isFalse();
    }

    /**
     * MyBuiltInFunctions
     */
    private static class MyBuiltInFunctions implements BuiltInFunctions
    {
        private static final long serialVersionUID = 1611279004878357751L;

        private boolean migrate = false;

        /**
         * @param migrate the migrate to set
         */
        public void setMigrate(boolean migrate)
        {
            this.migrate = migrate;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<ScriptableObject> listSensors()
        {
            System.out.println("listSensors start");

            NativeObject barometer = new NativeObject();
            barometer.put("name", barometer, "barometer");

            NativeObject camera = new NativeObject();
            camera.put("name", camera, "camera");

            NativeObject thermometer = new NativeObject();
            thermometer.put("name", thermometer, "thermometer");

            // NativeArray sensors = new NativeArray(3);
            List<ScriptableObject> sensors = new ArrayList<ScriptableObject>();
            sensors.add(barometer);
            sensors.add(thermometer);
            sensors.add(camera);

            // System.out.println("listSensors end");
            return sensors;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ScriptableObject getSensor(String name)
        {
            NativeObject sensor = new NativeObject();
            sensor.put("name", sensor, name);

            // System.out.println("getSensor");
            return sensor;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ScriptableObject getSensorValue(ScriptableObject sensor)
        {
            NativeObject sensorValue = new NativeObject();
            sensorValue.put("name", sensorValue, sensor.get("name"));
            sensorValue.put("value", sensorValue, "value");

            System.out.println("getSensorValue for " + sensor.get("name"));
            return sensorValue;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void executeTask(ScriptableObject managementParameters, ScriptableObject taskParameters)
        {
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

            // !('sensors' in taskParams) || !('length' in taskParams.sensors) || taskParams.sensors.length == 0 || taskParams.type)

            // TODO Auto-generated method stub
            return true;
        }
    }
}
