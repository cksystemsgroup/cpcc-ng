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

import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * MappingAttributesPKTest
 */
class MappingAttributesPKTest
{
    @Test
    void shouldHaveNothingInitializedWithDefaultConstructor()
    {
        MappingAttributesPK pk = new MappingAttributesPK();
        assertThat(pk.getDevice()).isNull();
        assertThat(pk.getTopic()).isNull();
    }

    static Stream<Arguments> primaryKeyDataProvider()
    {
        return Stream.of(
            arguments(null, null),
            arguments(new Device(), new Topic()),
            arguments(new Device(), new Topic()),
            arguments(new Device(), new Topic()),
            arguments(new Device(), new Topic())
        );
    };

    @ParameterizedTest
    @MethodSource("primaryKeyDataProvider")
    void shouldInitializeDeviceAndTopicCorrectlyByConstructor(Device device, Topic topic)
    {
        MappingAttributesPK pk = new MappingAttributesPK(device, topic);
        assertThat(pk.getDevice()).isEqualTo(device);
        assertThat(pk.getTopic()).isEqualTo(topic);
    }

    @ParameterizedTest
    @MethodSource("primaryKeyDataProvider")
    void shouldStoreDeviceAndTopicCorrectlyByConstructor(Device device, Topic topic)
    {
        MappingAttributesPK pk = new MappingAttributesPK();
        pk.setDevice(device);
        pk.setTopic(topic);
        assertThat(pk.getDevice()).isEqualTo(device);
        assertThat(pk.getTopic()).isEqualTo(topic);
    }
}
