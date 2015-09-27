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

package cpcc.rv.base.services;

import java.io.IOException;
import java.util.List;
import java.util.regex.Pattern;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import cpcc.core.entities.MappingAttributes;
import cpcc.core.entities.Parameter;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.services.CoreGeoJsonConverter;
import cpcc.core.services.QueryManager;
import cpcc.core.utils.PolarCoordinate;
import cpcc.ros.base.AbstractRosAdapter;
import cpcc.ros.sensors.AbstractGpsSensorAdapter;
import cpcc.ros.services.RosNodeService;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.services.VvGeoJsonConverter;
import cpcc.vvrte.services.VvRteRepository;
import sensor_msgs.NavSatFix;

/**
 * State service implementation.
 */
public class StateServiceImpl implements StateService
{
    // private static final Pattern RVS = Pattern.compile("^(rvs|rvs-.*|.*-rvs)$");
    private static final Pattern POS = Pattern.compile("^(pos|pos-.*|.*-pos)$");
    private static final Pattern VVS = Pattern.compile("^(vvs|vvs-.*|.*-vvs)$");

    private QueryManager qm;
    private RosNodeService rns;
    private VvRteRepository vvRepo;
    private CoreGeoJsonConverter pjc;
    private VvGeoJsonConverter vjc;

    /**
     * @param qm the query manager.
     * @param rns the ROS node service.
     * @param vvRepo the virtual vehicle RTE repository.
     * @param pjc the core geo JSON converter.
     * @param vjc the virtual vehicle JSON converter.
     */
    public StateServiceImpl(QueryManager qm, RosNodeService rns, VvRteRepository vvRepo, CoreGeoJsonConverter pjc,
        VvGeoJsonConverter vjc)
    {
        this.qm = qm;
        this.rns = rns;
        this.vvRepo = vvRepo;
        this.pjc = pjc;
        this.vjc = vjc;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FeatureCollection getState(String what) throws IOException
    {
        FeatureCollection fc = new FeatureCollection();
        PolarCoordinate position = findRealVehiclePosition();

        if (position != null && isNullOrMatches(what, POS))
        {
            Point point = pjc.toPoint(position);
            Feature pointFeature = new Feature();
            pointFeature.setGeometry(point);
            pointFeature.setProperty("type", "rvPosition");
            pointFeature.setProperty("rvHeading", 0);
            pointFeature.setProperty("rvPosition", point);
            
            RealVehicle rv = findRealVehicle();
            if (rv != null)
            {
                pointFeature.setProperty("rvType", rv.getType().name());
                pointFeature.setProperty("rvId", rv.getId());
                pointFeature.setProperty("rvName", rv.getName());

                RealVehicleState state = qm.findRealVehicleStateById(rv.getId());
                if (state != null)
                {
                    // TODO fixme
                    pointFeature.setProperty("rvState", "none"); // "idle", "busy"
                    // state.getState();
                }
            }
            else
            {
                pointFeature.setProperty("rvType", RealVehicleType.UNKNOWN.name());
                pointFeature.setProperty("rvId", -1);
                pointFeature.setProperty("rvName", "unknown");
                pointFeature.setProperty("rvState", "none");
            }

            fc.add(pointFeature);
        }

        if (isNullOrMatches(what, VVS))
        {
            List<VirtualVehicle> vvs = vvRepo.findAllVehicles();
            if (vvs != null && vvs.size() > 0)
            {
                fc.addAll(vjc.toFeatureList(vvs));
            }
        }

        return fc;
    }

    /**
     * @param a the value to be tested.
     * @param b another value.
     * @return true if a is null or a equals b.
     */
    private static boolean isNullOrMatches(String a, Pattern pattern)
    {
        return a == null || pattern.matcher(a).matches();
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
