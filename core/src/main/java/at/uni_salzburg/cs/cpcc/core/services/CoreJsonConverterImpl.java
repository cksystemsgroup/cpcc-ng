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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * CoreJsonConverterImpl
 */
public class CoreJsonConverterImpl implements CoreJsonConverter
{
    // private static final Logger LOG = LoggerFactory.getLogger(CoreJsonConverterImpl.class);

    private static final String POSITON_ALTITUDE = "alt";
    private static final String POSITON_LONGITUDE = "lon";
    private static final String POSITON_LATITUDE = "lat";

    private static final String REAL_VEHICLE_ID = "id";
    private static final String REAL_VEHICLE_SENSORS = "sen";
    private static final String REAL_VEHICLE_LAST_UPDATE = "upd";
    private static final String REAL_VEHICLE_AREA_OF_OPERATION = "aoo";
    private static final String REAL_VEHICLE_URL = "url";
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_TYPE = "type";

    private static final String SENSOR_DEFINITION_LAST_UPDATE = "lastUpdate";
    private static final String SENSOR_DEFINITION_PARAMETERS = "parameters";
    private static final String SENSOR_DEFINITION_VISIBILITY = "visibility";
    private static final String SENSOR_DEFINITION_MESSAGE_TYPE = "messageType";
    private static final String SENSOR_DEFINITION_TYPE = "type";
    private static final String SENSOR_DEFINITION_DESCRIPTION = "description";
    private static final String SENSOR_DEFINITION_ID = "id";

