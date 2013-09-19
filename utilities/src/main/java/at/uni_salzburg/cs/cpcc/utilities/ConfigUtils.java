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
package at.uni_salzburg.cs.cpcc.utilities;

import java.util.List;
import java.util.Map;

/**
 * ConfigUtils
 */
public final class ConfigUtils
{
    /**
     * ConfigUtils
     */
    private ConfigUtils()
    {
        // intentionally empty
    }
    
    /**
     * @param config the configuration.
     * @param propertyName the property value to be parsed.
     * @param index the index in the values list to use.
     * @param defaultValue the default value.
     * @return the parsed value.
     */
    public static double parseDouble(Map<String, List<String>> config, String propertyName, int index,
        double defaultValue)
    {
        List<String> property = config.get(propertyName);
        
        if (property == null)
        {
            return defaultValue;
        }
        
        String propertyValue = property.get(index);
        
        return propertyValue != null ? Double.parseDouble(propertyValue) : defaultValue;
    }
    
    /**
     * @param config the configuration.
     * @param propertyName the property value to be parsed.
     * @param index the index in the values list to use.
     * @param defaultValue the default value.
     * @return the parsed value.
     */
    public static int parseInteger(Map<String, List<String>> config, String propertyName, int index, int defaultValue)
    {
        List<String> property = config.get(propertyName);
        
        if (property == null)
        {
            return defaultValue;
        }
        
        String propertyValue = property.get(index);
        
        return propertyValue != null ? Integer.parseInt(propertyValue) : defaultValue;
    }
    
    /**
     * @param config the configuration.
     * @param propertyName the property value to be parsed.
     * @param startIndex the start index in the values list to use.
     * @return the parsed coordinate.
     */
    public static PolarCoordinate parsePolarCoordinate(Map<String, List<String>> config, String propertyName,
        int startIndex)
    {
        double latitude = ConfigUtils.parseDouble(config, propertyName, startIndex, 0);
        double longitude = ConfigUtils.parseDouble(config, propertyName, startIndex+1, 0);
        double altitude = ConfigUtils.parseDouble(config, propertyName, startIndex+2, 0);
        return new PolarCoordinate(latitude, longitude, altitude);
    }
}
