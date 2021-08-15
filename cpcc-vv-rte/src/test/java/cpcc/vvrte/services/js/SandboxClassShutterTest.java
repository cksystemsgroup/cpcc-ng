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

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * SandboxClassShutterTest
 */
public class SandboxClassShutterTest
{
    private SandboxClassShutter shutter;

    @SuppressWarnings("serial")
    private static final Set<String> EXTRA_REGEX_DEFINED_CLASSES = new HashSet<String>()
    {
        {
            add("cpcc\\.vvrte\\.services\\.js\\.\\$BuiltInFunctions_.*");
            add("cpcc\\.vvrte\\.services\\.js\\.BuiltInFunctions_.*");
        }
    };

    private static final Set<String> EXTRA_ALLOWED_CLASSES = new HashSet<String>();

    @BeforeEach
    public void setUp()
    {
        shutter = new SandboxClassShutter(EXTRA_ALLOWED_CLASSES, EXTRA_REGEX_DEFINED_CLASSES);
    }

    static Stream<Arguments> basicallyAllowedClasses()
    {
        return Stream.of(
            arguments(java.lang.Math.class.getName()),
            arguments(java.io.PrintStream.class.getName()),
            arguments(cpcc.vvrte.services.js.BuiltInFunctions.class.getName()),
            arguments(java.util.ArrayList.class.getName()),
            arguments(java.util.List.class.getName()),
            arguments(java.util.Map.class.getName()),
            arguments(java.util.HashMap.class.getName()),
            arguments(java.util.Set.class.getName()),
            arguments(java.util.HashSet.class.getName()));
    }

    @ParameterizedTest
    @MethodSource("basicallyAllowedClasses")
    public void shouldAcceptAllowedClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isTrue();
    }

    static Stream<Arguments> someForbiddenClasses()
    {
        return Stream.of(
            arguments(java.lang.Thread.class.getName()),
            arguments(java.lang.System.class.getName()),
            arguments(java.io.File.class.getName()),
            arguments(java.io.FileOutputStream.class.getName()),
            arguments(java.io.FileInputStream.class.getName()));

    }

    @ParameterizedTest
    @MethodSource("someForbiddenClasses")
    public void shouldNotAcceptOtherClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isFalse();
    }

    static Stream<Arguments> extraRegexDefinedClassesDataProvider()
    {
        return Stream.of(
            arguments("cpcc.vvrte.services.js.$BuiltInFunctions_proxy12312412"),
            arguments("cpcc.vvrte.services.js.BuiltInFunctions_buggerit!"));
    }

    @ParameterizedTest
    @MethodSource("extraRegexDefinedClassesDataProvider")
    public void shouldAcceptAdditionalRegisteredRegexClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isTrue();
    }

}
