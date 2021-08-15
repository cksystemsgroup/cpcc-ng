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
 * ParameterTest
 */
class ParameterTest
{
    Parameter parameter;

    @BeforeEach
    void setUp()
    {
        parameter = new Parameter();
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
        parameter.setId(id);
        assertThat(parameter.getId()).isEqualTo(id);
    }

    static Stream<Arguments> nameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("parameter1"),
            arguments("parameter2"));
    };

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldStoreName(String name)
    {
        parameter.setName(name);
        assertThat(parameter.getName()).isEqualTo(name);
    }

    static Stream<Arguments> valueDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("value1"),
            arguments("value2"));
    };

    @ParameterizedTest
    @MethodSource("valueDataProvider")
    void shouldStoreValue(String value)
    {
        parameter.setValue(value);
        assertThat(parameter.getValue()).isEqualTo(value);
    }

    @ParameterizedTest
    @MethodSource("integerDataProvider")
    void shouldStoreSort(Integer sort)
    {
        parameter.setSort(sort);
        assertThat(parameter.getSort()).isEqualTo(sort);
    }

}
