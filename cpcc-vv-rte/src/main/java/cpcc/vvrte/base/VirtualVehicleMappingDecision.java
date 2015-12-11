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

package cpcc.vvrte.base;

import java.util.List;

import cpcc.core.entities.RealVehicle;
import cpcc.vvrte.entities.Task;

/**
 * VirtualVehicleMappingDecision
 */
public class VirtualVehicleMappingDecision
{
    private boolean migration;
    private Task task;
    private List<RealVehicle> realVehicles;

    /**
     * @return the migration
     */
    public boolean isMigration()
    {
        return migration;
    }

    /**
     * @param newMigration the migration to set
     * @return this instance.
     */
    public VirtualVehicleMappingDecision setMigration(boolean newMigration)
    {
        this.migration = newMigration;
        return this;
    }

    /**
     * @return the task
     */
    public Task getTask()
    {
        return task;
    }

    /**
     * @param newTask the task to set
     * @return this instance.
     */
    public VirtualVehicleMappingDecision setTask(Task newTask)
    {
        this.task = newTask;
        return this;
    }

    /**
     * @return the real vehicles to migrate to.
     */
    public List<RealVehicle> getRealVehicles()
    {
        return realVehicles;
    }

    /**
     * @param newRealVehicles the desired real vehicles to migrate to.
     * @return this instance.
     */
    public VirtualVehicleMappingDecision setRealVehicles(List<RealVehicle> newRealVehicles)
    {
        this.realVehicles = newRealVehicles;
        return this;
    }
}
