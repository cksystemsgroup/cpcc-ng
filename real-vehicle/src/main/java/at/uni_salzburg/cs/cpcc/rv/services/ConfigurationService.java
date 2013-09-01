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
package at.uni_salzburg.cs.cpcc.rv.services;

import java.net.URI;
import java.util.Set;

import org.ros.namespace.GraphName;

/**
 * ConfigurationService
 */
public interface ConfigurationService
{
    /**
     * @param masterServerUri the master server URI.
     */
    void setMasterServerUri(URI masterServerUri);

    /**
     * @return the master server URI.
     */
    URI getMasterServerUri();

    /**
     * @param internalRosCore true: use internal RosCore, false: use external RosCore specified by master server URI. 
     */
    void setInternalRosCore(boolean internalRosCore);
    
    /**
     * @return true for an internal RosCore, false otherwise.
     */
    boolean isInternalRosCore();
    
    /**
     * @param positionSensor the sensor providing position information.
     */
    void setPositionSensor(SensorConfiguration positionSensor);

    /**
     * @return the sensor providing position information.
     */
    SensorConfiguration getPositionSensor();

    /**
     * @param wayPointController the way point controller configuration.
     */
    void setWayPointController(ActuatorConfiguration wayPointController);

    /**
     * @return the way point controller configuration.
     */
    ActuatorConfiguration getWayPointController();

    /**
     * @param sensor a sensor to be registered.
     */
    void addSensor(SensorConfiguration sensor);

    /**
     * @return the registered sensors.
     */
    Set<SensorConfiguration> getSensors();

    /**
     * @return the requested sensor or null.
     */
    SensorConfiguration getSensorByName(GraphName name);
    
    /**
     * @param subscriber a actuator to be registered.
     */
    void addActuator(ActuatorConfiguration actuator);

    /**
     * @return the registered actuators.
     */
    Set<ActuatorConfiguration> getActuators();

    /**
     * @return the requested actuator or null.
     */
    ActuatorConfiguration getActuatorByName(GraphName name);
}
