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

package cpcc.core.services.jobs;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.Date;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.services.jobs.TimeServiceImpl;

public class TimeServiceTest
{
    private TimeServiceImpl sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new TimeServiceImpl();
    }

    @DataProvider
    public Object[][] sequenceDataProvider()
    {
        return new Object[][]{
            new Object[]{0},
            new Object[]{1},
            new Object[]{2},
            new Object[]{3},
            new Object[]{4},
            new Object[]{5},
            new Object[]{6},
            new Object[]{7},
            new Object[]{8},
            new Object[]{9},
        };
    }

    @Test(dataProvider = "sequenceDataProvider")
    public void shouldCreateDateObjects(int number)
    {
        Date expected = new Date();
        Date actual = sut.newDate();

        assertThat(actual).isNotNull();
        assertThat(actual.getTime() - expected.getTime()).describedAs("time accuracy").isLessThan(10L);
    }

    @Test(dataProvider = "sequenceDataProvider")
    public void shouldReturnTheCurrentTime(int number)
    {
        long expected = System.currentTimeMillis();
        long actual = sut.currentTimeMillis();

        assertThat(actual - expected).describedAs("time accuracy").isLessThan(10L);
    }
}
