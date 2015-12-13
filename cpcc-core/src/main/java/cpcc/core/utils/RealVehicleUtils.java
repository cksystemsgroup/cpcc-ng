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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.base.PolygonZone;
import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;

/**
 * Real Vehicle Utils implementation.
 */
public final class RealVehicleUtils
{
    private RealVehicleUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param areaOfOperation the area of operation as a {@code String}.
     * @return the list of {@code LngLatAlt} depot positions.
     * @throws IOException in case of errors.
     */
    public static List<PolarCoordinate> getDepotPositions(String areaOfOperation) throws IOException
    {
        if (StringUtils.isBlank(areaOfOperation))
        {
            return Collections.emptyList();
        }

        List<PolarCoordinate> list = new ArrayList<>();
        FeatureCollection fc =
            new ObjectMapper().readValue(areaOfOperation.replace("\\n", "\n"), FeatureCollection.class);

        for (Feature feature : fc.getFeatures())
        {
            if (!"depot".equals(feature.getProperty("type")))
            {
                continue;
            }

            GeoJsonObject geom = feature.getGeometry();
            LngLatAlt coordinates = ((Point) geom).getCoordinates();
            list.add(new PolarCoordinate(coordinates.getLatitude(), coordinates.getLongitude(), 0.0));
        }

        return list;
    }

    /**
     * @param areaOfOperation the area of operation as a {@code String}.
     * @return the list of {@code PolygonZone} instances.
     * @throws IOException in case of errors.
     */
    public static List<PolygonZone> getPolygons(String areaOfOperation) throws IOException
    {
        if (StringUtils.isBlank(areaOfOperation))
        {
            return Collections.emptyList();
        }

        List<PolygonZone> list = new ArrayList<PolygonZone>();
        FeatureCollection fc =
            new ObjectMapper().readValue(areaOfOperation.replace("\\n", "\n"), FeatureCollection.class);

        for (Feature feature : fc.getFeatures())
        {
            GeoJsonObject geom = feature.getGeometry();
            if (geom instanceof Polygon)
            {
                List<LngLatAlt> coordinates = ((Polygon) geom).getCoordinates().get(0);
                list.add(new PolygonZone(coordinates));
            }
        }

        return list;
    }

    /**
     * @param areaOfOperation the area of operation as a {@code String}.
     * @param position the position in question.
     * @return true if the position is inside the area of operation.
     */
    public static boolean isInsideAreaOfOperation(String areaOfOperation, PolarCoordinate position)
    {
        if (StringUtils.isBlank(areaOfOperation))
        {
            return false;
        }

        try
        {
            for (PolygonZone zone : getPolygons(areaOfOperation))
            {
                if (zone.isInside(position))
                {
                    return true;
                }
            }

            return false;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * @param rvList the list of real vehicles.
     * @return the bounding box of all areas of operation.
     */
    public static double[] findBoundingBox(List<RealVehicle> rvList)
    {
        double[] bbox = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN};

        for (RealVehicle rv : rvList)
        {
            if (StringUtils.isBlank(rv.getAreaOfOperation()))
            {
                continue;
            }

            try
            {
                FeatureCollection collection =
                    new ObjectMapper().readValue(rv.getAreaOfOperation(), FeatureCollection.class);

                for (Feature feature : collection)
                {
                    double[] b = GeoJsonUtils.findBoundingBox(feature.getGeometry());
                    GeoJsonUtils.mergeBoundingBoxes(bbox, b);
                }

            }
            catch (IOException e)
            {
                continue;
            }
        }

        return bbox;
    }
}
