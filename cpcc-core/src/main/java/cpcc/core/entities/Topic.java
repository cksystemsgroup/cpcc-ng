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
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Topic
 */
@Entity
@Table(name = "topics")
public class Topic implements Serializable
{
    private static final long serialVersionUID = -195728103332481446L;

    @Id
    @GeneratedValue
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "node_type", length = 255, nullable = false)
    private RosNodeType nodeType;

    @Column(name = "sub_path", length = 50)
    private String subpath;

    @Column(name = "message_type", length = 50, nullable = false)
    private String messageType;

    @Column(name = "adapter_class_name", length = 120, nullable = false)
    private String adapterClassName;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TopicCategory category;

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

    /**
     * @return the adapter class name
     */
    public String getAdapterClassName()
    {
        return adapterClassName;
    }

    /**
     * @param adapterClassName the adapter class name to set
     */
    public void setAdapterClassName(String adapterClassName)
    {
        this.adapterClassName = adapterClassName;
    }

    /**
     * @return the category
     */
    public TopicCategory getCategory()
    {
        return category;
    }

    /**
     * @param category the category to set
     */
    public void setCategory(TopicCategory category)
    {
        this.category = category;
    }
}
