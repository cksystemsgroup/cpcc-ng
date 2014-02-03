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
package at.uni_salzburg.cs.cpcc.gs.services;

import java.io.IOException;
import java.util.List;

import org.apache.tapestry5.json.JSONArray;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;

/**
 * ConfigurationSynchronizer
 */
public interface ConfigurationSynchronizer
{
    /**
     * @param targets the real vehicle to synchronize the configuration with.
     * @throws IOException in case of errors.
     */
    void syncConfig(List<RealVehicle> targets) throws IOException;

    /**
     * @param sensorDefs the sensor definitions to be synchronized with the local database as a JSON array.
     * @return the sensor definitions that are newer in the local database.
     */
    JSONArray syncSensorDefinitionConfig(JSONArray sensorDefs);

    
    /**
     * @param realVehicles the real vehicles to be synchronized with the local database as a JSON array.
     * @return the real vehicles that are newer in the local database.
     */
    JSONArray syncRealVehicleConfig(JSONArray realVehicles);
    
    
    
    /**
     * The real vehicle configuration should be synchronized to all other real vehicles.
     */
//    void initRealVehicleConfigSync();

    /**
     * @return the last real vehicle configuration synchronization time.
     */
//    Date getLastRealVehicleConfigSync();

    /**
     * The sensor definition configuration should be synchronized to all other real vehicles.
     */
//    void initSensorDefinitionConfigSync();

    /**
     * @return the last sensor definition configuration synchronization time.
     */
//    Date getSensorDefinitionConfigSync();
}
