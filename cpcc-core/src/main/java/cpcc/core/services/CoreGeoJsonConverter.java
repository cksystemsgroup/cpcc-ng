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

package cpcc.core.services;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.Point;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;

/**
 * CoreGeoJsonConverter
 */
public interface CoreGeoJsonConverter
{
    /**
     * @param rv the real vehicle to be converted.
     * @return the real vehicle as GeoJSON Feature object.
     * @throws IOException thrown in case of errors.
     */
    Feature toFeature(RealVehicle rv) throws IOException;

    /**
     * @param position the position.
     * @return the position as a GeoJSON Point object.
     */
    Point toPoint(PolarCoordinate position);

    /**
     * @param position the position.
     * @return the position as a Map object.
     */
    Map<String, Double> toPosition(PolarCoordinate position);

    /**
     * @param rv the real vehicle
     * @return the list of depot positions of the real vehicle.
     */
    List<Point> findDepotPositions(RealVehicle rv);

    /**
     * @param rvList the list of real vehicles.
     * @return the bounding box of all areas of operation.
     */
    double[] findBoundingBox(List<RealVehicle> rvList);
}
