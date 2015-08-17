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

package at.uni_salzburg.cs.cpcc.core.services;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.Point;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

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
}
