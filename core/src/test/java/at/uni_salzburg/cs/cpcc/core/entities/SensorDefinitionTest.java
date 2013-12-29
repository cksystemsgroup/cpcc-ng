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
package at.uni_salzburg.cs.cpcc.core.entities;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * SensorDefinitionTest
 */
public class SensorDefinitionTest
{

    private SensorDefinition sd;

    @BeforeMethod
    public void setUp()
    {
        sd = new SensorDefinition();
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
        sd.setId(id);
        assertThat(sd.getId()).isNotNull().isEqualTo(id);
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
    public void shouldStoreDescription(String description)
    {
        sd.setDescription(description);
        assertThat(sd.getDescription()).isNotNull().isEqualTo(description);
    }

    @Test(dataProvider = "integerDataProvider")
    public void shouldStoreLastUpdate(int lastUpdate)
    {
        sd.setLastUpdate(new Date(lastUpdate));
        assertThat(sd.getLastUpdate()).isNotNull().isEqualTo(new Date(lastUpdate));
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreMessageType(String messageType)
    {
        sd.setMessageType(messageType);
        assertThat(sd.getMessageType()).isNotNull().isEqualTo(messageType);
    }

    @Test(dataProvider = "stringDataProvider")
    public void shouldStoreParameters(String parameters)
    {
        sd.setParameters(parameters);
        assertThat(sd.getParameters()).isNotNull().isEqualTo(parameters);
    }

    @DataProvider
    public Object[][] sensorTypeDataProvider()
    {
        Object[][] data = new Object[SensorType.values().length][];

        for (int k = 0, l = SensorType.values().length; k < l; ++k)
        {
            data[k] = new Object[]{SensorType.values()[k]};
        }

        return data;
    }

    @Test(dataProvider = "sensorTypeDataProvider")
    public void shouldStoreType(SensorType type)
    {
        sd.setType(type);
        assertThat(sd.getType()).isNotNull().isEqualTo(type);
    }

    @DataProvider
    public Object[][] visibilityDataProvider()
    {
        Object[][] data = new Object[SensorVisibility.values().length][];

        for (int k = 0, l = SensorVisibility.values().length; k < l; ++k)
        {
            data[k] = new Object[]{SensorVisibility.values()[k]};
        }

        return data;
    }

    @Test(dataProvider = "visibilityDataProvider")
    public void should(SensorVisibility visibility)
    {
        sd.setVisibility(visibility);
        assertThat(sd.getVisibility()).isNotNull().isEqualTo(visibility);
    }

}
