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

import at.uni_salzburg.cs.cpcc.javascript.runtime.types.LatLngAlt;

/**
 * NullPositionProvider
 */
public class NullPositionProvider implements PositonProvider
{

    /**
     * {@inheritDoc}
     */
    @Override
    public LatLngAlt getCurrentPosition()
    {
        LatLngAlt position = new LatLngAlt();
        //		position.setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getGlobalScope(), "LatLngAlt"));
        return position;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getSpeedOverGround()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getCourseOverGround()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double getAltitudeOverGround()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
