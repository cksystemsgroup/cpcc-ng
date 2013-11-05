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

import java.sql.Clob;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Vehicle
 */
@Entity
public class Vehicle
{
    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 36)
    private String uuid;
    
    @Lob
    private Clob code;
    
    @NotNull
    @Enumerated(EnumType.STRING)
    private VehicleState state;
    

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
     * @return the universally unique identifier
     */
    public String getUuid()
    {
        return uuid;
    }
    
    /**
     * @param uuid the universally unique identifier to set
     */
    public void setUuid(String uuid)
    {
        this.uuid = uuid;
    }
    
    /**
     * @return the code
     */
    public Clob getCode()
    {
        return code;
    }
    
    /**
     * @param code the code to set
     */
    public void setCode(Clob code)
    {
        this.code = code;
    }
    
    /**
     * @return the state
     */
    public VehicleState getState()
    {
        return state;
    }
    
    /**
     * @param state the state to set
     */
    public void setState(VehicleState state)
    {
        this.state = state;
    }
}
