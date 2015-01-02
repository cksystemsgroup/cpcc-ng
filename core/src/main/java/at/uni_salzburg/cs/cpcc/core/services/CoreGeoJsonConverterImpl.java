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
import java.util.Collection;

import org.apache.commons.lang.StringUtils;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.geojson.Point;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * CoreGeoJsonConverterImpl
 */
public class CoreGeoJsonConverterImpl implements CoreGeoJsonConverter
{
    private Logger logger;

    /**
     * @param logger the application logger.
     */
    public CoreGeoJsonConverterImpl(Logger logger)
    {
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Feature toFeature(RealVehicle rv) throws IOException
    {
        Feature feature = new Feature();
        feature.setId(Integer.toString(rv.getId()));
        feature.setProperty("type", "rv");
        feature.setProperty("rvtype", rv.getType().toString());
        feature.setProperty("name", rv.getName());

        if (!StringUtils.isEmpty(rv.getAreaOfOperation()))
        {
            try
            {
                GeoJsonObject aoo = new ObjectMapper().readValue(rv.getAreaOfOperation(), GeoJsonObject.class);
                feature.setGeometry(aoo);
            }
            catch (Exception e)
            {
                logger.error("Can not parse area of operation of real vehicle " + rv.getName()
                    + " (" + rv.getId() + ")");
            }
        }

        return feature;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureCollection toFeatureCollection(Collection<RealVehicle> rvList)
        throws JsonParseException, JsonMappingException, IOException
    {
        FeatureCollection fc = new FeatureCollection();

        for (RealVehicle rv : rvList)
        {
            fc.add(toFeature(rv));
        }

        return fc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Point toPoint(PolarCoordinate position)
    {
        return new Point(position.getLongitude(), position.getLatitude(), position.getAltitude());
    }
}
