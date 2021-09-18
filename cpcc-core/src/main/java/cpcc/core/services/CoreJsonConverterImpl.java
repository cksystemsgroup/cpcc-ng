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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.utils.JSONUtils;

/**
 * CoreJsonConverterImpl
 */
public class CoreJsonConverterImpl implements CoreJsonConverter
{
    private static final String POSITON_ALTITUDE = "alt";
    private static final String POSITON_LONGITUDE = "lon";
    private static final String POSITON_LATITUDE = "lat";

    private static final String REAL_VEHICLE_SENSORS = "sen";
    private static final String REAL_VEHICLE_LAST_UPDATE = "upd";
    private static final String REAL_VEHICLE_AREA_OF_OPERATION = "aoo";
    private static final String REAL_VEHICLE_URL = "url";
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_TYPE = "type";
    private static final String REAL_VEHICLE_DELETED = "deleted";

    private static final String SENSOR_DEFINITION_LAST_UPDATE = "lastUpdate";
    private static final String SENSOR_DEFINITION_PARAMETERS = "parameters";
    private static final String SENSOR_DEFINITION_VISIBILITY = "visibility";
    private static final String SENSOR_DEFINITION_MESSAGE_TYPE = "messageType";
    private static final String SENSOR_DEFINITION_TYPE = "type";
    private static final String SENSOR_DEFINITION_DESCRIPTION = "description";
    private static final String SENSOR_DEFINITION_ID = "id";
    private static final String SENSOR_DEFINITION_DELETED = "deleted";

    /**
     * PersistenceJsonConverterImpl
     */
    public CoreJsonConverterImpl()
    {
        // Intentionally empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(boolean sensorIdsOnly, RealVehicle vehicle)
    {
        JSONObject o = new JSONObject(
            SENSOR_DEFINITION_ID, vehicle.getId().toString(),
            REAL_VEHICLE_NAME, vehicle.getName());

        o.put(REAL_VEHICLE_AREA_OF_OPERATION, vehicle.getAreaOfOperation());
        o.put(REAL_VEHICLE_TYPE, vehicle.getType().toString());
        o.put(REAL_VEHICLE_URL, vehicle.getUrl());
        o.put(REAL_VEHICLE_DELETED, vehicle.getDeleted());

        if (vehicle.getLastUpdate() != null)
        {
            o.put(REAL_VEHICLE_LAST_UPDATE, vehicle.getLastUpdate().getTime());
        }

        if (sensorIdsOnly)
        {
            Integer[] ids = new Integer[vehicle.getSensors().size()];
            for (int k = 0, l = vehicle.getSensors().size(); k < l; ++k)
            {
                ids[k] = vehicle.getSensors().get(k).getId();
            }
            o.put(REAL_VEHICLE_SENSORS, toJsonArray(ids));
        }
        else
        {
            List<SensorDefinition> sensors = vehicle.getSensors();
            o.put(REAL_VEHICLE_SENSORS, toJsonArray(sensors.toArray(new SensorDefinition[sensors.size()])));
        }

        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(boolean sensorIdsOnly, RealVehicle... vehicleList)
    {
        JSONArray a = new JSONArray();
        for (RealVehicle vehicle : vehicleList)
        {
            a.add(toJson(sensorIdsOnly, vehicle));
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(SensorDefinition sensor)
    {
        JSONObject o = new JSONObject(
            SENSOR_DEFINITION_ID, sensor.getId().toString(),
            SENSOR_DEFINITION_DESCRIPTION, sensor.getDescription(),
            SENSOR_DEFINITION_TYPE, sensor.getType().toString(),
            SENSOR_DEFINITION_MESSAGE_TYPE, sensor.getMessageType(),
            SENSOR_DEFINITION_VISIBILITY, sensor.getVisibility().toString(),
            SENSOR_DEFINITION_PARAMETERS, sensor.getParameters());

        if (sensor.getLastUpdate() != null)
        {
            o.put(SENSOR_DEFINITION_LAST_UPDATE, sensor.getLastUpdate().getTime());
        }
        o.put(SENSOR_DEFINITION_DELETED, sensor.getDeleted());
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(SensorDefinition... sensorList)
    {
        JSONArray a = new JSONArray();
        for (SensorDefinition sensor : sensorList)
        {
            a.add(toJson(sensor));
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(PolarCoordinate coordinate)
    {
        return new JSONObject(
            POSITON_LATITUDE, String.format(Locale.US, "%.8f", coordinate.getLatitude()),
            POSITON_LONGITUDE, String.format(Locale.US, "%.8f", coordinate.getLongitude()),
            POSITON_ALTITUDE, String.format(Locale.US, "%.3f", coordinate.getAltitude()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(Integer... numbers)
    {
        JSONArray a = new JSONArray();
        a.addAll(Arrays.asList(numbers));
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toRegionJson(List<RealVehicle> rvList)
    {
        StringBuilder buff = new StringBuilder();

        boolean first = true;

        buff.append("{");
        for (RealVehicle rv : rvList)
        {
            if (StringUtils.isBlank(rv.getAreaOfOperation()))
            {
                continue;
            }

            if (first)
            {
                first = false;
            }
            else
            {
                buff.append(",");
            }

            buff.append("\"")
                .append(rv.getName())
                .append("\":")
                .append(rv.getAreaOfOperation().replaceAll("\\\\n\\s*", ""));
        }

        buff.append("}");

        return buff.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toRealVehicleStateJson(List<RealVehicleState> statesList)
    {
        Map<String, String> stateMap = new HashMap<>();

        for (RealVehicleState state : statesList)
        {
            String s = StringUtils.isBlank(state.getState()) ? "{\"features\":[]}" : state.getState();
            stateMap.put(state.getId().toString(), s);
        }

        return JSONUtils.toJsonString(stateMap);
    }
}
