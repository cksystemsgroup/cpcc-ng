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

package cpcc.ros.base;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * RosTopicTest
 */
public class RosTopicTest
{
    static Stream<Arguments> topicDataProvider()
    {
        return Stream.of(
            arguments(null, null),
            arguments("name1", null),
            arguments(null, "topic1"),
            arguments("name1", "topic1"));
    }

    @ParameterizedTest
    @MethodSource("topicDataProvider")
    public void shouldStoreNameAndType(String name, String type)
    {
        RosTopic topic = new RosTopic();
        topic.setName(name);
        topic.setType(type);

        assertThat(topic.getName()).isEqualTo(name);
        assertThat(topic.getType()).isEqualTo(type);
    }
}
