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

package cpcc.vvrte.services.task;

import java.util.List;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.vvrte.entities.Task;

/**
 * Gated TSP scheduling algorithm implementation.
 */
public class GatedTspSchedulingAlgorithm implements TaskSchedulingAlgorithm
{
    private AcoTspTasks tspSolver;

    /**
     * Default constructor.
     */
    public GatedTspSchedulingAlgorithm()
    {
        GeodeticSystem gs = new FlatWorld();
        tspSolver = new AcoTspTasks(gs);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schedule(PolarCoordinate position, List<PolarCoordinate> depots, List<Task> scheduledTasks
        , List<Task> pendingTasks)
    {
        if (!scheduledTasks.isEmpty())
        {
            return false;
        }

        if (depots.isEmpty())
        {
            scheduledTasks.addAll(tspSolver.calculateBestPathWithoutDepot(position, pendingTasks));
        }
        else
        {
            scheduledTasks.addAll(tspSolver.calculateBestPathWithDepot(position, depots.get(0), pendingTasks));
        }
        pendingTasks.clear();
        return true;
    }
}
