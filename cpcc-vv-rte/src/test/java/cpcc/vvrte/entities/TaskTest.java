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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;

/**
 * TaskTest
 */
public class TaskTest
{
    private Task sut;
    private VirtualVehicle vehicle;

    // private Logger logger;

    @BeforeEach
    public void setUp()
    {
        vehicle = mock(VirtualVehicle.class);

        sut = new Task();
    }

    static Stream<Arguments> idDataProvider()
    {
        return Stream.of(
            arguments(Integer.valueOf(1)),
            arguments(Integer.valueOf(2)),
            arguments(Integer.valueOf(3)),
            arguments(Integer.valueOf(4)),
            arguments(Integer.valueOf(5)));
    }

    @ParameterizedTest
    @MethodSource("idDataProvider")
    public void shouldStoreId(Integer id)
    {
        sut.setId(id);

        assertThat(sut.getId()).isEqualTo(id);
    }

    @ParameterizedTest
    @MethodSource("idDataProvider")
    public void shouldStoreOrder(Integer id)
    {
        sut.setOrder(id);

        assertThat(sut.getOrder()).isEqualTo(id);
    }

    static Stream<Arguments> stateDataProvider()
    {
        return Stream.of(TaskState.values()).map(t -> arguments(t));
    }

    @ParameterizedTest
    @MethodSource("stateDataProvider")
    public void shouldStoreState(TaskState state)
    {
        sut.setTaskState(state);

        assertThat(sut.getTaskState()).isEqualTo(state);
    }

    static Stream<Arguments> positionDataProvider()
    {
        return Stream.of(
            arguments(new PolarCoordinate(37.1234, -122.0898, 0.0)),
            arguments(new PolarCoordinate(37.1234, 122.0898, 100.0)),
            arguments(new PolarCoordinate(-37.1234, -122.0898, -100.0)),
            arguments(new PolarCoordinate(-37.1234, 122.0898, 1.0)));
    };

    @ParameterizedTest
    @MethodSource("positionDataProvider")
    public void shouldStorePosition(PolarCoordinate position)
    {
        sut.setPosition(position);

        assertThat(sut.getPosition()).isSameAs(position);
    }

    static Stream<Arguments> distanceDataProvider()
    {
        return Stream.of(
            arguments(3.9),
            arguments((Double)null),
            arguments(3.333),
            arguments(7.1111),
            arguments(8.3));
    };

    @ParameterizedTest
    @MethodSource("distanceDataProvider")
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

    static Stream<Arguments> timeDataProvider()
    {
        return Stream.of(
            arguments(-1L),
            arguments(1381003668231L),
            arguments(1000000000000L),
            arguments(2222222222222L),
            arguments(1213141516171L));
    };

    @ParameterizedTest
    @MethodSource("timeDataProvider")
    public void shouldStoreCreationTime(long time)
    {
        sut.setCreationTime(new Date(time));
        assertThat(sut.getCreationTime().getTime()).isEqualTo(time);
    }

    static Stream<Arguments> toleranceDistanceDataProvider()
    {
        return Stream.of(
            arguments(-1.0, -1.0),
            arguments(1.0, 1.0),
            arguments(2.0, 2.0),
            arguments(2.9, 2.9),
            arguments(3.0, 3.0),
            arguments(3.1, 3.1),
            arguments(10.0, 10.0));
    }

    @ParameterizedTest
    @MethodSource("toleranceDistanceDataProvider")
    public void shouldStoreTolerance(double tolerance, double expectedTolerance)
    {
        sut.setTolerance(tolerance);
        assertThat(sut.getTolerance()).isEqualTo(expectedTolerance, offset(1E-8));
    }

    static Stream<Arguments> booleanDataProvider()
    {
        return Stream.of(
            arguments(Boolean.FALSE),
            arguments(Boolean.TRUE));
    }

