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
package at.uni_salzburg.cs.cpcc.vvrte.task;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

/**
 * SimpleTaskAnalyzer
 */
public class SimpleTaskAnalyzer extends AbstractTaskAnalyzer
{
    private QueryManager qm;

    /**
     * @param qm the query manager.
     */
    public SimpleTaskAnalyzer(QueryManager qm)
    {
        this.qm = qm;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Task analyzeTaskParameters(ScriptableObject taskParameters, int sequenceNumber)
    {
        // TODO: Check parameters for null, etc.
        Task task = new Task();

        Number tolerance = (Number) taskParameters.get("tolerance");
        task.setTolerance(tolerance.doubleValue());

        NativeObject position = (NativeObject) taskParameters.get("position");
        double lat = (Double) position.get("lat");
        double lon = (Double) position.get("lng");
        double alt = (Double) position.get("alt");
        PolarCoordinate pos = new PolarCoordinate(lat, lon, alt);
        task.setPosition(pos);

        NativeArray sensors = (NativeArray) taskParameters.get("sensors");
        List<SensorDefinition> sensorDefinitions = new ArrayList<SensorDefinition>();
        for (int k = 0; k < sensors.getLength(); ++k)
        {
            NativeObject s = (NativeObject) sensors.get(k);
            SensorDefinition sd = qm.findSensorDefinitionByDescription((String) s.get("description"));
            sensorDefinitions.add(sd);
        }
        task.setSensors(sensorDefinitions);

        task.setLastInTaskGroup(true);
        
        return task;
    }
}
