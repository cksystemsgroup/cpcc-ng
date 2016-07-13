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

package cpcc.vvrte.services.task;

import java.util.ArrayList;
import java.util.List;

import org.apache.tapestry5.ioc.ServiceResources;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.hibernate.Session;
import org.slf4j.Logger;

import cpcc.core.entities.PolarCoordinate;
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
    private ServiceResources serviceResources;

    /**
     * @param scheduler the task scheduling algorithm.
     * @param logger the application logger.
     * @param session the database session.
     * @param taskRepository the task repository.
     */
    public TaskSchedulerServiceImpl(@Symbol(VvRteConstants.PROP_SCHEDULER_CLASS_NAME) String scheduler, Logger logger,
        Session session, TaskRepository taskRepository, ServiceResources serviceResources)
    {
        this.logger = logger;
        this.session = session;
        this.taskRepository = taskRepository;
        this.serviceResources = serviceResources;

        try
        {
            setAlgorithm(scheduler);
        }
        catch (ClassNotFoundException e)
        {
            logger.error("Can not load default scheduling algorithm: " + scheduler, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Task schedule(PolarCoordinate position, List<PolarCoordinate> depotPositions)
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

        if (!pendingTasks.isEmpty() && algorithm.schedule(position, depotPositions, scheduledTasks, pendingTasks))
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
    public void setAlgorithm(String className) throws ClassNotFoundException
    {
        Class<?> clazz = Class.forName(className);
        algorithm = (TaskSchedulingAlgorithm) serviceResources.getService(clazz);
    }
}
