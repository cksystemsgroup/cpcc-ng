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
package at.uni_salzburg.cs.cpcc.vvrte.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;
import org.hibernate.util.SerializationHelper;
import org.mozilla.javascript.ScriptableObject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * VirtualVehicleStorage
 */
@Entity
public class VirtualVehicleStorage
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @ManyToOne
    private VirtualVehicle virtualVehicle;

    @Type(type = "timestamp")
    private java.util.Date modificationTime;

    @Size(max = 128)
    private String name;

    @Lob
    private byte[] content;

    /**
     * @return the identification.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the identification to set.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the virtual vehicle.
     */
    public VirtualVehicle getVirtualVehicle()
    {
        return virtualVehicle;
    }

    /**
     * @param virtualVehicle the virtual vehicle to set.
     */
    public void setVirtualVehicle(VirtualVehicle virtualVehicle)
    {
        this.virtualVehicle = virtualVehicle;
    }

    /**
     * @return the last modification time.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public java.util.Date getModificationTime()
    {
        return modificationTime;
    }

    /**
     * @param modificationTime the last modification time to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setModificationTime(java.util.Date modificationTime)
    {
        this.modificationTime = modificationTime;
    }

    /**
     * @return the name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the name to set.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the storage item's content.
     */
    public ScriptableObject getContent()
    {
        return (ScriptableObject) SerializationHelper.deserialize(content);
    }

    /**
     * @param content the storage item's content to set.
     */
    public void setContent(ScriptableObject content)
    {
        this.content = SerializationHelper.serialize(content);
    }
    
    /**
     * @return the content as an array of bytes.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public byte[] getContentAsByteArray()
    {
        return content;
    }
    
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setContentAsByteArray(byte[] newContent)
    {
        this.content = newContent;
    }
}
