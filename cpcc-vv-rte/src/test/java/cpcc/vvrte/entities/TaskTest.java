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

package cpcc.vvrte.entities;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;

/**
 * TaskTest
 */
public class TaskTest
{
    private Task sut;
    private VirtualVehicle vehicle;

    //    private Logger logger;

    @BeforeMethod
    public void setUp()
    {
        vehicle = mock(VirtualVehicle.class);

        sut = new Task();
    }

    @DataProvider
    public Object[][] idDataProvider()
    {
        return new Object[][]{
            new Object[]{Integer.valueOf(1)},
            new Object[]{Integer.valueOf(2)},
            new Object[]{Integer.valueOf(3)},
            new Object[]{Integer.valueOf(4)},
            new Object[]{Integer.valueOf(5)},
        };
    }

    @Test(dataProvider = "idDataProvider")
    public void shouldStoreId(Integer id)
    {
        sut.setId(id);

        assertThat(sut.getId()).isEqualTo(id);
    }

    @Test(dataProvider = "idDataProvider")
    public void shouldStoreOrder(Integer id)
    {
        sut.setOrder(id);

        assertThat(sut.getOrder()).isEqualTo(id);
    }

    private int i = 0;

    @DataProvider
    public Object[][] stateDataProvider()
    {
        List<TaskState> values = Arrays.asList(TaskState.values());
        Object[][] data = new Object[values.size()][];
        values.stream().forEach(x -> data[i++] = new Object[]{x});
        return data;
    }

    @Test(dataProvider = "stateDataProvider")
    public void shouldStoreState(TaskState state)
    {
        sut.setTaskState(state);

        assertThat(sut.getTaskState()).isEqualTo(state);
    }

    @DataProvider
    public Object[][] positionDataProvider()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(37.1234, -122.0898, 0.0)},
            new Object[]{new PolarCoordinate(37.1234, 122.0898, 100.0)},
            new Object[]{new PolarCoordinate(-37.1234, -122.0898, -100.0)},
            new Object[]{new PolarCoordinate(-37.1234, 122.0898, 1.0)},
        };
    };

    @Test(dataProvider = "positionDataProvider")
    public void shouldStorePosition(PolarCoordinate position)
    {
        sut.setPosition(position);

        assertThat(sut.getPosition()).isSameAs(position);
    }

    @DataProvider
    public Object[][] distanceDataProvider()
    {
        return new Object[][]{
            new Object[]{3.9},
            new Object[]{null},
            new Object[]{3.333},
            new Object[]{7.1111},
            new Object[]{8.3},
        };
    };

    @Test(dataProvider = "distanceDataProvider")
    public void shouldStoreDistanceToTarget(Double expected)
    {
        sut.setDistanceToTarget(expected);
        assertThat(sut.getDistanceToTarget()).describedAs("distance to target").isEqualTo(expected);
    }

    @Test
    public void shouldHaveDefaultCreationTime()
    {
        long now = System.currentTimeMillis();
        assertThat(now - sut.getCreationTime().getTime()).isGreaterThanOrEqualTo(0).isLessThan(1000);
    }

    @DataProvider
    public Object[][] timeDataProvider()
    {
        return new Object[][]{
            new Object[]{-1L},
            new Object[]{1381003668231L},
            new Object[]{1000000000000L},
            new Object[]{2222222222222L},
            new Object[]{1213141516171L},
        };
    };

    @Test(dataProvider = "timeDataProvider")
    public void shouldStoreCreationTime(long time)
    {
        sut.setCreationTime(new Date(time));
        assertThat(sut.getCreationTime().getTime()).isEqualTo(time);
    }

    @DataProvider
    public static Object[][] toleranceDistanceDataProvider()
    {
        return new Object[][]{
            new Object[]{-1.0, -1.0},
            new Object[]{1.0, 1.0},
            new Object[]{2.0, 2.0},
            new Object[]{2.9, 2.9},
            new Object[]{3.0, 3.0},
            new Object[]{3.1, 3.1},
            new Object[]{10.0, 10.0},
        };
    }

    @Test(dataProvider = "toleranceDistanceDataProvider")
    public void shouldStoreTolerance(double tolerance, double expectedTolerance)
    {
        sut.setTolerance(tolerance);
        assertThat(sut.getTolerance()).isEqualTo(expectedTolerance, offset(1E-8));
    }

    @DataProvider
    public static Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{Boolean.FALSE},
            new Object[]{Boolean.TRUE},
        };
    }

    @DataProvider
    public Object[][] sensorListDataProvider()
    {
        return new Object[][]{
            new Object[]{Arrays.asList(mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class), mock(SensorDefinition.class))},
        };
    }

    @Test(dataProvider = "sensorListDataProvider")
    public void shouldStoreSensorList(List<SensorDefinition> sensorList)
    {
        sut.getSensors().addAll(sensorList);

        assertThat(sut.getSensors()).isNotNull().hasSize(sensorList.size());
        assertThat(sut.getSensors()).containsExactly(sensorList.toArray(new SensorDefinition[0]));
    }

    @DataProvider
    public Object[][] dateDataProvider()
    {
        return new Object[][]{
            new Object[]{new Date(12345678)},
            new Object[]{new Date(12345679)},
            new Object[]{new Date(12345680)},
            new Object[]{new Date(12345681)},
            new Object[]{new Date(12345682)},
        };
    }

    @Test(dataProvider = "dateDataProvider")
    public void shouldStoreExecutionStart(Date date)
    {
        sut.setExecutionStart(date);

        assertThat(sut.getExecutionStart()).isSameAs(date);
    }

    @Test(dataProvider = "dateDataProvider")
    public void shouldStoreExecutionEnd(Date date)
    {
        sut.setExecutionEnd(date);

        assertThat(sut.getExecutionEnd()).isSameAs(date);
    }

    @Test
    public void shouldStoreVehicle()
    {
        sut.setVehicle(vehicle);

        assertThat(sut.getVehicle()).isSameAs(vehicle);
    }

    @DataProvider
    public Object[][] keyValuePairsDataProvider()
    {
        return new Object[][]{
            new Object[]{"a", "123"},
            new Object[]{"b", 321},
            new Object[]{"c", new byte[]{1, 2, 3, 4, 5, 6}},
        };
    }

    @Test(dataProvider = "keyValuePairsDataProvider")
    public void shouldStoreSensorValues(String name, Object expected)
    {
        NativeObject sensorValues = new NativeObject();
        sensorValues.put(name, sensorValues, expected);

        sut.setSensorValues(sensorValues);

        ScriptableObject actual = sut.getSensorValues();

        assertThat(actual.get(name)).isEqualTo(expected);
    }
}
