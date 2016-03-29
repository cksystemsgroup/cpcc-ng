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

import java.util.ArrayList;
import java.util.List;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;

/**
 * Gated TSP scheduling algorithm implementation.
 */
public class GatedTspSchedulingAlgorithm implements TaskSchedulingAlgorithm
{
    private int maxTasks;
    private TspSolver tspSolver;

    /**
     * Default constructor.
     */
    public GatedTspSchedulingAlgorithm()
    {
        maxTasks = 30;
        tspSolver = new HeldKarpTspSolver();
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

        List<Task> taskList = new ArrayList<>();
        for (int k = 0, l = Math.min(maxTasks, pendingTasks.size()); k < l; ++k)
        {
            taskList.add(pendingTasks.remove(0));
        }

        scheduledTasks.addAll(tspSolver.calculateBestPath(position, taskList));
        return true;
    }
}
