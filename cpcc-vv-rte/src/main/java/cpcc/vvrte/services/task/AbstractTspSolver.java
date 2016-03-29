// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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
 * AbstractTspSolver implementation.
 */
public abstract class AbstractTspSolver implements TspSolver
{
    /**
     * @param position the current position of the Real Vehicle.
     * @param path the path of coordinates.
     * @return the initialized cost matrix.
     */
    protected static double[][] setupCostMatrix(PolarCoordinate position, List<Task> path)
    {
        int n = 1 + path.size();

        Task currentTask = new Task();
        currentTask.setPosition(position);

        List<Task> coords = new ArrayList<>(n);
        coords.add(currentTask);
        coords.addAll(path);

        double minX = coords.stream().map(p -> p.getPosition().getLongitude()).min(Double::compare).get().doubleValue();
        double maxX = coords.stream().map(p -> p.getPosition().getLongitude()).max(Double::compare).get().doubleValue();
        double minY = coords.stream().map(p -> p.getPosition().getLatitude()).min(Double::compare).get().doubleValue();
        double maxY = coords.stream().map(p -> p.getPosition().getLatitude()).max(Double::compare).get().doubleValue();

        double[][] costMatrix = new double[n][n];

        for (int i = 0; i < n; i++)
        {
            for (int j = 0; j < n; j++)
            {
                double x1 = scale(coords.get(i).getPosition().getLongitude(), minX, maxX);
                double x2 = scale(coords.get(j).getPosition().getLongitude(), minX, maxX);
                double y1 = scale(coords.get(i).getPosition().getLatitude(), minY, maxY);
                double y2 = scale(coords.get(j).getPosition().getLatitude(), minY, maxY);
                double dx = x1 - x2;
                double dy = y1 - y2;
                costMatrix[i][j] = Math.rint(Math.sqrt(dx * dx + dy * dy));
            }
        }

        return costMatrix;
    }

    /**
     * Scale the actual value in a range between 0 and 1000.
     * 
     * @param actualValue the actual value.
     * @param minValue the minimum value.
     * @param maxValue the maximum value.
     * @return
     */
    private static double scale(double actualValue, double minValue, double maxValue)
    {
        return 1000.0 * (actualValue - minValue) / (maxValue - minValue);
    }
}
