// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
import cpcc.vvrte.entities.Task;

/**
 * TaskSchedulingAlgorithm
 */
public interface TaskSchedulingAlgorithm
{
    /**
     * This method takes pending tasks and adds them to the list of scheduled tasks. Furthermore, it sorts the scheduled
     * tasks. Lower indices in this list indicate earlier execution.
     * 
     * @param position the current position of the Real Vehicle.
     * @param depotPositions the positions of the vehicle depots.
     * @param scheduledTasks the already scheduled tasks.
     * @param pendingTasks the currently pending tasks.
     * @return true if the algorithm changed the list of scheduled tasks.
     */
    boolean schedule(PolarCoordinate position, List<PolarCoordinate> depotPositions, List<Task> scheduledTasks
        , List<Task> pendingTasks);
}
