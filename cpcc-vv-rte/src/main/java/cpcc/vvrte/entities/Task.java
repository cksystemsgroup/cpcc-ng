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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.internal.util.SerializationHelper;
import org.mozilla.javascript.ScriptableObject;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.SensorDefinition;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Task
 */
@Entity(name = "tasks")
public class Task implements Serializable
{
    private static final long serialVersionUID = -3100648860303007085L;

    @Id
    @GeneratedValue
    private Integer id;

    @Embedded
    private PolarCoordinate position;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "task_state")
    private TaskState taskState = TaskState.INIT;

    @NotNull
    @Column(name = "task_order")
    private int order = 0;

    @NotNull
    @Column(name = "tolerance")
    private double tolerance = 5.0f;

    @Column(name = "distance_to_target")
    private Double distanceToTarget = null;

    @NotNull
    @Column(name = "creation_time")
    private Date creationTime = new Date();

    @Column(name = "execution_start")
    private Date executionStart;

    @Column(name = "execution_end")
    private Date executionEnd;

    @Lob
    @Column(name = "sensor_values")
    private byte[] sensorValues;

    @ManyToMany(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    @JoinTable(name = "tasks_sensors"
        , joinColumns = {@JoinColumn(name = "task_id")}
        , inverseJoinColumns = {@JoinColumn(name = "sensor_id")})
    private List<SensorDefinition> sensors = new ArrayList<>();

    @OneToOne(cascade = CascadeType.DETACH, fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "id")
    private VirtualVehicle vehicle;

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
     * @return the position.
     */
    public PolarCoordinate getPosition()
    {
        return position;
    }

    /**
     * @param position the position to set.
     */
    public void setPosition(PolarCoordinate position)
    {
        this.position = position;
    }

    /**
     * @return the task state.
     */
    public TaskState getTaskState()
    {
        return taskState;
    }

    /**
     * @param taskState the task state to set.
     */
    public void setTaskState(TaskState taskState)
    {
        this.taskState = taskState;
    }

    /**
     * @return the scheduling order number of this task.
     */
    public int getOrder()
    {
        return order;
    }

    /**
     * @param order the order number to set.
     */
    public void setOrder(int order)
    {
        this.order = order;
    }

    /**
     * @return the tolerance distance
     */
    public double getTolerance()
    {
        return tolerance;
    }

    /**
     * @param tolerance the tolerance distance to set
     */
    public void setTolerance(double tolerance)
    {
        this.tolerance = tolerance;
    }

    /**
     * @return the distance to the target position.
     */
    public Double getDistanceToTarget()
    {
        return distanceToTarget;
    }

    /**
     * @param distanceToTarget the distance to the target position to set.
     */
    public void setDistanceToTarget(Double distanceToTarget)
    {
        this.distanceToTarget = distanceToTarget;
    }

    /**
     * @return the creation date.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getCreationTime()
    {
        return creationTime;
    }

    /**
     * @param creationTime the creation time to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setCreationTime(Date creationTime)
    {
        this.creationTime = creationTime;
    }

    /**
     * @return the execution start time.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getExecutionStart()
    {
        return executionStart;
    }

    /**
     * @param executionStart the execution start time to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setExecutionStart(Date executionStart)
    {
        this.executionStart = executionStart;
    }

    /**
     * @return the execution end time.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public Date getExecutionEnd()
    {
        return executionEnd;
    }

    /**
     * @param executionEnd the execution end time to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setExecutionEnd(Date executionEnd)
    {
        this.executionEnd = executionEnd;
    }

    /**
     * @return the sensors
     */
    public List<SensorDefinition> getSensors()
    {
        return sensors;
    }

    /**
     * @return the sensor values.
     */
    public ScriptableObject getSensorValues()
    {
        return (ScriptableObject) SerializationHelper.deserialize(sensorValues);
    }

    /**
     * @param sensorValues the sensor values to set.
     */
    public void setSensorValues(ScriptableObject sensorValues)
    {
        this.sensorValues = SerializationHelper.serialize(sensorValues);
    }

    /**
     * @return the vehicle.
     */
    public VirtualVehicle getVehicle()
    {
        return vehicle;
    }

    /**
     * @param vehicle the vehicle to set.
     */
    public void setVehicle(VirtualVehicle vehicle)
    {
        this.vehicle = vehicle;
    }

}
