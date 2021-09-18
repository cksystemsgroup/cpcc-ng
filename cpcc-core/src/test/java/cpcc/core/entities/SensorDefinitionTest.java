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

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * SensorDefinitionTest
 */
class SensorDefinitionTest
{
    private SensorDefinition sd;

    @BeforeEach
    void setUp()
    {
        sd = new SensorDefinition();
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
        sd.setId(id);
        assertThat(sd.getId()).isNotNull().isEqualTo(id);
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

    static Stream<Arguments> booleanDataProvider()
    {
        return Stream.of(
            arguments((Boolean) null),
            arguments(Boolean.TRUE),
            arguments(Boolean.FALSE));
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreDescription(String description)
    {
        sd.setDescription(description);
        assertThat(sd.getDescription()).isNotNull().isEqualTo(description);
    }

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldStoreLastUpdate(int lastUpdate)
    {
        sd.setLastUpdate(new Date(lastUpdate));
        assertThat(sd.getLastUpdate()).isNotNull().isEqualTo(new Date(lastUpdate));
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreMessageType(String messageType)
    {
        sd.setMessageType(messageType);
        assertThat(sd.getMessageType()).isNotNull().isEqualTo(messageType);
    }

    @ParameterizedTest
    @MethodSource("booleanDataProvider")
    void shouldStoreMessageType(Boolean deleted)
    {
        sd.setDeleted(deleted);
        assertThat(sd.getDeleted()).isEqualTo(deleted);
    }

    @ParameterizedTest
    @MethodSource("stringDataProvider")
    void shouldStoreParameters(String parameters)
    {
        sd.setParameters(parameters);
        assertThat(sd.getParameters()).isNotNull().isEqualTo(parameters);
    }

    static Stream<Arguments> sensorTypeDataProvider()
    {
        return Stream.of(SensorType.values()).map(sType -> arguments(sType));
    }

    @ParameterizedTest
    @MethodSource("sensorTypeDataProvider")
    void shouldStoreType(SensorType type)
    {
        sd.setType(type);
        assertThat(sd.getType()).isNotNull().isEqualTo(type);
    }

    static Stream<Arguments> visibilityDataProvider()
    {
        return Stream.of(SensorVisibility.values()).map(sVis -> arguments(sVis));
    }

    @ParameterizedTest
    @MethodSource("visibilityDataProvider")
    void shouldStroreVisibility(SensorVisibility visibility)
    {
        sd.setVisibility(visibility);
        assertThat(sd.getVisibility()).isNotNull().isEqualTo(visibility);
    }

    @Test
    void shouldFindEqualObjects()
    {
        SensorDefinition sd1 = new SensorDefinition();
        sd1.setId(12);
        sd1.setDescription("sd1");
        sd1.setType(SensorType.ALTIMETER);
        sd1.setParameters("params1");
        sd1.setVisibility(SensorVisibility.ALL_VV);
        sd1.setLastUpdate(new Date(12345678));
        sd1.setMessageType("std_msgs/String");

        assertThat(sd1).isEqualTo(sd1);

        SensorDefinition sd2 = new SensorDefinition();
        sd2.setId(15);
        sd2.setDescription("sd2");
        sd2.setType(SensorType.AREA_OF_OPERATIONS);
        sd2.setParameters("param2");
        sd2.setVisibility(SensorVisibility.NO_VV);
        sd2.setLastUpdate(new Date(87654321));
        sd2.setMessageType("std_msgs/Float32");

        assertThat(sd1)
            .isNotEqualTo(null)
            .isNotEqualTo(new Date())
            .isNotEqualTo(sd2);

        sd2.setDescription("sd1");
        assertThat(sd1).isNotEqualTo(sd2);

        sd2.setId(12);
        assertThat(sd1).isNotEqualTo(sd2);

        sd2.setType(SensorType.ALTIMETER);
        assertThat(sd1).isNotEqualTo(sd2);

        sd2.setParameters("params1");
        assertThat(sd1).isNotEqualTo(sd2);

        sd2.setVisibility(SensorVisibility.ALL_VV);
        assertThat(sd1).isNotEqualTo(sd2);

        sd2.setMessageType("std_msgs/String");
        assertThat(sd1).isEqualTo(sd2);

        sd2.setLastUpdate(new Date(12345678));
        assertThat(sd1).isEqualTo(sd2);
    }

    @Test
    void shouldCalculateOwnHashCode()
    {
        SensorDefinition sd1 = new SensorDefinition();
        assertThat(sd1.hashCode()).isZero();

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
    void shouldImplementToString()
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
