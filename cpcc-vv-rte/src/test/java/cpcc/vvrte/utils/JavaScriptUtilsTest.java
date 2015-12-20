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
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONException;
import org.mozilla.javascript.NativeObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Java Script utilities tests.
 */
public class JavaScriptUtilsTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<JavaScriptUtils> cnt = JavaScriptUtils.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @SuppressWarnings("serial")
    @DataProvider
    public Object[][] jsDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new HashMap<String, Object>()
                {
                    {
                        put("one", 1);
                        put("two", Integer.valueOf(2));
                        put("thee", 3.1);
                        put("four", Double.valueOf(4.2));
                    }
                },
                "{\"thee\":3.1,\"four\":4.2,\"one\":1,\"two\":2}"
            },
            new Object[]{
                new HashMap<String, Object>()
                {
                    {
                        put("five", "FIVE");
                        put("six", Boolean.FALSE);
                        put("seven", Float.valueOf(7.9f));
                    }
                },
                "{\"six\":false,\"seven\":7.900000095367432,\"five\":\"FIVE\"}"
            },
        };
    }

    @Test(dataProvider = "jsDataProvider")
    public void shouldConvertJsObjectsToStrings(Map<String, Object> data, String expected) throws JSONException
    {
        NativeObject obj = new NativeObject();
        for (Entry<String, Object> entry : data.entrySet())
        {
            obj.put(entry.getKey(), obj, entry.getValue());
        }

        String actual = JavaScriptUtils.toJsonString(obj);
        System.out.println("actual " + actual);

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    @Test
    public void shouldReturnNullOnNullObject()
    {
        String actual = JavaScriptUtils.toJsonString(null);

        assertThat(actual).isNotNull().isEqualTo("null");
    }

}
