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

package cpcc.vvrte.services.task;

import java.util.List;

import org.assertj.core.api.Condition;

/**
 * Compares the elements two lists. The lists match if either both list are identical or one list is the reversed other
 * list.
 *
 * @param <T> the template class.
 */
public class TspListCondition<T> extends Condition<List<? extends T>>
{
    private List<T> expected;

    public TspListCondition(List<T> expected)
    {
        this.expected = expected;
    }

    @Override
    public boolean matches(List<? extends T> actual)
    {
        // System.out.println("List comparison:\n      actual=" + actual + "\n    expected=" + expected);

        if (expected.size() != actual.size())
        {
            return false;
        }

        boolean eq = true;
        for (int k = 0, l = expected.size(); k < l; ++k)
        {
            if (!expected.get(k).equals(actual.get(k)))
            {
                eq = false;
                break;
            }
        }

        if (!eq)
        {
            for (int k = 0, l = expected.size(); k < l; ++k)
            {
                if (!expected.get(k).equals(actual.get(l - 1 - k)))
                {
                    return false;
                }
            }
        }

        return true;
    }
}
