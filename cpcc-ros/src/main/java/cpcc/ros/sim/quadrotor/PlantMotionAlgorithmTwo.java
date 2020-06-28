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
 * PlantMotionAlgorithmTwo implementation.
 */
public class PlantMotionAlgorithmTwo implements PlantMotionAlgorithm
{
    private double dist;
    private double maxA;
    private double totalTime;

    /**
     * @param log the application logger.
     * @param dist the distance to travel.
     * @param maxA the maximum allowed acceleration.
     */
    public PlantMotionAlgorithmTwo(Logger log, double dist, double maxA)
    {
        this.dist = dist;
        this.maxA = maxA;
        this.totalTime = Math.sqrt(6.0 * dist / maxA);

        double maxVPrime = maxA * totalTime / 4.0;
        log.info("Two: dist={}, maxV'={}, maxA={}, totalTime={}", dist, maxVPrime, maxA, totalTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double distance(double time)
    {
        if (time < 0.0)
        {
            return 0.0;
        }

        return time >= totalTime
            ? dist
            : maxA * time * time * (3.0 * totalTime - 2.0 * time) / (6.0 * totalTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double velocity(double time)
    {
        return time < 0.0 || time >= totalTime
            ? 0.0
            : maxA * time * (1.0 - time / totalTime);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double acceleration(double time)
    {
        return time < 0.0 || time >= totalTime
            ? 0.0
            : maxA * (1.0 - 2.0 * time / totalTime);
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