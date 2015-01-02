// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package at.uni_salzburg.cs.cpcc.ros.sim.quadrotor;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;


/**
 * PlantState
 */
public class PlantState
{
    private PolarCoordinate position;
    private PolarCoordinate target;
    private double velocity;
    private double heading;
    private double acceleration;
    private double elevation;
    private double flyingTime;
    private double remainingBatteryCapacity;
    
    /**
     * 
     */
    public PlantState()
    {
        position = new PolarCoordinate();
        target = new PolarCoordinate();
        velocity = 0;
        heading = 0;
        acceleration = 0;
        elevation = 0;
        flyingTime = 0;
        remainingBatteryCapacity = 0;
    }
    
    /**
     * @param other the <code>PlantState</code> object to copy from.
     */
    public PlantState(PlantState other)
    {
        position = new PolarCoordinate(other.position);
        target = new PolarCoordinate(other.target);
        velocity = other.velocity;
        heading = other.heading;
        acceleration = other.acceleration;
        elevation = other.elevation;
        flyingTime = other.flyingTime;
        remainingBatteryCapacity = other.remainingBatteryCapacity;
    }

    /**
     * @return the current position
     */
    public PolarCoordinate getPosition()
    {
        return position;
    }
    
    /**
     * @param position the current position to set
     */
    public void setPosition(PolarCoordinate position)
    {
        this.position = position;
    }
    
    /**
     * @return the target position
     */
    public PolarCoordinate getTarget()
    {
        return target;
    }
    
    /**
     * @param target the target position to set
     */
    public void setTarget(PolarCoordinate target)
    {
        this.target = target;
    }
    
    /**
     * @return the velocity
     */
    public double getVelocity()
    {
        return velocity;
    }
    
    /**
     * @param velocity the velocity to set
     */
    public void setVelocity(double velocity)
    {
        this.velocity = velocity;
    }
    
    /**
     * @return the heading
     */
    public double getHeading()
    {
        return heading;
    }
    
    /**
     * @param heading the heading to set
     */
    public void setHeading(double heading)
    {
        this.heading = heading;
    }
    
    /**
     * @return the acceleration
     */
    public double getAcceleration()
    {
        return acceleration;
    }
    
    /**
     * @param acceleration the acceleration to set
     */
    public void setAcceleration(double acceleration)
    {
        this.acceleration = acceleration;
    }
    
    /**
     * @return the elevation
     */
    public double getElevation()
    {
        return elevation;
    }

    /**
     * @param elevation the elevation to set
     */
    public void setElevation(double elevation)
    {
        this.elevation = elevation;
    }
    
    /**
     * @return the flying time
     */
    public double getFlyingTime()
    {
        return flyingTime;
    }

    /**
     * @param flyingTime the flyingTime to set
     */
    public void setFlyingTime(double flyingTime)
    {
        this.flyingTime = flyingTime;
    }
    
    /**
     * @return the remainingBatteryCapacity
     */
    public double getRemainingBatteryCapacity()
    {
        return remainingBatteryCapacity;
    }
    
    /**
     * @param remainingBatteryCapacity the remainingBatteryCapacity to set
     */
    public void setRemainingBatteryCapacity(double remainingBatteryCapacity)
    {
        this.remainingBatteryCapacity = remainingBatteryCapacity;
    }

    /**
     * @param prefix the key prefix to be used.
     * @return the map of all state variables.
     */
    public Map<String, List<String>> getStateMap(String prefix)
    {
        Map<String, List<String>> map = new HashMap<String, List<String>>();

        map.put(prefix + ".acceleration", Arrays.asList(String.format(Locale.US, "%.2f", getAcceleration())));
        map.put(prefix + ".elevation", Arrays.asList(String.format(Locale.US, "%.3f", Math.toDegrees(getElevation()))));
        map.put(prefix + ".flyingTime", Arrays.asList(String.format(Locale.US, "%.2f", getFlyingTime())));
        map.put(prefix + ".heading", Arrays.asList(String.format(Locale.US, "%.0f", Math.toDegrees(getHeading()))));
        map.put(prefix + ".position", Arrays.asList(
            String.format(Locale.US, "%.8f", getPosition().getLatitude()),
            String.format(Locale.US, "%.8f", getPosition().getLongitude()),
            String.format(Locale.US, "%.3f", getPosition().getAltitude())
            ));
        map.put(prefix + ".batteryCapacity", Arrays.asList(
            String.format(Locale.US, "%.1f", getRemainingBatteryCapacity())));
        map.put(prefix + ".target", Arrays.asList(
            String.format(Locale.US, "%.8f", getTarget().getLatitude()),
            String.format(Locale.US, "%.8f", getTarget().getLongitude()),
            String.format(Locale.US, "%.3f", getTarget().getAltitude())
            ));
        map.put(prefix + ".velocity", Arrays.asList(String.format(Locale.US, "%.2f", getVelocity())));

        return map;
    }
    
}
