// @(#) AcoTspTasks.java
//
// This code is part of the CPCC project.
// Copyright (c) 2009-2016 Clemens Krainer
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
 * A TSP solver for {@code Task} lists.
 */
public class AcoTspTasks extends AbstractTspSolver
{
    /**
     * This is the index of the vehicle's current position. The calculated path starts at this position.
     */
    private static final int START_POINT_INDEX = 0;

    private int iterations = 3000;
    private int boost = 3;

    /**
     * Calculate the optimal path from a given list of positions.
     * 
     * @param position the current position of the real vehicle
     * @param path the given list of task positions to meet, i.e., the positions the Real Vehicle has to visit.
     * @return a list of positions to meet, optimized according to traveling distance.
     */
    @Override
    public List<Task> calculateBestPath(PolarCoordinate position, List<Task> path)
    {
        if (path.size() < 2)
        {
            return path;
        }
        
        double[][] costMatrix = setupCostMatrix(position, path);

        List<Integer> bestPath = AcoTspSimple.calculateBestPath(costMatrix, iterations, boost);
        bestPath = reorderPath(bestPath, START_POINT_INDEX, START_POINT_INDEX);

        List<Task> r = new ArrayList<Task>();
        for (Integer k : bestPath)
        {
            if (k - 1 >= 0 && k - 1 < path.size())
            {
                r.add(path.get(k - 1));
            }
        }

        return r;
    }

    /**
     * Reorder a given path.
     * 
     * @param path the path to be reordered.
     * @param first the index number of the element to be first element.
     * @param last the index number of the element to be last element.
     * @return the reordered path.
     */
    private static List<Integer> reorderPath(List<Integer> path, int first, int last)
    {
        int f = path.indexOf(first);
        int l = path.indexOf(last);

        if (f < 0)
        {
            throw new IllegalStateException("First index " + first + " is not element of the given path "
                + path.toString());
        }

        List<Integer> newPath = new ArrayList<Integer>();

        if (l + 1 == f)
        {
            for (int k = 0, len = path.size(); k < len; ++k)
            {
                newPath.add(path.get((k + f) % len));
            }
        }
        else
        {
            for (int len = path.size(), k = len; k > 0; --k)
            {
                newPath.add(path.get((k + f) % len));
            }
        }

        return newPath;
    }
}
