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

package cpcc.vvrte.services.js;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;

import java.io.PrintStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * VvRteFunctionsTest
 */
class VvRteFunctionsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<VvRteFunctions> cnt = VvRteFunctions.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> vvRteDataProvider()
    {
        return Stream.of(
            arguments(mock(BuiltInFunctions.class)),
            arguments(mock(BuiltInFunctions.class)),
            arguments(mock(BuiltInFunctions.class)));
    }

    @ParameterizedTest
    @MethodSource("vvRteDataProvider")
    void shouldStoreVvRte(BuiltInFunctions vvRte)
    {
        VvRteFunctions.setVvRte(vvRte);
        assertThat(VvRteFunctions.getVvRte()).isNotNull().isSameAs(vvRte);
    }

    static Stream<Arguments> stdOutDataProvider()
    {
        return Stream.of(
            arguments(mock(PrintStream.class)),
            arguments(mock(PrintStream.class)),
            arguments(mock(PrintStream.class)));
    }

    @ParameterizedTest
    @MethodSource("stdOutDataProvider")
    void shouldStoreVvRte(PrintStream stdOut)
    {
        VvRteFunctions.setStdOut(stdOut);
        assertThat(VvRteFunctions.getStdOut()).isNotNull().isSameAs(stdOut);
    }

}
