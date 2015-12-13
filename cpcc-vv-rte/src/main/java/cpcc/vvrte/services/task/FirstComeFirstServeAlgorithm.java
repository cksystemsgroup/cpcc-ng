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
import cpcc.vvrte.entities.Task;

/**
 * First Come First Serve Scheduling Algorithm
 */
public class FirstComeFirstServeAlgorithm implements TaskSchedulingAlgorithm
{
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schedule(PolarCoordinate position, List<PolarCoordinate> depot, List<Task> scheduledTasks
        , List<Task> pendingTasks)
    {
        if (pendingTasks.isEmpty())
        {
            return false;
        }

        scheduledTasks.addAll(pendingTasks);
        pendingTasks.clear();
        return true;
    }

}
