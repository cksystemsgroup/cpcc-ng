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
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;
import java.util.Properties;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PropertyUtilsTest
{
    private Properties props;

    @BeforeMethod
    public void setUp()
    {
        props = new Properties();
    }

    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<PropertyUtils> cnt = PropertyUtils.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{null, null, null},
            new Object[]{"abcd", null, null},
            new Object[]{"efgh", Boolean.FALSE, Boolean.FALSE.toString()},
            new Object[]{"xygt", Boolean.TRUE, Boolean.TRUE.toString()},
        };
    }

    @Test(dataProvider = "booleanDataProvider")
    public void shouldSetBooleanProperty(String key, Boolean data, String expected)
    {
        PropertyUtils.setProperty(props, key, data);

        int expectedSize = data == null ? 0 : 1;

        assertThat(props).hasSize(expectedSize);

        if (expectedSize > 0)
        {
            assertThat(props.getProperty(key)).isEqualTo(expected);
        }
    }

    @DataProvider
    public Object[][] longDataProvider()
    {
        return new Object[][]{
            new Object[]{null, null, null},
            new Object[]{"abcd", null, null},
            new Object[]{"efgh", 1234L, Long.valueOf(1234L).toString()},
            new Object[]{"xygt", 9876L, Long.valueOf(9876L).toString()},
        };
    }

    @Test(dataProvider = "longDataProvider")
    public void shouldSetLongProperty(String key, Long data, String expected)
    {
        PropertyUtils.setProperty(props, key, data);

        int expectedSize = data == null ? 0 : 1;

        assertThat(props).hasSize(expectedSize);

        if (expectedSize > 0)
        {
            assertThat(props.getProperty(key)).isEqualTo(expected);
        }
    }
}
