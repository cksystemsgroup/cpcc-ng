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

package cpcc.core.services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.utils.GeoJsonUtils;

/**
 * CoreGeoJsonConverterImpl
 */
public class CoreGeoJsonConverterImpl implements CoreGeoJsonConverter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public Feature toFeature(RealVehicle rv) throws IOException
    {
        Feature feature = new Feature();
        feature.setId(Integer.toString(rv.getId()));
        feature.setProperty("type", "rv");
        feature.setProperty("rvtype", rv.getType().name());
        feature.setProperty("name", rv.getName());

        return feature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point toPoint(PolarCoordinate position)
    {
        return new Point(position.getLongitude(), position.getLatitude(), position.getAltitude());
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("serial")
    @Override
    public Map<String, Double> toPosition(final PolarCoordinate position)
    {
        return new HashMap<String, Double>()
        {
            {
                put("lat", position.getLatitude());
                put("lon", position.getLongitude());
                put("alt", position.getAltitude());
            }
        };
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Point> findDepotPositions(RealVehicle realVehicle)
    {
        if (StringUtils.isBlank(realVehicle.getAreaOfOperation()))
        {
            return Collections.emptyList();
        }

        FeatureCollection collection;
        try
        {
            collection = new ObjectMapper().readValue(realVehicle.getAreaOfOperation(), FeatureCollection.class);
        }
        catch (IOException e)
        {
            return Collections.emptyList();
        }

        List<Point> depotList = new ArrayList<Point>();

        for (Feature feature : collection.getFeatures())
        {
            if ("depot".equals(feature.getProperty("type")) && feature.getGeometry() instanceof Point)
            {
                depotList.add((Point) feature.getGeometry());
            }
        }

        return depotList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double[] findBoundingBox(List<RealVehicle> rvList)
    {
        double[] bbox = new double[]{Double.NaN, Double.NaN, Double.NaN, Double.NaN};

        for (RealVehicle rv : rvList)
        {
            if (rv.getAreaOfOperation() == null)
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
