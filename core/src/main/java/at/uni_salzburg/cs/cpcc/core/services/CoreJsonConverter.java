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

import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * PersistenceJsonConverter
 */
public interface CoreJsonConverter
{
    /**
     * @param sensorIdsOnly if true the JSON objects contains the identifications of the sensors only.
     * @param vehicle a real vehicle object.
     * @return the requested JSON object.
     */
    JSONObject toJson(boolean sensorIdsOnly, RealVehicle vehicle);

    /**
     * @param sensorIdsOnly if true the JSON objects contains the identifications of the sensors only.
     * @param vehicles a list of real vehicles.
     * @return the requested JSON array.
     */
    JSONArray toJsonArray(boolean sensorIdsOnly, RealVehicle... vehicles);

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

    /**
     * @param numbers a list of integer numbers.
     * @return the requested JSON object.
     */
    JSONArray toJsonArray(Integer... numbers);
    
    /**
     * @param numbers a list of double numbers.
     * @return the requested JSON object.
     */
    JSONArray toJsonArray(Double... numbers);
    
    /**
     * @param vehicleList the list of real vehicles as JSON array.
     * @return the converted list.
     */
    List<RealVehicle> toRealVehicleList(JSONArray vehicleList);
    
    /**
     * Convert a real vehicle JSON object to a real vehicle object. This method assumes that sensor definitions are 
     * handed over as an array of sensor definition IDs. Sensor definition IDs referenced by the JSON object must have
     * corresponding counterparts in the database to have this method working properly.
     * 
     * @param vehicle the vehicle as a JSON object.
     * @return the converted vehicle.
     */
    RealVehicle toRealVehicle(JSONObject vehicle);
    
    /**
     * @param sensorList the list of sensor definitions as JSON array.
     * @return the converted list.
     */
    List<SensorDefinition> toSensorDefinitionList(JSONArray sensorList);
    
    /**
     * @param sensor the sensor definition as a JSON object.
     * @return the converted sensor definition.
     */
    SensorDefinition toSensorDefinition(JSONObject sensor);
}
