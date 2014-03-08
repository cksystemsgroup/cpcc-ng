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

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * CoreJsonConverter
 */
public interface CoreJsonConverter
{
    String POSITON_ALTITUDE = "alt";
    String POSITON_LONGITUDE = "lon";
    String POSITON_LATITUDE = "lat";

    String REAL_VEHICLE_ID = "id";
    String REAL_VEHICLE_SENSORS = "sen";
    String REAL_VEHICLE_LAST_UPDATE = "upd";
    String REAL_VEHICLE_AREA_OF_OPERATION = "aoo";
    String REAL_VEHICLE_URL = "url";
    String REAL_VEHICLE_NAME = "name";
    String REAL_VEHICLE_TYPE = "type";
    String REAL_VEHICLE_DELETED = "deleted";

    String SENSOR_DEFINITION_LAST_UPDATE = "lastUpdate";
    String SENSOR_DEFINITION_PARAMETERS = "parameters";
    String SENSOR_DEFINITION_VISIBILITY = "visibility";
    String SENSOR_DEFINITION_MESSAGE_TYPE = "messageType";
    String SENSOR_DEFINITION_TYPE = "type";
    String SENSOR_DEFINITION_DESCRIPTION = "description";
    String SENSOR_DEFINITION_ID = "id";
    String SENSOR_DEFINITION_DELETED = "deleted";

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
     * @param rv the vehicle object to be filled.
     * @param rvObj the vehicle as a JSON object.
     * @return 1 if the vehicle has been updated, 0 if both vehicles are equal, -1 if the JSON object is older.
     */
    int fillInNewerRealVehicleFromJsonObject(RealVehicle rv, JSONObject rvObj);

    /**
     * @param sd the sensor definition object to be filled.
     * @param sdObj the source sensor definition as a JSON object.
     * @return 1 if the sensor definition has been updated, 0 if both sensor definitions are equal, -1 if the JSON
     *         object is older.
     */
    int fillInNewerSensorDefinitionFromJsonObject(SensorDefinition sd, JSONObject sdObj);
}
