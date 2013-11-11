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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import org.apache.tapestry5.json.JSONArray;
import org.apache.tapestry5.json.JSONObject;

import at.uni_salzburg.cs.cpcc.persistence.entities.Parameter;
import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMapperImpl
 */
public class VirtualVehicleMapperImpl implements VirtualVehicleMapper
{
    private PolygonZone areaOfOperation;

    /**
     * @param qm the query manager.
     */
    public VirtualVehicleMapperImpl(QueryManager qm)
    {
        Parameter rvNameParam = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME);

        RealVehicle realVehicle = qm.findRealVehicleByName(rvNameParam.getValue());

        String areaOfOperationString = realVehicle.getAreaOfOperation();

        JSONArray polygon = new JSONArray(areaOfOperationString);

        PolarCoordinate[] coordinates = new PolarCoordinate[polygon.length()];
        for (int k = 0, l = polygon.length(); k < l; ++k)
        {
            JSONObject point = (JSONObject) polygon.get(k);
            double lat = point.getDouble("lat");
            double lon = point.getDouble("lon");
            coordinates[k] = new PolarCoordinate(lat, lon, 0.0);
        }

        areaOfOperation = new PolygonZone(coordinates);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VirtualVehicleMappingDecision findMappingDecision(Task task)
    {
        VirtualVehicleMappingDecision decision = new VirtualVehicleMappingDecision();
        decision.setMigration(!areaOfOperation.isInside(task.getPosition()));
        return decision;
    }

}
