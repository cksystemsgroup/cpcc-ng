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

package cpcc.vvrte.services.js;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.hibernate.HibernateSessionManager;
import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.services.PerthreadManager;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;
import org.mozilla.javascript.serialize.ScriptableInputStream;
import org.mozilla.javascript.serialize.ScriptableOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.utils.ExceptionFormatter;
import cpcc.vvrte.entities.VirtualVehicleState;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * JavascriptWorker
 */
public class JavascriptWorker extends Thread
{
    private static final Logger LOG = LoggerFactory.getLogger(JavascriptWorker.class);

    private static final String VVRTE_API_FORMAT = "vvrte-api-%1$03d.js";

    private ServiceResources serviceResources;
    private VirtualVehicleState workerState;
    private String script;
    private Set<String> allowedClasses;
    private Set<String> allowedClassesRegex;
    private String result = null;
    private byte[] snapshot = null;
    private int scriptStartLine;
    private Set<JavascriptWorkerStateListener> stateListeners = new HashSet<JavascriptWorkerStateListener>();
    private Object applicationState;

    /**
     * @param serviceResources the service resources.
     * @param scriptSource the script source code.
     * @param apiVersion the used API version.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     * @param allowedClassesRegex additionally allowed class names defined by regular expressions.
     * @throws IOException thrown in case of errors.
     */
    public JavascriptWorker(ServiceResources serviceResources, String scriptSource, int apiVersion
        , Set<String> allowedClasses, Set<String> allowedClassesRegex) throws IOException
    {
        this.serviceResources = serviceResources;
        this.allowedClasses = allowedClasses;
        this.allowedClassesRegex = allowedClassesRegex;

        String apiScript = loadApiScript(apiVersion);
        scriptStartLine = StringUtils.countMatches(apiScript, "\n") + 1;
        script = "(function(){ " + apiScript + "\n" + scriptSource + "\n})();";
        workerState = VirtualVehicleState.INIT;
    }

    /**
     * @param serviceResources the service resources.
     * @param snapshot the frozen program.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     * @param allowedClassesRegex additionally allowed class names defined by regular expressions.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JavascriptWorker(ServiceResources serviceResources, byte[] snapshot, Set<String> allowedClasses
        , Set<String> allowedClassesRegex)
    {
        this.serviceResources = serviceResources;
        this.snapshot = snapshot;
        this.allowedClasses = allowedClasses;
        this.allowedClassesRegex = allowedClassesRegex;

        workerState = VirtualVehicleState.INIT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        applicationState = null;

        changeState(VirtualVehicleState.RUNNING);

        if (snapshot == null && script == null)
        {
            changeState(VirtualVehicleState.DEFECTIVE);
            return;
        }

        Context cx = Context.enter();
        cx.setOptimizationLevel(-1);
        ScriptableObject scope = cx.initStandardObjects();
        try
        {
            cx.setClassShutter(new SandboxClassShutter(allowedClasses, allowedClassesRegex));
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

            LOG.info("Result obj: " + Context.toString(resultObj));
            result = Context.toString(resultObj);
            changeState(VirtualVehicleState.FINISHED);
        }
        catch (ContinuationPending cp)
        {
            applicationState = cp.getApplicationState();
            LOG.info("Application State " + applicationState);
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

                changeState(VirtualVehicleState.INTERRUPTED);
                LOG.info("snapshot is " + snapshot.length + " bytes long.");
            }
            catch (IOException e)
            {
                result = ExceptionFormatter.toString(e);
                snapshot = null;
                changeState(VirtualVehicleState.DEFECTIVE);
            }
        }
        catch (RhinoException e)
        {
            result = e.getMessage() + ", line=" + (e.lineNumber() - scriptStartLine) + ":" + e.columnNumber()
                + ", source='" + e.lineSource() + "'";
            changeState(VirtualVehicleState.DEFECTIVE);
        }
        catch (ClassNotFoundException | IOException | NoSuchMethodError e)
        {
            result = ExceptionFormatter.toString(e);
            changeState(VirtualVehicleState.DEFECTIVE);
        }
        finally
        {
            Context.exit();
            serviceResources.getService(HibernateSessionManager.class).commit();
            serviceResources.getService(PerthreadManager.class).cleanup();
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
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose.")
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
    public VirtualVehicleState getWorkerState()
    {
        return workerState;
    }

    /**
     * @return the application state
     */
    public Object getApplicationState()
    {
        return applicationState;
    }

    /**
     * @param newState the new state.
     */
    private void changeState(VirtualVehicleState newState)
    {
        if (workerState != newState)
        {
            for (JavascriptWorkerStateListener listener : stateListeners)
            {
                listener.notify(this, newState);
            }

            workerState = newState;
        }
    }

    /**
     * @param listener the listener to add.
     */
    public void addStateListener(JavascriptWorkerStateListener listener)
    {
        if (listener != null && !stateListeners.contains(listener))
        {
            stateListeners.add(listener);
        }
    }

}
