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
package at.uni_salzburg.cs.cpcc.javascript.runtime.types;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;

/**
 * VirtualVehicleActionPoint
 */
public class VirtualVehicleActionPoint extends ScriptableObject
{

    private static final long serialVersionUID = -2708423075745822484L;

    private LatLngAlt position;
    private double tolerance;
    private NativeArray values;

    private void setPrototype()
    {
        setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getContextScope(Context.getCurrentContext()),
            "ActionPoint"));
    }

    /**
     * Construct a <code>VirtualVehicleActionPoint</code> object.
     */
    public VirtualVehicleActionPoint()
    {
        setPrototype();
    }

    /**
     * Constructor creating the action point.
     * 
     * @name LatLngAlt
     * @class Immutable container for real-world latitude and longitude coordinates.
     * @param actionPosition The position of the action point.
     * @param positionTolerance The allowed position tolerance.
     * @param sensorValues The sensor values of this action point.
     */
    public void jsConstructor(LatLngAlt actionPosition, double positionTolerance, NativeArray sensorValues)
    {
        this.position = actionPosition;
        this.tolerance = positionTolerance;
        this.values = sensorValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName()
    {
        return "ActionPoint";
    }

    /**
     * Method returns the position.
     * 
     * @memberOf ActionPoint#
     * @name position
     * @function
     * @return The position
     */
    public LatLngAlt jsGet_position()
    {
        return position;
    }

    /**
     * Method sets the position.
     * 
     * @memberOf ActionPoint#
     * @name position
     * @function
     * @param newPosition The new position
     */
    public void jsSet_position(LatLngAlt newPosition)
    {
        this.position = newPosition;
    }

    /**
     * Method returns the tolerance.
     * 
     * @memberOf ActionPoint#
     * @name tolerance
     * @function
     * @return The tolerance
     */
    public double jsGet_tolerance()
    {
        return tolerance;
    }

    /**
     * Method sets the tolerance.
     * 
     * @memberOf ActionPoint#
     * @name tolerance
     * @function
     * @param newTolerance The new tolerance
     */
    public void jsSet_tolerance(double newTolerance)
    {
        this.tolerance = newTolerance;
    }

    /**
     * Method returns the values.
     * 
     * @memberOf ActionPoint#
     * @name values
     * @function
     * @return The values
     */
    public NativeArray jsGet_values()
    {
        return values;
    }

    /**
     * Method sets the values.
     * 
     * @memberOf ActionPoint#
     * @name values
     * @function
     * @param newValues The new values
     */
    public void jsSet_values(NativeArray newValues)
    {
        this.values = newValues;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("Point ").append(position.toString());
        b.append(" tolerance ").append(tolerance).append("\n");
        for (int k = 0; k < values.size(); ++k)
        {
            b.append(values.get(k).toString()).append("\n");
        }
        return b.toString();
    }
}
