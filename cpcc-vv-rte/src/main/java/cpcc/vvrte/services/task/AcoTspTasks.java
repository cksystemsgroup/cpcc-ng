// @(#) AcoTspTasks.java
//
// This code is part of the CPCC project.
// Copyright (c) 2012 Clemens Krainer
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
import cpcc.core.utils.CartesianCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.vvrte.entities.Task;

/**
 * A TSP solver for {@code Task} lists.
 */
public class AcoTspTasks
{
    /**
     * This is the index of the vehicle's current position. The calculated path starts at this position.
     */
    private static final int START_POINT_INDEX = 0;

    /**
     * This is the index of the vehicle's depot position. The calculated ends at this position.
     */
    private static final int END_POINT_INDEX = 1;

    /**
     * The geodetic system to be used for coordinate transformations.
     */
    private GeodeticSystem geodeticSystem;

    /**
     * Construct a <code>AcoTsp</code> instance.
     * 
     * @param geodeticSystem the geodetic system to be used for coordinate transformations.
     */
    public AcoTspTasks(GeodeticSystem geodeticSystem)
    {
        this.geodeticSystem = geodeticSystem;
    }

    /**
     * Calculate the optimal path from a given list of positions.
     * 
     * @param position the current position of the real vehicle.
     * @param depot the position of the depot.
     * @param path the given list of positions to meet, i.e., the positions the Real Vehicle has to visit.
     * @return a list of positions to meet, optimized according to traveling distance.
     */
    public List<Task> calculateBestPathWithDepot(PolarCoordinate position, PolarCoordinate depot, List<Task> path)
    {
        if (depot == null && path.size() < 2)
        {
            return path;
        }

        List<CartesianCoordinate> cList = new ArrayList<CartesianCoordinate>();

        cList.add(geodeticSystem.polarToRectangularCoordinates(position));

        if (depot != null)
        {
            cList.add(geodeticSystem.polarToRectangularCoordinates(depot));
        }

        for (Task p : path)
        {
            CartesianCoordinate c = geodeticSystem.polarToRectangularCoordinates(p.getPosition());
            cList.add(c);
        }

        double[][] costMatrix = setupCostMatrix(cList);

        int o = 1;
        if (depot != null)
        {
            costMatrix[START_POINT_INDEX][END_POINT_INDEX] = 0;
            costMatrix[END_POINT_INDEX][START_POINT_INDEX] = 0;
            o = 2;
        }

        List<Integer> bestPath = AcoTspSimple.calculateBestPath(costMatrix, getIterations(cList.size()), 3);
        bestPath = reorderPath(bestPath, START_POINT_INDEX, depot != null ? END_POINT_INDEX : START_POINT_INDEX);

        List<Task> r = new ArrayList<Task>();
        for (Integer k : bestPath)
        {
            if (k - o >= 0 && k - o < path.size())
            {
                r.add(path.get(k - o));
            }
        }

        return r;
    }

    /**
     * @param size the number of points to visit in a TSP run.
     * @return the number of required ant iteration cycles.
     */
    private int getIterations(int size)
    {
        int i = size * size * size * 200 + 1000;
        return i > 400000 ? 400000 : i;
    }

    /**
     * @param path the path of coordinates.
     * @return the initialized cost matrix.
     */
    private double[][] setupCostMatrix(List<CartesianCoordinate> path)
    {
        int n = path.size();
        double[][] costMatrix = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                costMatrix[i][j] = path.get(j).subtract(path.get(i)).norm();
            }
        }

        return costMatrix;
    }

    /**
     * Calculate the optimal path from a given list of positions.
     * 
     * @param position the current position of the real vehicle
     * @param path the given list of task positions to meet, i.e., the positions the Real Vehicle has to visit.
     * @return a list of positions to meet, optimized according to traveling distance.
     */
    public List<Task> calculateBestPathWithoutDepot(PolarCoordinate position, List<Task> path)
    {
        return calculateBestPathWithDepot(position, null, path);
    }

    /**
     * Reorder a given path.
     * 
     * @param path the path to be reordered.
     * @param first the index number of the element to be first element.
     * @param last the index number of the element to be last element.
     * @return the reordered path.
     */
    static List<Integer> reorderPath(List<Integer> path, int first, int last)
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
