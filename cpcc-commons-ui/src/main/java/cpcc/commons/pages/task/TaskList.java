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

package cpcc.commons.pages.task;

import java.text.DecimalFormat;
import java.text.Format;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.Property;

import cpcc.commons.services.SensorDescriptionListFormat;
import cpcc.vvrte.entities.Task;
import cpcc.vvrte.services.db.TaskRepository;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Tasks page
 */
public class TaskList
{
    @Property
    @Inject
    private TaskRepository taskRepo;

    @Property
    private Task currentTask;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "The template uses this formatter.")
    @Property
    private Format sensorFormat;

    @SuppressFBWarnings(value = "URF_UNREAD_FIELD", justification = "The template uses this formatter.")
    @Property
    private Format distanceFormat;

    void onActivate()
    {
        sensorFormat = new SensorDescriptionListFormat();
        distanceFormat = new DecimalFormat("0.0  m");
    }
}
