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

import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.SensorDefinition;

/**
 * CoreJsonConverter
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
     * @param rvList the list of real vehicles.
     * @return the real vehicle regions as a JSON object.
     */
    String toRegionJson(List<RealVehicle> rvList);

    /**
     * @param statesList the list of real vehicle states.
     * @return the real vehicle states as a JSON object.
     */
    String toRealVehicleStateJson(List<RealVehicleState> statesList);
}
