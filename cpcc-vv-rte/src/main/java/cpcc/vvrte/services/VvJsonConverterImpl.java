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

package cpcc.vvrte.services;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.services.CoreJsonConverter;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.task.Task;

/**
 * VvJsonConverterImpl
 */
public class VvJsonConverterImpl implements VvJsonConverter
{
    private CoreJsonConverter pjc;

    /**
     * @param pjc the JSON converter of the common module.
     */
    public VvJsonConverterImpl(CoreJsonConverter pjc)
    {
        this.pjc = pjc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(VirtualVehicle vehicle)
    {
        return new JSONObject(
            "uuid", vehicle.getUuid(),
            "name", vehicle.getName(),
            "state", vehicle.getState().toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(VirtualVehicle... vehicleList)
    {
        JSONArray a = new JSONArray();
        for (VirtualVehicle vehicle : vehicleList)
        {
            a.put(toJson(vehicle));
        }
        return a;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONObject toJson(Task task)
    {
        JSONObject o = new JSONObject("tolerance", Double.toString(task.getTolerance()));
        o.put("position", pjc.toJson(task.getPosition()));
        o.put("sensors", pjc.toJsonArray(task.getSensors().toArray(new SensorDefinition[0])));
        return o;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public JSONArray toJsonArray(Task... taskList)
    {
        JSONArray a = new JSONArray();
        for (Task task : taskList)
        {
            a.put(toJson(task));
        }
        return a;
    }
}
