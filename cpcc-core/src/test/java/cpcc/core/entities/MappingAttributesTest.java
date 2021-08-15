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
import static org.mockito.Mockito.when;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * MappingAttributesTest
 */
class MappingAttributesTest
{
    MappingAttributes attributes;

    @BeforeEach
    void setUp()
    {
        attributes = new MappingAttributes();
    }

    static Stream<Arguments> primaryKeyDataProvider()
    {
        MappingAttributesPK pk1 = mock(MappingAttributesPK.class);
        when(pk1.getDevice()).thenReturn(new Device());
        when(pk1.getTopic()).thenReturn(new Topic());

        MappingAttributesPK pk2 = mock(MappingAttributesPK.class);
        when(pk2.getDevice()).thenReturn(new Device());
        when(pk2.getTopic()).thenReturn(new Topic());

        MappingAttributesPK pk3 = mock(MappingAttributesPK.class);
        when(pk3.getDevice()).thenReturn(new Device());
        when(pk3.getTopic()).thenReturn(new Topic());

        MappingAttributesPK pk4 = mock(MappingAttributesPK.class);
        when(pk4.getDevice()).thenReturn(new Device());
        when(pk4.getTopic()).thenReturn(new Topic());

        return Stream.of(
            arguments((MappingAttributesPK) null),
            arguments(pk1),
            arguments(pk2),
            arguments(pk3),
            arguments(pk4));
    };

    @ParameterizedTest
    @MethodSource("primaryKeyDataProvider")
    void shouldStorePrimaryKey(MappingAttributesPK pk)
    {
        attributes.setPk(pk);
        assertThat(attributes.getPk()).isEqualTo(pk);
    }

    static Stream<Arguments> booleanDataProvider()
    {
        return Stream.of(
            arguments((Boolean) null),
            arguments(Boolean.FALSE),
            arguments(Boolean.TRUE));
    };

    @ParameterizedTest
    @MethodSource("booleanDataProvider")
    void shouldStoreVvVisible(Boolean value)
    {
        attributes.setVvVisible(value);
        assertThat(attributes.getVvVisible()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("booleanDataProvider")
    void shouldConnectedToAutopilot(Boolean value)
    {
        attributes.setConnectedToAutopilot(value);
        assertThat(attributes.getConnectedToAutopilot()).isEqualTo(value);
    }

    @Test
    void shouldStoreSensorDefinition()
    {
        SensorDefinition def = mock(SensorDefinition.class);
        attributes.setSensorDefinition(def);
        assertThat(attributes.getSensorDefinition()).isNotNull().isEqualTo(def);
    }
}
