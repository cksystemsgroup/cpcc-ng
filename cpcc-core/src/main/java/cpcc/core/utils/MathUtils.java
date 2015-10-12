// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.utils;

/**
 * Math utilities.
 */
public final class MathUtils
{
    private MathUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param a a double value.
     * @param b another double value.
     * @return the average of the two double values.
     */
    public static double avg(double a, double b)
    {
        return (a + b) / 2.0;
    }

    /**
     * @param numbers an array of double values.
     * @return true if none of the values is a NaN.
     */
    public static boolean containsNoNaN(double... numbers)
    {
        for (double n : numbers)
        {
            if (Double.isNaN(n))
            {
                return false;
            }
        }

        return true;
    }
}
