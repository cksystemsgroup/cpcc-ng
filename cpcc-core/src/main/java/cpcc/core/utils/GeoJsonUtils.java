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

package cpcc.core.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.geojson.Feature;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;

/**
 * GeoJSON Utilities.
 */
public final class GeoJsonUtils
{
    private GeoJsonUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param obj the GeoJSON object.
     * @return the bounding box of the object.
     */
    public static double[] findBoundingBox(GeoJsonObject obj)
    {
        if (obj instanceof Point)
        {
            LngLatAlt c = ((Point) obj).getCoordinates();
            return new double[]{c.getLongitude(), c.getLatitude(), c.getLongitude(), c.getLatitude()};
        }

        if (obj instanceof Polygon)
        {
            List<List<LngLatAlt>> geometryList = ((Polygon) obj).getCoordinates();
            double[] boundingBox = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN};
            for (List<LngLatAlt> positionList : geometryList)
            {
                for (LngLatAlt position : positionList)
                {
                    mergeBoundingBox(boundingBox, position);
                }
            }
            return boundingBox;
        }

        return ArrayUtils.EMPTY_DOUBLE_ARRAY;
    }

    /**
     * Extends an existing bounding box by checking if or if not a position is within that box. If the position is not
     * within the box, the box will be enlarged to include the position.
     * 
     * @param boundingBox an existing bounding box.
     * @param position a position not yet considered in the bounding box.
     */
    public static void mergeBoundingBox(double[] boundingBox, LngLatAlt position)
    {
        double lon = position.getLongitude();
        double lat = position.getLatitude();

        if (Double.isNaN(boundingBox[0]) || lon < boundingBox[0])
        {
            boundingBox[0] = lon;
        }

        if (Double.isNaN(boundingBox[1]) || lat < boundingBox[1])
        {
            boundingBox[1] = lat;
        }

        if (Double.isNaN(boundingBox[2]) || lon > boundingBox[2])
        {
            boundingBox[2] = lon;
        }

        if (Double.isNaN(boundingBox[3]) || lat > boundingBox[3])
        {
            boundingBox[3] = lat;
        }
    }

    /**
     * Extends an existing bounding box by checking if or if not another bounding box is within the existing bounding
     * box. If the other bounding box is not entirely within the existing bounding box, the existing box will be
     * enlarged accordingly.
     * 
     * @param boundingBox an existing bounding box.
     * @param other another bounding box.
     */
    public static void mergeBoundingBoxes(double[] boundingBox, double[] other)
    {
        if (Double.isNaN(boundingBox[0]) || other[0] < boundingBox[0])
        {
            boundingBox[0] = other[0];
        }

        if (Double.isNaN(boundingBox[1]) || other[1] < boundingBox[1])
        {
            boundingBox[1] = other[1];
        }

        if (Double.isNaN(boundingBox[2]) || other[2] > boundingBox[2])
        {
            boundingBox[2] = other[2];
        }

        if (Double.isNaN(boundingBox[3]) || other[3] > boundingBox[3])
        {
            boundingBox[3] = other[3];
        }
    }

    /**
     * @param rv the real vehicle to be converted.
     * @return the real vehicle as GeoJSON Feature object.
     */
    public static Feature toFeature(RealVehicle rv)
    {
        Feature feature = new Feature();
        feature.setId(Integer.toString(rv.getId()));
        feature.setProperty("type", "rv");
        feature.setProperty("rvtype", rv.getType().name());
        feature.setProperty("name", rv.getName());

        return feature;
    }

    /**
     * @param position the position.
     * @return the position as a GeoJSON Point object.
     */
    public static Point toPoint(PolarCoordinate position)
    {
        return new Point(position.getLongitude(), position.getLatitude(), position.getAltitude());
    }

    /**
     * @param position the position.
     * @return the position as a Map object.
     */
    public static Map<String, Double> toPosition(final PolarCoordinate position)
    {
        return Stream
            .of(Pair.of("lat", position.getLatitude()),
                Pair.of("lon", position.getLongitude()),
                Pair.of("alt", position.getAltitude()))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight));
    }
}
