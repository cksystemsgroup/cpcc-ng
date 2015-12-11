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

import org.geojson.FeatureCollection;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;

/**
 * State contributor interface.
 */
public interface StateContributor
{
    /**
     * @param featureCollection the collection to contribute to.
     * @param rvPosition the current position of the host Real Vehicle.
     * @param taskList the list of tasks containing the current executed task and the scheduled tasks.
     */
    void contribute(FeatureCollection featureCollection, PolarCoordinate rvPosition, List<Task> taskList);
}
