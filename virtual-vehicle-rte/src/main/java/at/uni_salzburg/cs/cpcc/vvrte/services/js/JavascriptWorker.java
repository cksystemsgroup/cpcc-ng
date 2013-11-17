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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.serialize.ScriptableInputStream;
import org.mozilla.javascript.serialize.ScriptableOutputStream;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * JavascriptWorker
 */
public class JavascriptWorker implements Runnable
{
    private static final String VVRTE_API_FORMAT = "vvrte-api-%1$03d.js";

    /**
     * The worker state.
     */
    public enum State
    {
        INITIALIZED,
        RUNNING,
        DEFECTIVE,
        INTERRUPTED,
        FINISHED
    };

    private State state;
    private String script;
    private Set<String> allowedClasses;
    private String result = null;
    private byte[] snapshot = null;
    private int scriptStartLine;
    private Set<JavascriptWorkerStateListener> stateListeners = new HashSet<JavascriptWorkerStateListener>();

    /**
     * @param scriptSource the script source code.
     * @param apiVersion the used API version.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     * @throws IOException thrown in case of errors.
     */
<<<<<<< HEAD:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JavascriptWorker.java
    public JavascriptWorker(String scriptSource, int apiVersion, Set<String> allowedClasses) throws IOException
=======
    public JsWorker(String scriptSource, int apiVersion, Set<String> allowedClasses) throws IOException
>>>>>>> ea54c8dbe8b8727cac1510b7eee552d4b7f5c8bc:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JsWorker.java
    {
        this.allowedClasses = allowedClasses;
        String apiScript = loadApiScript(apiVersion);
        scriptStartLine = StringUtils.countMatches(apiScript, "\n") + 1;
        script = "(function(){ " + apiScript + "\n" + scriptSource + "\n})();";
<<<<<<< HEAD:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JavascriptWorker.java
        state = State.INITIALIZED;
=======
>>>>>>> ea54c8dbe8b8727cac1510b7eee552d4b7f5c8bc:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JsWorker.java
    }

    /**
     * @param snapshot the frozen program.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JavascriptWorker(byte[] snapshot, Set<String> allowedClasses)
    {
        this.snapshot = snapshot;
        this.allowedClasses = allowedClasses;
        state = State.INITIALIZED;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        changeState(State.RUNNING);

        if (snapshot == null && script == null)
        {
            changeState(State.DEFECTIVE);
            return;
        }

        Context cx = Context.enter();
        cx.setOptimizationLevel(-1);
        ScriptableObject scope = cx.initStandardObjects();
        try
        {
            cx.setClassShutter(new SandboxClassShutter(allowedClasses));
            scope.defineFunctionProperties(VvRteFunctions.FUNCTIONS, VvRteFunctions.class, ScriptableObject.DONTENUM);

            Object resultObj;
            if (snapshot == null)
            {
                Script compiledScript = cx.compileString(script, "<vehicle>", 1, null);
                resultObj = cx.executeScriptWithContinuations(compiledScript, scope);
            }
            else
            {
                ByteArrayInputStream bais = new ByteArrayInputStream(snapshot);
                ScriptableInputStream sis = new ScriptableInputStream(bais, scope);
                Scriptable globalScope = (Scriptable) sis.readObject();
                Object c = sis.readObject();
                sis.close();
                bais.close();
                resultObj = cx.resumeContinuation(c, globalScope, Boolean.TRUE);
            }

            System.out.println(Context.toString(resultObj));
            result = Context.toString(resultObj);
            changeState(State.FINISHED);
        }
        catch (ContinuationPending cp)
        {
            changeState(State.INTERRUPTED);
            Object applicationState = cp.getApplicationState();
            System.err.println("Application State " + applicationState);
            try
            {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ScriptableOutputStream sos = new ScriptableOutputStream(baos, scope);
                sos.excludeStandardObjectNames();
                sos.writeObject(scope);
                sos.writeObject(cp.getContinuation());
                sos.close();
                baos.close();
                snapshot = baos.toByteArray();

                System.err.println("snapshot is " + snapshot.length + " bytes long.");
            }
            catch (IOException e)
            {
                e.printStackTrace();
                result = e.getMessage();
                changeState(State.DEFECTIVE);
                snapshot = null;
            }
        }
        catch (RhinoException e)
        {
            result = e.getMessage() + ", line=" + (e.lineNumber() - scriptStartLine) + ":" + e.columnNumber()
                + ", source='" + e.lineSource() + "'";
            changeState(State.DEFECTIVE);
        }
        catch (ClassNotFoundException e)
        {
            result = e.getMessage();
            changeState(State.DEFECTIVE);
        }
        catch (IOException e)
        {
            result = e.getMessage();
            changeState(State.DEFECTIVE);
        }
        finally
        {
            Context.exit();
        }
    }

    /**
     * @return the API script or null in case of errors.
     * @throws IOException thrown in case of errors.
     */
    private String loadApiScript(int apiVersion) throws IOException
    {
        InputStream apiStream = this.getClass().getResourceAsStream(String.format(VVRTE_API_FORMAT, apiVersion));
        if (apiStream == null)
        {
            throw new IOException("Can not handle API version " + apiVersion);
        }

        return IOUtils.toString(apiStream, "UTF-8");
    }

    /**
     * @return the result
     */
    public String getResult()
    {
        return result;
    }

    /**
     * @return the snapshot
     */
    @SuppressFBWarnings("EI_EXPOSE_REP")
    public byte[] getSnapshot()
    {
        return snapshot;
    }

    /**
     * @return the complete script source code
     */
    public String getScript()
    {
        return script;
    }

    /**
     * @return the script start line
     */
    public int getScriptStartLine()
    {
        return scriptStartLine;
    }

    /**
     * @return the state
     */
    public State getState()
    {
        return state;
    }

    /**
     * @param newState the new state.
     */
    private void changeState(State newState)
    {
        if (state != newState)
        {
            for (JavascriptWorkerStateListener listener : stateListeners)
            {
                listener.notify(this);
            }
            state = newState;
        }
    }

    /**
     * @param listener the listener to add.
     */
<<<<<<< HEAD:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JavascriptWorker.java
    public void addStateListener(JavascriptWorkerStateListener listener)
=======
    public void awaitCompletion() throws InterruptedException
>>>>>>> ea54c8dbe8b8727cac1510b7eee552d4b7f5c8bc:virtual-vehicle-rte/src/main/java/at/uni_salzburg/cs/cpcc/vvrte/services/js/JsWorker.java
    {
        if (listener != null && !stateListeners.contains(listener))
        {
            stateListeners.add(listener);
        }
    }
    
    /**
     * @return the script
     */
    public String getScript()
    {
        return script;
    }
    
    /**
     * @return the scriptStartLine
     */
    public int getScriptStartLine()
    {
        return scriptStartLine;
    }
}
