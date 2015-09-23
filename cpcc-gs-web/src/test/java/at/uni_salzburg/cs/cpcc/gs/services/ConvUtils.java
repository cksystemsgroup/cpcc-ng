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

package at.uni_salzburg.cs.cpcc.gs.services;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.entities.SensorVisibility;

public class ConvUtils
{
    private static final String REAL_VEHICLE_ID = "id";
    private static final String REAL_VEHICLE_SENSORS = "sen";
    private static final String REAL_VEHICLE_LAST_UPDATE = "upd";
    private static final String REAL_VEHICLE_AREA_OF_OPERATION = "aoo";
    private static final String REAL_VEHICLE_URL = "url";
    private static final String REAL_VEHICLE_NAME = "name";
    private static final String REAL_VEHICLE_TYPE = "type";
    // private static final String REAL_VEHICLE_DELETED = "deleted";

    private static final String SENSOR_DEFINITION_LAST_UPDATE = "lastUpdate";
    private static final String SENSOR_DEFINITION_PARAMETERS = "parameters";
    private static final String SENSOR_DEFINITION_VISIBILITY = "visibility";
    private static final String SENSOR_DEFINITION_MESSAGE_TYPE = "messageType";
    private static final String SENSOR_DEFINITION_TYPE = "type";
    private static final String SENSOR_DEFINITION_DESCRIPTION = "description";
    private static final String SENSOR_DEFINITION_ID = "id";

    // private static final String SENSOR_DEFINITION_DELETED = "deleted";

    /**
     * @param vehicleList the list of real vehicles as JSON array.
     * @return the converted list.
     */
    public static List<RealVehicle> toRealVehicleList(JSONArray vehicleList)
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
     * Convert a real vehicle JSON object to a real vehicle object. This method assumes that sensor definitions are
     * handed over as an array of sensor definition IDs. Sensor definition IDs referenced by the JSON object must have
     * corresponding counterparts in the database to have this method working properly.
     * 
     * @param vehicle the vehicle as a JSON object.
     * @return the converted vehicle.
     */
    public static RealVehicle toRealVehicle(JSONObject vehicle)
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
     * @param sensorList the list of sensor definitions as JSON array.
     * @return the converted list.
     */
    public static List<SensorDefinition> toSensorDefinitionList(JSONArray sensorList)
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
     * @param sensor the sensor definition as a JSON object.
     * @return the converted sensor definition.
     */
    public static SensorDefinition toSensorDefinition(JSONObject sensor)
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
