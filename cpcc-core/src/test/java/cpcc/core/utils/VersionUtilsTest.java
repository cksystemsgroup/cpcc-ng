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
import java.lang.reflect.Modifier;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class VersionUtilsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<VersionUtils> cnt = VersionUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> versionResourcesDataProvicer()
    {
        return Stream.of(
            arguments("cpcc/core/utils/versionUtilsTest/version-01.properties", "1.0-SNAPSHOT"),
            arguments("cpcc/core/utils/versionUtilsTest/version-02.properties", "2.3.4"),
            arguments("cpcc/core/utils/versionUtilsTest/version-03.properties", "1.0-RC97"),
            arguments("cpcc/core/utils/versionUtilsTest/version-04.properties", "10.3"),
            arguments("cpcc/core/utils/versionUtilsTest/not-existing.properties", "0.0.0-UNKNOWN"));
    }

    @ParameterizedTest
    @MethodSource("versionResourcesDataProvicer")
    void shouldReadCorrectVersionInfo(String propResource, String expectedVersion)
    {
        String actual = VersionUtils.getVersion(propResource);

        assertThat(actual).isEqualTo(expectedVersion);
    }
}
