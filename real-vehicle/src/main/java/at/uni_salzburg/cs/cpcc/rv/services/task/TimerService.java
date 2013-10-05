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
package at.uni_salzburg.cs.cpcc.rv.services.task;

import java.util.TimerTask;

/**
 * TimerService
 */
public interface TimerService
{
    /**
     * @param timerTask the task to be executed periodically.
     * @param cycleTime the invocation cycle time.
     */
    void periodicSchedule(TimerTask timerTask, long cycleTime);

    /**
     * @param timerTask the periodic task to be canceled.
     */
    void cancelSchedule(TimerTask timerTask);
}
