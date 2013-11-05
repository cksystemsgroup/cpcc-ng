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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import at.uni_salzburg.cs.cpcc.vvrte.entities.Vehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VehicleState;

/**
 * VehicleLauncherImpl
 */
public class VehicleLauncherImpl implements VehicleLauncher
{
    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Vehicle vehicle) throws VehicleLaunchException
    {
        // TODO Auto-generated method stub

        VehicleState state = vehicle.getState();

        if (state != VehicleState.INIT)
        {
            throw new VehicleLaunchException("Expected vehicle in state " + VehicleState.INIT + ", but got " + state);
        }
        
        if (!state.canTraverseTo(VehicleState.RUNNING))
        {
            throw new VehicleLaunchException("Can not switch vehicle to state " + VehicleState.RUNNING);
        }

        vehicle.setState(state.traverse(VehicleState.RUNNING));
        
    }
}
