/*
 * This code is part of the CPCC-NG and the JNavigator project.
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

/**
 * MercatorProjection
 */
public class MercatorProjection
{
    private int zoomLevel;
    private int xTile;
    private int yTile;
    private int xPixel;
    private int yPixel;
    

    /**
     * @param zoomLevel the zoom level
     * @param latitude the latitude
     * @param longitude the longitude
     */
    public MercatorProjection(int zoomLevel, double latitude, double longitude)
    {
        this.zoomLevel = zoomLevel;
        double latRad = Math.toRadians(latitude);
        long n = (long) Math.pow(2, zoomLevel);
        double xTileD = ((longitude + 180.0) / 360.0) * n;
        double yTileD = (1.0 - (Math.log(Math.tan(latRad) + 1.0 / Math.cos(latRad)) / Math.PI)) / 2.0 * n;
        xTile = (int) xTileD;
        yTile = (int) yTileD;
        xPixel = (int) (256.0 * (xTileD - xTile));
        yPixel = (int) (256.0 * (yTileD - yTile));
    }

    /**
     * @return the X-tile.
     */
    public int getxTile()
    {
        return xTile;
    }

    /**
     * @return the Y-tile.
     */
    public int getyTile()
    {
        return yTile;
    }

    /**
     * @return the X-pixel.
     */
    public int getxPixel()
    {
        return xPixel;
    }

    /**
     * @return the Y-pixel.
     */
    public int getyPixel()
    {
        return yPixel;
    }

    /**
     * @param other the other tile.
     * @return true if the other tile equals this tile.
     */
    public boolean equalsTile(MercatorProjection other)
    {
        return zoomLevel == other.zoomLevel && xTile == other.xTile && yTile == other.yTile;
    }
}
