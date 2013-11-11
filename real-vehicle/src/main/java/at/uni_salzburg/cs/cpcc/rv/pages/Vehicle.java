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
package at.uni_salzburg.cs.cpcc.rv.pages;

import static org.apache.tapestry5.EventConstants.ACTIVATE;

import java.util.Collection;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.services.VvRteRepository;

/**
 * Vehicles
 */
public class Vehicle
{
    @Inject
    private VvRteRepository repository;

    @Property
    private Collection<VirtualVehicle> virtualVehicleList;

    @Property
    private VirtualVehicle virtualVehicle;

    @OnEvent(ACTIVATE)
    void loadParameters()
    {
        virtualVehicleList = repository.findAllVehicles();
    }

    @OnEvent("deleteVehicle")
    @CommitAfter
    void deleteDevice(Integer id)
    {
        System.out.println("deleteVehicle " + id);
        
        VirtualVehicle vehicle = repository.findVirtualVehicleById(id);
        repository.delete(vehicle);
    }

    @OnEvent("startVehicle")
    @CommitAfter
    void startVehicle(Integer id)
    {
        System.out.println("startVehicle " + id);
    }

    @OnEvent("pauseVehicle")
    @CommitAfter
    void pauseVehicle(Integer id)
    {
        System.out.println("pauseVehicle " + id);
    }

    @OnEvent("stopVehicle")
    @CommitAfter
    void stopVehicle(Integer id)
    {
        System.out.println("stopVehicle " + id);
    }
}
