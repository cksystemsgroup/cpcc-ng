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

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import org.apache.tapestry5.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * JSONUtilsTest
 */
class JSONUtilsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<JSONUtils> cnt = JSONUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> jsonDataProvider()
    {
        return Stream.of(
            arguments("{\"a\":1}"),
            arguments("{\"b\":1,\"a\":2}"),
            arguments("{\"features\":[{\"properties\":{\"type\":\"depot\"},\"type\":\"Feature\",\"geometry\":"
                + "{\"type\":\"Point\",\"coordinates\":[-122.42642641067503,37.807478357821374]}}],\"properties\":"
                + "{\"center\":{\"lng\":-122.4251925945282,\"lat\":37.80787675625266},\"zoom\":17,\"layer\":null},"
                + "\"type\":\"FeatureCollection\"}"));
    }

    @ParameterizedTest
    @MethodSource("jsonDataProvider")
    void shouldConvertJsonObjectsToByteArrays(String jsonString) throws IOException
    {
        JSONObject obj = new JSONObject(jsonString);
        byte[] ba = JSONUtils.toByteArray(obj);

        assertThat(ba).isNotNull().isEqualTo(jsonString.getBytes());
    }

    static Stream<Arguments> mapDataProvider()
    {
        return Stream.of(
            arguments(
                new String[][]{{"a", "1"}, {"b", "2"}, {"c", "\"3\""}}, "{\"a\":1,\"b\":2,\"c\":\"3\"}"));
    }

    @ParameterizedTest
    @MethodSource("mapDataProvider")
    void shouldConvertMapToJsonObject(String[][] data, String expected)
    {
        Map<String, String> actual = new TreeMap<String, String>();
        for (String[] entry : data)
        {
            actual.put(entry[0], entry[1]);
        }

        assertThat(JSONUtils.toJsonString(actual)).isEqualTo(expected);
    }

}
