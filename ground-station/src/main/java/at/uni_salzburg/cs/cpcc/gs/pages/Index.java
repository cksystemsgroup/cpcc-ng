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

package at.uni_salzburg.cs.cpcc.gs.pages;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.slf4j.Logger;

import at.uni_salzburg.cs.cpcc.core.entities.Device;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicleState;
import at.uni_salzburg.cs.cpcc.core.services.QueryManager;
import cpcc.rv.base.services.StateSynchronizer;

/**
 * Index
 */
public class Index
{
    @Inject
    private QueryManager qm;

    @Inject
    private StateSynchronizer stateSyncService;

    @Inject
    private Logger logger;

    /**
     * @return the list of devices.
     */
    public List<Device> getDeviceList()
    {
        return qm.findAllDevices();
    }

    @Property
    private RealVehicleState stateString;

    /**
     * @return the state list.
     */
    public Collection<RealVehicleState> getStateList()
    {
        return Collections.emptyList();
    }

    /**
     * Event RvSync received.
     */
    public void onRvSync()
    {
        logger.info("onRvSync");
        stateSyncService.realVehicleStatusUpdate();
    }

    /**
     * Event ConfigSync received.
     */
    @CommitAfter
    public void onConfigSync()
    {
        logger.info("onConfigSync");
        stateSyncService.pushConfiguration();
    }
}
