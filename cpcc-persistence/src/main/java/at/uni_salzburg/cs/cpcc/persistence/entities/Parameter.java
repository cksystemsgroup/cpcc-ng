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

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Parameter
 */
@Entity
public class Parameter
{
    public static final String MASTER_SERVER_URI = "masterServerURI";
    public static final String USE_INTERNAL_ROS_CORE = "useInternalRosCore";
    public static final String REAL_VEHICLE_NAME = "realVehicleName";
    public static final String VIRTUAL_VEHICLE_MIGRATION_CHUNK_SIZE = "virtualVehicleMigChunkSize";
    
    @Id
    @GeneratedValue
    private Integer id;
    
    @NotNull
    @Size(max = 100)
    private String name;
    
    @Size(max = 255)
    private String value;
    
    @NotNull
    private Integer sort;

    /**
     * @return the parameter ID.
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * @param id the parameter ID.
     */
    public void setId(Integer id)
    {
        this.id = id;
    }

    /**
     * @return the parameter name.
     */
    public String getName()
    {
        return name;
    }

    /**
     * @param name the parameter name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /**
     * @return the parameter value.
     */
    public String getValue()
    {
        return value;
    }

    /**
     * @param value the parameter value.
     */
    public void setValue(String value)
    {
        this.value = value;
    }

    /**
     * @return the sorting rank.
     */
    public Integer getSort()
    {
        return sort;
    }

    /**
     * @param sort the sorting rank.
     */
    public void setSort(Integer sort)
    {
        this.sort = sort;
    }
    
}
