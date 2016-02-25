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

/**
 * Plant motion algorithm interface.
 */
interface PlantMotionAlgorithm
{
    /**
     * @param time the current flight time.
     * @return the distance at the given flight time.
     */
    double distance(double time);

    /**
     * @param time the current flight time.
     * @return the acceleration at the given flight time.
     */
    double acceleration(double time);

    /**
     * @param time the current flight time.
     * @return the velocity at the given flight time.
     */
    double velocity(double time);

    /**
     * @return the total flight time of the current flight segment.
     */
    double getTotalTime();
}