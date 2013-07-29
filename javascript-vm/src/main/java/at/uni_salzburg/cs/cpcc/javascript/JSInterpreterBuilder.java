/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2012  Clemens Krainer
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
package at.uni_salzburg.cs.cpcc.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.mozilla.javascript.ClassShutter;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContextFactory;
import org.mozilla.javascript.Scriptable;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.runtime.base.BuiltinFunctions;
import at.uni_salzburg.cs.cpcc.javascript.runtime.base.NullPositionProvider;
import at.uni_salzburg.cs.cpcc.javascript.runtime.base.SystemConsoleProviderImpl;
import at.uni_salzburg.cs.cpcc.javascript.runtime.sandbox.SandboxContextFactory;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.LatLngAlt;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.SensorValue;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.VirtualVehicleActionPoint;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.VirtualVehicleFile;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.VirtualVehiclePrintWriter;

/**
 * JSInterpreterBuilder
 */
public class JSInterpreterBuilder
{

    private String[] builtinFns = {
        "getpos", "println", "migrate", "sleep", "random", "flyTo"
    };

    @SuppressWarnings("serial")
    private static final Set<Class<? extends Scriptable>> builtinTypes = new HashSet<Class<? extends Scriptable>>()
    {
        {
            add(LatLngAlt.class);
            add(VirtualVehicleFile.class);
            add(VirtualVehiclePrintWriter.class);
            add(SensorValue.class);
            add(VirtualVehicleActionPoint.class);
        }
    };

    private Map<String, Object> providedPackages = new HashMap<String, Object>();

    private Set<Class<? extends Scriptable>> providedTypes = new HashSet<Class<? extends Scriptable>>();

    private Map<String, InputStream> codeFiles = new HashMap<String, InputStream>();

    /**
     * @param packageScope
     * @param o
     */
    public void addProvidedPackage(String packageScope, Object o)
    {
        providedPackages.put(packageScope, o);
    }

    /**
     * @param type
     */
    public void addProvidedTypes(Class<? extends Scriptable> type)
    {
        providedTypes.add(type);
    }

    /**
     * @param name
     * @param inputStream
     */
    public void addCodefile(String name, InputStream inputStream)
    {
        codeFiles.put(name, inputStream);
    }

    /**
     * @return
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     * @throws IOException
     */
    public JSInterpreter build() throws IllegalAccessException, InstantiationException, InvocationTargetException,
        IOException
    {

        ContextFactory contextFactory = new SandboxContextFactory();

        if (!ContextFactory.hasExplicitGlobal())
        {
            ContextFactory.initGlobal(contextFactory);
        }
        Context context = ContextFactory.getGlobal().enterContext();

        ScriptableObject contextScope = context.initStandardObjects();
        JSInterpreter.setContextScope(contextScope);

        for (Entry<String, Object> e : providedPackages.entrySet())
        {
            Object wrappedOut = Context.javaToJS(e.getValue(), contextScope);
            ScriptableObject.putProperty(contextScope, e.getKey(), wrappedOut);
        }

        context.setClassShutter(new MyClassShutter(providedPackages.keySet()));

        context.setOptimizationLevel(-1);

        contextScope.defineFunctionProperties(builtinFns, BuiltinFunctions.class, ScriptableObject.DONTENUM);

        for (Class<? extends Scriptable> bt : builtinTypes)
        {
            ScriptableObject.defineClass(contextScope, bt, true);
        }

        for (Class<? extends Scriptable> bt : providedTypes)
        {
            ScriptableObject.defineClass(contextScope, bt, true);
        }

        for (Entry<String, InputStream> e : codeFiles.entrySet())
        {
            context.evaluateReader(contextScope, new InputStreamReader(e.getValue()), e.getKey(), 1, null);
        }

        JSInterpreter jsi = new JSInterpreter();

        jsi.setConsoleProvider(new SystemConsoleProviderImpl());
        jsi.setPositionProvider(new NullPositionProvider());

        return jsi;
    }

    /**
     * MyClassShutter
     */
    private class MyClassShutter implements ClassShutter
    {

        private Set<String> allowedPackages;

        /**
         * @param allowedPackages
         */
        public MyClassShutter(Set<String> allowedPackages)
        {
            this.allowedPackages = allowedPackages;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean visibleToScripts(String className)
        {
            for (String allowed : allowedPackages)
            {
                if (allowed.startsWith(className))
                {
                    return true;
                }
            }
            return false;
        }
    }

}
