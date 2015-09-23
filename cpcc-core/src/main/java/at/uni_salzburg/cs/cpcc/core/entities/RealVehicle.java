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

package at.uni_salzburg.cs.cpcc.core.entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * RealVehicle
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
public class RealVehicle implements Serializable
{
    private static final long serialVersionUID = 1765647234477466288L;

    @GeneratedValue(generator = "UniqueIntegerIdGenerator")
    @GenericGenerator(name = "UniqueIntegerIdGenerator",
        strategy = "at.uni_salzburg.cs.cpcc.core.services.UniqueIntegerIdGenerator")
    @Id
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(30) default 'UNKNOWN'")
    private RealVehicleType type;

    @NotNull
    @Size(max = 1024)
    private String url;

    @Lob
    private String areaOfOperation;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SensorDefinition> sensors = new ArrayList<SensorDefinition>();

    @NotNull
    @Type(type = "timestamp")
    private java.util.Date lastUpdate;

    @NotNull
    private boolean deleted;

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
     * @return the name
     */
    public String getName()
    {
        return name;
    }

    /**
     * @return the real vehicle type.
     */
    public RealVehicleType getType()
    {
        return type;
    }

    /**
     * @param type the real vehicle type to set.
     */
    public void setType(RealVehicleType type)
    {
        this.type = type;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the URL
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @param url the URL to set
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * @return the area of operation as JSON string
     */
    public String getAreaOfOperation()
    {
        return areaOfOperation;
    }

    /**
     * @param areaOfOperation the area of operation to set as a JSON string
     */
    public void setAreaOfOperation(String areaOfOperation)
    {
        this.areaOfOperation = areaOfOperation;
    }

    /**
     * @return the sensors
     */
    public List<SensorDefinition> getSensors()
    {
        return sensors;
    }

    /**
     * @param sensors the sensors to set
     */
    public void setSensors(List<SensorDefinition> sensors)
    {
        this.sensors = sensors;
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

    /**
     * @return true if this record is to be considered as deleted.
     */
    public boolean getDeleted()
    {
        return deleted;
    }

    /**
     * @param deleted set to true if this record is to be considered as deleted.
     */
    public void setDeleted(boolean deleted)
    {
        this.deleted = deleted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null)
        {
            return false;
        }

        if (!(obj instanceof RealVehicle))
        {
            return false;
        }

        RealVehicle other = (RealVehicle) obj;

        if (this == other)
        {
            return true;
        }

        return equalsHelperOne(other);
    }

    /**
     * @param obj the other object
     * @return true on equality, false otherwise.
     */
    private boolean equalsHelperOne(RealVehicle other)
    {
        if (getId().intValue() != other.getId().intValue())
        {
            return false;
        }

        if (getType() != other.getType())
        {
            return false;
        }

        if (getLastUpdate().getTime() != other.getLastUpdate().getTime())
        {
            return false;
        }

        return equalsHelperTwo(other);
    }

    /**
     * @param other the other
     * @return true on equality, false otherwise.
     */
    private boolean equalsHelperTwo(RealVehicle other)
    {
        if (getSensors() != null && other.getSensors() != null && getSensors().size() != other.getSensors().size())
        {
            return false;
        }

        if (!getName().equals(other.getName()))
        {
            return false;
        }

        if (getSensors() == null && other.getSensors() != null)
        {
            return false;
        }

        if (getSensors() != null && other.getSensors() == null)
        {
            return false;
        }

        return equalsHelperThree(other);
    }

    /**
     * @param other the other
     * @return true on equality, false otherwise.
     */
    private boolean equalsHelperThree(RealVehicle other)
    {
        if (!getUrl().equals(other.getUrl()))
        {
            return false;
        }

        if (!getAreaOfOperation().equals(other.getAreaOfOperation()))
        {
            return false;
        }

        if (!getSensors().containsAll(other.getSensors()))
        {
            return false;
        }

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        int hc = (id != null ? id.hashCode() : 0) * 41
            + (name != null ? name.hashCode() : 0) * 37
            + (type != null ? type.hashCode() : 0) * 31
            + (url != null ? url.hashCode() : 0) * 29
            + (areaOfOperation != null ? areaOfOperation.hashCode() : 0) * 23
            + (lastUpdate != null ? lastUpdate.hashCode() : 0) * 19;

        if (getSensors() != null)
        {
            for (SensorDefinition sd : getSensors())
            {
                hc = hc * 59 + sd.getId();
            }
        }

        return hc;
    }

    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        b.append("(id=").append(id != null ? id : -1)
            .append(", name=").append(name)
            .append(", type=").append(type)
            .append(", url=").append(url)
            .append(", areaOfOperation=").append(areaOfOperation)
            .append(", lastUpdate=").append(lastUpdate != null ? lastUpdate.getTime() : -1L)
            .append(", deleted=").append(deleted)
            .append(", sensors=[");

        for (int k = 0, l = getSensors() != null ? getSensors().size() : 0; k < l; ++k)
        {
            if (k > 0)
            {
                b.append(", ");
            }
            b.append(getSensors().get(k).getId());
        }

        b.append("]");
        return b.toString();
    }
}
