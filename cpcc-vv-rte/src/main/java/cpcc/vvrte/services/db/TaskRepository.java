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

package cpcc.vvrte.services.db;

import java.util.List;

import cpcc.vvrte.entities.Task;

/**
 * Task repository interface.
 */
public interface TaskRepository
{
    /**
     * @return list of not completed tasks.
     */
    List<Task> findAllIncompleteTasks();

    /**
     * @param taskId the task identification.
     * @return the task according to taskId or null if not found.
     */
    Task findTaskById(int taskId);

    /**
     * @return the currently pending tasks.
     */
    List<Task> getPendingTasks();

    /**
     * @return the tasks currently scheduled for execution.
     */
    List<Task> getScheduledTasks();

    /**
     * @return the task the execution service currently processes.
     */
    Task getCurrentRunningTask();

    /**
     * @param id the Virtual Vehicle identification.
     */
    void unlinkTasksFromVirtualVehicleById(Integer id);
}
