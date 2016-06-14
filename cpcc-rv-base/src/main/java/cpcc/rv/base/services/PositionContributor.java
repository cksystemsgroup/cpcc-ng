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

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.core.utils.GeoJsonUtils;
import cpcc.vvrte.entities.Task;

/**
 * Position contributor implementation.
 */
public class PositionContributor implements StateContributor
{
    private RealVehicleRepository rvRepo;
    private TimeService timeService;

    /**
     * @param timeService the time service.
     * @param rvRepo the real vehicle repository.
     */
    public PositionContributor(TimeService timeService, RealVehicleRepository rvRepo)
    {
        this.rvRepo = rvRepo;
        this.timeService = timeService;
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

        Point point = GeoJsonUtils.toPoint(rvPosition);
        Feature pointFeature = new Feature();
        pointFeature.setGeometry(point);
        pointFeature.setProperty("type", "rvPosition");
        pointFeature.setProperty("rvHeading", 0);
        pointFeature.setProperty("rvPosition", point);
        pointFeature.setProperty("rvTime", timeService.currentTimeMillis());

        RealVehicle rv = rvRepo.findOwnRealVehicle();

        if (rv != null)
        {
            pointFeature.setProperty("rvType", rv.getType().name());
            pointFeature.setProperty("rvId", rv.getId());
            pointFeature.setProperty("rvName", rv.getName());
            pointFeature.setProperty("rvState", taskList.isEmpty() ? "idle" : "busy");
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
}
