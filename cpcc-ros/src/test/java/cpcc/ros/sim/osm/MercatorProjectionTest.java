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

package cpcc.ros.sim.osm;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.ros.sim.osm.MercatorProjection;

public class MercatorProjectionTest
{

    @DataProvider
    public Object[][] mercatorData()
    {
        return new Object[][]{
            new Object[]{19, 37.80789, -122.42697, 83846, 202587, 205, 125},
            new Object[]{18, 37.80789, -122.42697, 41923, 101293, 102, 190},
        };
    }

    @Test(dataProvider = "mercatorData")
    public void shouldConvertMercatorToTileCoordinates(int zoomLevel, double latitude, double longitude, int x, int y,
        int xPixel, int yPixel)
    {
        MercatorProjection mp = new MercatorProjection(zoomLevel, latitude, longitude);
        Assert.assertEquals(mp.getxTile(), x);
        Assert.assertEquals(mp.getyTile(), y);
        Assert.assertEquals(mp.getxPixel(), xPixel);
        Assert.assertEquals(mp.getyPixel(), yPixel);
        Assert.assertTrue(mp.equalsTile(mp));
    }

    @Test
    public void shouldRecognizeTheSameTileCoordinates()
    {
        MercatorProjection mp1 = new MercatorProjection(19, 37.80789, -122.42697);
        MercatorProjection mp2 = new MercatorProjection(19, 37.80788, -122.42696);

        Assert.assertTrue(mp1.equalsTile(mp2));
        Assert.assertFalse(mp1.equals(mp2));
    }

    @DataProvider
    public Object[][] nonEqualMercatorProjectionDataProvider()
    {
        return new Object[][]{
            new Object[]{
                new MercatorProjection(19, 37.80789, -122.42697),
                new MercatorProjection(19, 37.80789, -102.42697)
            },
            new Object[]{
                new MercatorProjection(19, 37.80789, -122.42697),
                new MercatorProjection(19, 36.80789, -122.42697)
            },
            new Object[]{
                new MercatorProjection(19, 37.80788, -122.42696),
                new MercatorProjection(18, 37.80789, -122.42697)
            },
        };
    };

    @Test(dataProvider = "nonEqualMercatorProjectionDataProvider")
    public void shouldRecognizeDifferentTileCoordinates(MercatorProjection a, MercatorProjection b)
    {
        Assert.assertFalse(a.equalsTile(b));
    }
}
