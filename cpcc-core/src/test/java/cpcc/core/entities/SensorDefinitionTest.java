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

package cpcc.core.entities;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;

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

    @DataProvider
    public Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{Boolean.TRUE},
            new Object[]{Boolean.FALSE},
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

    @Test(dataProvider = "booleanDataProvider")
    public void shouldStoreMessageType(Boolean deleted)
    {
        sd.setDeleted(deleted);
        assertThat(sd.getDeleted()).isEqualTo(deleted);
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
    public void shouldStroreVisibility(SensorVisibility visibility)
    {
        sd.setVisibility(visibility);
        assertThat(sd.getVisibility()).isNotNull().isEqualTo(visibility);
    }

    @Test
    public void shouldFindEqualObjects()
    {
        SensorDefinition sd1 = new SensorDefinition();
        sd1.setId(12);
        sd1.setDescription("sd1");
        sd1.setType(SensorType.ALTIMETER);
        sd1.setParameters("params1");
        sd1.setVisibility(SensorVisibility.ALL_VV);
        sd1.setLastUpdate(new Date(12345678));
        sd1.setMessageType("std_msgs/String");

        assertThat(sd1.equals(sd1)).isTrue();

        SensorDefinition sd2 = new SensorDefinition();
        sd2.setId(15);
        sd2.setDescription("sd2");
        sd2.setType(SensorType.AREA_OF_OPERATIONS);
        sd2.setParameters("param2");
        sd2.setVisibility(SensorVisibility.NO_VV);
        sd2.setLastUpdate(new Date(87654321));
        sd2.setMessageType("std_msgs/Float32");

        assertThat(sd1.equals(null)).isFalse();
        assertThat(sd1.equals(new Date())).isFalse();
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setDescription("sd1");
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setId(12);
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setType(SensorType.ALTIMETER);
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setParameters("params1");
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setVisibility(SensorVisibility.ALL_VV);
        assertThat(sd1.equals(sd2)).isFalse();

        sd2.setMessageType("std_msgs/String");
        assertThat(sd1.equals(sd2)).isTrue();

        sd2.setLastUpdate(new Date(12345678));
        assertThat(sd1.equals(sd2)).isTrue();
    }

    @Test
    public void shouldCalculateOwnHashCode()
    {
        SensorDefinition sd1 = new SensorDefinition();
        assertThat(sd1.hashCode()).isEqualTo(0);

        sd1.setId(12);
        assertThat(sd1.hashCode()).isEqualTo(492);

        sd1.setDescription("sd1");
        assertThat(sd1.hashCode()).isEqualTo(4206060);

        sd1.setType(SensorType.ALTIMETER);
        assertThat(sd1.hashCode()).isEqualTo(4206091);

        sd1.setParameters("params1");
        assertThat(sd1.hashCode()).isEqualTo(-1532332758);

        sd1.setVisibility(SensorVisibility.ALL_VV);
        assertThat(sd1.hashCode()).isEqualTo(-1532332689);

        sd1.setMessageType("std_msgs/String");
        assertThat(sd1.hashCode()).isEqualTo(-1068436571);

        sd1.setLastUpdate(new Date(12345678));
        assertThat(sd1.hashCode()).isEqualTo(-1068436571);
    }

    @Test
    public void shouldImplementToString()
    {
        SensorDefinition sd1 = new SensorDefinition();
        sd1.setId(12);
        sd1.setDescription("sd1");
        sd1.setType(SensorType.ALTIMETER);
        sd1.setParameters("p1");
        sd1.setVisibility(SensorVisibility.ALL_VV);
        sd1.setLastUpdate(new Date(12345678));
        sd1.setMessageType("std_msgs/String");

        assertThat(sd1.toString()).isNotNull().isEqualTo(
            "(id=12, description=sd1, type=ALTIMETER, lastUpdate=12345678, parameters=p1, visibility=ALL_VV, "
                + "messageType=std_msgs/String)");

        SensorDefinition sd2 = new SensorDefinition();
        sd2.setId(15);
        sd2.setDescription("sd2");
        sd2.setType(SensorType.CO2);
        sd2.setParameters("param2");
        sd2.setVisibility(SensorVisibility.NO_VV);
        sd2.setLastUpdate(new Date(87654321));
        sd2.setMessageType("std_msgs/Float32");

        assertThat(sd2.toString()).isNotNull().isEqualTo(
            "(id=15, description=sd2, type=CO2, lastUpdate=87654321, parameters=param2, visibility=NO_VV, "
                + "messageType=std_msgs/Float32)");
    }
}
