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
package at.uni_salzburg.cs.cpcc.rv.services.task;

import static org.fest.assertions.api.Assertions.assertThat;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;

/**
 * TaskTest
 */
public class TaskTest
{
    @DataProvider
    public Object[][] positionDataProvider()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(37.1234, -122.0898, 0.0)},
            new Object[]{new PolarCoordinate(37.1234,  122.0898, 100.0)},
            new Object[]{new PolarCoordinate(-37.1234, -122.0898, -100.0)},
            new Object[]{new PolarCoordinate(-37.1234,  122.0898, 1.0)},
        };
    };
    
    @Test(dataProvider = "positionDataProvider")
    public void shouldStorePosition(PolarCoordinate position)
    {
        Task task = new Task();
        task.setPosition(position);
        assertThat(task.getPosition()).isEqualTo(position);
    }
    
    @DataProvider
    public Object[][] timeDataProvider()
    {
        return new Object[][]{
            new Object[]{-1L},
            new Object[]{1381003668231L},
            new Object[]{1000000000000L},
            new Object[]{2222222222222L},
            new Object[]{1213141516171L},
        };
    };
    
    @Test(dataProvider = "timeDataProvider")
    public void shouldStorePosition(long time)
    {
        Task task = new Task();
        task.setCreationTime(time);
        assertThat(task.getCreationTime()).isEqualTo(time);
    }
}
