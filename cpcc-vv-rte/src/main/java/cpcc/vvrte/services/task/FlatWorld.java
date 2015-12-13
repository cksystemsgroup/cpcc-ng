// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services.task;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.CartesianCoordinate;
import cpcc.core.utils.GeodeticSystem;

/**
 * FlatWorld implementation.
 */
public class FlatWorld implements GeodeticSystem
{
    @Override
    public PolarCoordinate rectangularToPolarCoordinates(CartesianCoordinate coordinates)
    {
        throw new IllegalAccessError("Not allowed in Flat World!");
    }

    @Override
    public PolarCoordinate rectangularToPolarCoordinates(double x, double y, double z)
    {
        throw new IllegalAccessError("Not allowed in Flat World!");
    }

    @Override
    public CartesianCoordinate polarToRectangularCoordinates(PolarCoordinate pos)
    {
        return new CartesianCoordinate(pos.getLongitude(), pos.getLatitude(), pos.getAltitude());
    }

    @Override
    public CartesianCoordinate polarToRectangularCoordinates(double latitude, double longitude, double altitude)
    {
        throw new IllegalAccessError("Not allowed in Flat World!");
    }

    @Override
    public PolarCoordinate walk(PolarCoordinate startPosition, double x, double y, double z)
    {
        throw new IllegalAccessError("Not allowed in Flat World!");
    }

    @Override
    public double calculateDistance(PolarCoordinate a, PolarCoordinate b)
    {
        throw new IllegalAccessError("Not allowed in Flat World!");
    }

}
