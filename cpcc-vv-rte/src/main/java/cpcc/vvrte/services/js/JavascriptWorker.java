// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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
import java.nio.charset.StandardCharsets;
import java.util.Collections;
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

import cpcc.core.utils.ExceptionFormatter;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * JavascriptWorker
 */
public class JavascriptWorker extends Thread
{
    private static final String DEFECTIVE_VV = "Defective: VV={} ({} / {})";

    private static final String VVRTE_API_FORMAT = "vvrte-api-%1$03d.js";

    private Logger logger;
    private ServiceResources serviceResources;
    private VirtualVehicleState workerState;
    private String script;
    private Set<String> allowedClassesRegex;
    private String result = null;
    private byte[] snapshot = null;
    private int scriptStartLine;
    private Set<JavascriptWorkerStateListener> stateListeners = new HashSet<>();
    private ApplicationState applicationState;
    private VirtualVehicle vehicle;

    /**
     * @param vehicle the Virtual Vehicle.
     * @param useContinuation true if the available continuation data should be applied.
     * @param logger the application logger.
     * @param serviceResources the service resources instance.
     * @param allowedClassesRegex the additionally allowed class names as a regular expression.
     * @throws IOException in case of errors.
     */
    public JavascriptWorker(VirtualVehicle vehicle, boolean useContinuation, Logger logger,
        ServiceResources serviceResources, Set<String> allowedClassesRegex)
        throws IOException
    {
        this.logger = logger;
        this.serviceResources = serviceResources;
        this.allowedClassesRegex = allowedClassesRegex;
        this.vehicle = vehicle;

        workerState = VirtualVehicleState.INIT;

        if (useContinuation && vehicle.getContinuation() != null)
        {
            snapshot = vehicle.getContinuation();
        }
        else
        {
            String apiScript = loadApiScript();
            scriptStartLine = StringUtils.countMatches(apiScript, "\n") + 1;

            String code = vehicle.getCode();
            script = StringUtils.isNotBlank(code)
                ? "(function(){ " + apiScript + "\n" + code.replace("\\n", "\n") + "\n})();"
                : null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        String name = Thread.currentThread().getName();
        Thread.currentThread().setName("Virtual Vehicle: " + vehicle.getName() + " (" + vehicle.getId() + ")");

        HibernateSessionManager sessionManager = serviceResources.getService(HibernateSessionManager.class);

        applicationState = null;

        changeState(sessionManager, VirtualVehicleState.RUNNING);

        if (snapshot == null && script == null)
        {
            changeState(sessionManager, VirtualVehicleState.DEFECTIVE);
            sessionManager.commit();
            serviceResources.getService(PerthreadManager.class).cleanup();
            return;
        }

        Context cx = Context.enter();
        cx.setOptimizationLevel(-1);
        ScriptableObject scope = cx.initStandardObjects();
        try
        {
            cx.setClassShutter(new SandboxClassShutter(Collections.emptySet(), allowedClassesRegex));
            scope.defineFunctionProperties(VvRteFunctions.FUNCTIONS, VvRteFunctions.class, ScriptableObject.DONTENUM);

            Object resultObj;
            if (snapshot == null)
            {
                Script compiledScript = cx.compileString(script, "<vehicle>", 1, null);
                resultObj = cx.executeScriptWithContinuations(compiledScript, scope);
            }
            else
            {
                try (ByteArrayInputStream bais = new ByteArrayInputStream(snapshot);
                    ScriptableInputStream sis = new ScriptableInputStream(bais, scope))
                {
                    Scriptable globalScope = (Scriptable) sis.readObject();
                    Object c = sis.readObject();
                    resultObj = cx.resumeContinuation(c, globalScope, Boolean.TRUE);
                }
            }

            result = Context.toString(resultObj);
            logger.info("Result obj: {}", result);

            changeState(sessionManager, VirtualVehicleState.FINISHED);
        }
        catch (ContinuationPending cp)
        {
            logger.info("Application State {}", cp.getApplicationState());
            applicationState = (ApplicationState) cp.getApplicationState();
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

                if (applicationState.isTask())
                {
                    changeState(sessionManager, VirtualVehicleState.TASK_COMPLETION_AWAITED);
                }
                else
                {
                    changeState(sessionManager, VirtualVehicleState.INTERRUPTED);
                }

                logger.info("snapshot is {} bytes long.", snapshot.length);
            }
            catch (IOException e)
            {
                logger.error(DEFECTIVE_VV, vehicle.getName(), vehicle.getId(), vehicle.getUuid(), e);
                result = ExceptionFormatter.toString(e);
                snapshot = null;
                changeState(sessionManager, VirtualVehicleState.DEFECTIVE);
            }
        }
        catch (RhinoException e)
        {
            logger.error(DEFECTIVE_VV, vehicle.getName(), vehicle.getId(), vehicle.getUuid(), e);

            result = e.getMessage() + ", line=" + (e.lineNumber() - scriptStartLine) + ":" + e.columnNumber()
                + ", source='" + e.lineSource() + "'";
            changeState(sessionManager, VirtualVehicleState.DEFECTIVE);
        }
        catch (ClassNotFoundException | IOException | NoSuchMethodError e)
        {
            logger.error(DEFECTIVE_VV, vehicle.getName(), vehicle.getId(), vehicle.getUuid(), e);
            result = ExceptionFormatter.toString(e);
            changeState(sessionManager, VirtualVehicleState.DEFECTIVE);
        }
        finally
        {
            Context.exit();
            sessionManager.commit();
            serviceResources.getService(PerthreadManager.class).cleanup();
            Thread.currentThread().setName(name);
        }
    }

    /**
     * @throws IOException thrown in case of errors.
     */
    private String loadApiScript() throws IOException
    {
        int apiVersion = vehicle.getApiVersion();

        InputStream apiStream = this.getClass().getResourceAsStream(String.format(VVRTE_API_FORMAT, apiVersion));
        if (apiStream == null)
        {
            throw new IOException("Can not handle API version " + apiVersion);
        }

        return IOUtils.toString(apiStream, StandardCharsets.UTF_8.name()).replace("%vehicleUUID%", vehicle.getUuid());
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
     * @param sessionManager the Hibernate session manager.
     * @param newState the new state.
     */
    private void changeState(HibernateSessionManager sessionManager, VirtualVehicleState newState)
    {
        if (workerState != newState)
        {
            sessionManager.commit();

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
