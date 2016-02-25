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
 * Plant motion algorithm one implementation.
 */
public class PlantMotionAlgorithmOne implements PlantMotionAlgorithm
{
    private double dist;
    private double maxV;
    private double totalTime;
    private double timeOne;
    private double timeTwo;
    private double distTimeOne;

    /**
     * @param log the application logger.
     * @param dist the travel distance.
     * @param maxV the maximum allowed velocity to travel.
     * @param maxA the maximum allowed acceleration to travel.
     */
    public PlantMotionAlgorithmOne(Logger log, double dist, double maxV, double maxA)
    {
        this.dist = dist;
        this.maxV = maxV;

        distTimeOne = 4.0 * maxV * maxV / 3.0 / maxA;
        totalTime = (dist + distTimeOne) / maxV;
        timeOne = 2.0 * maxV / maxA;
        timeTwo = totalTime - timeOne;

        log.info("One: dist=" + dist + ", maxV=" + maxV + ", maxA=" + maxA + ", totalTime=" + totalTime + ", timeOne="
            + timeOne + ", timeTwo=" + timeTwo);
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
        
        if (time >= totalTime)
        {
            return dist;
        }

        if (time <= timeOne)
        {
            return maxV * time * time * (3.0 * timeOne - time) / (3.0 * timeOne * timeOne);
        }

        if (time <= timeTwo)
        {
            return distTimeOne + maxV * (time - timeOne);
        }

        double tt = totalTime - time;
        // return dist - maxA * maxA * tt * tt * (2.0 * maxV / maxA - tt / 3.0) / (4.0 * maxV);
        return dist - maxV * tt * tt * (3.0 * timeOne - tt) / 3.0 / timeOne / timeOne;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double velocity(double time)
    {
        if (time < 0.0)
        {
            return 0.0;
        }
        
        if (time >= totalTime)
        {
            return 0.0;
        }

        if (time <= timeOne)
        {
            return maxV * time * (2.0 * timeOne - time) / timeOne / timeOne;
        }

        if (time <= timeTwo)
        {
            return maxV;
        }

        double tt = totalTime - time;
        return maxV * tt * (2.0 * timeOne - tt) / timeOne / timeOne;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double acceleration(double time)
    {
        if (time < 0.0)
        {
            return 0.0;
        }
        
        if (time >= totalTime)
        {
            return 0.0;
        }

        if (time <= timeOne)
        {
            return 2.0 * maxV * (timeOne - time) / timeOne / timeOne;
        }

        if (time <= timeTwo)
        {
            return 0.0;
        }

        return 2.0 * maxV * (totalTime - time - timeOne) / timeOne / timeOne;
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