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

package cpcc.vvrte.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.Type;

import cpcc.core.entities.RealVehicle;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Vehicle
 */
@Entity
@Table(name = "virtual_vehicles")
public class VirtualVehicle implements Serializable
{
    private static final long serialVersionUID = 8974799441897681454L;

    private static final int MAX_STATE_LENGTH = 30;

    @Id
    @GeneratedValue
    private Integer id;

    @NotNull
    @Size(max = 36)
    private String uuid;

    @Size(max = 36)
    private String name;

    @Column(name = "api_version", nullable = false, columnDefinition = "INTEGER DEFAULT 1")
    private Integer apiVersion;

    @Lob
    private String code;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(length = MAX_STATE_LENGTH)
    private VirtualVehicleState state;

    @Enumerated(EnumType.STRING)
    @Column(name = "pre_migration_state", length = MAX_STATE_LENGTH)
    private VirtualVehicleState preMigrationState;

    @ManyToOne
    private RealVehicle migrationDestination;

    @ManyToOne
    private RealVehicle migrationSource;

    @Column(name = "chunk_number")
    private Integer chunkNumber;

    @Type(type = "timestamp")
    @Column(name = "migration_start_time")
    private java.util.Date migrationStartTime;

    @Lob
    private byte[] continuation;

    @Type(type = "timestamp")
    @Column(name = "start_time")
    private java.util.Date startTime;

    @Type(type = "timestamp")
    @Column(name = "end_time")
    private java.util.Date endTime;

    @Type(type = "timestamp")
    @Column(name = "update_time")
    private java.util.Date updateTime;

    @Column(name = "state_info")
    @Lob
    private String stateInfo;

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "task_id", referencedColumnName = "id")
    private Task task;

    @Transient
    private boolean selected;

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
     * @return the state before migration started.
     */
    public VirtualVehicleState getPreMigrationState()
    {
        return preMigrationState;
    }

    /**
     * @param preMigrationState the state before migration started to set.
     */
    public void setPreMigrationState(VirtualVehicleState preMigrationState)
    {
        this.preMigrationState = preMigrationState;
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
     * @return the real vehicle to migrate from or null.
     */
    public RealVehicle getMigrationSource()
    {
        return migrationSource;
    }

    /**
     * @param migrationSource the real vehicle to migrate from.
     */
    public void setMigrationSource(RealVehicle migrationSource)
    {
        this.migrationSource = migrationSource;
    }

    /**
     * @return the migration chunk number or null.
     */
    public Integer getChunkNumber()
    {
        return chunkNumber;
    }

    /**
     * @param chunkNumber the migration chunk number to set.
     */
    public void setChunkNumber(Integer chunkNumber)
    {
        this.chunkNumber = chunkNumber;
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
     * @return
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "This is exposed on purpose")
    public java.util.Date getUpdateTime()
    {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "This is exposed on purpose")
    public void setUpdateTime(java.util.Date updateTime)
    {
        this.updateTime = updateTime;
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

    /**
     * @return the currently running task.
     */
    public Task getTask()
    {
        return task;
    }

    /**
     * @param task the currently running task to set.
     */
    public void setTask(Task task)
    {
        this.task = task;
    }

    /**
     * @return true if this virtual vehicle has been selected, false otherwise.
     */
    public boolean isSelected()
    {
        return selected;
    }

    /**
     * @param selected the selected value to set.
     */
    public void setSelected(boolean selected)
    {
        this.selected = selected;
    }
}
