// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.WGS84;

/**
 * ExampleDemo implementation. http://wiki.openstreetmap.org/wiki/Slippy_map_tilenames#Zoom_levels
 */
class ExampleDemo
{
    public static void main(String[] args)
    {
        new ExampleDemo().shouldShowExampleCalculation();
    }

    void shouldShowExampleCalculation()
    {
        double cameraWidth = 320.0;
        double cameraHeight = 240.0;
        int zoomLevel = 19;
        // PolarCoordinate pos = new PolarCoordinate(47.821896, 13.041135, 50);
        // PolarCoordinate pos = new PolarCoordinate(47.821896, 13.040466, 50);
        // PolarCoordinate pos = new PolarCoordinate(47.821922,13.040812,50);
        // PolarCoordinate pos = new PolarCoordinate(0.0, 0.0, 50);
        PolarCoordinate pos = new PolarCoordinate(47.822, 13.040, 50);

        GeodeticSystem gs = new WGS84();
        double alpha = 1.0;
        double deltaWE = Math.tan(alpha / 2.0) * pos.getAltitude();
        double deltaNS = deltaWE * cameraHeight / cameraWidth;

        PolarCoordinate posNW = gs.walk(pos, -deltaNS, -deltaWE, 0.0);
        PolarCoordinate posSE = gs.walk(pos, deltaNS, deltaWE, 0.0);

        System.out.printf("alpha=%.3frad, alt=%.1f, deltaWE=%.1fm, deltaNS=%.1fm%n",
            alpha, pos.getAltitude(), deltaWE, deltaNS);

        System.out.println("posNW: " + polarToString(posNW));
        System.out.println("pos:   " + pos);
        System.out.println("posSE: " + polarToString(posSE));

        MercatorProjection mpNW = new MercatorProjection(zoomLevel, posNW.getLatitude(), posNW.getLongitude());
        MercatorProjection mpPos = new MercatorProjection(zoomLevel, pos.getLatitude(), pos.getLongitude());
        MercatorProjection mpSE = new MercatorProjection(zoomLevel, posSE.getLatitude(), posSE.getLongitude());

        InverseMercator impNW = new InverseMercator(mpNW.getxTile(), mpNW.getyTile(), zoomLevel);
        InverseMercator impSE = new InverseMercator(mpSE.getxTile() + 1, mpSE.getyTile() + 1, zoomLevel);

        System.out.println("mpNW:  " + mpToString(mpNW, zoomLevel));
        System.out.println("mpPos: " + mpToString(mpPos, zoomLevel));
        System.out.println("mpSE:  " + mpToString(mpSE, zoomLevel));

        System.out.printf("NW:    (%.6f\u00B0, %.6f\u00B0)%n", impNW.getLat(), impNW.getLon());
        System.out.printf("SE:    (%.6f\u00B0, %.6f\u00B0)%n", impSE.getLat(), impSE.getLon());
    }

    private static String polarToString(PolarCoordinate pos)
    {
        return String.format("(%.6f\u00B0, %.6f\u00B0, %.1fm)", pos.getLatitude(), pos.getLongitude(),
            pos.getAltitude());
    }

    private static String mpToString(MercatorProjection mp, int zoomLevel)
    {
        return String.format("xTile=%6d, xPixel=%3d, yTile=%6d, yPixel=%3d, zoom=%d",
            mp.getxTile(), mp.getxPixel(), mp.getyTile(), mp.getyPixel(), zoomLevel);
    }

    static class InverseMercator
    {
        private double lonDeg;
        private double latDeg;

        public InverseMercator(double xtile, double ytile, int zoom)
        {
            double n = Math.pow(2.0, zoom);
            lonDeg = xtile / n * 360.0 - 180.0;
            double latRad = Math.atan(Math.sinh(Math.PI * (1 - 2 * ytile / n)));
            latDeg = Math.toDegrees(latRad);
        }

        public double getLat()
        {
            return latDeg;
        }

        public double getLon()
        {
            return lonDeg;
        }
    }
}
