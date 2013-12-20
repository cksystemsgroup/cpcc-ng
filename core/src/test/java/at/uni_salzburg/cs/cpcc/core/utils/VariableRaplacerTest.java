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

import java.util.Properties;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.utils.VariableReplacer;

/**
 * VariableRaplacerTest
 */
public class VariableRaplacerTest
{
    @SuppressWarnings("serial")
    private static final Properties props = new Properties()
    {
        {
            setProperty("a", "AA");
            setProperty("b", "BB");
            setProperty("a.b.c", "ABC");
            setProperty("d", "DDD");
        }
    };

    @DataProvider
    public Object[][] validTestStringsDataProvider()
    {
        return new Object[][]{
            new Object[]{"", ""},
            new Object[]{"x ${a}", "x AA"},
            new Object[]{"x ${a} ${b}", "x AA BB"},
            new Object[]{"x ${a} ${b}${a.b.c}", "x AA BBABC"},
            new Object[]{"x ${a} ${b}${a.b.c} ${d}", "x AA BBABC DDD"},
        };
    }

    @Test(dataProvider = "validTestStringsDataProvider")
    public void shouldReplaceKnownVariables(String testString, String expectedResult)
    {
        VariableReplacer variableReplacer = new VariableReplacer(props);
        String result = variableReplacer.replace(testString);
        assertThat(result).isNotNull().isEqualTo(expectedResult);
    }
}
