/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.ros.sim.quadrotor;

import java.util.List;
import java.util.Map;

import org.ros.node.NodeConfiguration;

import at.uni_salzburg.cs.cpcc.utilities.ConfigUtils;
import at.uni_salzburg.cs.cpcc.utilities.GeodeticSystem;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.utilities.WGS84;

/**
 * Configuration
 */
public class Configuration
{
    private static final String CFG_TOPIC_ROOT = "topicRoot";
    private static final String CFG_ORIGIN = "origin";
    private static final String CFG_MAX_VELOCITY = "maxVelocity";
    private static final String CFG_MAX_ACCELERATION = "maxAcceleration";
    private static final String CFG_PRECISION = "precision";
    private static final String CFG_UPDATE_CYCLE = "updateCycle";
    private static final String CFG_IDLE_POWER = "idlePower";
    private static final String CFG_HOVER_POWER = "hoverPower";
    private static final String CFG_MASS = "mass";
    private static final String CFG_BATTERY_CAPACITY = "batteryCapacity";
    private static final String CFG_RECHARGING_TIME = "rechargingTime";
    private static final String CFG_TAKEOFF_HEIGHT = "takeOffHeight";
    private static final String CFG_TAKEOFF_VELOCITY = "takeOffVelocity";
    private static final String CFG_TAKEOFF_ACCELERATION = "takeOffAcceleration";
    private static final String CFG_LANDING_VELOCITY = "landingVelocity";
    private static final String CFG_LANDING_ACCELERATION = "landingAcceleration";

    private String topicRoot;
    private big_actor_msgs.LatLngAlt origin;
    private double maxVelocity;
    private double maxAcceleration;
    private double precision;
    private long updateCycle;
    private double idlePower;
    private double hoverPower;
    private double mass;
    private double batteryCapacity;
    private double rechargingTime;
    private double takeOffHeight;
    private double takeOffVelocity;
    private double takeOffAcceleration;
    private double landingVelocity;
    private double landingAcceleration;
    private GeodeticSystem geodeticSystem;
    
    /**
     * @param config the configuration as a map.
     */
    /**
     * @param nodeConfiguration the node configuration.
     * @param config the parsed configuration parameters.
     */
    public Configuration(NodeConfiguration nodeConfiguration, Map<String, List<String>> config)
    {
        this.topicRoot = config.get(CFG_TOPIC_ROOT).get(0);

        PolarCoordinate originPosition = ConfigUtils.parsePolarCoordinate(config, CFG_ORIGIN, 0);
        
        origin = nodeConfiguration.getTopicMessageFactory().newFromType(big_actor_msgs.LatLngAlt._TYPE);
        origin.setLatitude(originPosition.getLatitude());
        origin.setLongitude(originPosition.getLongitude());
        origin.setAltitude(originPosition.getAltitude());
        
        maxVelocity = ConfigUtils.parseDouble(config, CFG_MAX_VELOCITY, 0, 5);
        maxAcceleration = ConfigUtils.parseDouble(config, CFG_MAX_ACCELERATION, 0, 1);
        precision = ConfigUtils.parseDouble(config, CFG_PRECISION, 0, 3);
        updateCycle = ConfigUtils.parseInteger(config, CFG_UPDATE_CYCLE, 0, 100);
        idlePower = ConfigUtils.parseDouble(config, CFG_IDLE_POWER, 0, 0);
        hoverPower = ConfigUtils.parseDouble(config, CFG_HOVER_POWER, 0, 0);
        mass = ConfigUtils.parseDouble(config, CFG_MASS, 0, 1);
        batteryCapacity = ConfigUtils.parseDouble(config, CFG_BATTERY_CAPACITY, 0, 1);
        rechargingTime = ConfigUtils.parseDouble(config, CFG_RECHARGING_TIME, 0, 0);
        takeOffHeight = ConfigUtils.parseDouble(config, CFG_TAKEOFF_HEIGHT, 0, 1);
        takeOffVelocity = ConfigUtils.parseDouble(config, CFG_TAKEOFF_VELOCITY, 0, 1);
        takeOffAcceleration = ConfigUtils.parseDouble(config, CFG_TAKEOFF_ACCELERATION, 0, 1);
        landingVelocity = ConfigUtils.parseDouble(config, CFG_LANDING_VELOCITY, 0, 1);
        landingAcceleration = ConfigUtils.parseDouble(config, CFG_LANDING_ACCELERATION, 0, 1);
        geodeticSystem = new WGS84();
    }

    /**
     * @return the topic root
     */
    public String getTopicRoot()
    {
        return topicRoot;
    }

    /**
     * @return the origin
     */
    public big_actor_msgs.LatLngAlt getOrigin()
    {
        return origin;
    }

    /**
     * @return the maximum velocity
     */
    public double getMaxVelocity()
    {
        return maxVelocity;
    }

    /**
     * @return the maximum acceleration
     */
    public double getMaxAcceleration()
    {
        return maxAcceleration;
    }

    /**
     * @return the required precision to reach a target.
     */
    public double getPrecision()
    {
        return precision;
    }

    /**
     * @return the update cycle in milliseconds.
     */
    public long getUpdateCycle()
    {
        return updateCycle;
    }
    
    /**
     * @return the idlePower
     */
    public double getIdlePower()
    {
        return idlePower;
    }
    
    /**
     * @return the hover power
     */
    public double getHoverPower()
    {
        return hoverPower;
    }
    
    /**
     * @return the mass
     */
    public double getMass()
    {
        return mass;
    }
    
    /**
     * @return the battery capacity.
     */
    public double getBatteryCapacity()
    {
        return batteryCapacity;
    }
    
    /**
     * @return the re-charging time of the battery.
     */
    public double getRechargingTime()
    {
        return rechargingTime;
    }
    
    /**
     * @return the take-off height
     */
    public double getTakeOffHeight()
    {
        return takeOffHeight;
    }
    
    /**
     * @return the take-off velocity
     */
    public double getTakeOffVelocity()
    {
        return takeOffVelocity;
    }
    
    /**
     * @return the takeOffAcceleration
     */
    public double getTakeOffAcceleration()
    {
        return takeOffAcceleration;
    }
    
    /**
     * @return the landingVelocity
     */
    public double getLandingVelocity()
    {
        return landingVelocity;
    }
    
    /**
     * @return the landingAcceleration
     */
    public double getLandingAcceleration()
    {
        return landingAcceleration;
    }
    
    /**
     * @return the geodeticSystem
     */
    public GeodeticSystem getGeodeticSystem()
    {
        return geodeticSystem;
    }
}
