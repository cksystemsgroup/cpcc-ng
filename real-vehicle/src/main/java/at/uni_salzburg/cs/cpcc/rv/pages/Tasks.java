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
package at.uni_salzburg.cs.cpcc.rv.pages;

import static org.apache.tapestry5.EventConstants.ACTIVATE;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;

import at.uni_salzburg.cs.cpcc.core.utils.StringUtilities;
import at.uni_salzburg.cs.cpcc.vvrte.task.Task;
import at.uni_salzburg.cs.cpcc.vvrte.task.TaskExecutionService;

/**
 * Tasks page
 */
public class Tasks
{
    @Inject
    protected Messages messages;

    @Property
    @Inject
    private TaskExecutionService tes;

    @Property
    private Task currentRunningTask;

    @Property
    private Task scheduledTask;

    @Property
    private Task pendingTask;

    private SimpleDateFormat dateFormatter;

    @OnEvent(ACTIVATE)
    void loadParameters()
    {
        dateFormatter = new SimpleDateFormat(messages.get("dateFormat"));
    }

    /**
     * @return the current active task as a list.
     */
    public List<Task> getCurrentActiveTaskList()
    {
        if (tes.getCurrentRunningTask() == null)
        {
            return new ArrayList<Task>();
        }
        return Arrays.asList(tes.getCurrentRunningTask());
    }

    /**
     * @param time the time in milliseconds since the Epoch.
     * @return the time formatted as string.
     */
    public String getTime(Long time)
    {
        if (time == null)
        {
            return "";
        }
        return dateFormatter.format(new Date(time));
    }

    /**
     * @param task the task in question.
     * @return the sensor descriptions as a string.
     */
    public String getSortedSensorList(Task task)
    {
        if (task == null || task.getSensors().isEmpty())
        {
            return "";
        }

        String[] a = new String[task.getSensors().size()];
        for (int k = 0, l = task.getSensors().size(); k < l; ++k)
        {
            a[k] = task.getSensors().get(k).getDescription();
        }

        Arrays.sort(a);

        return StringUtilities.join(", ", a);
    }
}
