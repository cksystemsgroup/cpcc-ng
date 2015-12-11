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

package cpcc.vvrte.task;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Session;
import org.slf4j.Logger;

import cpcc.core.utils.UpdateConsumer;
import cpcc.vvrte.base.VvRteConstants;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.entities.TaskState;
import cpcc.vvrte.services.db.TaskRepository;

/**
 * Task Scheduler Service Implementation.
 */
public class TaskSchedulerServiceImpl implements TaskSchedulerService
{
    private TaskSchedulingAlgorithm algorithm = null;
    private Logger logger;
    private Session session;
    private TaskRepository taskRepository;

    /**
     * @param scheduler the task scheduling algorithm.
     * @param logger the application logger.
     * @param session the database session.
     * @param taskRepository the task repository.
     */
    public TaskSchedulerServiceImpl(@Symbol(VvRteConstants.PROP_DEFAULT_SCHEDULER) String scheduler
        , Logger logger, Session session, TaskRepository taskRepository)
    {
        this.logger = logger;
        this.session = session;
        this.taskRepository = taskRepository;

        try
        {
            setAlgorithm(scheduler);
        }
        catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
            | IllegalAccessException | IllegalArgumentException | InvocationTargetException e)
        {
            logger.error("Can not load default scheduling algorithm: " + scheduler, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task schedule()
    {
        if (algorithm == null)
        {
            logger.error("No TaskSchedulingAlgorithm defined!");
            return null;
        }

        Task currentRunningTask = taskRepository.getCurrentRunningTask();
        if (currentRunningTask != null)
        {
            return currentRunningTask;
        }

        List<Task> scheduledTasks = new ArrayList<>(taskRepository.getScheduledTasks());
        List<Task> pendingTasks = new ArrayList<>(taskRepository.getPendingTasks());

        if (!pendingTasks.isEmpty() && algorithm.schedule(scheduledTasks, pendingTasks))
        {
            int order = 0;
            for (Task task : scheduledTasks)
            {
                task.setOrder(++order);
                task.setTaskState(TaskState.SCHEDULED);
            }
        }

        if (scheduledTasks.isEmpty())
        {
            return null;
        }

        currentRunningTask = scheduledTasks.get(0);
        currentRunningTask.setOrder(0);
        currentRunningTask.setTaskState(TaskState.RUNNING);
        scheduledTasks.stream().forEach(new UpdateConsumer<>(session));
        return currentRunningTask;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setAlgorithm(String className) throws ClassNotFoundException, NoSuchMethodException, SecurityException,
        InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException
    {
        Class<?> clazz = Class.forName(className);
        Constructor<?> ctor = clazz.getDeclaredConstructor(new Class<?>[0]);
        Object instance = ctor.newInstance(new Object[0]);
        algorithm = (TaskSchedulingAlgorithm) instance;
    }
}
