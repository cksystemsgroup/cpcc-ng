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
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * ConvertUtilsTest
 */
class ConvertUtilsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<ConvertUtils> cnt = ConvertUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> doubleValuesDataProvider()
    {
        return Stream.of(
            arguments(new double[0], new String[]{"[]"}),
            arguments(new double[]{1}, new String[]{"[1.0]"}),
            arguments(new double[]{1, 2}, new String[]{"[1.0, 2.0]"}),
            arguments(new double[]{1, 2, 3}, new String[]{"[1.0, 2.0, 3.0]"}));
    }

    @ParameterizedTest
    @MethodSource("doubleValuesDataProvider")
    void shouldConvertDoubleListToString(double[] values, String[] expectedResult)
    {
        List<String> result = ConvertUtils.doubleListAsString(values);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }

    static Stream<Arguments> byteValuesDataProvider()
    {
        return Stream.of(
            arguments((byte) 0, "0"),
            arguments((byte) 1, "1"),
            arguments((byte) 2, "2"),
            arguments((byte) 3, "3"),
            arguments((byte) 127, "127"),
            arguments((byte) 128, "128"),
            arguments((byte) 255, "255"));
    }

    @ParameterizedTest
    @MethodSource("byteValuesDataProvider")
    void shouldConvertByteToString(byte value, String expectedResult)
    {
        List<String> result = ConvertUtils.byteAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }

    static Stream<Arguments> shortValuesDataProvider()
    {
        return Stream.of(
            arguments((short) 0, "0"),
            arguments((short) 1, "1"),
            arguments((short) 2, "2"),
            arguments((short) 3, "3"),
            arguments((short) 127, "127"),
            arguments((short) 128, "128"),
            arguments((short) 255, "255"),
            arguments((short) 256, "256"),
            arguments((short) -1, "-1"),
            arguments((short) -128, "-128"),
            arguments((short) -256, "-256"));
    }

    @ParameterizedTest
    @MethodSource("shortValuesDataProvider")
    void shouldConvertShortToString(short value, String expectedResult)
    {
        List<String> result = ConvertUtils.shortAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }

    static Stream<Arguments> floatValuesDataProvider()
    {
        return Stream.of(
            arguments((float) 0, "0.0"),
            arguments((float) 1.1, "1.1"),
            arguments((float) 2.2, "2.2"),
            arguments((float) 3.3, "3.3"),
            arguments((float) 127.1, "127.1"),
            arguments((float) 128.2, "128.2"),
            arguments((float) 255.3, "255.3"),
            arguments((float) 256.1, "256.1"),
            arguments((float) -1.2, "-1.2"),
            arguments((float) -128.3, "-128.3"),
            arguments((float) -256.4, "-256.4"));
    }

    @ParameterizedTest
    @MethodSource("floatValuesDataProvider")
    void shouldConvertFloatToString(float value, String expectedResult)
    {
        List<String> result = ConvertUtils.floatAsString(value);
        assertThat(result).isNotNull().containsExactly(expectedResult);
    }
}
