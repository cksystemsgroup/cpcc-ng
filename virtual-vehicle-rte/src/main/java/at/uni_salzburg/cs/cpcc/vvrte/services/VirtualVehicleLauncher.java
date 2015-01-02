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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.io.IOException;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;

/**
 * VirtualVehicleLauncher
 */
public interface VirtualVehicleLauncher
{
    /**
     * @param vehicle the vehicle to launch.
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    void start(VirtualVehicle vehicle) throws VirtualVehicleLaunchException, IOException;
    
    /**
     * @param vehicle the vehicle to launch.
     * @throws VirtualVehicleLaunchException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    void resume(VirtualVehicle vehicle) throws VirtualVehicleLaunchException, IOException;

    /**
     * @param id the virtual vehicle ID
     * @return the associated worker, or null.
     */
//    JavascriptWorker findWorkerByVirtualVehicleId(Integer id);

}
