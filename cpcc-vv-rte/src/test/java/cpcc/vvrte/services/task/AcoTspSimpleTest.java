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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.utils.CartesianCoordinate;

/**
 * AcoTspSimpleTest implementation.
 */
public class AcoTspSimpleTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<AcoTspSimple> cnt = AcoTspSimple.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] pathDataProvider()
    {
        return new Object[][]{
            new Object[]{
                Arrays.asList(new CartesianCoordinate(1.0, 1.0, 1.0)),
                Collections.<Integer> emptyList()
            },
            new Object[]{
                Arrays.asList(
                    new CartesianCoordinate(1.0, 1.0, 1.0),
                    new CartesianCoordinate(5.0, 3.0, 1.0),
                    new CartesianCoordinate(8.0, 3.0, 1.0),
                    new CartesianCoordinate(2.0, 8.0, 1.0)),
                Arrays.asList(1, 2, 3)
            },
        };
    }

    @Test(dataProvider = "pathDataProvider")
    public void shouldCalculateTspPath(List<CartesianCoordinate> path, List<Integer> expected)
    {
        int n = path.size();

        double[][] costMatrix = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                costMatrix[i][j] = path.get(j).subtract(path.get(i)).norm();
            }
        }

        List<Integer> bestPath = AcoTspSimple.calculateBestPath(costMatrix, 1000, 3);
        AcoTspSimple.fixPath(0, bestPath);
        bestPath.remove(0);

        assertThat(bestPath).has(new TspListCondition<Integer>(expected));
    }

    @DataProvider
    public Object[][] wreckedCostMatrixDataprovider()
    {
        return new Object[][]{
            new Object[]{3, 4},
            new Object[]{0, 4},
            new Object[]{4, 3},
            new Object[]{4, 0},
        };
    }

    @Test(dataProvider = "wreckedCostMatrixDataprovider")
    public void shouldThrowExceptionOnInvalidMatrixDimenstions(int w, int h)
    {
        double[][] costMatrix = new double[w][h];

        for (int i = 0; i < w; i++)
        {
            for (int j = 0; j < h; j++)
            {
                costMatrix[i][j] = RandomUtils.nextDouble(0.0, 10.0);
            }
        }

        try
        {
            AcoTspSimple.calculateBestPath(costMatrix, 1000, 3);

            failBecauseExceptionWasNotThrown(IllegalStateException.class);
        }
        catch (IllegalStateException e)
        {
            assertThat(e).hasMessage("Cost matrix heigth and width must be equal!");
        }
    }

    @DataProvider
    public Object[][] fixPathDataProvider()
    {
        return new Object[][]{
            new Object[]{0, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(0, 1, 2, 3, 4, 5)},
            new Object[]{1, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(1, 2, 3, 4, 5, 0)},
            new Object[]{2, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(2, 3, 4, 5, 0, 1)},
            new Object[]{3, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(3, 4, 5, 0, 1, 2)},
            new Object[]{4, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(4, 5, 0, 1, 2, 3)},
            new Object[]{5, Arrays.asList(0, 1, 2, 3, 4, 5), Arrays.asList(5, 0, 1, 2, 3, 4)},
        };
    }

    @Test(dataProvider = "fixPathDataProvider")
    public void shouldFixPath(int index, List<Integer> data, List<Integer> expected)
    {
        List<Integer> actual = new ArrayList<>(data);

        AcoTspSimple.fixPath(index, actual);

        assertThat(actual).containsExactly(expected.toArray(new Integer[expected.size()]));
    }
}
