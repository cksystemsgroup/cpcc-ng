/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2013  Clemens Krainer
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

import at.uni_salzburg.cs.cpcc.javascript.runtime.base.PositonProvider;
import at.uni_salzburg.cs.cpcc.javascript.runtime.types.LatLngAlt;

public class StaticPositionProviderMock implements PositonProvider
{

    private double mLat;
    private double mLng;
    private double mAlt;

    public StaticPositionProviderMock(double lat, double lng, double alt)
    {
        this.mLat = lat;
        this.mLng = lng;
        this.mAlt = alt;
    }

    @Override
    public LatLngAlt getCurrentPosition()
    {
        LatLngAlt position = new LatLngAlt(mLat, mLng, mAlt);
        return position;
    }

    @Override
    public Double getSpeedOverGround()
    {
        return null;
    }

    @Override
    public Double getCourseOverGround()
    {
        return null;
    }

    @Override
    public Double getAltitudeOverGround()
    {
        return null;
    }

}
