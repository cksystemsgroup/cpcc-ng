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

package at.uni_salzburg.cs.cpcc.vvrte.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Vehicle
 */
@Entity
public class VirtualVehicle implements Serializable
{
    private static final long serialVersionUID = 8974799441897681454L;

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 36)
    private String uuid;

    @Size(max = 36)
    private String name;

    @Column(nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer apiVersion;

    @Lob
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    private VirtualVehicleState state;

    @ManyToOne
    private RealVehicle migrationDestination;

    @Type(type = "timestamp")
    private java.util.Date migrationStartTime;

    @Lob
    private byte[] continuation;

    @Type(type = "timestamp")
    private java.util.Date startTime;

    @Type(type = "timestamp")
    private java.util.Date endTime;

    @Column(name = "state_info")
    @Lob
    private String stateInfo;

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
     * @return the JavaScript API version
     */
    public Integer getApiVersion()
    {
        return apiVersion;
    }

    /**
     * @param apiVersion the JavaScript API version to set
     */
    public void setApiVersion(Integer apiVersion)
    {
        this.apiVersion = apiVersion;
    }

    /**
     * @return the code
     */
    public String getCode()
    {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(String code)
    {
        this.code = code;
    }

    /**
     * @return the state
     */
    public VirtualVehicleState getState()
    {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(VirtualVehicleState state)
    {
        this.state = state;
    }

    /**
     * @return the real vehicle to migrate to or null.
     */
    public RealVehicle getMigrationDestination()
    {
        return migrationDestination;
    }

    /**
     * @param migrationDestination the real vehicle to migrate to.
     */
    public void setMigrationDestination(RealVehicle migrationDestination)
    {
        this.migrationDestination = migrationDestination;
    }

    /**
     * @return the start time of the current migration.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public java.util.Date getMigrationStartTime()
    {
        return migrationStartTime;
    }

    /**
     * @param migrationStartTime set the start time of the current migration.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setMigrationStartTime(java.util.Date migrationStartTime)
    {
        this.migrationStartTime = migrationStartTime;
    }

    /**
     * @return the continuation
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public byte[] getContinuation()
    {
        return continuation;
    }

    /**
     * @param continuation the continuation to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setContinuation(byte[] continuation)
    {
        this.continuation = continuation;
    }

    /**
     * @return the start time
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public java.util.Date getStartTime()
    {
        return startTime;
    }

    /**
     * @param startTime the start time to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setStartTime(java.util.Date startTime)
    {
        this.startTime = startTime;
    }

    /**
     * @return the end time
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public java.util.Date getEndTime()
    {
        return endTime;
    }

    /**
     * @param endTime the end time to set
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setEndTime(java.util.Date endTime)
    {
        this.endTime = endTime;
    }

    /**
     * @return the state info.
     */
    public String getStateInfo()
    {
        return stateInfo;
    }

    /**
     * @param stateInfo the state info to set.
     */
    public void setStateInfo(String stateInfo)
    {
        this.stateInfo = stateInfo;
    }
}
