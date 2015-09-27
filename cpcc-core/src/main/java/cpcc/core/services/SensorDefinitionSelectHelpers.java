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

import java.util.Collection;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.internal.SelectModelImpl;

import cpcc.core.entities.SensorDefinition;

/**
 * SensorDefinitionSelectHelpers
 */
public class SensorDefinitionSelectHelpers
{
    private QueryManager qm;

    /**
     * @param queryManager the query manager service.
     */
    public SensorDefinitionSelectHelpers(QueryManager queryManager)
    {
        this.qm = queryManager;
    }

    /**
     * @return the new value encoder.
     */
    public ValueEncoder<SensorDefinition> valueEncoder()
    {
        return new ValueEncoder<SensorDefinition>()
        {
            /**
             * {@inheritDoc}
             */
            @Override
            public SensorDefinition toValue(String clientValue)
            {
                return clientValue == null ? null : qm.findSensorDefinitionByDescription(clientValue);
            }

            /**
             * @param value the sensor definition.
             * @return the sensor definition as a string.
             */
            @Override
            public String toClient(SensorDefinition value)
            {
                return value == null ? "" : value.getDescription();
            }
        };
    }

    /**
     * @param sensorDefinitionList the list of sensor definition objects.
     * @return the new selection model.
     */
    public static SelectModel selectModel(Collection<SensorDefinition> sensorDefinitionList)
    {
        OptionModel[] optionModels = new OptionModel[sensorDefinitionList.size()];

        int i = 0;

        for (SensorDefinition sensorDefinition : sensorDefinitionList)
        {
            optionModels[i++] = new OptionModelImpl(sensorDefinition.getDescription(), sensorDefinition);
        }

        return new SelectModelImpl(optionModels);
    }
}
