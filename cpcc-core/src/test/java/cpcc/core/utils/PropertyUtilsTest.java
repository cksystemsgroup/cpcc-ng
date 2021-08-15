// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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
import java.util.Properties;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

public class PropertyUtilsTest
{
    private Properties props;

    @BeforeEach
    public void setUp()
    {
        props = new Properties();
    }

    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<PropertyUtils> cnt = PropertyUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> booleanDataProvider()
    {
        return Stream.of(
            arguments(null, null, null),
            arguments("abcd", null, null),
            arguments("efgh", Boolean.FALSE, Boolean.FALSE.toString()),
            arguments("xygt", Boolean.TRUE, Boolean.TRUE.toString()));
    }

    @ParameterizedTest
    @MethodSource("booleanDataProvider")
    public void shouldSetBooleanProperty(String key, Boolean data, String expected)
    {
        PropertyUtils.setProperty(props, key, data);

        int expectedSize = data == null ? 0 : 1;

        assertThat(props).hasSize(expectedSize);

        if (expectedSize > 0)
        {
            assertThat(props.getProperty(key)).isEqualTo(expected);
        }
    }

    static Stream<Arguments> longDataProvider()
    {
        return Stream.of(
            arguments(null, null, null),
            arguments("abcd", null, null),
            arguments("efgh", 1234L, Long.valueOf(1234L).toString()),
            arguments("xygt", 9876L, Long.valueOf(9876L).toString()));
    }

    @ParameterizedTest
    @MethodSource("longDataProvider")
    public void shouldSetLongProperty(String key, Long data, String expected)
    {
        PropertyUtils.setProperty(props, key, data);

        int expectedSize = data == null ? 0 : 1;

        assertThat(props).hasSize(expectedSize);

        if (expectedSize > 0)
        {
            assertThat(props.getProperty(key)).isEqualTo(expected);
        }
    }
}
