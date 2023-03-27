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

package cpcc.core.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * MappingAttributes
 */
@Entity
@Table(name = "mapping_attributes")
public class MappingAttributes implements Serializable
{
    private static final long serialVersionUID = -1681551157732297163L;

    @EmbeddedId
    private MappingAttributesPK pk;

    @Column(name="vv_visible", nullable = false)
    private Boolean vvVisible;

    @Column(name="connected_to_autopilot", nullable = false)
    private Boolean connectedToAutopilot;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private SensorDefinition sensorDefinition;

    /**
     * @return the primary key
     */
    public MappingAttributesPK getPk()
    {
        return pk;
    }

    /**
     * @param pk the primary key to set
     */
    public void setPk(MappingAttributesPK pk)
    {
        this.pk = pk;
    }

    /**
     * @return the true if this mapping is visible to virtual vehicles, false otherwise.
     */
    public Boolean getVvVisible()
    {
        return vvVisible;
    }

    /**
     * @param vvVisible set to true if this mapping is visible to virtual vehicles, use false otherwise.
     */
    public void setVvVisible(Boolean vvVisible)
    {
        this.vvVisible = vvVisible;
    }

    /**
     * @return true, if connected to the autopilot.
     */
    public Boolean getConnectedToAutopilot()
    {
        return connectedToAutopilot;
    }

    /**
     * @param connectedToAutopilot true, if connected to the autopilot.
     */
    public void setConnectedToAutopilot(Boolean connectedToAutopilot)
    {
        this.connectedToAutopilot = connectedToAutopilot;
    }

    /**
     * @return the sensor definition
     */
    public SensorDefinition getSensorDefinition()
    {
        return sensorDefinition;
    }

    /**
     * @param sensorDefinition the sensor definition to set
     */
    public void setSensorDefinition(SensorDefinition sensorDefinition)
    {
        this.sensorDefinition = sensorDefinition;
    }
}
