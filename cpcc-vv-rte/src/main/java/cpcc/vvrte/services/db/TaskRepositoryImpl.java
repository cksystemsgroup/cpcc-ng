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
    private static final String ID = "id";
    private static final String CREATION_TIME = "creationTime";
    private static final String ORDER = "order";
    private static final String EXECUTION_START = "executionStart";
    private static final String TASK_STATE = "taskState";

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
            .add(Restrictions.not(Restrictions.eq(TASK_STATE, TaskState.COMPLETED)))
            .add(Restrictions.not(Restrictions.eq(TASK_STATE, TaskState.EXECUTED)))
            .addOrder(Order.desc(EXECUTION_START))
            .addOrder(Order.desc(ORDER))
            .addOrder(Order.desc(CREATION_TIME))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long countAllIncompleteTasks()
    {
        return (Long) session.createCriteria(Task.class)
            .add(Restrictions.not(Restrictions.eq(TASK_STATE, TaskState.COMPLETED)))
            .add(Restrictions.not(Restrictions.eq(TASK_STATE, TaskState.EXECUTED)))
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
            .add(Restrictions.eq(ID, taskId))
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
            .add(Restrictions.eq(TASK_STATE, TaskState.PENDING))
            .addOrder(Order.asc(CREATION_TIME))
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
            .add(Restrictions.eq(TASK_STATE, TaskState.SCHEDULED))
            .addOrder(Order.asc(ORDER))
            .list();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task getCurrentRunningTask()
    {
        return (Task) session.createCriteria(Task.class)
            .add(Restrictions.eq(TASK_STATE, TaskState.RUNNING))
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
            .add(Restrictions.eq(ID, id))
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
