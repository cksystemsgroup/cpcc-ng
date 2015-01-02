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

package at.uni_salzburg.cs.cpcc.gs.pages;

import java.util.List;

import javax.inject.Inject;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * GsViewer
 */
public class GsViewer
{
    @Inject
    private QueryManager qm;

    public String getMapCenter()
    {
        return "[37.8085124939787,-122.42505311965941]";
    }

    public String getZoomLevel()
    {
        return "17";
    }

    //    public JSONObject getRegions()
    //    {
    //        JSONObject obj = new JSONObject();
    //
    //        List<RealVehicle> rvList = qm.findAllRealVehicles();
    //        for (RealVehicle rv : rvList)
    //        {
    //            obj.put(rv.getName(), new JSONObject(rv.getAreaOfOperation()));
    //        }
    //        
    //        System.out.println("getRegions: " + obj.toCompactString());
    //
    //        return obj;
    //    }

    public String getRegions()
    {
        StringBuilder b = new StringBuilder();

        List<RealVehicle> rvList = qm.findAllRealVehicles();
        boolean first = true;

        b.append("{");
        for (RealVehicle rv : rvList)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                b.append(",");
            }
            b.append("\"").append(rv.getName()).append("\":").append(rv.getAreaOfOperation());
        }
        b.append("}");

        return b.toString();
    }

    public String getVehicles()
    {
        return "{}";
    }
}
