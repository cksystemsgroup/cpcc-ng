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

package cpcc.rv.web.pages;

import java.util.List;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.slf4j.Logger;

import cpcc.core.entities.Device;
import cpcc.core.services.QueryManager;

/**
 * Start page of real-vehicle web application.
 */
public class Index
{
    @Inject
    private QueryManager qm;

//    @Inject
//    private StateSynchronizer stateSyncService;

    @Inject
    private Logger logger;

    /**
     * @return the list of devices.
     */
    public List<Device> getDeviceList()
    {
        return qm.findAllDevices();
    }
}
