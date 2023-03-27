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

package cpcc.vvrte.utils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.json.JSONException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mozilla.javascript.NativeObject;
import org.skyscreamer.jsonassert.JSONAssert;

import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;

/**
 * Java Script utilities tests.
 */
class JavaScriptUtilsTest
{
    @Test
    void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<JavaScriptUtils> cnt = JavaScriptUtils.class.getDeclaredConstructor();
        assertThat(Modifier.isPrivate(cnt.getModifiers())).isTrue();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    static Stream<Arguments> jsDataProvider()
    {
        return Stream.of(
            arguments(Stream
                .of(Pair.of("one", 1),
                    Pair.of("two", Integer.valueOf(2)),
                    Pair.of("thee", 3.1),
                    Pair.of("four", Double.valueOf(4.2)))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)),
                "{\"thee\":3.1,\"four\":4.2,\"one\":1,\"two\":2}"),
            arguments(Stream
                .of(Pair.of("five", "FIVE"),
                    Pair.of("six", Boolean.FALSE),
                    Pair.of("seven", Float.valueOf(7.9f)))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)),
                "{\"six\":false,\"seven\":7.900000095367432,\"five\":\"FIVE\"}"),
            arguments(Stream
                .of(Pair.of("two", "FIVE"),
                    Pair.of("one", Boolean.FALSE),
                    Pair.of("six", Float.valueOf(7.9f)),
                    Pair.of("nine", "bugger=lala looney=3.141592 caspar='xxx uu'"))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)),
                "{\"nine\":\"bugger=lala looney=3.141592 caspar='xxx uu'\""
                    + ",\"six\":7.900000095367432,\"one\":false,\"two\":\"FIVE\"}"),
            arguments(Stream
                .of(Pair.of("eleven", "FIVE"),
                    Pair.of("sen", SensorType.CAMERA.name()),
                    Pair.of("vis", SensorVisibility.PRIVILEGED_VV.name()))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)),
                "{\"vis\":\"PRIVILEGED_VV\",\"eleven\":\"FIVE\",\"sen\":\"CAMERA\"}"));
    }

    @ParameterizedTest
    @MethodSource("jsDataProvider")
    void shouldConvertJsObjectsToStrings(Map<String, Object> data, String expected) throws JSONException
    {
        NativeObject obj = new NativeObject();
        for (Entry<String, Object> entry : data.entrySet())
        {
            obj.put(entry.getKey(), obj, entry.getValue());
        }

        String actual = JavaScriptUtils.toJsonString(obj);
        System.out.println("actual " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    @Test
    void shouldReturnNullOnNullObject()
    {
        String actual = JavaScriptUtils.toJsonString(null);

        assertThat(actual).isNotNull().isEqualTo("null");
    }

}
