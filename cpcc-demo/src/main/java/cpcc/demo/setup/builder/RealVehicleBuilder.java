// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.demo.setup.builder;

import java.util.List;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;

/**
 * RealVehicleBuilder implementation.
 */
public class RealVehicleBuilder
{
    private RealVehicle rv = new RealVehicle();

    /**
     * @param id the id to set
     * @return this instance.
     */
    public RealVehicleBuilder setId(Integer id)
    {
        rv.setId(id);
        return this;
    }

    /**
     * @param type the real vehicle type to set.
     * @return this instance.
     */
    public RealVehicleBuilder setType(RealVehicleType type)
    {
        rv.setType(type);
        return this;
    }

    /**
     * @param name the name to set
     * @return this instance.
     */
    public RealVehicleBuilder setName(String name)
    {
        rv.setName(name);
        return this;
    }

    /**
     * @param url the URL to set
     * @return this instance.
     */
    public RealVehicleBuilder setUrl(String url)
    {
        rv.setUrl(url);
        return this;
    }

    /**
     * @param areaOfOperation the area of operation to set as a JSON string
     * @return this instance.
     */
    public RealVehicleBuilder setAreaOfOperation(String areaOfOperation)
    {
        rv.setAreaOfOperation(areaOfOperation);
        return this;
    }

    /**
     * @param sensors the sensors to set
     * @return this instance.
     */
    public RealVehicleBuilder setSensors(List<SensorDefinition> sensors)
    {
        rv.getSensors().addAll(sensors);
        return this;
    }

    /**
     * @param lastUpdate the last update time stamp to set
     * @return this instance.
     */
    public RealVehicleBuilder setLastUpdate(java.util.Date lastUpdate)
    {
        rv.setLastUpdate(lastUpdate);
        return this;
    }

    /**
     * @param deleted set to true if this record is to be considered as deleted.
     * @return this instance.
     */
    public RealVehicleBuilder setDeleted(boolean deleted)
    {
        rv.setDeleted(deleted);
        return this;
    }

    /**
     * @param state the real vehicle state to set.
     * @return this instance.
     */
    public RealVehicleBuilder setState(RealVehicleState state)
    {
        rv.setState(state);
        return this;
    }

    /**
     * @return the newly built real vehicle.
     */
    public RealVehicle build()
    {
        return rv;
    }
}
