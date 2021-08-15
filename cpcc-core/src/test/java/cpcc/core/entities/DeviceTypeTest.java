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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * DeviceTypeTest
 */
class DeviceTypeTest
{
    DeviceType deviceType;

    @BeforeEach
    void setUp()
    {
        deviceType = new DeviceType();
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
    }

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldStoreId(Integer id)
    {
        deviceType.setId(id);
        assertThat(deviceType.getId()).isEqualTo(id);
    }

    static Stream<Arguments> nameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("Generic GPS Receiver"),
            arguments("Camera Sensor"),
            arguments("Sonar"),
            arguments("Thermometer"));
    }

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldStoreName(String name)
    {
        deviceType.setName(name);
        assertThat(deviceType.getName()).isEqualTo(name);
    }

    static Stream<Arguments> topicDataProvider()
    {
        return Stream.of(
            arguments(createTopic(0)),
            arguments(createTopic(1)),
            arguments(createTopic(2)),
            arguments(createTopic(3)),
            arguments(createTopic(4)),
            arguments(createTopic(5)),
            arguments(createTopic(6)),
            arguments(createTopic(7)),
            arguments(createTopic(8)),
            arguments(createTopic(9)));

    };

    static Topic createTopic(int counter)
    {
        Topic topic = new Topic();
        topic.setId(counter);
        topic.setAdapterClassName("testclass" + counter);
        topic.setSubpath("path" + counter);
        return topic;
    }

    @ParameterizedTest
    @MethodSource("topicDataProvider")
    void shouldStoreMainTopic(Topic topic)
    {
        deviceType.setMainTopic(topic);
        assertThat(deviceType.getMainTopic()).isEqualTo(topic);
    }

    static Stream<Arguments> classNameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("cpcc.ros.actuators.MorseWayPointControllerAdapter"),
            arguments("cpcc.ros.sensors.CameraSensorAdapter"));
    }

    @ParameterizedTest
    @MethodSource("classNameDataProvider")
    void shouldStoreClassName(String className)
    {
        deviceType.setClassName(className);
        assertThat(deviceType.getClassName()).isEqualTo(className);
    }

    static Stream<Arguments> subTopicsDataProvider()
    {
        return Stream.of(
            arguments(createTopicList(0)),
            arguments(createTopicList(1)),
            arguments(createTopicList(2)),
            arguments(createTopicList(3)),
            arguments(createTopicList(4)),
            arguments(createTopicList(5)),
            arguments(createTopicList(6)),
            arguments(createTopicList(7)),
            arguments(createTopicList(8)),
            arguments(createTopicList(9)));
    };

    static List<Topic> createTopicList(int counter)
    {
        return IntStream.range(0, counter)
            .mapToObj(i -> createTopic(i))
            .collect(Collectors.toList());
    }

    @ParameterizedTest
    @MethodSource("subTopicsDataProvider")
    void shouldStoreSubTopics(List<Topic> subTopics)
    {
        deviceType.setSubTopics(subTopics);
        assertThat(deviceType.getSubTopics()).isEqualTo(subTopics);
    }

}
