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
package at.uni_salzburg.cs.cpcc.persistence.services;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;

/**
 * PersistenceJsonConverter
 */
public interface PersistenceJsonConverter
{
    /**
     * @param vehicle a real vehicle object.
     * @return the requested JSON object.
     */
    JSONObject toJson(RealVehicle vehicle);

    /**
     * @param vehicles a list of real vehicles.
     * @return the requested JSON array.
     */
    JSONArray toJsonArray(RealVehicle... vehicles);

    /**
     * @param sensor a sensor definition.
     * @return the requested JSON object.
     */
    JSONObject toJson(SensorDefinition sensor);

    /**
     * @param sensors a list of sensor definitions.
     * @return the requested JSON array.
     */
    JSONArray toJsonArray(SensorDefinition... sensors);

    /**
     * @param coordinate a polar coordinate.
     * @return the requested JSON object.
     */
    JSONObject toJson(PolarCoordinate coordinate);

}
