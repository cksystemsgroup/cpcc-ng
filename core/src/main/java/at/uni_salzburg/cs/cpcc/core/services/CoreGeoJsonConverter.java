/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.core.services;

import java.io.IOException;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * CoreGeoJsonConverter
 */
public interface CoreGeoJsonConverter
{
    /**
     * @param rv the real vehicle to be converted.
     * @return the real vehicle as GeoJSON Feature object.
     * @throws JsonParseException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    Feature toFeature(RealVehicle rv)
        throws JsonParseException, JsonMappingException, IOException;

    /**
     * @param rvList the list of real vehicles to be converted.
     * @return the real vehicles as GeoJSON FeatureCollection object.
     * @throws JsonParseException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    FeatureCollection toFeatureCollection(List<RealVehicle> rvList)
        throws JsonParseException, JsonMappingException, IOException;

    /**
     * @param position the position.
     * @return the position as a GeoJSON Point object.
     */
    Point toPoint(PolarCoordinate position);

}
