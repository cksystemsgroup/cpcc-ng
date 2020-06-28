// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services.js;

import cpcc.vvrte.base.VirtualVehicleMappingDecision;
import cpcc.vvrte.entities.Task;

/**
 * Virtual Vehicle application state.
 */
public class ApplicationState
{
    private VirtualVehicleMappingDecision decision;
    private Task task;

    /**
     * @param decision the Virtual Vehicle mapping decision.
     */
    public ApplicationState(VirtualVehicleMappingDecision decision)
    {
        this.decision = decision;
    }

    /**
     * @param task the task to keep.
     */
    public ApplicationState(Task task)
    {
        this.task = task;
    }

    /**
     * @return true if this application state contains a {@code VirtualVehicleMappingDecision}.
     */
    public boolean isMappingDecision()
    {
        return decision != null;
    }

    /**
     * @return the {@code VirtualVehicleMappingDecision} or null, if not set.
     */
    public VirtualVehicleMappingDecision getDecision()
    {
        return decision;
    }

    /**
     * @return true if this application state contains a {@code Task}.
     */
    public boolean isTask()
    {
        return task != null;
    }

    /**
     * @return the task or null, if not set.
     */
    public Task getTask()
    {
        return task;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        if (decision != null)
        {
            return "Decision: " + decision.toString();
        }

        return isTask()
            ? "Task: " + task
            : "Application state unknown!";
    }
}
