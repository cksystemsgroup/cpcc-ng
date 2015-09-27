// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.services.QueryManager;
import cpcc.core.utils.JSONUtils;

/**
 * GsViewer
 */
public class GsViewer
{
    @Inject
    private QueryManager qm;

    /**
     * @return the map center coordinates.
     */
    public String getMapCenter()
    {
        // TODO fix me!
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
        List<RealVehicle> rvList = qm.findAllRealVehicles();

        Map<String, String> rvMap = new HashMap<String, String>();
        for (RealVehicle rv : rvList)
        {
            rvMap.put(rv.getName(), rv.getAreaOfOperation());
        }

        return JSONUtils.toJsonString(rvMap);
    }

    /**
     * @return the currently active vehicles.
     */
    public String getVehicles()
    {
        try
        {
            Map<String, String> stateMap = new HashMap<String, String>();

            for (RealVehicleState state : qm.findAllRealVehicleStates())
            {
                stateMap.put(state.getId().toString(), state.getState());
            }

            return JSONUtils.toJsonString(stateMap);
        }
        catch (Throwable t)
        {
            t.printStackTrace();
            return "{}";
        }
    }
}
