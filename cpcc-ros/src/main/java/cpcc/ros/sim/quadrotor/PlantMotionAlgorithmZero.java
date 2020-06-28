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

package cpcc.ros.sim.quadrotor;

import org.slf4j.Logger;

/**
 * Plant motion algorithm zero implementation.
 */
public class PlantMotionAlgorithmZero implements PlantMotionAlgorithm
{
    private double dist;
    private double totalTime;

    /**
     * @param log the application logger.
     * @param state the plant state.
     * @param dist the distance to travel.
     * @param maxV the maximum allowed travel velocity.
     * @param maxA the maximum allowed acceleration.
     */
    public PlantMotionAlgorithmZero(Logger log, State state, double dist, double maxV, double maxA)
    {
        this.dist = dist;
        this.totalTime = 1.5 * dist / maxV;

        double ma = 6.0 * dist / (totalTime * totalTime);
        if (ma > maxA)
        {
            log.warn("{}: maximum acceleration reduced from {} m/s^2 to {} m/s^2", state, ma, maxA);

            double totalTimeNew = Math.sqrt(6 * dist / maxA);
            log.warn("{}: total time prolonged from {} s to {} s", state, totalTime, totalTimeNew);
            totalTime = totalTimeNew;

            double maxVnew = 1.5 * dist / totalTime;
            log.warn("{}: maximum velocity reduced from {} m/s to {} m/s", state, maxV, maxVnew);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double distance(double time)
    {
        return dist * time * time / (totalTime * totalTime) * (3 - 2 * time / totalTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double velocity(double time)
    {
        return dist * 6.0 * time / (totalTime * totalTime) * (1 - time / totalTime);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double acceleration(double time)
    {
        return dist * 6.0 / (totalTime * totalTime) * (1 - 2 * time / totalTime);

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTotalTime()
    {
        return totalTime;
    }
}