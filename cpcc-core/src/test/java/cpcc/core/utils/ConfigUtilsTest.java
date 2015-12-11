// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertEquals;

import java.lang.reflect.Constructor;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.ConfigUtils;

/**
 * ConfigUtilsTest
 */
public class ConfigUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ConfigUtils> cnt = ConfigUtils.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @SuppressWarnings("serial")
    Map<String, List<String>> stringConfig = new HashMap<String, List<String>>()
    {
        {
            put("a", Arrays.asList("a", "b", "c"));
            put("b", Arrays.asList("d", "e", "f"));
            put("c", Arrays.asList("g", "h", "k"));
            put("d", Arrays.asList("l", null, null));
        }
    };

    @DataProvider
    public Object[][] stringConfigDataProvider()
    {
        return new Object[][]{
            new Object[]{"a", 0, "x", "a"},
            new Object[]{"a", 1, "x", "b"},
            new Object[]{"a", 2, "x", "c"},
            new Object[]{"b", 0, "x", "d"},
            new Object[]{"b", 1, "x", "e"},
            new Object[]{"b", 2, "x", "f"},
            new Object[]{"c", 0, "x", "g"},
            new Object[]{"c", 1, "x", "h"},
            new Object[]{"c", 2, "x", "k"},
            new Object[]{"d", 0, "x", "l"},
            new Object[]{"d", 1, "y", "y"},
            new Object[]{"d", 2, "z", "z"},
            new Object[]{"e", 0, "t", "t"},
        };
    };

    @Test(dataProvider = "stringConfigDataProvider")
    public void shouldParseString(String propertyName, int index, String defaultValue, String expectedResult)
    {
        assertEquals(ConfigUtils.parseString(stringConfig, propertyName, index, defaultValue), expectedResult);
    }

    @SuppressWarnings("serial")
    Map<String, List<String>> doubleConfig = new HashMap<String, List<String>>()
    {
        {
            put("a", Arrays.asList("3.14", "15.92", "6"));
            put("b", Arrays.asList("2.79", null, null));
        }
    };

    @DataProvider
    public Object[][] doubleConfigDataProvider()
    {
        return new Object[][]{
            new Object[]{"a", 0, 1.7, 3.14},
            new Object[]{"a", 1, 1.7, 15.92},
            new Object[]{"a", 2, 1.7, 6.0},
            new Object[]{"b", 0, 1.7, 2.79},
            new Object[]{"b", 1, 1.8, 1.8},
            new Object[]{"b", 2, 1.9, 1.9},
            new Object[]{"c", 0, 1.6, 1.6},
        };
    };

    @Test(dataProvider = "doubleConfigDataProvider")
    public void shouldParseDouble(String propertyName, int index, double defaultValue, double expectedResult)
    {
        assertEquals(ConfigUtils.parseDouble(doubleConfig, propertyName, index, defaultValue), expectedResult);
    }

    @SuppressWarnings("serial")
    Map<String, List<String>> integerConfig = new HashMap<String, List<String>>()
    {
        {
            put("a", Arrays.asList("314", "1592", "6"));
            put("b", Arrays.asList("279", null, null));
        }
    };

    @DataProvider
    public Object[][] integerConfigDataProvider()
    {
        return new Object[][]{
            new Object[]{"a", 0, 17, 314},
            new Object[]{"a", 1, 17, 1592},
            new Object[]{"a", 2, 17, 6},
            new Object[]{"b", 0, 17, 279},
            new Object[]{"b", 1, 18, 18},
            new Object[]{"b", 2, 19, 19},
            new Object[]{"c", 0, 16, 16},
        };
    };

    @Test(dataProvider = "integerConfigDataProvider")
    public void shouldParseInteger(String propertyName, int index, int defaultValue, int expectedResult)
    {
        assertEquals(ConfigUtils.parseInteger(integerConfig, propertyName, index, defaultValue), expectedResult);
    }

    @SuppressWarnings("serial")
    Map<String, List<String>> polarCoordinateConfig = new HashMap<String, List<String>>()
    {
        {
            put("a", Arrays.asList("47.4567", "-122.9223", "5"));
            put("b", Arrays.asList(null, "47.4567", "+122.9223", "6"));
            put("c", Arrays.asList(null, null, "-47.457", "12.92", "7"));
            put("d", Arrays.asList(null, null, null, "+47.457", "12.92", "60"));
        }
    };

    @DataProvider
    public Object[][] polarCoordinateDataProvider()
    {
        return new Object[][]{
            new Object[]{"a", 0, new PolarCoordinate(47.4567, -122.9223, 5)},
            new Object[]{"b", 1, new PolarCoordinate(47.4567, +122.9223, 6)},
            new Object[]{"c", 2, new PolarCoordinate(-47.457, 12.92, 7)},
            new Object[]{"d", 3, new PolarCoordinate(+47.457, 12.92, 60)},
            new Object[]{"d", 0, new PolarCoordinate(0, 0, 0)},
            new Object[]{"e", 0, new PolarCoordinate(0, 0, 0)},
        };
    };

    @Test(dataProvider = "polarCoordinateDataProvider")
    public void shouldParsePolarCoordinates(String propertyName, int startIndex, PolarCoordinate expectedResult)
    {
        PolarCoordinate actual = ConfigUtils.parsePolarCoordinate(polarCoordinateConfig, propertyName, startIndex);
        assertEquals(actual.getLatitude(), expectedResult.getLatitude());
        assertEquals(actual.getLongitude(), expectedResult.getLongitude());
        assertEquals(actual.getAltitude(), expectedResult.getAltitude());
    }

}
