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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;

/**
 * RealVehicleTest
 */
class RealVehicleTest
{
    private RealVehicle sut;

    @BeforeEach
    void setUp()
    {
        sut = new RealVehicle();
    }

    static Stream<Arguments> integerDataProvider()
    {
        return Stream.of(
            arguments(0),
            arguments(1),
            arguments(2),
            arguments(3),
            arguments(5),
            arguments(7),
            arguments(11),
            arguments(1009));
    }

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldStoreId(int id)
    {
        sut.setId(id);
        assertThat(sut.getId()).isNotNull().isEqualTo(id);
    }

    static Stream<Arguments> stringDataProvider()
    {
        return Stream.of(
            arguments(""),
            arguments("a"),
            arguments("a,b"),
            arguments("a, b,c"),
            arguments("a,b , c"));
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreAreaOfOperations(String areaOfOperation)
    {
        sut.setAreaOfOperation(areaOfOperation);
        assertThat(sut.getAreaOfOperation()).isNotNull().isEqualTo(areaOfOperation);
    }

    static Stream<Arguments> booleanDataProvider()
    {
        return Stream.of(
            arguments(Boolean.TRUE),
            arguments(Boolean.FALSE));
    }

    @ParameterizedTest
    @MethodSource("booleanDataProvider")
    void shouldStoreDeletedMarker(boolean deleted)
    {
        sut.setDeleted(deleted);
        assertThat(sut.getDeleted()).isEqualTo(deleted);
    }

    static Stream<Arguments> dateDataProvider()
    {
        return Stream.of(
            arguments(new Date(System.currentTimeMillis() + 10)),
            arguments(new Date(System.currentTimeMillis() + 100)),
            arguments(new Date(System.currentTimeMillis() + 1000)));
    }

    @ParameterizedTest
    @MethodSource("dateDataProvider")
    void shouldStoreLastUpdate(Date lastUpdate)
    {
        sut.setLastUpdate(lastUpdate);
        assertThat(sut.getLastUpdate()).isNotNull().isEqualTo(lastUpdate);
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreName(String name)
    {
        sut.setName(name);
        assertThat(sut.getName()).isNotNull().isEqualTo(name);
    }

    static Stream<Arguments> typeDataProvider()
    {
        return Stream
            .of(RealVehicleType.values())
            .map(vType -> arguments(vType));
    }

    @ParameterizedTest
    @MethodSource("typeDataProvider")
    void shouldStoreType(RealVehicleType type)
    {
        sut.setType(type);
        assertThat(sut.getType()).isNotNull().isEqualTo(type);
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreUrl(String url)
    {
        sut.setUrl(url);
        assertThat(sut.getUrl()).isNotNull().isEqualTo(url);
    }

    static Stream<Arguments> sensorDataProvider()
    {
        return Stream.of(
            arguments(Arrays.asList(
                mock(SensorDefinition.class))),
            arguments(Arrays.asList(
                mock(SensorDefinition.class), mock(SensorDefinition.class))),
            arguments(Arrays.asList(
                mock(SensorDefinition.class), mock(SensorDefinition.class), mock(SensorDefinition.class))));
    }

    @ParameterizedTest
    @MethodSource("sensorDataProvider")
    void should(List<SensorDefinition> sensors)
    {
        sut.setSensors(sensors);
        assertThat(sut.getSensors())
            .isNotNull()
            .containsExactlyElementsOf(sensors);
    }

    @SuppressWarnings("unchecked")
    private static RealVehicle setupRealVehicle(Object... data)
    {
        RealVehicle realVehicle = new RealVehicle();
        realVehicle.setAreaOfOperation((String) data[0]);
        realVehicle.setDeleted((Boolean) data[1]);
        realVehicle.setId((Integer) data[2]);
        realVehicle.setLastUpdate((Date) data[3]);
        realVehicle.setName((String) data[4]);
        realVehicle.setSensors((List<SensorDefinition>) data[5]);
        realVehicle.setType((RealVehicleType) data[6]);
        realVehicle.setUrl((String) data[7]);
        return realVehicle;
    }

    static Stream<Arguments> equalRealVehicleDataProvider()
    {
        SensorDefinition sen1 = mock(SensorDefinition.class);

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvC = setupRealVehicle("abc", true, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        return Stream.of(
            arguments(rvA, rvA),
            arguments(rvA, rvB),
            arguments(rvA, rvC));
    }

    @ParameterizedTest
    @MethodSource("equalRealVehicleDataProvider")
    void shouldFindEqualRealVehicles(RealVehicle a, RealVehicle b)
    {
        assertThat(a)
            .isEqualTo(b)
            .hasSameHashCodeAs(b);
    }

    static Stream<Arguments> notEqualRealVehicleDataProvider()
    {
        SensorDefinition sen1 = mock(SensorDefinition.class);
        Mockito.when(sen1.getId()).thenReturn(1);
        SensorDefinition sen2 = mock(SensorDefinition.class);
        Mockito.when(sen1.getId()).thenReturn(2);

        RealVehicle rvA = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvB = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01",
            Arrays.asList(new SensorDefinition[0]), RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvC = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen2),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvD = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", null,
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        RealVehicle rvE = setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", null,
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        return Stream.of(
            arguments(rvA,
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 20, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 10, new Date(987654321), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv02", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1, sen2),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.GROUND_STATION, "http://localhost:8080/rv01")),
            arguments(rvA,
                setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv02")),
            arguments(setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1, sen2),
                RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"), rvA),
            arguments(rvA, rvB),
            arguments(rvB, rvA),
            arguments(rvA, rvC),
            arguments(rvC, rvA),
            arguments(rvA, rvD),
            arguments(rvD, rvA),
            arguments(rvD, rvE),
            arguments(rvE, rvD));
    }

    @ParameterizedTest
    @MethodSource("notEqualRealVehicleDataProvider")
    void shouldFindNotEqualRealVehicles(RealVehicle a, RealVehicle b)
    {
        assertThat(a).isNotEqualTo(b);
        assertThat(a.hashCode()).describedAs("hash code").isNotEqualTo(b.hashCode());
    }

    @Test
    void shouldReturnFalseOnComparisonWithNull()
    {
        RealVehicle a = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        assertThat(a).isNotEqualTo(null);
    }

    @Test
    void shouldReturnFalseOnComparisonWithOtherTypes()
    {
        RealVehicle a = setupRealVehicle("abc", false, 10, new Date(123456789), "rv01", Arrays.asList(),
            RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01");

        assertThat(a).isNotEqualTo(new Object[0]);
    }

    static Stream<Arguments> realVehicleDataProvider()
    {
        SensorDefinition sen1 = new SensorDefinition();
        sen1.setId(9174);

        return Stream.of(
            arguments(Arrays.asList(
                setupRealVehicle("abcd", false, 10, new Date(123456789L), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle(null, false, 10, new Date(123456789L), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, null, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, null, "rv01", Arrays.asList(sen1), RealVehicleType.QUADROCOPTER,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), null, Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", null, RealVehicleType.QUADROCOPTER,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1), null,
                    "http://localhost:8080/rv01"),
                setupRealVehicle("abcd", false, 10, new Date(123456789), "rv01", Arrays.asList(sen1),
                    RealVehicleType.QUADROCOPTER, null))));
    }

    @ParameterizedTest
    @MethodSource("realVehicleDataProvider")
    void shouldCalculateHashCode(List<RealVehicle> rvList)
    {
        Set<Integer> codeSet = new HashSet<Integer>();

        for (RealVehicle rv : rvList)
        {
            int hash = rv.hashCode();
            assertThat(codeSet).doesNotContain(hash);
            codeSet.add(hash);
        }
    }

    @Test
    void shouldStoreState()
    {
        RealVehicleState state = mock(RealVehicleState.class);

        sut.setState(state);

        assertThat(sut.getState()).isSameAs(state);
    }
}
