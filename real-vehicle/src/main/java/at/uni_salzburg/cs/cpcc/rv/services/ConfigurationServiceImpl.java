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

import at.uni_salzburg.cs.cpcc.ros.actuators.AbstractActuator;
import at.uni_salzburg.cs.cpcc.ros.sensors.AbstractSensor;

/**
 * ConfigurationServiceImpl
 */
@Singleton
public class ConfigurationServiceImpl implements ConfigurationService
{
    private URI masterServerUri;
    
    private Set<AbstractSensor> sensors = Collections.synchronizedSet(new HashSet<AbstractSensor>());
    
    private Set<AbstractActuator> actuators = Collections.synchronizedSet(new HashSet<AbstractActuator>());

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
    public void addSensor(AbstractSensor name)
    {
        sensors.add(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractSensor> getSensors()
    {
        return sensors;
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void addActuator(AbstractActuator name)
    {
        actuators.add(name);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public Set<AbstractActuator> getActuators()
    {
        return actuators;
    }
}
