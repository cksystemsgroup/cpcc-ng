/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2012  Clemens Krainer, Michael Lippautz
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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.runtime.base.ConsoleProvider;
import at.uni_salzburg.cs.cpcc.javascript.runtime.base.InstructionCountObserver;
import at.uni_salzburg.cs.cpcc.javascript.runtime.base.PositonProvider;
import at.uni_salzburg.cs.cpcc.javascript.runtime.sandbox.SandboxContextFactory;

/**
 * JSInterpreter
 */
public class JSInterpreter
{
    private static Map<Context, ScriptableObject> contextScope =
        Collections.synchronizedMap(new HashMap<Context, ScriptableObject>());

    private static Map<Context, ConsoleProvider> consoleProviders =
        Collections.synchronizedMap(new HashMap<Context, ConsoleProvider>());

    private static Map<Context, PositonProvider> positionProviders =
        Collections.synchronizedMap(new HashMap<Context, PositonProvider>());

    private static Map<Context, Properties> contextProperties =
        Collections.synchronizedMap(new HashMap<Context, Properties>());

    /**
     * @param cx
     * @return
     */
    public static ScriptableObject getContextScope(Context cx)
    {
        return contextScope.get(cx);
    }

    /**
     * @param scope
     */
    public static void setContextScope(ScriptableObject scope)
    {
        contextScope.put(Context.getCurrentContext(), scope);
    }

    /**
     * close
     */
    public void close()
    {
        Context.exit();
    }

    /**
     * @return
     */
    public static ConsoleProvider getConsoleProvider()
    {
        return consoleProviders.get(Context.getCurrentContext());
    }

    /**
     * @param consoleProvider
     */
    public void setConsoleProvider(ConsoleProvider consoleProvider)
    {
        consoleProviders.put(Context.getCurrentContext(), consoleProvider);
    }

    /**
     * @return
     */
    public static PositonProvider getPositionProvider()
    {
        return positionProviders.get(Context.getCurrentContext());
    }

    /**
     * @param positionProvider
     */
    public void setPositionProvider(PositonProvider positionProvider)
    {
        positionProviders.put(Context.getCurrentContext(), positionProvider);
    }

    /**
     * @return
     */
    public static Properties getContextProperties()
    {
        return contextProperties.get(Context.getCurrentContext());
    }

    /**
     * @param props
     */
    public static void setContextProperties(Properties props)
    {
        Context cx = Context.getCurrentContext();
        contextProperties.put(cx, props);
    }

    /**
     * @param threshold
     */
    public void setInstructionObserverThreshold(int threshold)
    {
        Context cx = Context.getCurrentContext();
        cx.setInstructionObserverThreshold(threshold);
    }

    /**
     * @param obs
     */
    public void addInstructionCountObserver(InstructionCountObserver obs)
    {
        SandboxContextFactory.addInstructionCountObserver(Context.getCurrentContext(), obs);
    }

    /**
     * @return
     * @throws IOException
     */
    public byte[] start() throws IOException
    {
        Context context = Context.getCurrentContext();
        ScriptableObject scope = getContextScope(context);
        try
        {
            Function f = (Function) (scope.get("VV", scope));
            context.callFunctionWithContinuations(f, scope, new Object[1]);
        }
        catch (ContinuationPending cp)
        {
            Object o = cp.getApplicationState();
            System.err.println("Application State " + o);
            return Utils.serialize(cp.getContinuation(), scope);
        }
        return null;
    }

    /**
     * @param snapshot
     * @return
     * @throws IOException
     */
    public byte[] start(byte[] snapshot) throws IOException
    {
        Context context = Context.getCurrentContext();
        ScriptableObject scope = getContextScope(context);
        try
        {
            Object c = Utils.deserialize(snapshot, scope);
            context.resumeContinuation(c, scope, null);
        }
        catch (ContinuationPending cp)
        {
            return Utils.serialize(cp.getContinuation(), scope);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
