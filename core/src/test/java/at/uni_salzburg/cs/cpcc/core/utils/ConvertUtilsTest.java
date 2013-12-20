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

import static org.fest.assertions.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;
import java.util.List;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.utils.ConvertUtils;

/**
 * ConvertUtilsTest
 */
public class ConvertUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ConvertUtils> cnt = ConvertUtils.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] doubleValuesDataProvider()
    {
        return new Object[][] {
            new Object[]{new double[0], new String[]{"[]"}},
            new Object[]{new double[]{1}, new String[]{"[1.0]"}},
            new Object[]{new double[]{1,2}, new String[]{"[1.0, 2.0]"}},
            new Object[]{new double[]{1,2,3}, new String[]{"[1.0, 2.0, 3.0]"}},
        };
    }
    
    @Test(dataProvider = "doubleValuesDataProvider")
    public void shouldConvertDoubleListToString(double[] values, String[] expectedResult)
    {
        List<String> result = ConvertUtils.doubleListAsString(values);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }
    
    @DataProvider
    public Object[][] byteValuesDataProvider()
    {
        return new Object[][] {
            new Object[]{(byte)0, "0"},
            new Object[]{(byte)1, "1"},
            new Object[]{(byte)2, "2"},
            new Object[]{(byte)3, "3"},
            new Object[]{(byte)127, "127"},
            new Object[]{(byte)128, "128"},
            new Object[]{(byte)255, "255"},
        };
    }
    
    @Test(dataProvider = "byteValuesDataProvider")
    public void shouldConvertByteToString(byte value, String expectedResult)
    {
        List<String> result = ConvertUtils.byteAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }
    
    @DataProvider
    public Object[][] shortValuesDataProvider()
    {
        return new Object[][] {
            new Object[]{(short)0, "0"},
            new Object[]{(short)1, "1"},
            new Object[]{(short)2, "2"},
            new Object[]{(short)3, "3"},
            new Object[]{(short)127, "127"},
            new Object[]{(short)128, "128"},
            new Object[]{(short)255, "255"},
            new Object[]{(short)256, "256"},
            new Object[]{(short)-1, "-1"},
            new Object[]{(short)-128, "-128"},
            new Object[]{(short)-256, "-256"},
        };
    }
    
    @Test(dataProvider = "shortValuesDataProvider")
    public void shouldConvertShortToString(short value, String expectedResult)
    {
        List<String> result = ConvertUtils.shortAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }
    
    @DataProvider
    public Object[][] floatValuesDataProvider()
    {
        return new Object[][] {
            new Object[]{(float)0, "0.0"},
            new Object[]{(float)1.1, "1.1"},
            new Object[]{(float)2.2, "2.2"},
            new Object[]{(float)3.3, "3.3"},
            new Object[]{(float)127.1, "127.1"},
            new Object[]{(float)128.2, "128.2"},
            new Object[]{(float)255.3, "255.3"},
            new Object[]{(float)256.1, "256.1"},
            new Object[]{(float)-1.2, "-1.2"},
            new Object[]{(float)-128.3, "-128.3"},
            new Object[]{(float)-256.4, "-256.4"},
        };
    }
    
    @Test(dataProvider = "floatValuesDataProvider")
    public void shouldConvertFloatToString(float value, String expectedResult)
    {
        List<String> result = ConvertUtils.floatAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }
}
