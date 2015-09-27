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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * RealVehicleState
 */
@Entity
@Table(name = "real_vehicle_state")
public class RealVehicleState implements Serializable
{
    private static final long serialVersionUID = -965159833673755971L;

    @Id
    private Integer id;

    @Column(name = "real_vehicle_name", nullable = false)
    private String realVehicleName;

    @Lob
    private String state;

    @Type(type = "timestamp")
    private java.util.Date lastUpdate;

    /**
     * @return the id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the real vehicle name.
     */
    public String getRealVehicleName()
    {
        return realVehicleName;
    }

    /**
     * @param realVehicleName the real vehicle name to set.
     */
    public void setRealVehicleName(String realVehicleName)
    {
        this.realVehicleName = realVehicleName;
    }

    /**
     * @return the real vehicle state as a JSON string.
     */
    public String getState()
    {
        return state;
    }

    /**
     * @param state the real vehicle state to set as a JSON string
     */
    public void setState(String state)
    {
        this.state = state;
    }

    /**
     * @return the last update time stamp
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public java.util.Date getLastUpdate()
    {
        return lastUpdate;
    }

    /**
     * @param lastUpdate the last update time stamp to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setLastUpdate(java.util.Date lastUpdate)
    {
        this.lastUpdate = lastUpdate;
    }
}
