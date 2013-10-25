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
package at.uni_salzburg.cs.cpcc.persistence.entities;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Device
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"topicRoot"})})
public class Device implements ITreeNode
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String topicRoot;

    @NotNull
    @OneToOne
    private DeviceType type;

    @Size(max = 255)
    private String configuration;

    /**
     * @return the device ID.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the device ID.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the topic root path
     */
    public String getTopicRoot()
    {
        return topicRoot;
    }

    /**
     * @param topicRoot the topic root path to set
     */
    public void setTopicRoot(String topicRoot)
    {
        this.topicRoot = topicRoot;
    }

    /**
     * @return the device type.
     */
    public DeviceType getType()
    {
        return type;
    }

    /**
     * @param type the device type.
     */
    public void setType(DeviceType type)
    {
        this.type = type;
    }

    /**
     * @return the device configuration string.
     */
    public String getConfiguration()
    {
        return configuration;
    }

    /**
     * @param configuration the device configuration string.
     */
    public void setConfiguration(String configuration)
    {
        this.configuration = configuration;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasChildren()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ITreeNode> getChildren()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getLabel()
    {
        return topicRoot;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getParentLabel()
    {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUniqueId()
    {
        return "device:" + getId();
    }
}