    static Stream<Arguments> sensorListDataProvider()
    {
        return Stream.of(
            arguments(Arrays.asList(mock(SensorDefinition.class))),
            arguments(Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class))),
            arguments(Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class))),
            arguments(Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class), mock(SensorDefinition.class))));
    }

    @ParameterizedTest
    @MethodSource("sensorListDataProvider")
    public void shouldStoreSensorList(List<SensorDefinition> sensorList)
    {
        sut.getSensors().addAll(sensorList);

        assertThat(sut.getSensors()).isNotNull().hasSize(sensorList.size());
        assertThat(sut.getSensors()).containsExactly(sensorList.toArray(new SensorDefinition[0]));
    }

    static Stream<Arguments> dateDataProvider()
    {
        return Stream.of(
            arguments(new Date(12345678)),
            arguments(new Date(12345679)),
            arguments(new Date(12345680)),
            arguments(new Date(12345681)),
            arguments(new Date(12345682)));
    }

    @ParameterizedTest
    @MethodSource("dateDataProvider")
    public void shouldStoreExecutionStart(Date date)
    {
        sut.setExecutionStart(date);

        assertThat(sut.getExecutionStart()).isSameAs(date);
    }

    @ParameterizedTest
    @MethodSource("dateDataProvider")
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

    static Stream<Arguments> keyValuePairsDataProvider()
    {
        return Stream.of(
            arguments("a", "123"),
            arguments("b", 321),
            arguments("c", new byte[]{1, 2, 3, 4, 5, 6}));
    }

    @ParameterizedTest
    @MethodSource("keyValuePairsDataProvider")
    public void shouldStoreSensorValues(String name, Object expected)
    {
        NativeObject sensorValues = new NativeObject();
        sensorValues.put(name, sensorValues, expected);

        sut.setSensorValues(sensorValues);

        ScriptableObject actual = sut.getSensorValues();

        assertThat(actual.get(name)).isEqualTo(expected);
    }

    static Stream<Arguments> valueDataProvider()
    {
        VirtualVehicle v1 = mock(VirtualVehicle.class);
        when(v1.toString()).thenReturn("VV-1");
        when(v1.getName()).thenReturn("VV-1");
        when(v1.getUuid()).thenReturn("2efed8c8-8681-11e6-97ae-8f77cc48f640");

        VirtualVehicle v2 = mock(VirtualVehicle.class);
        when(v2.toString()).thenReturn("VV-2");
        when(v2.getName()).thenReturn("VV-2");
        when(v2.getUuid()).thenReturn("32daca92-8681-11e6-826d-2bc72e1cad17");

        return Stream.of(
            arguments(
                new Date(12345678), new Date(12345679), new Date(12345680),
                new PolarCoordinate(37.1234, -122.0898, 0.0), TaskState.EXECUTED, 1, 4.5, v1,
                "created 1970-01-01 04:25:45, started 1970-01-01 04:25:45, ended 1970-01-01 04:25:45, "
                    + "pos (37.1234°, -122.0898°, 0.0m), state EXECUTED, order 1, tolerance 4.5, "
                    + "VV VV-1 (2efed8c8-8681-11e6-97ae-8f77cc48f640)"),
            arguments(
                new Date(12345681), new Date(12345682), new Date(12345680),
                new PolarCoordinate(37.1234, 122.0898, 100.0), TaskState.COMPLETED, 2, 6.3, v2,
                "created 1970-01-01 04:25:45, started 1970-01-01 04:25:45, ended 1970-01-01 04:25:45, "
                    + "pos (37.1234°, 122.0898°, 100.0m), state COMPLETED, order 2, tolerance 6.3, "
                    + "VV VV-2 (32daca92-8681-11e6-826d-2bc72e1cad17)"),
            arguments(
                new Date(12345681), new Date(12345678), new Date(12345679),
                new PolarCoordinate(-37.1234, -122.0898, -100.0), TaskState.RUNNING, 3, 9.8, null,
                "created 1970-01-01 04:25:45, started 1970-01-01 04:25:45, ended 1970-01-01 04:25:45, "
                    + "pos (-37.1234°, -122.0898°, -100.0m), state RUNNING, order 3, tolerance 9.8, "),
            arguments(
                new Date(12345678), null, new Date(12345679),
                new PolarCoordinate(37.1234, -122.0898, 0.0), TaskState.EXECUTED, 1, 4.5, v1,
                "created 1970-01-01 04:25:45, started -, ended 1970-01-01 04:25:45, "
                    + "pos (37.1234°, -122.0898°, 0.0m), state EXECUTED, order 1, tolerance 4.5, "
                    + "VV VV-1 (2efed8c8-8681-11e6-97ae-8f77cc48f640)"),
            arguments(
                new Date(12345681), new Date(12345680), null,
                new PolarCoordinate(37.1234, 122.0898, 100.0), TaskState.COMPLETED, 2, 6.3, v2,
                "created 1970-01-01 04:25:45, started 1970-01-01 04:25:45, ended -, "
                    + "pos (37.1234°, 122.0898°, 100.0m), state COMPLETED, order 2, tolerance 6.3, "
                    + "VV VV-2 (32daca92-8681-11e6-826d-2bc72e1cad17)"));
    }

    @ParameterizedTest
    @MethodSource("valueDataProvider")
    public void shouldHaveProperStringRepresentation(Date creationTime, Date executionStart, Date executionEnd,
        PolarCoordinate position, TaskState taskState, int order, double tolerance, VirtualVehicle vehicle,
        String expected)
    {
        sut.setCreationTime(creationTime);
        sut.setExecutionStart(executionStart);
        sut.setExecutionEnd(executionEnd);
        sut.setPosition(position);
        sut.setTaskState(taskState);
        sut.setOrder(order);
        sut.setTolerance(tolerance);
        sut.setVehicle(vehicle);

        assertThat(sut.toString()).isEqualTo(expected);
    }
}
