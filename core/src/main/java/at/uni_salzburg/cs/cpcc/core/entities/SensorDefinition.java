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

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * SensorDefinition
 */
@Entity
public class SensorDefinition
{
    @GeneratedValue(generator = "UniqueIntegerIdGenerator")
    @GenericGenerator(name = "UniqueIntegerIdGenerator",
        strategy = "at.uni_salzburg.cs.cpcc.core.services.UniqueIntegerIdGenerator")
    @Id
    private Integer id;

    @NotNull
    @Size(max = 1024)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SensorType type;

    @Size(max = 1024)
    private String parameters;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SensorVisibility visibility;

    @NotNull
    @Type(type = "timestamp")
    private java.util.Date lastUpdate;

    @Size(max = 50)
    private String messageType;

    @NotNull
    private Boolean deleted;

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
     * @return the description
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * @return the type
     */
    public SensorType getType()
    {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(SensorType type)
    {
        this.type = type;
    }

    /**
     * @return the parameters
     */
    public String getParameters()
    {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(String parameters)
    {
        this.parameters = parameters;
    }

    /**
     * @return the visibility
     */
    public SensorVisibility getVisibility()
    {
        return visibility;
    }

    /**
     * @param visibility the visibility to set
     */
    public void setVisibility(SensorVisibility visibility)
    {
        this.visibility = visibility;
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
     * @return the ROS message type
     */
    public String getMessageType()
    {
        return messageType;
    }

    /**
     * @param messageType the ROS message type to set
     */
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }

    /**
     * @return true if this record is to be considered as deleted.
     */
    public Boolean getDeleted()
    {
        return deleted;
    }

    /**
     * @param deleted set to true if this record is to be considered as deleted.
     */
    public void setDeleted(Boolean deleted)
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

        if (!(obj instanceof SensorDefinition))
        {
            return false;
        }

        SensorDefinition other = (SensorDefinition) obj;

        if (this == other)
        {
            return true;
        }

        if (!getDescription().equals(other.getDescription()))
        {
            return false;
        }

        if (getId().intValue() != other.getId().intValue())
        {
            return false;
        }

        if (getType() != other.getType())
        {
            return false;
        }

        if (!StringUtils.equals(getParameters(), other.getParameters()))
        {
            return false;
        }

        if (getVisibility() != other.getVisibility())
        {
            return false;
        }

        if (!StringUtils.equals(getMessageType(), other.getMessageType()))
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        return (id != null ? id.hashCode() : 0) * 41
            + (description != null ? description.hashCode() : 0) * 37
            + (type != null ? type.ordinal() + 1 : 0) * 31
            + (parameters != null ? parameters.hashCode() : 0) * 29
            + (visibility != null ? visibility.ordinal() + 1 : 0) * 23
            + (messageType != null ? messageType.hashCode() : 0) * 19;
    }

    @Override
    public String toString()
    {
        return "(id=" + id + ", description=" + description + ", type=" + type
            + ", lastUpdate=" + lastUpdate.getTime() + ", parameters=" + parameters
            + ", visibility=" + visibility + ", messageType=" + messageType + ")";
    }
}
