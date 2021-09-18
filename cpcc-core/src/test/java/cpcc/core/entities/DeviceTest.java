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

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * DeviceTest
 */
class DeviceTest
{
    private Device device;

    @BeforeEach
    void setUp()
    {
        device = new Device();
    }

    static Stream<Arguments> integerDataProvider()
    {
        return Stream.of(
            arguments(1),
            arguments(10),
            arguments(1000),
            arguments(100000),
            arguments(1000000),
            arguments(10000000),
            arguments(100000000),
            arguments(1000000000));
    };

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldStoreId(Integer id)
    {
        device.setId(id);
        assertThat(device.getId()).isEqualTo(id);
    }

    static Stream<Arguments> topicRootDataProvider()
    {
        return Stream.of(
            arguments("/"),
            arguments("/a"),
            arguments("/abcdefg"));
    };

    @ParameterizedTest
    @MethodSource("topicRootDataProvider")
    void shouldStoreTopicRoot(String topicRoot)
    {
        device.setTopicRoot(topicRoot);
        assertThat(device.getTopicRoot()).isEqualTo(topicRoot);
    }

    static final Stream<Arguments> deviceTypeDataProvider()
    {
        return Stream.of(
            arguments(mock(DeviceType.class)),
            arguments(mock(DeviceType.class)),
            arguments(mock(DeviceType.class))

        );
    };

    @ParameterizedTest
    @MethodSource("deviceTypeDataProvider")
    void shouldStoreType(DeviceType deviceType)
    {
        device.setType(deviceType);
        assertThat(device.getType()).isSameAs(deviceType);
    }

    static Stream<Arguments> configurationDataProvider()
    {
        return Stream.of(
            arguments(""),
            arguments("a=b"),
            arguments("a=b c=d"),
            arguments("a=(1,2,3) b=7 c='lala'"));
    };

    @ParameterizedTest
    @MethodSource("configurationDataProvider")
    void shouldStoreConfiguration(String configuration)
    {
        device.setConfiguration(configuration);
        assertThat(device.getConfiguration()).isEqualTo(configuration);
    }

    @Test
    void shouldBeALeaf()
    {
        assertThat(device.isLeaf()).isTrue();
    }

    @Test
    void shouldHaveNoChildren()
    {
        assertThat(device.hasChildren()).isFalse();
        assertThat(device.getChildren()).isNotNull().isEmpty();
    }

    @Test
    void shouldHaveNoLabel()
    {
        assertThat(device.getLabel()).isNull();
    }

    @Test
    void shouldHaveNoParentLabel()
    {
        assertThat(device.getParentLabel()).isNull();
    }

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldHaveUniqueId(Integer id)
    {
        device.setId(id);
        assertThat(device.getUniqueId()).isEqualTo("device:" + id);
    }

}
