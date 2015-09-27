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

package cpcc.commons.pages;

import java.text.DecimalFormat;
import java.text.Format;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.Messages;

import cpcc.commons.services.MillisecondTimeFormat;
import cpcc.commons.services.SensorDescriptionListFormat;
import cpcc.vvrte.task.Task;
import cpcc.vvrte.task.TaskExecutionService;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Tasks page
 */
public class Tasks
{
    private static final String DATE_FORMAT = "dateFormat";

    @Inject
    private Messages messages;

    @Property
    @Inject
    private TaskExecutionService tes;

    @Property
    private Task currentRunningTask;

    @Property
    private Task scheduledTask;

    @Property
    private Task pendingTask;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Tasks.tml uses this formatter.")
    @Property
    private Format timeFormat;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Tasks.tml uses this formatter.")
    @Property
    private Format sensorFormat;
    
    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "Tasks.tml uses this formatter.")
    @Property
    private Format distanceFormat;
    
    
    void onActivate()
    {
        timeFormat = new MillisecondTimeFormat(messages.get(DATE_FORMAT));
        sensorFormat = new SensorDescriptionListFormat();
        distanceFormat = new DecimalFormat("0.0  m");
    }

    /**
     * @return the current active task as a list.
     */
    public List<Task> getCurrentActiveTaskList()
    {
        return tes.getCurrentRunningTask() != null
            ? Arrays.asList(tes.getCurrentRunningTask())
            : Collections.<Task> emptyList();
    }

    /**
     * @param task the task.
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

        return StringUtils.join(a, ", ");
    }
}