    /**
     * PersistenceJsonConverterImpl
     */
    public CoreJsonConverterImpl()
    {
        // intentionally empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(boolean sensorIdsOnly, RealVehicle vehicle)
    {
        JSONObject o = new JSONObject(
            SENSOR_DEFINITION_ID, vehicle.getId().toString(),
            REAL_VEHICLE_NAME, vehicle.getName(),
            REAL_VEHICLE_URL, vehicle.getUrl()
            );

        o.put(REAL_VEHICLE_AREA_OF_OPERATION, vehicle.getAreaOfOperation());
        o.put(REAL_VEHICLE_LAST_UPDATE, Long.toString(vehicle.getLastUpdate().getTime()));
        o.put(REAL_VEHICLE_TYPE, vehicle.getType().toString());
        o.put(REAL_VEHICLE_URL, vehicle.getUrl());

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
            o.put(REAL_VEHICLE_SENSORS, toJsonArray(vehicle.getSensors().toArray(new SensorDefinition[0])));
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
            a.put(toJson(sensorIdsOnly, vehicle));
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(SensorDefinition sensor)
    {
        return new JSONObject(
            SENSOR_DEFINITION_ID, sensor.getId().toString(),
            SENSOR_DEFINITION_DESCRIPTION, sensor.getDescription(),
            SENSOR_DEFINITION_TYPE, sensor.getType().toString(),
            SENSOR_DEFINITION_MESSAGE_TYPE, sensor.getMessageType(),
            SENSOR_DEFINITION_VISIBILITY, sensor.getVisibility().toString(),
            SENSOR_DEFINITION_PARAMETERS, sensor.getParameters(),
            SENSOR_DEFINITION_LAST_UPDATE, Long.toString(sensor.getLastUpdate().getTime()));
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
            a.put(toJson(sensor));
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
        for (Integer num : numbers)
        {
            a.put(num);
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(Double... numbers)
    {
        JSONArray a = new JSONArray();
        for (Double num : numbers)
        {
            a.put(num);
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<RealVehicle> toRealVehicleList(JSONArray vehicleList)
    {
        List<RealVehicle> sdList = new ArrayList<RealVehicle>();
        for (int k = 0, l = vehicleList.length(); k < l; ++k)
        {
            JSONObject sd = vehicleList.getJSONObject(k);
            sdList.add(toRealVehicle(sd));
        }
        return sdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RealVehicle toRealVehicle(JSONObject vehicle)
    {
        RealVehicle rv = new RealVehicle();
        rv.setId(vehicle.getInt(REAL_VEHICLE_ID));
        rv.setLastUpdate(new Date(vehicle.getLong(REAL_VEHICLE_LAST_UPDATE)));
        rv.setAreaOfOperation(vehicle.getString(REAL_VEHICLE_AREA_OF_OPERATION));
        rv.setUrl(vehicle.getString(REAL_VEHICLE_URL));
        rv.setName(vehicle.getString(REAL_VEHICLE_NAME));
        rv.setType(RealVehicleType.valueOf(vehicle.getString(REAL_VEHICLE_TYPE)));

        JSONArray sensors = (JSONArray) vehicle.get(REAL_VEHICLE_SENSORS);

        for (int k = 0, l = sensors.length(); k < l; ++k)
        {
            SensorDefinition sd = new SensorDefinition();
            sd.setId(sensors.getInt(k));
            rv.getSensors().add(sd);
        }

        return rv;

        //        int id = vehicle.getInt(SENSOR_DEFINITION_ID);
        //        RealVehicle rv = qm.findRealVehicleById(id);
        //
        //        if (rv == null)
        //        {
        //            rv = new RealVehicle();
        //            rv.setId(id);
        //        }
        //
        //        JSONArray sensors = (JSONArray) vehicle.get(REAL_VEHICLE_SENSORS);
        //        Set<Integer> sensorIds = new HashSet<Integer>();
        //
        //        for (int k = 0, l = sensors.length(); k < l; ++k)
        //        {
        //            Integer sensorId = (Integer) sensors.get(k);
        //            sensorIds.add(sensorId);
        //
        //            SensorDefinition foundSd = null;
        //            for (SensorDefinition sd : rv.getSensors())
        //            {
        //                if (sd.getId().intValue() == sensorId.intValue())
        //                {
        //                    foundSd = sd;
        //                    break;
        //                }
        //            }
        //
        //            if (foundSd == null)
        //            {
        //                SensorDefinition sd = qm.findSensorDefinitionById(sensorId);
        //                rv.getSensors().add(sd);
        //                LOG.debug("Adding sensor definition " + sensorId + " (" + sd.getDescription()
        //                    + ") to real vehicle " + rv.getName() + " (" + rv.getId() + ")");
        //            }
        //        }
        //
        //        List<SensorDefinition> toBeRemoved = new ArrayList<SensorDefinition>();
        //        for (int k = 0, l = rv.getSensors().size(); k < l; ++k)
        //        {
        //            SensorDefinition sd = rv.getSensors().get(k);
        //            if (!sensorIds.contains(sd.getId()))
        //            {
        //                toBeRemoved.add(sd);
        //            }
        //        }
        //
        //        rv.getSensors().removeAll(toBeRemoved);
        //        return rv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<SensorDefinition> toSensorDefinitionList(JSONArray sensorList)
    {
        List<SensorDefinition> sdList = new ArrayList<SensorDefinition>();
        for (int k = 0, l = sensorList.length(); k < l; ++k)
        {
            JSONObject sd = sensorList.getJSONObject(k);
            sdList.add(toSensorDefinition(sd));
        }
        return sdList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorDefinition toSensorDefinition(JSONObject sensor)
    {
        SensorDefinition sd = new SensorDefinition();
        sd.setId(sensor.getInt(SENSOR_DEFINITION_ID));
        sd.setDescription(sensor.getString(SENSOR_DEFINITION_DESCRIPTION));
        sd.setType(SensorType.valueOf(sensor.getString(SENSOR_DEFINITION_TYPE)));
        sd.setMessageType(sensor.getString(SENSOR_DEFINITION_MESSAGE_TYPE));
        sd.setVisibility(SensorVisibility.valueOf(sensor.getString(SENSOR_DEFINITION_VISIBILITY)));
        sd.setParameters(
            sensor.isNull(SENSOR_DEFINITION_PARAMETERS)
                ? null
                : sensor.getString(SENSOR_DEFINITION_PARAMETERS));
        sd.setLastUpdate(new Date(sensor.getLong(SENSOR_DEFINITION_LAST_UPDATE)));
        return sd;
    }
}
