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
package at.uni_salzburg.cs.cpcc.commons.pages.rv;

import static org.apache.tapestry5.EventConstants.ACTIVATE;

import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;

/**
 * Real Vehicle List
 */
public class RvList
{
    @Inject
    private QueryManager qm;
    
    @Property
    private List<RealVehicle> realVehicleList;
    
    @Property
    private RealVehicle realVehicle;
    
    @OnEvent(ACTIVATE)
    void loadSensorList()
    {
        realVehicleList = qm.findAllRealVehicles();
    }
    
    @OnEvent("deleteRealVehicle")
    @CommitAfter
    void deleteRealVehicle(Integer id)
    {
        RealVehicle rv = qm.findRealVehicleById(id);
        qm.delete(rv);
    }
}
