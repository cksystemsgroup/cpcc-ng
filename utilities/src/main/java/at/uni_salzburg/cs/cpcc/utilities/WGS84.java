/*
 * This code is part of the JNavigator project.
 *
 * Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.utilities;

/**
 * This geodetic system implements the Department of Defense World Geodetic System 1984 (WGS84)
 * 
 * @see http://earth-info.nga.mil/GandG/publications/tr8350.2/tr8350_2.html NGA: DoD World Geodetic System 1984, Its
 *      Definition and Relationships with Local Geodetic Systems
 */
public class WGS84 implements GeodeticSystem
{
    private static final double EQUATORIAL_AXIS = 6378137;
    private static final double POLAR_AXIS = 6356752.3142;
    private static final double ANGULAR_ECCENTRICITY = Math.acos(POLAR_AXIS / EQUATORIAL_AXIS);
    private static final double FIRST_ECCENTRICITY = 8.1819190842622E-2;

    /**
     * {@inheritDoc}
     */
    public CartesianCoordinate polarToRectangularCoordinates(PolarCoordinate coordinates)
    {
        return polarToRectangularCoordinates(coordinates.getLatitude(), coordinates.getLongitude(),
            coordinates.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    public CartesianCoordinate polarToRectangularCoordinates(double latitude,
        double longitude, double altitude)
    {

        double u = Math.sin(Math.toRadians(latitude)) * FIRST_ECCENTRICITY;
        double n = EQUATORIAL_AXIS / Math.sqrt(1 - u * u);

        double x = (n + altitude) * Math.cos(Math.toRadians(latitude)) * Math.cos(Math.toRadians(longitude));
        double y = (n + altitude) * Math.cos(Math.toRadians(latitude)) * Math.sin(Math.toRadians(longitude));
        double v = POLAR_AXIS / EQUATORIAL_AXIS;
        double z = (v * v * n + altitude) * Math.sin(Math.toRadians(latitude));

        return new CartesianCoordinate(x, y, z);
    }

    /**
     * {@inheritDoc}
     */
    public PolarCoordinate rectangularToPolarCoordinates(CartesianCoordinate coordinates)
    {
        return rectangularToPolarCoordinates(coordinates.getX(), coordinates.getY(), coordinates.getZ());
    }

    /**
     * {@inheritDoc}
     */
    public PolarCoordinate rectangularToPolarCoordinates(double x, double y, double z)
    {

        double newLatitude = 90;
        double latitude = 0;
        double u, v, w, n = 0;
        double sin2AE = Math.sin(2 * ANGULAR_ECCENTRICITY);
        double sinAE = Math.sin(ANGULAR_ECCENTRICITY);

        while (Math.abs(latitude - newLatitude) > 1E-13)
        {
            latitude = newLatitude;

            u = Math.sin(latitude) * Math.sin(ANGULAR_ECCENTRICITY);
            n = EQUATORIAL_AXIS / Math.sqrt(1 - u * u);

            v = n * Math.sin(latitude);
            w = n * Math.cos(latitude);

            double numerator = EQUATORIAL_AXIS * EQUATORIAL_AXIS * z + v * v * v * sin2AE * sin2AE / 4;
            double denominator =
                EQUATORIAL_AXIS * EQUATORIAL_AXIS * Math.sqrt(x * x + y * y) - w * w * w * sinAE * sinAE;
            newLatitude = Math.atan(numerator / denominator);
        }

        double cosNLat = Math.cos(newLatitude);
        double sinNLat = Math.sin(newLatitude);

        double altitude = cosNLat * Math.sqrt(x * x + y * y) + sinNLat * (z + sinAE * sinAE * n * sinNLat) - n;

        double longitude = Math.asin(y / ((n + altitude) * cosNLat));

        return new PolarCoordinate(Math.toDegrees(newLatitude), Math.toDegrees(longitude), altitude);
    }

    /**
     * {@inheritDoc}
     */
    public PolarCoordinate walk(PolarCoordinate startPosition, double x, double y, double z)
    {

        double latitude = Math.toRadians(startPosition.getLatitude());
        double longitude = Math.toRadians(startPosition.getLongitude());
        double altitude = startPosition.getAltitude() + z;

        double n = EQUATORIAL_AXIS;

        // TODO This is only an approximation using a sphere with radius N
        double dLatitude = x / (n + startPosition.getAltitude());
        double dLongitude = y / ((n + startPosition.getAltitude()) * Math.cos(latitude));
        latitude -= dLatitude;
        longitude += dLongitude;

        return new PolarCoordinate(Math.toDegrees(latitude), Math.toDegrees(longitude), altitude);
    }
}
