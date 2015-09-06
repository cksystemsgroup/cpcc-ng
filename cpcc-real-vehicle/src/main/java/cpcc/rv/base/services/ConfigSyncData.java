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

package cpcc.rv.base.services;

import java.util.List;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.core.entities.SensorDefinition;

import com.owlike.genson.annotation.JsonProperty;

/**
 * ConfigSyncData
 */
public class ConfigSyncData
{
    private List<SensorDefinition> sen;
    private List<RealVehicle> rvs;

    /**
     * @param sen the list of sensor definitions.
     * @param rvs the list of real vehicles.
     */
    public ConfigSyncData(@JsonProperty("sen") List<SensorDefinition> sen
        , @JsonProperty("rvs") List<RealVehicle> rvs)
    {
        this.sen = sen;
        this.rvs = rvs;
    }

    /**
     * @return the sensor definition list.
     */
    public List<SensorDefinition> getSen()
    {
        return sen;
    }

    /**
     * @param sen the sensor definition list to set
     */
    public void setSen(List<SensorDefinition> sen)
    {
        this.sen = sen;
    }

    /**
     * @return the the real vehicle list
     */
    public List<RealVehicle> getRvs()
    {
        return rvs;
    }

    /**
     * @param rvs the real vehicle list to set.
     */
    public void setRvs(List<RealVehicle> rvs)
    {
        this.rvs = rvs;
    }

}
