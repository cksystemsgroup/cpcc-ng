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
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Singleton;

import org.ros.namespace.GraphName;

/**
 * ConfigurationServiceImpl
 */
@Singleton
public class ConfigurationServiceImpl implements ConfigurationService
{
    private URI masterServerUri;
    
    private boolean internalRosCore;
    
    private SensorConfiguration positionSensor;
    
    private ActuatorConfiguration wayPointController;
    
    private Set<SensorConfiguration> sensors = Collections.synchronizedSet(new HashSet<SensorConfiguration>());
    
    private Set<ActuatorConfiguration> actuators = Collections.synchronizedSet(new HashSet<ActuatorConfiguration>());

    /**
     * {@inheritDoc}
     */
    @Override
    public void setMasterServerUri(URI masterServerUri)
    {
        this.masterServerUri = masterServerUri;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public URI getMasterServerUri()
    {
        return masterServerUri;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setInternalRosCore(boolean internalRosCore)
    {
        this.internalRosCore = internalRosCore;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isInternalRosCore()
    {
        return internalRosCore;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setPositionSensor(SensorConfiguration positionSensor)
    {
        this.positionSensor = positionSensor;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SensorConfiguration getPositionSensor()
    {
        return positionSensor;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void setWayPointController(ActuatorConfiguration wayPointController)
    {
        this.wayPointController = wayPointController;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorConfiguration getWayPointController()
    {
        return wayPointController;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addSensor(SensorConfiguration sensorName)
    {
        sensors.add(sensorName);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<SensorConfiguration> getSensors()
    {
        return sensors;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public SensorConfiguration getSensorByName(GraphName name)
    {
        for (SensorConfiguration sc : sensors)
        {
            if (sc.getName().equals(name))
            {
                return sc;
            }
        }
        
        return null;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addActuator(ActuatorConfiguration actuatorName)
    {
        actuators.add(actuatorName);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<ActuatorConfiguration> getActuators()
    {
        return actuators;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public ActuatorConfiguration getActuatorByName(GraphName name)
    {
        for (ActuatorConfiguration ac : actuators)
        {
            if (ac.getName().equals(name))
            {
                return ac;
            }
        }

        return null;
    }
}
