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
package at.uni_salzburg.cs.cpcc.vvrte.task;

import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TimerServiceImpl
 */
public class TimerServiceImpl implements TimerService
{
//    private Map<TimerTask, Timer> schedules = new HashMap<TimerTask, Timer>();

    private Set<TimerTask> schedules = new HashSet<TimerTask>();
    private Timer timer = new Timer();
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void periodicSchedule(TimerTask timerTask, long cycleTime)
    {
        if (schedules.contains(timerTask))
        {
            throw new IllegalArgumentException("TimerTask already has a schedule.");
        }
        timer.schedule(timerTask, 0, cycleTime);
        schedules.add(timerTask);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void cancelSchedule(TimerTask timerTask)
    {
        if (schedules.contains(timerTask))
        {
            schedules.remove(timerTask);
            timerTask.cancel();
            timer.purge();
        }
    }
    
    /**
     * Cancel all schedules and clear the timer list.
     */
    private void cleanup()
    {
        for (TimerTask task : schedules)
        {
            task.cancel();
        }
        schedules.clear();
        timer.purge();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finalize() throws Throwable
    {
        cleanup();
        super.finalize();
    }
}
