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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;

/**
 * VirtualVehicleMappingDecision
 */
public class VirtualVehicleMappingDecision
{
    private boolean migration;
    private Task task;
    private RealVehicle realVehicle;

    /**
     * @return the migration
     */
    public boolean isMigration()
    {
        return migration;
    }

    /**
     * @param migration the migration to set
     */
    public void setMigration(boolean migration)
    {
        this.migration = migration;
    }

    /**
     * @return the task
     */
    public Task getTask()
    {
        return task;
    }

    /**
     * @param task the task to set
     */
    public void setTask(Task task)
    {
        this.task = task;
    }

    /**
     * @return the real vehicle to migrate to.
     */
    public RealVehicle getRealVehicle()
    {
        return realVehicle;
    }

    /**
     * @param realVehicle the desired real vehicle to migrate to.
     */
    public void setRealVehicle(RealVehicle realVehicle)
    {
        this.realVehicle = realVehicle;
    }
}
