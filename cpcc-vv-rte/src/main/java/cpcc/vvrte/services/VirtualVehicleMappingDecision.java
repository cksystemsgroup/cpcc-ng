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

package cpcc.vvrte.services;

import java.util.List;

import cpcc.core.entities.RealVehicle;
import cpcc.vvrte.task.Task;

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
     * @return the real vehicles to migrate to.
     */
    public List<RealVehicle> getRealVehicles()
    {
        return realVehicles;
    }

    /**
     * @param realVehicles the desired real vehicles to migrate to.
     */
    public void setRealVehicles(List<RealVehicle> realVehicles)
    {
        this.realVehicles = realVehicles;
    }
}
