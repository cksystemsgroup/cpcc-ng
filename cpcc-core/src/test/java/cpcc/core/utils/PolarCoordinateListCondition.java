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

import java.util.List;

import org.assertj.core.api.Condition;

import cpcc.core.entities.PolarCoordinate;

/**
 * Compares the elements two lists. The lists match if either both list are identical or one list is the reversed other
 * list.
 *
 * @param <T> the template class.
 */
public class PolarCoordinateListCondition extends Condition<List<? extends PolarCoordinate>>
{
    private List<PolarCoordinate> expected;
    private double offset;

    public PolarCoordinateListCondition(List<PolarCoordinate> expected, double offset)
    {
        this.expected = expected;
        this.offset = offset;
    }

    @Override
    public boolean matches(List<? extends PolarCoordinate> actual)
    {
        System.out.println("List comparison:"
            + "\n      actual=" + (actual)
            + "\n    expected=" + (expected));

        if (expected.size() != actual.size())
        {
            return false;
        }

        for (int k = 0, l = expected.size(); k < l; ++k)
        {
            if (Math.abs(expected.get(k).getLongitude() - actual.get(k).getLongitude()) > offset)
            {
                return false;
            }

            if (Math.abs(expected.get(k).getLatitude() - actual.get(k).getLatitude()) > offset)
            {
                return false;
            }

        }

        return true;
    }

//    private String toString(List<? extends PolarCoordinate> actual)
//    {
//        return "[" + actual.stream().map(x -> x.toString()).collect(Collectors.joining(",")) + "]";
//    }
}
