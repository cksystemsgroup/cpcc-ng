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
 * JsWorker
 */
public class JsWorker implements Runnable
{
    private static final String VVRTE_API_FORMAT = "vvrte-api-%1$03d.js";

    private String script;
    private Set<String> allowedClasses;
    private String result = null;
    private boolean initialized = true;
    private boolean running = false;
    private boolean defective = false;
    private boolean interrupted = false;
    private byte[] snapshot = null;

    private int scriptStartLine;

    /**
     * @param scriptSource the script source code.
     * @param apiVersion the used API version.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     * @throws IOException thrown in case of errors.
     */
    public JsWorker(String scriptSource, int apiVersion, Set<String> allowedClasses) throws IOException
    {
        this.allowedClasses = allowedClasses;
        String apiScript = loadApiScript(apiVersion);
        scriptStartLine = StringUtils.countMatches(apiScript, "\n") + 1;
        script = "(function(){ " + apiScript + "\n" + scriptSource + "\n})();";
    }

    /**
     * @param snapshot the frozen program.
     * @param allowedClasses additionally allowed classes to be accessed via JavaScript.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public JsWorker(byte[] snapshot, Set<String> allowedClasses)
    {
        this.snapshot = snapshot;
        this.allowedClasses = allowedClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        running = true;
        initialized = false;

        if (snapshot == null && script == null)
        {
            running = false;
            defective = true;
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
        }
        catch (ContinuationPending cp)
        {
            interrupted = true;
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
                defective = true;
                snapshot = null;
            }
        }
        catch (RhinoException e)
        {
            result = e.getMessage() + ", line=" + (e.lineNumber() - scriptStartLine) + ":" + e.columnNumber()
                + ", source='" + e.lineSource() + "'";
            defective = true;
        }
        catch (ClassNotFoundException e)
        {
            result = e.getMessage();
            defective = true;
        }
        catch (IOException e)
        {
            result = e.getMessage();
            defective = true;
        }
        finally
        {
            Context.exit();
        }
        running = false;
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
     * @return the initialized
     */
    public boolean isInitialized()
    {
        return initialized;
    }

    /**
     * @return true if the worker is running, false otherwise.
     */
    public boolean isRunning()
    {
        return running;
    }

    /**
     * @return the defective
     */
    public boolean isDefective()
    {
        return defective;
    }

    /**
     * @return the interrupted
     */
    public boolean isInterrupted()
    {
        return interrupted;
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
     * @throws InterruptedException in case of an interruption.
     */
    public void awaitCompletion() throws InterruptedException
    {
        while (isInitialized() || isRunning())
        {
            Thread.sleep(100);
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
