/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.gs.pages;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.geojson.FeatureCollection;

import at.uni_salzburg.cs.cpcc.core.services.CoreGeoJsonConverter;
import at.uni_salzburg.cs.cpcc.core.services.GeoJsonStreamResponse;
import at.uni_salzburg.cs.cpcc.gs.services.RealVehicleStateService;
import at.uni_salzburg.cs.cpcc.gs.services.RealVehicleStatus;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * JsonState
 */
public class JsonState
{
    @Inject
    private RealVehicleStateService rvs;

    @Inject
    private CoreGeoJsonConverter conf;

    /**
     * @return the GeoJSON stream response.
     * @throws IOException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws JsonParseException thrown in case of errors.
     */
    public Object onActivate() throws JsonParseException, JsonMappingException, IOException
    {
        return onActivate(null);
    }

    /**
     * @param what the subset of the MSE to be emitted.
     * @return the GeoJSON stream response.
     * @throws IOException thrown in case of errors.
     * @throws JsonMappingException thrown in case of errors.
     * @throws JsonParseException thrown in case of errors.
     */
    public StreamResponse onActivate(final String what) throws JsonParseException, JsonMappingException, IOException
    {
        if ("rvZones".equals(what))
        {
            return new GeoJsonStreamResponse(conf.toFeatureCollection(rvs.getRealVehicles()));
        }

        for (RealVehicleStatus status : rvs.getRealVehicleStatus())
        {
            status.getStatus();
        }

        FeatureCollection jsonObject = new FeatureCollection();
        return new GeoJsonStreamResponse(jsonObject );
    }
}
