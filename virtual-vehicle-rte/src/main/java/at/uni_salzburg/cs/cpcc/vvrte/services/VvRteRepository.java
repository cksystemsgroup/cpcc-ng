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

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;

/**
 * VvRteRepository
 */
public interface VvRteRepository
{
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
}
