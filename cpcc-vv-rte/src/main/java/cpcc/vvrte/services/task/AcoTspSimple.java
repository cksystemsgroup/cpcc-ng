// This code is part of the CPCC-NG project.
//
// Copyright (c) 2012 Clemens Krainer
// Copyright (C) 1981-2009 Murilo Pontes <murilo.pontes@gmail.com>
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

import org.apache.commons.lang3.RandomUtils;

/**
 * This code is based on Murilo Pontes' Java TSP solver available at
 * http://www.pudn.com/downloads170/sourcecode/java/detail786923.html
 */
public final class AcoTspSimple
{
    private AcoTspSimple()
    {
        // Intentionally empty.
    }

    /**
     * @param costMatrix the cost matrix.
     * @param maxIterations the maximum number of iterations
     * @param boost the boost value.
     * @return the best path as a list of integers.
     */
    public static List<Integer> calculateBestPath(double[][] costMatrix, int maxIterations, int boost)
    {
        if (costMatrix.length == 0 || costMatrix.length != costMatrix[0].length)
        {
            throw new IllegalStateException("Cost matrix heigth and width must be equal!");
        }

        double[][] pheromoneTrails = new double[costMatrix.length][costMatrix.length];

        for (int k = 0, l = costMatrix.length; k < l; ++k)
        {
            for (int j = 0; j < l; ++j)
            {
                pheromoneTrails[k][j] = 0;
            }
        }

        double bestLen = Double.POSITIVE_INFINITY;
        List<Integer> bestPath = new ArrayList<Integer>();

        for (int iteration = 0; iteration < maxIterations; iteration++)
        {
            List<Integer> path = generatePath(costMatrix, pheromoneTrails);
            double pathLen = realPathLength(costMatrix, path);

            if (pathLen < bestLen)
            {
                updatePheromoneTrails(pheromoneTrails, path, boost);
                bestLen = pathLen;
                bestPath = path;
            }

            evaporatePheromoneTrails(pheromoneTrails, (double) boost / (double) maxIterations);
        }

        return bestPath;
    }

    /**
     * @param costMatrix the cost matrix.
     * @param pheromoneTrails the pheromone trails.
     * @param used the list of used values.
     * @param current the current value.
     * @param soughtTotal the total sought.
     * @return the weight sum.
     */
    private static int findSumWeight(double[][] costMatrix, double[][] pheromoneTrails, List<Integer> used,
        int current, double soughtTotal)
    {
        double runningTotal = 0.0;
        int next = 0;

        for (int city = 0; city < costMatrix.length; city++)
        {
            if (runningTotal >= soughtTotal)
            {
                break;
            }

            if (used.indexOf(city) == -1)
            {
                runningTotal += (1.0 + pheromoneTrails[current][city]) / (1.0 + costMatrix[current][city]);
                next = city;
            }
        }

        return next;
    }

    /**
     * @param costMatrix the cost matrix.
     * @param pheromoneTrails the pheromone trails.
     * @param used the list of used values.
     * @param current the current value.
     * @return the weight sum.
     */
    private static double doSumWeight(double[][] costMatrix, double[][] pheromoneTrails, List<Integer> used
        , int current)
    {
        double runningTotal = 0.0;

        for (int city = 0; city < costMatrix.length; city++)
        {
            if (used.indexOf(city) == -1)
            {
                runningTotal += (1.0 + pheromoneTrails[current][city]) / (1.0 + costMatrix[current][city]);
            }
        }

        return runningTotal;
    }

    /**
     * @param costMatrix the cost matrix.
     * @param pheromoneTrails the pheromone trails.
     * @return the path.
     */
    private static List<Integer> generatePath(double[][] costMatrix, double[][] pheromoneTrails)
    {
        // int current = (int) (costMatrix.length * Math.random());
        int current = RandomUtils.nextInt(0, costMatrix.length);
        List<Integer> used = new ArrayList<Integer>();
        used.add(current);

        while (used.size() < costMatrix.length)
        {
            double sumWeight = doSumWeight(costMatrix, pheromoneTrails, used, current);
            // double rndValue = Math.random() * sumWeight;
            double rndValue = RandomUtils.nextDouble(0, sumWeight);
            current = findSumWeight(costMatrix, pheromoneTrails, used, current, rndValue);
            used.add(current);
        }

        return used;
    }

    /**
     * @param pheromoneTrails the pheromone trails.
     * @param evaporationDecrement the evaporation decrement.
     */
    private static void evaporatePheromoneTrails(double[][] pheromoneTrails, double evaporationDecrement)
    {
        for (int r = 0; r < pheromoneTrails.length; r++)
        {
            for (int c = 0; c < pheromoneTrails.length; c++)
            {
                if (pheromoneTrails[r][c] > evaporationDecrement)
                {
                    pheromoneTrails[r][c] = pheromoneTrails[r][c] - evaporationDecrement;
                }
                else
                {
                    pheromoneTrails[r][c] = 0.0;
                }
            }
        }
    }

    /**
     * @param pheromoneTrails the pheromone trails.
     * @param path the path.
     * @param boost the boost value.
     */
    private static void updatePheromoneTrails(double[][] pheromoneTrails, List<Integer> path, int boost)
    {
        for (int k = 0, l = path.size(); k < l; ++k)
        {
            int a = path.get(k);
            int b = k + 1 == l ? path.get(0) : path.get(k + 1);
            pheromoneTrails[a][b] = pheromoneTrails[a][b] + boost;
        }
    }

    /**
     * @param costMatrix the cost matrix.
     * @param path the path.
     * @return the real path length.
     */
    private static double realPathLength(double[][] costMatrix, List<Integer> path)
    {
        double sum = 0;

        for (int k = 0, l = path.size(); k < l; ++k)
        {
            int a = path.get(k);
            int b = k + 1 == l ? path.get(0) : path.get(k + 1);
            sum += costMatrix[a][b];
        }

        return sum;
    }

    /**
     * @param index the index number to be first in the resulting array.
     * @param path the path to fix.
     */
    public static void fixPath(int index, List<Integer> path)
    {
        int shift = path.indexOf(index);

        if (shift <= 0)
        {
            return;
        }

        while (shift-- > 0)
        {
            Integer v = path.remove(0);
            path.add(v);
        }
    }
}
