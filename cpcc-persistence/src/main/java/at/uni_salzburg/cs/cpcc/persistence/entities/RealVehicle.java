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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * RealVehicle
 */
@Entity
@Table(uniqueConstraints = {@UniqueConstraint(columnNames = {"url"})})
public class RealVehicle
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    @Size(max = 255)
    private String url;

    @NotNull
    private String areaOfOperation;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<SensorDefinition> sensors = new ArrayList<SensorDefinition>();

    @NotNull
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
     * @return the name
     */
    public String getName()
    {
        return name;
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
}
