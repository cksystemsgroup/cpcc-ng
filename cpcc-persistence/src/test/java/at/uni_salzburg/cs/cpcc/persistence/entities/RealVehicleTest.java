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
package at.uni_salzburg.cs.cpcc.persistence.entities;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * RealVehicleTest
 */
public class RealVehicleTest
{

    private RealVehicle rv;

    @BeforeMethod
    public void setUp()
    {
        rv = new RealVehicle();
    }

    @DataProvider
    public Object[][] integerDataProvider()
    {
        return new Object[][]{
            new Object[]{0},
            new Object[]{1},
            new Object[]{2},
            new Object[]{3},
            new Object[]{5},
            new Object[]{7},
            new Object[]{11},
            new Object[]{1009},
        };
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreId(int id)
    {
        rv.setId(id);
        assertThat(rv.getId()).isNotNull().isEqualTo(id);
    }

    @DataProvider
    public Object[][] stringDataProvider()
    {
        return new Object[][]{
            new Object[]{""},
            new Object[]{"a"},
            new Object[]{"a,b"},
            new Object[]{"a, b,c"},
            new Object[]{"a,b , c"},
        };
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreAreaOfOperations(String areaOfOperation)
    {
        rv.setAreaOfOperation(areaOfOperation);
        assertThat(rv.getAreaOfOperation()).isNotNull().isEqualTo(areaOfOperation);
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreLastUpdate(int lastUpdate)
    {
        rv.setLastUpdate(lastUpdate);
        assertThat(rv.getLastUpdate()).isNotNull().isEqualTo(lastUpdate);
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreName(String name)
    {
        rv.setName(name);
        assertThat(rv.getName()).isNotNull().isEqualTo(name);
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreUrl(String url)
    {
        rv.setUrl(url);
        assertThat(rv.getUrl()).isNotNull().isEqualTo(url);
    }

    @DataProvider
    public Object[][] sensorDataProvider()
    {
        return new Object[][]{
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class)}
            },
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class), mock(SensorDefinition.class)}
            },
            new Object[]{new SensorDefinition[]{
                mock(SensorDefinition.class), mock(SensorDefinition.class), mock(SensorDefinition.class)}
            },
        };
    }

    @Test(dataProvider = "sensorDataProvider")
    public void should(SensorDefinition[] sensors)
    {
        rv.setSensors(Arrays.asList(sensors));
        assertThat(rv.getSensors()).isNotNull().containsExactly(sensors);
    }
}
