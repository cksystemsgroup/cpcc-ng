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
package at.uni_salzburg.cs.cpcc.rv.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import at.uni_salzburg.cs.cpcc.ros.services.RosNodeType;

/**
 * Topic
 */
@Entity
public class Topic
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RosNodeType nodeType;

    @Size(max = 50)
    private String subpath;

    @NotNull
    @Size(max = 50)
    private String messageType;

    /**
     * @return the topic ID.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the topic ID.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the node type.
     */
    public RosNodeType getNodeType()
    {
        return nodeType;
    }

    /**
     * @param nodeType the node type.
     */
    public void setNodeType(RosNodeType nodeType)
    {
        this.nodeType = nodeType;
    }

    /**
     * @return the topic sub-path.
     */
    public String getSubpath()
    {
        return subpath;
    }

    /**
     * @param subpath the topic sub-path.
     */
    public void setSubpath(String subpath)
    {
        this.subpath = subpath;
    }

    /**
     * @return the message type.
     */
    public String getMessageType()
    {
        return messageType;
    }

    /**
     * @param messageType the message type.
     */
    public void setMessageType(String messageType)
    {
        this.messageType = messageType;
    }
    
}
