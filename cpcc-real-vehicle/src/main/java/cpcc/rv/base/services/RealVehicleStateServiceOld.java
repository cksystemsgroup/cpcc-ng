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

package cpcc.rv.base.services;

import java.util.Collection;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;

/**
 * RealVehicleStateService
 */
public interface RealVehicleStateServiceOld
{
    /**
     * Update the state of the active real vehicles.
     */
    @CommitAfter
    void realVehicleStatusUpdate();

    /**
     * Reload the data from the database.
     */
    void reload();

    /**
     * @return the list of real vehicle status information.
     */
    Collection<RealVehicleState> getRealVehicleStatus();

    /**
     * @return the list of real vehicles being monitored.
     */
    Collection<RealVehicle> getRealVehicles();

    /**
     * @param listener the real vehicle state listener.
     */
    void addRealVehicleStateListener(RealVehicleStateListener listener);

    /**
     * Inform the service that the configuration has been changed.
     */
    void notifyConfigurationChange();
}
