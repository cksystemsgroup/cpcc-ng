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
package at.uni_salzburg.cs.cpcc.ros.sim.osm;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class MercatorProjectionTest
{
    @DataProvider
    public Object[][] mercatorData()
    {
        return new Object[][]{
            new Object[]{19, 37.80789, -122.42697, 83846, 202587},
            new Object[]{18, 37.80789, -122.42697, 41923, 101293},
        };
    }

    @Test(dataProvider = "mercatorData")
    public void shouldConvertMercatorToTileCoordinates(int zoomLevel, double latitude, double longitude, int x, int y)
    {
        MercatorProjection mp = new MercatorProjection(zoomLevel, latitude, longitude);
        Assert.assertEquals(mp.getxTile(), x);
        Assert.assertEquals(mp.getyTile(), y);
    }
}
