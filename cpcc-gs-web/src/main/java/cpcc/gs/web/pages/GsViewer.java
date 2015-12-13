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

package cpcc.gs.web.pages;

import java.util.List;

import javax.inject.Inject;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.services.CoreJsonConverter;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.utils.MathUtils;
import cpcc.core.utils.RealVehicleUtils;

/**
 * GsViewer
 */
public class GsViewer
{
    @Inject
    private RealVehicleRepository realVehicleRepository;

    @Inject
    private CoreJsonConverter jsonConverter;

    /**
     * @return the map center coordinates.
     */
    public String getMapCenter()
    {
        double[] bbox = RealVehicleUtils.findBoundingBox(realVehicleRepository.findAllRealVehicles());

        if (bbox.length == 4 && MathUtils.containsNoNaN(bbox))
        {
            return "[" + MathUtils.avg(bbox[1], bbox[3]) + "," + MathUtils.avg(bbox[0], bbox[2]) + "]";
        }

        return "[37.8085124939787,-122.42505311965941]";
    }

    /**
     * @return the map's zoom level.
     */
    public String getZoomLevel()
    {
        // TODO fix me!
        return "17";
    }

    /**
     * @return the current regions.
     */
    public String getRegions()
    {
        List<RealVehicle> rvList = realVehicleRepository.findAllRealVehicles();
        return jsonConverter.toRegionJson(rvList);
    }

    /**
     * @return the currently active vehicles.
     */
    public String getVehicles()
    {
        List<RealVehicleState> statesList = realVehicleRepository.findAllRealVehicleStates();
        return jsonConverter.toRealVehicleStateJson(statesList);
    }
}
