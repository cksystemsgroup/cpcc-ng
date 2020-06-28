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
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Device
 */
@Entity
@Table(name = "device_types", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
public class DeviceType implements Serializable
{
    private static final long serialVersionUID = -3688447694342721194L;

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @OneToOne
    private Topic mainTopic;

    @Size(max = 120)
    private String className;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "device_types_topics"
        , joinColumns = {@JoinColumn(name = "device_types_id")}
        , inverseJoinColumns = {@JoinColumn(name = "subtopics_id")})
    private List<Topic> subTopics = new ArrayList<>();

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
     * @return the device name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the device name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the main topic
     */
    public Topic getMainTopic()
    {
        return mainTopic;
    }

    /**
     * @param mainTopic the main topic to set
     */
    public void setMainTopic(Topic mainTopic)
    {
        this.mainTopic = mainTopic;
    }

    /**
     * @return the className
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @param className the className to set
     */
    public void setClassName(String className)
    {
        this.className = className;
    }

    /**
     * @return the sub topics.
     */
    public List<Topic> getSubTopics()
    {
        return subTopics;
    }

    /**
     * @param subTopics the sub topics.
     */
    public void setSubTopics(List<Topic> subTopics)
    {
        this.subTopics = subTopics;
    }

}