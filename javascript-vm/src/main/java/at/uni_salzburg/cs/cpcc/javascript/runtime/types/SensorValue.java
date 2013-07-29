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
package at.uni_salzburg.cs.cpcc.javascript.runtime.types;

import java.util.Locale;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;

/**
 * SensorValue
 */
public class SensorValue extends ScriptableObject
{

    private static final long serialVersionUID = -1932147576184568709L;

    private Object value;
    private Double time;
    private String name;

    private void setPrototype()
    {
        setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getContextScope(Context.getCurrentContext()),
            "SensorValue"));
    }

    /**
     * Construct a <code>SensorValue</code> object.
     */
    public SensorValue()
    {
        setPrototype();
    }

    /**
     * Constructor creating the name, time, and value tuple.
     * 
     * @name SensorValue
     * @class Immutable container for name, time, and values of sensors.
     * @param {String} name The sensor name
     * @param {Double} time The completion time.
     * @param {Object} value The captured value.
     */
    public void jsConstructor(String name, Double time, Object value)
    {
        this.name = name;
        this.time = time;
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName()
    {
        return "SensorValue";
    };

    /**
     * Method returns the sensor's name.
     * 
     * @memberOf SensorValue#
     * @name name
     * @function
     * @return {String} The sensor's name
     */
    public String jsGet_name()
    {
        return name;
    }

    /**
     * Method returns the sensor's name.
     * 
     * @memberOf SensorValue#
     * @name name
     * @function
     * @return {String} The sensor's name
     */
    public void jsSet_name(String name)
    {
        this.name = name;
    }

    /**
     * Method returns the completion time.
     * 
     * @memberOf SensorValue#
     * @name name
     * @function
     * @return {Double} The completion time
     */
    public Double jsGet_time()
    {
        return time;
    }

    /**
     * Method sets the completion time.
     * 
     * @memberOf SensorValue#
     * @name name
     * @function
     * @return {Double} The completion time
     */
    public void jsSet_time(Double time)
    {
        this.time = time;
    }

    /**
     * Method returns the captured sensor value.
     * 
     * @memberOf SensorValue#
     * @name value
     * @function
     * @return {Object} The sensor value
     */
    public Object jsGet_value()
    {
        return value;
    }

    /**
     * Method sets the captured sensor value.
     * 
     * @memberOf SensorValue#
     * @name value
     * @function
     * @return {Object} The sensor value
     */
    public void jsSet_value(Object value)
    {
        this.value = value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (time == null || time.longValue() == 0)
        {
            return name;
        }

        String v;
        if (value instanceof String)
        {
            v = String.format(Locale.US, "\"%s\"", value);
        }
        else
        {
            v = String.format(Locale.US, "%f", value);
        }

        return String.format(Locale.US, "%s (%d, %s)", name, time.longValue(), v);
    }
}
