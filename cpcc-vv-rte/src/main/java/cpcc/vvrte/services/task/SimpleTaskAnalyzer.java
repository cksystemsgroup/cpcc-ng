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

package cpcc.vvrte.services.task;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.services.QueryManager;
import cpcc.vvrte.entities.Task;

/**
 * SimpleTaskAnalyzer
 */
public class SimpleTaskAnalyzer extends AbstractTaskAnalyzer
{
    private QueryManager qm;
    private double minToleranceDistance;

    /**
     * @param qm the query manager.
     * @param minToleranceDistance the minimum tolerance distance.
     */
    public SimpleTaskAnalyzer(QueryManager qm, double minToleranceDistance)
    {
        this.qm = qm;
        this.minToleranceDistance = minToleranceDistance;
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
        task.setTolerance(fixToleranceDistance(tolerance.floatValue()));

        NativeObject position = (NativeObject) taskParameters.get("position");

        PolarCoordinate taskPosition = new PolarCoordinate();
        taskPosition.setLatitude((Double) position.get("lat"));
        taskPosition.setLongitude((Double) position.get("lng"));
        taskPosition.setAltitude((Double) position.get("alt"));
        task.setPosition(taskPosition);

        NativeArray sensors = (NativeArray) taskParameters.get("sensors");
        List<SensorDefinition> sensorDefinitions = new ArrayList<SensorDefinition>();
        for (int k = 0; k < sensors.getLength(); ++k)
        {
            NativeObject s = (NativeObject) sensors.get(k);
            if (s != null)
            {
                SensorDefinition sd = qm.findSensorDefinitionByDescription((String) s.get("description"));
                if (sd != null && sd.getVisibility() != SensorVisibility.NO_VV)
                {
                    sensorDefinitions.add(sd);
                }
            }
        }

        task.getSensors().addAll(sensorDefinitions);
        return task;
    }

    /**
     * @param toleranceDistance the tolerance distance to set.
     * @return the tolerance distance if it is greater than the minimum tolerance distance, otherwise the minimum
     *         tolerance distance.
     */
    private double fixToleranceDistance(double toleranceDistance)
    {
        return toleranceDistance < minToleranceDistance ? minToleranceDistance : toleranceDistance;
    }

}
