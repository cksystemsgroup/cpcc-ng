// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.core.services;

import java.util.List;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;

/**
 * Real Vehicle Repository.
 */
public interface RealVehicleRepository
{
    /**
     * @return the list of available real vehicles
     */
    List<RealVehicle> findAllRealVehicles();

    /**
     * @return the list of active (not deleted) real vehicles
     */
    List<RealVehicle> findAllActiveRealVehicles();

    /**
     * @return the list of available ground stations.
     */
    List<RealVehicle> findAllGroundStations();

    /**
     * @return the list of available real vehicles except own real vehicle.
     */
    List<RealVehicle> findAllRealVehiclesExceptOwn();

    /**
     * @return the list of available real vehicles ordered by name.
     */
    List<RealVehicle> findAllRealVehiclesOrderByName();

    /**
     * @param name the real vehicle name.
     * @return the real vehicle or null if not found.
     */
    RealVehicle findRealVehicleByName(String name);

    /**
     * @param url the real vehicle's URL.
     * @return the real vehicle or null if not found.
     */
    RealVehicle findRealVehicleByUrl(String url);

    /**
     * @param id the real vehicle identification.
     * @return the real vehicle or null if not found.
     */
    RealVehicle findRealVehicleById(Integer id);

    /**
     * @return the real vehicle carting myself.
     */
    RealVehicle findOwnRealVehicle();

    /**
     * @return the list of real vehicle state entries for each real vehicle.
     */
    List<RealVehicleState> findAllRealVehicleStates();

    /**
     * @param id the real vehicle identification.
     * @return the real vehicle state or null if not found.
     */
    RealVehicleState findRealVehicleStateById(int id);

    /**
     * Cleanup old real vehicle states.
     */
    void cleanupOldVehicleStates();

    /**
     * @param id the real vehicle identification.
     * @return true, if a connection to the real vehicle is possible.
     */
    boolean isRealVehicleConnected(int id);

}
