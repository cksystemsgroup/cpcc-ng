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

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
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
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * JavascriptServiceTest
 */
public class JavascriptServiceTest
{
    @Test
    public void shouldExecuteSimpleJS() throws InterruptedException, IOException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JavascriptWorker x = jss.createWorker("function f(x){return x+1} f(7)", 1);
        MyWorkerStateListener wl = new MyWorkerStateListener();
        x.addStateListener(wl);
        x.run();

        assertThat(x.getResult()).isNotNull().isEqualTo("undefined");
        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.FINISHED);
        assertThat(wl.getWorker()).isNotNull().isEqualTo(x);
    }

    @Test
    public void shouldNotExecuteNaughtyScript() throws InterruptedException, IOException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JavascriptWorker x = jss.createWorker("java.lang.System.currentTimeMillis()", 1);
        x.run();

        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.DEFECTIVE);
        assertThat(x.getResult()).isNotNull().startsWith("TypeError: Cannot call property currentTimeMillis in object");
    }

    @Test
    public void shouldDenyWrongApiVersion() throws InterruptedException, IOException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        catchException(jss).createWorker("function f(x){return x+1} f(7)", 1000);

        assertThat(caughtException()).isInstanceOf(IOException.class);
        assertThat(caughtException().getMessage()).isEqualTo("Can not handle API version 1000");
    }

    @Test
    public void shouldHandleVvRte() throws IOException, InterruptedException
    {
        MyBuiltInFunctions functions = new MyBuiltInFunctions();
        JavascriptService jss = new JavascriptServiceImpl();
        jss.setVvRteFunctions(functions);
        jss.addAllowedClass("org.apache.maven.surefire.report.ConsoleOutputCapture$ForwardingPrintStream");

        InputStream scriptStream = this.getClass().getResourceAsStream("simple-vv.js");
        String script = IOUtils.toString(scriptStream, "UTF-8");
        assertThat(script).isNotNull().isNotEmpty();

        functions.setMigrate(true);
        JavascriptWorker x = jss.createWorker(script, 1);
        x.run();

        System.out.println("shouldHandleVvRte() result1: '" + x.getResult() + "'");
        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.INTERRUPTED);

        functions.setMigrate(false);
        byte[] snapshot = x.getSnapshot();
        x = jss.execute(snapshot);
        x.run();
        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.FINISHED);

        System.out.println("shouldHandleVvRte() result2: '" + x.getResult() + "'");
    }

    @DataProvider
    public static Object[][] emptyScriptDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"\n"},
            new Object[]{"\n\n\n\n\r\n"},
        };
    }

    @Test(dataProvider = "emptyScriptDataProvider")
    public void shouldHandleEmptyScript(String script) throws IOException, InterruptedException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JavascriptWorker x = jss.createWorker(script, 1);
        x.run();
        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.FINISHED);
    }

    @Test(dataProvider = "emptyScriptDataProvider")
    public void shouldCompileEmptyScript(String script) throws IOException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        Object[] result = jss.codeVerification(script, 1);
        assertThat(result).isNull();
    }

    @Test
    public void shouldHandleNullContinuation() throws InterruptedException
    {
        JavascriptService jss = new JavascriptServiceImpl();
        JavascriptWorker x = jss.execute(null);
        x.run();
        assertThat(x.getState()).isNotNull().isEqualTo(JavascriptWorker.State.DEFECTIVE);
    }

    @Test
    public void shouldReturnScriptWithApiPrefix() throws IOException
    {
        String script = "function f(x){return x+1} f(7)";
        JavascriptService jss = new JavascriptServiceImpl();
        JavascriptWorker x = jss.createWorker(script, 1);
        assertThat(x.getScript()).isNotNull().endsWith(script + "\n})();");
    }

    @Test
    public void shouldNotCompileErroneousScript() throws IOException
    {
        String script = "var x = 0;\nx x x";
        JavascriptService jss = new JavascriptServiceImpl();
        Object[] result = jss.codeVerification(script, 1);
        assertThat(result).isNotNull();

        Integer column = (Integer) result[0];
        Integer line = (Integer) result[1];
        String errorMessage = (String) result[2];
        String sourceLine = (String) result[3];

        assertThat(column).isNotNull().isEqualTo(4);
        assertThat(line).isNotNull().isEqualTo(2);
        assertThat(errorMessage).isNotNull().isEqualTo("missing ; before statement");
        assertThat(sourceLine).isNotNull().isEqualTo("x x x");
    }

    @Test
    public void shouldCompileProperScript() throws IOException
    {
        String script = "function f(x){return x+1} f(7)";
        JavascriptService jss = new JavascriptServiceImpl();
        Object[] result = jss.codeVerification(script, 1);
        assertThat(result).isNull();
    }

    /**
     * MyBuiltInFunctions
     */
    private static class MyBuiltInFunctions implements BuiltInFunctions
    {

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
            // TODO fix this.
            return listActiveSensors();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public List<ScriptableObject> listActiveSensors()
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

    /**
     * MyWorkerStateListener
     */
    private static class MyWorkerStateListener implements JavascriptWorkerStateListener
    {
        private JavascriptWorker worker = null;

        /**
         * {@inheritDoc}
         */
        @Override
        public void notify(JavascriptWorker worker)
        {
            this.worker = worker;
        }

        /**
         * @return the worker
         */
        public JavascriptWorker getWorker()
        {
            return worker;
        }
    }
}
