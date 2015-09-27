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

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.tapestry5.ioc.ServiceResources;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.Script;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JavascriptServiceImpl
 */
public class JavascriptServiceImpl implements JavascriptService
{
    private static final Logger LOG = LoggerFactory.getLogger(JavascriptServiceImpl.class);

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    private Set<String> allowedClasses = new HashSet<String>();
    private Set<String> allowedClassesRegex = new HashSet<String>();

    private ServiceResources serviceResources;

    /**
     * @param serviceResources the service resources.
     * @param functions the built-in functions to use.
     */
    public JavascriptServiceImpl(ServiceResources serviceResources, BuiltInFunctions functions)
    {
        this.serviceResources = serviceResources;
        
        VvRteFunctions.setVvRte(functions);
        // VvRteFunctions.setStdOut(System.out);
        
        try
        {
            if (!ContextFactory.hasExplicitGlobal())
            {
                ContextFactory.initGlobal(new SandboxContextFactory());
            }
        }
        catch (IllegalStateException e)
        {
            LOG.debug("ContextFactory already initialized");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavascriptWorker createWorker(String script, int apiVersion) throws IOException
    {
        return new JavascriptWorker(serviceResources, script, apiVersion, allowedClasses, allowedClassesRegex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JavascriptWorker createWorker(byte[] continuation)
    {
        return new JavascriptWorker(serviceResources, continuation, allowedClasses, allowedClassesRegex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAllowedClass(String className)
    {
        allowedClasses.add(className);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addAllowedClassRegex(String regex)
    {
        allowedClassesRegex.add(regex);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] codeVerification(String script, int apiVersion) throws IOException
    {
        JavascriptWorker w = 
            new JavascriptWorker(serviceResources, script, apiVersion, allowedClasses, allowedClassesRegex);
        String completedScript = w.getScript();

        Context cx = Context.enter();
        cx.setOptimizationLevel(-1);

        try
        {
            Script compiledScript = cx.compileString(completedScript, "<check>", 1, null);
            if (compiledScript != null)
            {
                return EMPTY_OBJECT_ARRAY;
            }
        }
        catch (RhinoException e)
        {
            return new Object[]{e.columnNumber(), e.lineNumber() - w.getScriptStartLine(), e.details(), e.lineSource()};
        }
        finally
        {
            Context.exit();
        }

        return new Object[]{0, 0, "Can not compile script!", ""};
    }
}
