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

import java.lang.reflect.Constructor;

import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class GeoJsonUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<GeoJsonUtils> cnt = GeoJsonUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] geoJsonObjectsDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new GeoJsonObject(){},
                new double[]{}
            },
            new Object[]{
                new Point(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new Polygon(
                    new LngLatAlt(-122.426, 37.808),
                    new LngLatAlt(-122.427, 37.808),
                    new LngLatAlt(-122.427, 37.809),
                    new LngLatAlt(-122.426, 37.809),
                    new LngLatAlt(-122.426, 37.808)
                ),
                new double[]{-122.427, 37.808, -122.426, 37.809}
            },
        };
    }

    @Test(dataProvider = "geoJsonObjectsDataProvider")
    public void shouldFindBoundingBox(GeoJsonObject obj, double[] expected)
    {
        double[] actual = GeoJsonUtils.findBoundingBox(obj);

        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] bboxPositionDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN},
                new LngLatAlt(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.8, 37.2),
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.9, 37.3),
                new double[]{-122.9, 37.2, -122.8, 37.3}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new LngLatAlt(-122.7, 37.1),
                new double[]{-122.8, 37.1, -122.7, 37.2}
            },
        };
    }

    @Test(dataProvider = "bboxPositionDataProvider")
    public void shouldMergeBoundingBoxWithPosition(double[] actual, LngLatAlt position, double[] expected)
    {
        GeoJsonUtils.mergeBoundingBox(actual, position);

        assertThat(actual).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] bboxesDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN},
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.8, 37.2, -122.8, 37.2}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.9, 37.3, -122.8, 37.3},
                new double[]{-122.9, 37.2, -122.8, 37.3}
            },
            new Object[]{
                new double[]{-122.8, 37.2, -122.8, 37.2},
                new double[]{-122.7, 37.1, -122.7, 37.1},
                new double[]{-122.8, 37.1, -122.7, 37.2}
            },
        };
    }

    @Test(dataProvider = "bboxesDataProvider")
    public void shouldMergeBoundingBoxes(double[] actual, double[] other, double[] expected)
    {
        GeoJsonUtils.mergeBoundingBoxes(actual, other);

        assertThat(actual).isEqualTo(expected);
    }
}
