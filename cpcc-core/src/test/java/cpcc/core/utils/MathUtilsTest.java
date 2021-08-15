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
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * ConvertUtilsTest
 */
public class MathUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<MathUtils> cnt = MathUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> doubleValuesDataProvider()
    {
        return Stream.of(
            arguments(1, 3, 2),
            arguments(1.03, 1.01, 1.02));
    }

    @ParameterizedTest
    @MethodSource("doubleValuesDataProvider")
    public void shouldConvertDoubleListToString(double a, double b, double expected)
    {
        double actual = MathUtils.avg(a, b);

        assertThat(actual).isEqualTo(expected, offset(1E-8));
    }

    static Stream<Arguments> doubleNaNValuesDataProvider()
    {
        return Stream.of(
            arguments(new double[]{1, 3, 2}, true),
            arguments(new double[]{1.03, 1.01, 1.02}, true),
            arguments(new double[]{Double.NaN, 1.01, 1.02}, false),
            arguments(new double[]{1.03, Double.NaN, 1.02}, false),
            arguments(new double[]{1.03, 1.01, Double.NaN, 1.02}, false));
    }

    @ParameterizedTest
    @MethodSource("doubleNaNValuesDataProvider")
    public void shouldRecognizeNaNs(double[] values, boolean expected)
    {
        boolean actual = MathUtils.containsNoNaN(values);

        assertThat(actual).isEqualTo(expected);
    }
}
