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

package cpcc.commons.services;

import static org.assertj.core.api.Assertions.assertThat;

import java.text.ParsePosition;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.commons.services.MillisecondTimeFormat;

public class MillisecondTimeFormatTest
{
    @DataProvider
    public Object[][] dateDataProvider()
    {
        return new Object[][]{
            new Object[]{"yyyy-MM-dd HH:mm:ss", null, ""},
            new Object[]{"yyyy-MM-dd HH:mm:ss", 1439721013072L, "2015-08-16 12:30:13"},
            new Object[]{"yyyy-MM-dd HH:mm:ss.SSS", 1439721013072L, "2015-08-16 12:30:13.072"},
        };
    }

    @Test(dataProvider = "dateDataProvider")
    public void shouldFormatDate(String pattern, Long time, String expected)
    {
        MillisecondTimeFormat sut = new MillisecondTimeFormat(pattern);

        StringBuffer actual = new StringBuffer();

        sut.format(time, actual, null);

        assertThat(actual.toString()).isEqualTo(expected);
    }

    @DataProvider
    public Object[][] dateStringDataProvider()
    {
        return new Object[][]{
            new Object[]{"yyyy-MM-dd HH:mm:ss", "2015-08-16 12:30:13", 1439721013000L},
            new Object[]{"yyyy-MM-dd HH:mm:ss.SSS", "2015-08-16 12:30:13.072", 1439721013072L},
        };
    }

    @Test(dataProvider = "dateStringDataProvider")
    public void shouldParseDate(String pattern, String data, Long expected)
    {
        MillisecondTimeFormat sut = new MillisecondTimeFormat(pattern);

        Object actual = sut.parseObject(data, new ParsePosition(0));

        assertThat(actual).isInstanceOf(Long.class);
        assertThat((Long) actual).isEqualTo(expected);
    }
}
