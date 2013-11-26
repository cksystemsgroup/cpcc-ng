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

/**
 * StringUtilities
 */
public final class StringUtilities
{
    private StringUtilities()
    {
        // intentionally empty.
    }

    /**
     * @param a first string or null.
     * @param b second string or null.
     * @return true if both strings are equal or both strings are null.
     */
    public static boolean equals(String a, String b)
    {
        if (a == null && b == null)
        {
            return true;
        }

        if (a != null && b != null)
        {
            return a.equals(b);
        }

        return false;
    }

    /**
     * @param joinString the join string.
     * @param stringArray the array of strings to be joined.
     * @return the joined string.
     */
    public static String join(String joinString, String[] stringArray)
    {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (String s : stringArray)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                b.append(joinString);
            }
            b.append(s);
        }
        return b.toString();
    }
}
