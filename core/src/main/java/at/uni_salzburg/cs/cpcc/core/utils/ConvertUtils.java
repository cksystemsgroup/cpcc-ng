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
package at.uni_salzburg.cs.cpcc.core.utils;

import java.util.Arrays;
import java.util.List;

/**
 * ConvertUtils
 */
public final class ConvertUtils
{
    private ConvertUtils()
    {
        // intentionally empty.
    }

    /**
     * @param values the double values.
     * @return the values as a list of strings.
     */
    public static List<String> doubleListAsString(double[] values)
    {
        StringBuilder b = new StringBuilder("[");
        boolean first = true;
        for (double v : values)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                b.append(", ");
            }
            b.append(v);
        }
        b.append("]");
        return Arrays.asList(b.toString());
    }

    /**
     * @param value the byte value.
     * @return the values as a list of strings.
     */
    public static List<String> byteAsString(byte value)
    {
        return Arrays.asList(Integer.toString(0xFF & value));
    }

    /**
     * @param value the short value.
     * @return the values as a list of strings.
     */
    public static List<String> shortAsString(short value)
    {
        return Arrays.asList(Short.toString(value));
    }

    /**
     * @param value the float value.
     * @return the values as a list of strings.
     */
    public static List<String> floatAsString(float value)
    {
        return Arrays.asList(Float.toString(value));
    }
}
