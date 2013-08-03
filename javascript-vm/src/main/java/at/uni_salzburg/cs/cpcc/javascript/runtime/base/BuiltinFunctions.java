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
package at.uni_salzburg.cs.cpcc.javascript.runtime.base;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ContinuationPending;
import org.mozilla.javascript.Function;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.Scriptable;

import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.LatLngAlt;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.SensorValue;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.VirtualVehicleActionPoint;

/**
 * BuiltinFunctions
 */
public final class BuiltinFunctions implements Serializable
{
    private static final long serialVersionUID = -5978941480045547826L;

    private BuiltinFunctions()
    {
        // intentionally empty.
    }

    /**
     * Function acquires the latest available position and returns immediately.
     * 
     * @name getpos
     * @function
     * @memberOf _global_
     * @return The latest available position.
     * @throws Exception thrown in case of errors.
     */
    public static LatLngAlt getpos() throws Exception
    {
        PositonProvider positionProvider = JSInterpreter.getPositionProvider();
        if (positionProvider == null)
        {
            throw new NullPointerException("positionprovider == null");
        }
        return positionProvider.getCurrentPosition();
    }

    /**
     * Prints all provided arguments, followed by a newline.
     * 
     * @name println
     * @function
     * @memberOf _global_
     * @param cx the interpreter context.
     * @param thisObj the reference to the JavaScript object.
     * @param args the arguments.
     * @param funObj funObj
     */
    public static void println(Context cx, Scriptable thisObj, Object[] args, Function funObj)
    {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (Object o : args)
        {
            if (first)
            {
                sb.append(o.toString());
                first = false;
            }
            else
            {
                sb.append(" ").append(o.toString());
            }
        }
        JSInterpreter.getConsoleProvider().print(sb.toString() + "\n");
    }

    /**
     * Causes the currently executing thread to sleep (temporarily cease execution) for the specified number of
     * milliseconds.
     * 
     * @name sleep
     * @function
     * @memberOf _global_
     * @param arg the length of time to sleep in milliseconds.
     */
    public static void sleep(Object arg)
    {
        long millis = ((Double) arg).longValue();
        try
        {
            Thread.sleep(millis);
        }
        catch (InterruptedException e)
        {
            // we don't care
            e.printStackTrace();
        }
    }

    /**
     * Migrate back to the groundstation and initialize a mapping step.
     * 
     * @name migrate
     * @function
     * @memberOf _global_
     */
    public static void migrate()
    {
        Context cx = Context.enter();
        try
        {
            ContinuationPending cp = cx.captureContinuation();
            throw cp;
        }
        finally
        {
            Context.exit();
        }
    }

    /**
     * Returns a double value with a positive sign, greater than or equal to 0.0 and less than 1.0. Returned values are
     * chosen pseudo-randomly with (approximately) uniform distribution from that range.
     * 
     * @name sleep
     * @function
     * @memberOf _global_
     * @return the next pseudorandom, uniformly distributed float value between 0.0 and 1.0 from the Math.random()
     *         sequence.
     */
    public static double random()
    {
        return RandomUtils.nextDouble();
    }

    /**
     * Fly to the next action point. Arguments are one instance of LatLngAlt followed by one or more sensor names
     * (Strings).
     * 
     * @name flyTo
     * @function
     * @memberOf _global_
     * @param ctx the interpreter context.
     * @param thisObj the reference to the JavaScript object.
     * @param args the arguments.
     * @param funObj funObj
     * @return a <code>VirtualVehicleActionPoint</code> instance.
     */
    public static VirtualVehicleActionPoint flyTo(Context ctx, Scriptable thisObj, Object[] args, Function funObj)
    {

        if (args.length < 3 || args[0] == null || args[1] == null || !(args[0] instanceof LatLngAlt)
            || !(args[1] instanceof Double))
        {
            throw new IllegalArgumentException(
                "Too few parameters or first parameter is not a LatLngAlt instance or "
                    + "second parameter is not a number.");
        }

        LatLngAlt latLonAlt = (LatLngAlt) args[0];
        Double tolerance = (Double) args[1];
        List<SensorValue> s = new ArrayList<SensorValue>();
        for (int k = 2; k < args.length; ++k)
        {
            if (args[k] != null && !"undefined".equals(args[k]))
            {
                SensorValue v = new SensorValue();
                v.jsConstructor((String) args[k], null, null);
                s.add(v);
            }
        }

        if (s.size() == 0)
        {
            return null;
        }

        VirtualVehicleActionPoint actionPoint = new VirtualVehicleActionPoint();
        actionPoint.jsConstructor(latLonAlt, tolerance, new NativeArray(s.toArray(new SensorValue[0])));
        Context cx = Context.enter();
        try
        {
            ContinuationPending cp = cx.captureContinuation();
            cp.setApplicationState(actionPoint);
            throw cp;
        }
        finally
        {
            Context.exit();
        }
    }
}
