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

import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.CoreGeoJsonConverter;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.core.utils.PolarCoordinate;
import cpcc.vvrte.task.Task;

/**
 * Position contributor implementation.
 */
public class PositionContributor implements StateContributor
{
    private RealVehicleRepository rvRepo;
    private TimeService timeService;
    private CoreGeoJsonConverter jsonConv;
    private long connectionTimeout;

    /**
     * @param timeService the time service.
     * @param rvRepo the real vehicle repository.
     * @param jsonConv the core GeoJSON converter.
     */
    public PositionContributor(TimeService timeService, RealVehicleRepository rvRepo, CoreGeoJsonConverter jsonConv)
    {
        this.rvRepo = rvRepo;
        this.timeService = timeService;
        this.jsonConv = jsonConv;
        this.connectionTimeout = 10000L;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void contribute(FeatureCollection featureCollection, PolarCoordinate rvPosition, List<Task> taskList)
    {
        if (rvPosition == null)
        {
            return;
        }

        Point point = jsonConv.toPoint(rvPosition);
        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "rvPosition");
        pointFeature.setProperty("rvHeading", 0);
        pointFeature.setProperty("rvPosition", point);

        RealVehicle rv = rvRepo.findOwnRealVehicle();

        if (rv != null)
        {
            pointFeature.setProperty("rvType", rv.getType().name());
            pointFeature.setProperty("rvId", rv.getId());
            pointFeature.setProperty("rvName", rv.getName());
            pointFeature.setProperty("rvState", getRvState(taskList, rv));
        }
        else
        {
            pointFeature.setProperty("rvType", RealVehicleType.UNKNOWN.name());
            pointFeature.setProperty("rvId", -1);
            pointFeature.setProperty("rvName", "unknown");
            pointFeature.setProperty("rvState", "none");
        }

        featureCollection.add(pointFeature);
    }

    /**
     * @param taskList the currently scheduled tasks.
     * @param realVehicle the host Real Vehicle.
     * @return the Real Vehicle state.
     */
    private String getRvState(List<Task> taskList, RealVehicle realVehicle)
    {
        RealVehicleState state = rvRepo.findRealVehicleStateById(realVehicle.getId());

        String stateString = "none";

        if (state != null && timeService.currentTimeMillis() - state.getLastUpdate().getTime() < connectionTimeout)
        {
            stateString = taskList.isEmpty() ? "idle" : "busy";
        }

        return stateString;
    }
}
