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

package at.uni_salzburg.cs.cpcc.gs.pages;

import java.io.IOException;

import org.apache.tapestry5.StreamResponse;
import org.geojson.FeatureCollection;

import at.uni_salzburg.cs.cpcc.core.utils.GeoJsonStreamResponse;

/**
 * JsonState
 */
public class JsonState
{
    //    @Inject
    //    private RealVehicleStateService rvs;

    //    @Inject
    //    private CoreGeoJsonConverter conf;

    /**
     * @return the GeoJSON stream response.
     * @throws IOException thrown in case of errors.
     */
    public Object onActivate() throws IOException
    {
        return onActivate(null);
    }

    /**
     * @param what the subset of the MSE to be emitted.
     * @return the GeoJSON stream response.
     * @throws IOException thrown in case of errors.
     */
    public StreamResponse onActivate(final String what) throws IOException
    {
        // TODO
        //if ("rvZones".equals(what))
        //{
        //    return new GeoJsonStreamResponse(conf.toFeatureCollection(rvs.getRealVehicles()));
        //}
        //
        //if ("rvPositions".equals(what))
        //{
        //    FeatureCollection rvPositions = new FeatureCollection();
        //    for (RealVehicleState status : rvs.getRealVehicleStatus())
        //    {
        //        byte[] s = status.getStatus();
        //        if (s.length == 0)
        //        {
        //            continue;
        //        }
        //        
        //        FeatureCollection rvFeatures = new ObjectMapper().readValue(s, 0, s.length, FeatureCollection.class);
        //
        //        for (Feature feature : rvFeatures.getFeatures())
        //        {
        //            String type = feature.getProperty("type");
        //            if (type != null && "rvPosition".equals(type))
        //            {
        //                feature.setProperty("rvId", status.getRealVehicle().getId());
        //                feature.setProperty("rvName", status.getRealVehicle().getName());
        //                rvPositions.add(feature);
        //            }
        //        }
        //    }
        //    return new GeoJsonStreamResponse(rvPositions);
        //}

        FeatureCollection emptyCollection = new FeatureCollection();
        return new GeoJsonStreamResponse(emptyCollection);
    }
}
