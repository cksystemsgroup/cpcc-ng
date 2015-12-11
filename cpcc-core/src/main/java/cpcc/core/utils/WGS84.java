// This code is part of the JNavigator project.
//
// Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.utils;

import cpcc.core.entities.PolarCoordinate;

/**
 * This geodetic system implements the Department of Defense World Geodetic System 1984 (WGS84)
 * 
 * @see http://earth-info.nga.mil/GandG/publications/tr8350.2/tr8350_2.html NGA: DoD World Geodetic System 1984, Its
 *      Definition and Relationships with Local Geodetic Systems
 * @see http://www.oc.nps.edu/oc2902w/coord/llhxyz.htm
 * @see http://en.wikipedia.org/wiki/Geodetic_system
 * @see Book "Integrierte Navigationssysteme" by Jan Wendel, Listings 3.22 - 3.28.
 * @see Book "Global Positioning Systems, Inertial Navigation, and Integration" by Grewal, Weill, and Andrews.
 */
public class WGS84 implements GeodeticSystem
{
    private static final double EQUATORIAL_AXIS = 6378137.0;
    private static final double POLAR_AXIS = 6356752.3142;
    private static final double FIRST_ECCENTRICITY = 8.1819190842622E-2;
    private static final double FIRST_ECCENTRICITY_SQ = FIRST_ECCENTRICITY * FIRST_ECCENTRICITY;
    private static final double ES2 = EQUATORIAL_AXIS * EQUATORIAL_AXIS / (POLAR_AXIS * POLAR_AXIS) - 1;

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
        double n = EQUATORIAL_AXIS / Math.sqrt(1.0 - u * u);

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
     * {@inheritDoc} Converts an ECEF coordinate to a WGS84 coordinate. Algorithm from the book
     * "Integrierte Navigationssysteme" by Jan Wendel, Listings 3.22 - 3.28. Note: this method uses a simplified formula
     * which is only valid for the Low Earth Orbit (LEO) which goes up to approximately 2000km above ground.
     * 
     * @param x value in meters
     * @param y value in meters
     * @param z value in meters
     * @return WGS84 coordinate as double-array with value[0]: latitude in degrees value[1]: longitude in degrees
     *         value[2]: altitude in metres over ellipsoid
     */
    public PolarCoordinate rectangularToPolarCoordinates(double x, double y, double z)
    {
        if (x == 0 && y == 0)
        {
            if (z > 0)
            {
                return new PolarCoordinate(90.0, 0.0, z - POLAR_AXIS);
            }
            else
            {
                return new PolarCoordinate(-90.0, 0.0, z + POLAR_AXIS);
            }
        }
        double p = Math.sqrt((x * x) + (y * y));
        double theta = Math.atan((z * EQUATORIAL_AXIS) / (p * POLAR_AXIS));
        double sinTheta = Math.sin(theta);
        double cosTheta = Math.cos(theta);
        double lat = Math.atan((z + (ES2 * POLAR_AXIS * sinTheta * sinTheta * sinTheta))
            / (p - (FIRST_ECCENTRICITY_SQ * EQUATORIAL_AXIS * cosTheta * cosTheta * cosTheta)));
        double lon = Math.atan2(y, x);
        double n = EQUATORIAL_AXIS / Math.sqrt(1 - FIRST_ECCENTRICITY_SQ * Math.sin(lat) * Math.sin(lat));
        double alt = (p / Math.cos(lat)) - n;
        return new PolarCoordinate(Math.toDegrees(lat), Math.toDegrees(lon), alt);
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

    /**
     * {@inheritDoc}
     */
    @Override
    public double calculateDistance(PolarCoordinate a, PolarCoordinate b)
    {
        return polarToRectangularCoordinates(a).subtract(polarToRectangularCoordinates(b)).norm();
    }
}
