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

import org.apache.tapestry5.hibernate.HibernateSessionManager;

import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.TaskState;

/**
 * The task repository implementation.
 */
public class TaskRepositoryImpl implements TaskRepository
{
    private static final String ID = "id";
    private static final String TASK_STATE = "taskState";

    private HibernateSessionManager sessionManager;

    /**
     * @param sessionManager the Hibernate session manager.
     */
    public TaskRepositoryImpl(HibernateSessionManager sessionManager)
    {
        this.sessionManager = sessionManager;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> findAllIncompleteTasks()
    {
        return sessionManager.getSession()
            .createQuery("FROM Task "
                + "WHERE taskState != :completed AND taskState != :executed "
                + "ORDER BY executionStart, order, creationTime", Task.class)
            .setParameter("completed", TaskState.COMPLETED)
            .setParameter("executed", TaskState.EXECUTED)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countAllIncompleteTasks()
    {
        return sessionManager.getSession()
            .createQuery("SELECT COUNT(*) FROM Task "
                + "WHERE taskState != :completed AND taskState != :executed", Long.class)
            .setParameter("completed", TaskState.COMPLETED)
            .setParameter("executed", TaskState.EXECUTED)
            .getSingleResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task findTaskById(int taskId)
    {
        return sessionManager.getSession()
            .createQuery("FROM Task WHERE id = :id", Task.class)
            .setParameter(ID, taskId)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getPendingTasks()
    {
        return sessionManager.getSession()
            .createQuery("FROM Task WHERE taskState = :pending ORDER BY creationTime", Task.class)
            .setParameter("pending", TaskState.PENDING)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Task> getScheduledTasks()
    {
        return sessionManager.getSession()
            .createQuery("FROM Task WHERE taskState = :scheduled ORDER BY order", Task.class)
            .setParameter("scheduled", TaskState.SCHEDULED)
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getCurrentRunningTask()
    {
        return sessionManager.getSession()
            .createQuery("FROM Task WHERE taskState = :taskState", Task.class)
            .setParameter(TASK_STATE, TaskState.RUNNING)
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unlinkTasksFromVirtualVehicleById(Integer id)
    {
        List<Task> taskList = sessionManager.getSession()
            .createQuery("SELECT t FROM Task t "
                + "LEFT OUTER JOIN VirtualVehicle v ON t.vehicle.id = v.id "
                + "WHERE v.id = :id", Task.class)
            .setParameter(ID, id)
            .list();

        for (Task task : taskList)
        {
            if (task.getTaskState() == TaskState.COMPLETED)
            {
                task.setVehicle(null);
                sessionManager.getSession().update(task);
            }
            else
            {
                task.getSensors().clear();
                sessionManager.getSession().update(task);
                sessionManager.getSession().delete(task);
            }
        }
    }
}
