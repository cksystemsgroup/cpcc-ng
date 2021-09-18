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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * TopicTest
 */
class TopicTest
{
    private Topic topic;

    @BeforeEach
    void setUp()
    {
        topic = new Topic();
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
        topic.setId(id);
        assertThat(topic.getId()).isEqualTo(id);
    }

    static Stream<Arguments> nodeTypeDataProvider()
    {
        return Stream.of(RosNodeType.values()).map(t -> arguments(t));
    }

    @ParameterizedTest
    @MethodSource("nodeTypeDataProvider")
    void shouldStoreNodeType(RosNodeType nodeType)
    {
        topic.setNodeType(nodeType);
        assertThat(topic.getNodeType()).isEqualTo(nodeType);
    }

    static Stream<Arguments> pathDataProvider()
    {
        return Stream.of(
            arguments(""),
            arguments("/"),
            arguments("/a"),
            arguments("/abcdefg"));
    };

    @ParameterizedTest
    @MethodSource("pathDataProvider")
    void shouldStoreSubPath(String path)
    {
        topic.setSubpath(path);
        assertThat(topic.getSubpath()).isEqualTo(path);
    }

    static Stream<Arguments> messageTypeDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("std_msgs/String"),
            arguments("sensor_msgs/Image"),
            arguments("sensor_msgs/Camera_Info"),
            arguments("std_msgs/Float32"));
    };

    @ParameterizedTest
    @MethodSource("messageTypeDataProvider")
    void shouldStoreMessagetype(String messageType)
    {
        topic.setMessageType(messageType);
        assertThat(topic.getMessageType()).isEqualTo(messageType);
    }

    static Stream<Arguments> classNameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("cpcc.ros.actuators.MorseWayPointControllerAdapter"),
            arguments("cpcc.ros.sensors.CameraSensorAdapter"));
    };

    @ParameterizedTest
    @MethodSource("classNameDataProvider")
    void shouldStoreAdapterClassName(String className)
    {
        topic.setAdapterClassName(className);
        assertThat(topic.getAdapterClassName()).isEqualTo(className);
    }

    static Stream<Arguments> topicCategoryDataProvider()
    {
        return Stream.of(TopicCategory.values()).map(t -> arguments(t));
    };

    @ParameterizedTest
    @MethodSource("topicCategoryDataProvider")
    void shouldStoreCategory(TopicCategory category)
    {
        topic.setCategory(category);
        assertThat(topic.getCategory()).isEqualTo(category);
    }
}
