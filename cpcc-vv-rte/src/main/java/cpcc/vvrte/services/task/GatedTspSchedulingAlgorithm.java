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
import java.util.concurrent.TimeoutException;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.Task;

/**
 * Gated TSP scheduling algorithm implementation.
 */
public class GatedTspSchedulingAlgorithm implements TaskSchedulingAlgorithm
{
    private static final Logger LOG = LoggerFactory.getLogger(GatedTspSchedulingAlgorithm.class);

    private TimeService timeService;
    private int maxTasks;

    /**
     * @param timeService the time service.
     * @param maxTasks the maximum number of GTSP tasks to connect.
     */
    public GatedTspSchedulingAlgorithm(TimeService timeService,
        @Symbol(VvRteConstants.PROP_GTSP_MAX_TASKS) int maxTasks)
    {
        this.timeService = timeService;
        this.maxTasks = maxTasks;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean schedule(PolarCoordinate position, List<PolarCoordinate> depots, List<Task> scheduledTasks,
        List<Task> pendingTasks)
    {
        if (!scheduledTasks.isEmpty())
        {
            return false;
        }

        int mt = maxTasks;

        while (mt > 0)
        {
            List<Task> taskList = getTaskList(mt, pendingTasks);
            try
            {
                scheduledTasks.addAll(new HeldKarpTspSolver(timeService).calculateBestPath(position, taskList));
                shift(pendingTasks, taskList.size());
                return true;
            }
            catch (TimeoutException e)
            {
                mt /= 2;
                LOG.warn("Reducing GTSP path length from {} to {}", maxTasks, mt);
            }
        }

        return false;
    }

    /**
     * @param taskList the list of tasks.
     * @param nrOfShifts the number of elements to remove from the list beginning.
     */
    private static void shift(List<Task> taskList, int nrOfShifts)
    {
        for (int k = 0, l = nrOfShifts; k < l; ++k)
        {
            taskList.remove(0);
        }
    }

    /**
     * @param maxTasks the maximum number of tasks to consider in the GTSP algorithm.
     * @param pendingTasks the list of pending tasks.
     * @return the list of tasks to schedule.
     */
    private static List<Task> getTaskList(int maxTasks, List<Task> pendingTasks)
    {
        List<Task> taskList = new ArrayList<>();

        for (int k = 0, l = Math.min(maxTasks, pendingTasks.size()); k < l; ++k)
        {
            taskList.add(pendingTasks.get(k));
        }

        return taskList;
    }
}
