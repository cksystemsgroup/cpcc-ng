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

package cpcc.core.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.PolarCoordinate;

/**
 * ConfigUtilsTest
 */
class ConfigUtilsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ConfigUtils> cnt = ConfigUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    Map<String, List<String>> stringConfig = Stream
        .of(Pair.of("a", Arrays.asList("a", "b", "c")),
            Pair.of("b", Arrays.asList("d", "e", "f")),
            Pair.of("c", Arrays.asList("g", "h", "k")),
            Pair.of("d", Arrays.asList("l", null, null)))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    static Stream<Arguments> stringConfigDataProvider()
    {
        return Stream.of(
            arguments("a", 0, "x", "a"),
            arguments("a", 1, "x", "b"),
            arguments("a", 2, "x", "c"),
            arguments("b", 0, "x", "d"),
            arguments("b", 1, "x", "e"),
            arguments("b", 2, "x", "f"),
            arguments("c", 0, "x", "g"),
            arguments("c", 1, "x", "h"),
            arguments("c", 2, "x", "k"),
            arguments("d", 0, "x", "l"),
            arguments("d", 1, "y", "y"),
            arguments("d", 2, "z", "z"),
            arguments("e", 0, "t", "t"));
    };

    @ParameterizedTest
    @MethodSource("stringConfigDataProvider")
    void shouldParseString(String propertyName, int index, String defaultValue, String expectedResult)
    {
        assertThat(ConfigUtils.parseString(stringConfig, propertyName, index, defaultValue)).isEqualTo(expectedResult);
    }

    Map<String, List<String>> doubleConfig = Stream
        .of(Pair.of("a", Arrays.asList("3.14", "15.92", "6")),
            Pair.of("b", Arrays.asList("2.79", null, null)))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    static Stream<Arguments> doubleConfigDataProvider()
    {
        return Stream.of(
            arguments("a", 0, 1.7, 3.14),
            arguments("a", 1, 1.7, 15.92),
            arguments("a", 2, 1.7, 6.0),
            arguments("b", 0, 1.7, 2.79),
            arguments("b", 1, 1.8, 1.8),
            arguments("b", 2, 1.9, 1.9),
            arguments("c", 0, 1.6, 1.6));
    };

    @ParameterizedTest
    @MethodSource("doubleConfigDataProvider")
    void shouldParseDouble(String propertyName, int index, double defaultValue, double expectedResult)
    {
        assertThat(ConfigUtils.parseDouble(doubleConfig, propertyName, index, defaultValue)).isEqualTo(expectedResult);
    }

    Map<String, List<String>> integerConfig = Stream
        .of(Pair.of("a", Arrays.asList("314", "1592", "6")),
            Pair.of("b", Arrays.asList("279", null, null)))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    static Stream<Arguments> integerConfigDataProvider()
    {
        return Stream.of(
            arguments("a", 0, 17, 314),
            arguments("a", 1, 17, 1592),
            arguments("a", 2, 17, 6),
            arguments("b", 0, 17, 279),
            arguments("b", 1, 18, 18),
            arguments("b", 2, 19, 19),
            arguments("c", 0, 16, 16));
    };

    @ParameterizedTest
    @MethodSource("integerConfigDataProvider")
    void shouldParseInteger(String propertyName, int index, int defaultValue, int expectedResult)
    {
        assertThat(ConfigUtils.parseInteger(integerConfig, propertyName, index, defaultValue))
            .isEqualTo(expectedResult);
    }

    Map<String, List<String>> polarCoordinateConfig = Stream
        .of(Pair.of("a", Arrays.asList("47.4567", "-122.9223", "5")),
            Pair.of("b", Arrays.asList(null, "47.4567", "+122.9223", "6")),
            Pair.of("c", Arrays.asList(null, null, "-47.457", "12.92", "7")),
            Pair.of("d", Arrays.asList(null, null, null, "+47.457", "12.92", "60")))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));

    static Stream<Arguments> polarCoordinateDataProvider()
    {
        return Stream.of(
            arguments("a", 0, new PolarCoordinate(47.4567, -122.9223, 5)),
            arguments("b", 1, new PolarCoordinate(47.4567, +122.9223, 6)),
            arguments("c", 2, new PolarCoordinate(-47.457, 12.92, 7)),
            arguments("d", 3, new PolarCoordinate(+47.457, 12.92, 60)),
            arguments("d", 0, new PolarCoordinate(0, 0, 0)),
            arguments("e", 0, new PolarCoordinate(0, 0, 0)));
    };

    @ParameterizedTest
    @MethodSource("polarCoordinateDataProvider")
    void shouldParsePolarCoordinates(String propertyName, int startIndex, PolarCoordinate expectedResult)
    {
        PolarCoordinate actual = ConfigUtils.parsePolarCoordinate(polarCoordinateConfig, propertyName, startIndex);
        assertThat(actual.getLatitude()).isEqualTo(expectedResult.getLatitude());
        assertThat(actual.getLongitude()).isEqualTo(expectedResult.getLongitude());
        assertThat(actual.getAltitude()).isEqualTo(expectedResult.getAltitude());
    }

}
