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

import java.util.Collection;
import java.util.List;

import at.uni_salzburg.cs.cpcc.persistence.services.QueryManager;
import at.uni_salzburg.cs.cpcc.persistence.services.Repository;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleStorage;

/**
 * VvRteRepository
 */
public interface VvRteRepository extends Repository
{
    /**
     * @return the query manager.
     */
    QueryManager getQueryManager();

    /**
     * @return the available virtual vehicles.
     */
    List<VirtualVehicle> findAllVehicles();

    /**
     * @param list the database objects to be saved.
     */
    void saveOrUpdateAll(Collection<?> list);

    /**
     * @param o the database object to be saved.
     */
    void saveOrUpdate(Object o);

    /**
     * @param o the database object to be deleted.
     */
    void delete(Object o);

    /**
     * @param list the database objects to be deleted.
     */
    void deleteAll(Collection<?> list);

    /**
     * @param id the virtual vehicle ID
     * @return the virtual vehicle or null, if not found.
     */
    VirtualVehicle findVirtualVehicleById(Integer id);

    /**
     * @param name the virtual vehicle name.
     * @return the virtual vehicle or null, if not found.
     */
    VirtualVehicle findVirtualVehicleByName(String name);

    /**
     * @param uuid the virtual vehicle UUID
     * @return the virtual vehicle or null, if not found.
     */
    VirtualVehicle findVirtualVehicleByUUID(String uuid);

    /**
     * @return all item names of items in the virtual vehicle storage.
     */
    List<String> findAllStorageItemNames();

    /**
     * @param vehicle the associated virtual vehicle.
     * @param name the item name.
     * @return the requested storage item or null, if not found.
     */
    VirtualVehicleStorage findStorageItemByVirtualVehicleAndName(VirtualVehicle vehicle, String name);

    /**
     * @param id the storage item identification.
     * @return the requested storage item or null, if not found.
     */
    VirtualVehicleStorage findStorageItemById(Integer id);

    /**
     * @param id the virtual vehicle identification.
     * @return the requested storage item or null, if not found.
     */
    List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id);

    /**
     * @param id the virtual vehicle identification.
     * @param startName the storage name to start with.
     * @param maxEntries the maximum number of entries to return.
     * @return the requested entries, or null if not found.
     */
    List<VirtualVehicleStorage> findStorageItemsByVirtualVehicle(Integer id, String startName, int maxEntries);
}
