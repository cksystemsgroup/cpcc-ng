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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.api.Condition;

import cpcc.core.base.PolygonZone;

/**
 * Compares the elements two {@code Polygon} lists.
 */
public class PolygonListCondition extends Condition<List<? extends PolygonZone>>
{
    private List<PolygonZone> expected;
    private double offset;

    public PolygonListCondition(List<PolygonZone> expected, double offset)
    {
        this.expected = expected;
        this.offset = offset;
    }

    @Override
    public boolean matches(List<? extends PolygonZone> actual)
    {
        System.out.println("List comparison:"
            + "\n      actual=" + toString(actual)
            + "\n    expected=" + toString(expected));

        if (expected.size() != actual.size())
        {
            return false;
        }

        for (int k = 0, l = expected.size(); k < l; ++k)
        {
            assertThat(actual.get(k).getVertices())
                .describedAs("Polygon vertices")
                .has(new LngLatAltListCondition(expected.get(k).getVertices(), offset));
        }

        return true;
    }

    private String toString(List<? extends PolygonZone> actual)
    {
        return "[" + actual.stream().map(x -> x.toString()).collect(Collectors.joining(",")) + "]";
    }

}
