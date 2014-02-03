/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.commons.pages;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import sensor_msgs.NavSatFix;
import at.uni_salzburg.cs.cpcc.core.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.core.entities.Parameter;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.core.entities.SensorType;
import at.uni_salzburg.cs.cpcc.core.services.CoreGeoJsonConverter;
import at.uni_salzburg.cs.cpcc.core.services.GeoJsonStreamResponse;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sensors.AbstractGpsSensorAdapter;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.services.VvGeoJsonConverter;
import at.uni_salzburg.cs.cpcc.vvrte.services.VvRteRepository;

/**
 * Status
 */
public class Status
{
    @Inject
    private QueryManager qm;

    @Inject
    private VvRteRepository vvRepo;

    @Inject
    private RosNodeService rns;

    @Inject
    private CoreGeoJsonConverter pjc;

    @Inject
    private VvGeoJsonConverter vjc;

    /**
     * @return the current status as a GeoJSON object.
     * @throws IOException thrown in case of errors.
     */
    public StreamResponse onActivate() throws IOException
    {
        return onActivate(null);
    }

    /**
     * @param what the data subset to be transferred or null for all data.
     * @return the current status as a GeoJSON object.
     * @throws IOException thrown in case of errors.
     */
    public StreamResponse onActivate(String what) throws IOException
    {
        //        JSONObject response = new JSONObject();
        //
        //        RealVehicle realVehicle = findRealVehicle();
        //
        //        if (realVehicle != null)
        //        {
        //            JSONObject jsonRealVehicle = pjc.toJson(false, realVehicle);
        //
        //            PolarCoordinate position = findRealVehiclePosition();
        //            if (position != null)
        //            {
        //                jsonRealVehicle.put("position", pjc.toJson(position));
        //            }
        //
        //            response.put("rv", jsonRealVehicle);
        //        }
        //
        //        List<VirtualVehicle> vvs = vvRepo.findAllVehicles();
        //        if (vvs != null && vvs.size() > 0)
        //        {
        //            response.put("vvs", vjc.toJsonArray(vvs.toArray(new VirtualVehicle[0])));
        //        }
        //
        //        return new JsonStreamResponse(response);

        FeatureCollection fc = new FeatureCollection();

        if (what == null || "rvs".equals(what))
        {
            fc.add(pjc.toFeature(findRealVehicle()));
        }

        PolarCoordinate position = findRealVehiclePosition();

        if (position != null && (what == null || "pos".equals(what)))
        {
            Point point = pjc.toPoint(position);
            Feature pointFeature = new Feature();
            pointFeature.setGeometry(point);
            pointFeature.setProperty("type", "rvPosition");
            fc.add(pointFeature);
        }

        if (what == null || "vvs".equals(what))
        {
            List<VirtualVehicle> vvs = vvRepo.findAllVehicles();
            if (vvs != null && vvs.size() > 0)
            {
                fc.addAll(vjc.toFeatureList(vvs));
            }
        }

        return new GeoJsonStreamResponse(fc);
    }

    /**
     * @return the current real vehicle.
     */
    private RealVehicle findRealVehicle()
    {
        Parameter rvn = qm.findParameterByName(Parameter.REAL_VEHICLE_NAME, "");
        return qm.findRealVehicleByName(rvn.getValue());
    }

    /**
     * @return the real vehicle's current position, or null if unknown.
     */
    private PolarCoordinate findRealVehiclePosition()
    {
        for (MappingAttributes attr : qm.findAllMappingAttributes())
        {
            if (!attr.getConnectedToAutopilot())
            {
                continue;
            }

            SensorDefinition sd = attr.getSensorDefinition();
            if (sd == null || sd.getType() != SensorType.GPS)
            {
                continue;
            }

            AbstractRosAdapter adapter = rns.findAdapterNodeBySensorDefinitionId(sd.getId());
            if (adapter != null && adapter instanceof AbstractGpsSensorAdapter)
            {
                NavSatFix pos = ((AbstractGpsSensorAdapter) adapter).getPosition();
                if (pos != null)
                {
                    return new PolarCoordinate(pos.getLatitude(), pos.getLongitude(), pos.getAltitude());
                }
            }
        }

        return null;
    }
}
