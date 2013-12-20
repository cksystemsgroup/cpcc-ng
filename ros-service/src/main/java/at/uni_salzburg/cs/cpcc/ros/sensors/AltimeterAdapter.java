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
package at.uni_salzburg.cs.cpcc.ros.sensors;

import java.util.List;
import java.util.Map;

import at.uni_salzburg.cs.cpcc.core.utils.ConvertUtils;

/**
 * AltimeterAdapter
 */
public class AltimeterAdapter extends Float32SensorAdapter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public SensorType getType()
    {
        return SensorType.ALTIMETER;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, List<String>> getCurrentState()
    {
        Map<String, List<String>> map = super.getCurrentState();

        if (getValue() != null)
        {
            map.put("sensor.altitude", ConvertUtils.floatAsString(getValue().getData()));
        }

        return map;
    }
}
