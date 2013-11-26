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
package at.uni_salzburg.cs.cpcc.vvrte.task;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;

/**
 * Task
 */
public class Task
{
    private static final double MIN_TOLERANCE_DISTANCE = 3.0;

    private static final Logger LOG = LoggerFactory.getLogger(Task.class);

    private PolarCoordinate position;
    private double tolerance = 5.0;
    private long creationTime = System.currentTimeMillis();
    private boolean lastInTaskGroup = true;
    private List<SensorDefinition> sensors;
    private boolean completed = false;
    private Thread waitingThread;
    

    /**
     * @return the task's position.
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
        this.tolerance = tolerance >= MIN_TOLERANCE_DISTANCE ? tolerance : MIN_TOLERANCE_DISTANCE;
    }

    /**
     * @return the creation time.
     */
    public long getCreationTime()
    {
        return creationTime;
    }

    /**
     * @param creationTime the creation time.
     */
    public void setCreationTime(long creationTime)
    {
        this.creationTime = creationTime;
    }
    
    /**
     * @return true if the task is the last in a group.
     */
    public boolean isLastInTaskGroup()
    {
        return lastInTaskGroup;
    }
    
    /**
     * @param lastInTaskGroup the last task in the group to set.
     */
    public void setLastInTaskGroup(boolean lastInTaskGroup)
    {
        this.lastInTaskGroup = lastInTaskGroup;
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
     * @return true if the task has been completed.
     */
    public boolean isCompleted()
    {
        return completed;
    }
    
    /**
     * Set the task to completed.
     */
    public void setCompleted()
    {
        completed = true;

        if (waitingThread != null)
        {
            waitingThread.interrupt();
        }
    }

    /**
     * Await the completion of the task.
     */
    public void awaitCompletion()
    {
        waitingThread = Thread.currentThread();

        while (!completed)
        {
            try
            {
                Thread.sleep(60000);
            }
            catch (InterruptedException e)
            {
                LOG.debug("Task has been completed: " + completed);
            }
        }
    }

}
