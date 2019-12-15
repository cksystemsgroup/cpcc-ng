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

import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.TaskState;

/**
 * The task repository implementation.
 */
public class TaskRepositoryImpl implements TaskRepository
{
    private Session session;

    /**
     * @param session the Hibernate {@link Session}
     */
    public TaskRepositoryImpl(Session session)
    {
        this.session = session;
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> findAllIncompleteTasks()
    {
        return session.createCriteria(Task.class)
            .add(Restrictions.not(Restrictions.eq("taskState", TaskState.COMPLETED)))
            .add(Restrictions.not(Restrictions.eq("taskState", TaskState.EXECUTED)))
            .addOrder(Order.desc("executionStart"))
            .addOrder(Order.desc("order"))
            .addOrder(Order.desc("creationTime"))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countAllIncompleteTasks()
    {
        return (Long) session.createCriteria(Task.class)
            .add(Restrictions.not(Restrictions.eq("taskState", TaskState.COMPLETED)))
            .add(Restrictions.not(Restrictions.eq("taskState", TaskState.EXECUTED)))
            .setProjection(Projections.rowCount())
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task findTaskById(int taskId)
    {
        return (Task) session.createCriteria(Task.class)
            .add(Restrictions.eq("id", taskId))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> getPendingTasks()
    {
        return session.createCriteria(Task.class)
            .add(Restrictions.eq("taskState", TaskState.PENDING))
            .addOrder(Order.asc("creationTime"))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    @Override
    public List<Task> getScheduledTasks()
    {
        return session.createCriteria(Task.class)
            .add(Restrictions.eq("taskState", TaskState.SCHEDULED))
            .addOrder(Order.asc("order"))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getCurrentRunningTask()
    {
        return (Task) session.createCriteria(Task.class)
            .add(Restrictions.eq("taskState", TaskState.RUNNING))
            .uniqueResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void unlinkTasksFromVirtualVehicleById(Integer id)
    {
        @SuppressWarnings("unchecked")
        List<Task> taskList = session.createCriteria(Task.class, "t")
            .createCriteria("t.vehicle", JoinType.LEFT_OUTER_JOIN)
            .add(Restrictions.eq("id", id))
            .list();

        for (Task task : taskList)
        {
            if (task.getTaskState() == TaskState.COMPLETED)
            {
                task.setVehicle(null);
                session.update(task);
            }
            else
            {
                task.getSensors().clear();
                session.update(task);
                session.delete(task);
            }
        }
    }
}
