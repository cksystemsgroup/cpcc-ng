/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.testng.Assert.assertFalse;

import java.lang.reflect.Constructor;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.utils.StringUtilities;

/**
 * StringCompareUtilsTest
 */
public class StringUtilitiesTest
{
    @Test
    public void shouldHavePrivateConstructor() throws Exception
    {
        Constructor<StringUtilities> cnt = StringUtilities.class.getDeclaredConstructor();
        assertFalse(cnt.isAccessible());
        cnt.setAccessible(true);
        cnt.newInstance();
    }

    @DataProvider
    public Object[][] equalStringsDataProvider()
    {
        return new Object[][]{
            new Object[]{null, null},
            new Object[]{"a", "a"},
            new Object[]{"BBBB", "BBBB"},
            new Object[]{" C ", " C "},
        };
    }

    @Test(dataProvider = "equalStringsDataProvider")
    public void shouldDetectEqualStrings(String a, String b)
    {
        assertThat(StringUtilities.equals(a, b)).isTrue();
    }

    @DataProvider
    public Object[][] notEqualStringsDataProvider()
    {
        return new Object[][]{
            new Object[]{null, "a"},
            new Object[]{"a", null},
            new Object[]{"BBBB", "AAAA"},
            new Object[]{" C ", ""},
        };
    }

    @Test(dataProvider = "notEqualStringsDataProvider")
    public void shouldDetectNotEqualStrings(String a, String b)
    {
        assertThat(StringUtilities.equals(a, b)).isFalse();
    }

    @DataProvider
    public Object[][] stringArrayDataProvider()
    {
        return new Object[][]
        {
            new Object[]{",", new String[]{}, ""},
            new Object[]{"-", new String[]{"a"}, "a"},
            new Object[]{".", new String[]{"a","b"}, "a.b"},
            new Object[]{" # ", new String[]{"a","b","c"}, "a # b # c"},
            new Object[]{" *", new String[]{"a","b","c","d"}, "a *b *c *d"},
            new Object[]{"~ ", new String[]{"a","b","c","d","e"}, "a~ b~ c~ d~ e"},
        };
    }

    @Test(dataProvider = "stringArrayDataProvider")
    public void shouldJoinAnArrayOfStrings(String joinString, String[] stringArray, String expectedResult)
    {
        assertThat(StringUtilities.join(joinString, stringArray)).isNotNull().isEqualTo(expectedResult);
    }
}
