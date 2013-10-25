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
package at.uni_salzburg.cs.cpcc.ros.services;

import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import at.uni_salzburg.cs.cpcc.persistence.entities.Device;
import at.uni_salzburg.cs.cpcc.persistence.entities.MappingAttributes;
import at.uni_salzburg.cs.cpcc.ros.base.AbstractRosAdapter;
import at.uni_salzburg.cs.cpcc.ros.sim.RosNodeGroup;

/**
 * RosNodeService
 */
public interface RosNodeService
{
    /**
     * @param uri the master server URI.
     */
    void updateMasterServerURI(URI uri);
    
    /**
     * @param internal true if an internal <code>RosCore</code> should be used, false otherwise.
     */
    void updateRosCore(boolean internal);
    
    /**
     * @param device the device.
     */
    void updateDevice(Device device);
    
    /**
     * @param device the device.
     */
    void shutdownDevice(Device device);
    
    /**
     * @param mappings the mappings
     */
    void updateMappingAttributes(Collection<MappingAttributes> mappings);
    
    /**
     * @param mappings the mappings
     */
    void shutdownMappingAttributes(Collection<MappingAttributes> mappings);
    
    /**
     * @return the device nodes.
     */
    Map<String, RosNodeGroup> getDeviceNodes();
    
    /**
     * @return the adapter nodes.
     */
    Map<String, List<AbstractRosAdapter>> getAdapterNodes();
    
    /**
     * @param topic the ROS topic.
     * @return the adapter node, or null if not found.
     */
    AbstractRosAdapter getAdapterNodeByTopic(String topic);
}
