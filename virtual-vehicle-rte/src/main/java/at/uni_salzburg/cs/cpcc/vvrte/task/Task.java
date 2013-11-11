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

import at.uni_salzburg.cs.cpcc.persistence.entities.SensorDefinition;
import at.uni_salzburg.cs.cpcc.utilities.PolarCoordinate;

/**
 * Task
 */
public class Task
{
    private PolarCoordinate position;
    private long creationTime;
    private List<SensorDefinition> sensors;

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
}
