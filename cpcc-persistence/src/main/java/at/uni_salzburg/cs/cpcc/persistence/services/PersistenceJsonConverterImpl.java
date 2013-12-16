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

/**
 * PersistenceJsonConverterImpl
 */
public class PersistenceJsonConverterImpl implements PersistenceJsonConverter
{

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(RealVehicle... vehicleList)
    {
        JSONArray a = new JSONArray();
        for (RealVehicle vehicle : vehicleList)
        {
            JSONObject o = new JSONObject(
                "name", vehicle.getName(),
                "url", vehicle.getUrl()
                );
            o.put("sensors", toJsonArray(vehicle.getSensors().toArray(new SensorDefinition[0])));
            a.put(o);
        }
        return a;
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
            JSONObject o = new JSONObject(
                "description", sensor.getDescription(),
                "type", sensor.getType().toString(),
                "messageType", sensor.getMessageType(),
                "visibility", sensor.getVisibility().toString(),
                "parameters", sensor.getParameters()
                );
            a.put(o);
        }
        return a;
    }
}
