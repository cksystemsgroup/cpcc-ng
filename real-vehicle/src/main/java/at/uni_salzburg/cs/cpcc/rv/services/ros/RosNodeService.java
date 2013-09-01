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
package at.uni_salzburg.cs.cpcc.rv.services.ros;

import java.net.URI;

import at.uni_salzburg.cs.cpcc.rv.entities.Device;

/**
 * RosNodeService
 */
public interface RosNodeService
{
    /**
     * @return the master server URI.
     */
    URI getMasterServerURI();
    
    /**
     * @param uri the master server URI.
     */
    void setMasterServerURI(URI uri);
    
    /**
     * @return true if an internal <code>RosCore</code> is used, false otherwise.
     */
    boolean isInternalRosCore();
    
    /**
     * @param internal true if an internal <code>RosCore</code> should be used, false otherwise.
     */
    void setInternalRosCore(boolean internal);
    
    /**
     * @param device the device.
     */
    void startDeviceDriver(Device device);
    
    /**
     * @param device the device.
     */
    void stopDeviceDriver(Device device);
    
    
}
