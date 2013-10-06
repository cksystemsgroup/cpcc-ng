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
package at.uni_salzburg.cs.cpcc.rv.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * ParameterTest
 */
public class ParameterTest
{
    Parameter parameter;

    @BeforeMethod
    public void setUp()
    {
        parameter = new Parameter();
    }

    @DataProvider
    public Object[][] integerDataProvider()
    {
        return new Object[][]{
            new Object[]{1},
            new Object[]{10},
            new Object[]{1000},
            new Object[]{100000},
            new Object[]{1000000},
            new Object[]{10000000},
            new Object[]{100000000},
            new Object[]{1000000000},
        };
    };

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreId(Integer id)
    {
        parameter.setId(id);
        assertThat(parameter.getId()).isEqualTo(id);
    }

    @DataProvider
    public Object[][] nameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"parameter1"},
            new Object[]{"parameter2"},
        };
    };

    @Test(dataProvider = "nameDataProvider")
    public void shouldStoreName(String name)
    {
        parameter.setName(name);
        assertThat(parameter.getName()).isEqualTo(name);
    }

    @DataProvider
    public Object[][] valueDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"value1"},
            new Object[]{"value2"},
        };
    };

    @Test(dataProvider = "valueDataProvider")
    public void shouldStoreValue(String value)
    {
        parameter.setValue(value);
        assertThat(parameter.getValue()).isEqualTo(value);
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreSort(Integer sort)
    {
        parameter.setSort(sort);
        assertThat(parameter.getSort()).isEqualTo(sort);
    }

}
