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

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;
import java.util.TreeMap;

import org.apache.tapestry5.json.JSONObject;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.utils.JSONUtils;

/**
 * JSONUtilsTest
 */
public class JSONUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<JSONUtils> cnt = JSONUtils.class.getDeclaredConstructor();
        assertThat(cnt.isAccessible()).isFalse();
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] jsonDataProvider()
    {
        return new Object[][]{
            new Object[]{"{\"a\":1}"},
            new Object[]{"{\"b\":1,\"a\":2}"},
            new Object[]{"{\"features\":[{\"properties\":{\"type\":\"depot\"},\"type\":\"Feature\",\"geometry\":"
                + "{\"type\":\"Point\",\"coordinates\":[-122.42642641067503,37.807478357821374]}}],\"properties\":"
                + "{\"center\":{\"lng\":-122.4251925945282,\"lat\":37.80787675625266},\"zoom\":17,\"layer\":null},"
                + "\"type\":\"FeatureCollection\"}"},
        };
    }

    @Test(dataProvider = "jsonDataProvider")
    public void shouldConvertJsonObjectsToByteArrays(String jsonString) throws IOException
    {
        JSONObject obj = new JSONObject(jsonString);
        byte[] ba = JSONUtils.toByteArray(obj);

        assertThat(ba).isNotNull().isEqualTo(jsonString.getBytes());
    }

    @DataProvider
    public Object[][] mapDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new String[][]{{"a", "1"}, {"b", "2"}, {"c", "\"3\""}}, "{\"a\":1,\"b\":2,\"c\":\"3\"}"
            }
        };
    }

    @Test(dataProvider = "mapDataProvider")
    public void shouldConvertMapToJsonObject(String[][] data, String expected)
    {
        Map<String, String> actual = new TreeMap<String, String>();
        for (String[] entry : data)
        {
            actual.put(entry[0], entry[1]);
        }

        assertThat(JSONUtils.toJsonString(actual)).isEqualTo(expected);
    }

}
