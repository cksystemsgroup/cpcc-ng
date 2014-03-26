/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.core.services;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.fest.assertions.api.Assertions.assertThat;

import java.util.TimerTask;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * TimerServiceTest
 */
public class TimerServiceTest
{
    private TimerServiceImpl timerService;

    @BeforeMethod
    public void setUp()
    {
        timerService = new TimerServiceImpl();
    }

    static int counter1 = 0;
    static long time1 = 0;

    @Test
    public void shouldExecuteTimerTaskPeriodically() throws InterruptedException
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                ++counter1;
                time1 = System.currentTimeMillis();
            }
        };

        long startTime = System.currentTimeMillis();
        timerService.periodicSchedule(timerTask, 0, 500);
        while (counter1 < 5)
        {
            Thread.sleep(50);
        }

        assertThat((time1 - startTime) / 5.0).isLessThan(490.0);
        assertThat(counter1).isEqualTo(5);
    }

    @Test
    public void shouldThrowIAEOnDuplicateScheduleOfTimerTask()
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                // Intentionally empty.
            }
        };

        timerService.periodicSchedule(timerTask, 0, 100);
        catchException(timerService).periodicSchedule(timerTask, 0, 100);

        assertThat(caughtException()).isNotNull().isInstanceOf(IllegalArgumentException.class);
        assertThat(caughtException().getMessage()).isEqualTo("TimerTask already has a schedule.");
    }

    static int counter2 = 0;

    @Test
    public void shouldCancelActiveTask() throws InterruptedException
    {
        TimerTask timerTask = new TimerTask()
        {
            @Override
            public void run()
            {
                ++counter2;
            }
        };

        timerService.periodicSchedule(timerTask, 0, 1000);
        Thread.sleep(200);
        timerService.cancelSchedule(timerTask);
        timerService.cancelSchedule(null);

        assertThat(counter2).isEqualTo(1);
    }
}
