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

/**
 * TimerServiceTest
 */
@Deprecated
public class TimerServiceTest
{
    //    private TimerTask timerTaskA;
    //    private TimerTask timerTaskB;
    //    private TimerService timerService;
    //
    //    @BeforeMethod
    //    public void setUp()
    //    {
    //        timerTaskA = spy(new TimerTask()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                System.out.print("A");
    //            }
    //        });
    //
    //        timerTaskB = spy(new TimerTask()
    //        {
    //            @Override
    //            public void run()
    //            {
    //                System.out.print("B");
    //            }
    //        });
    //
    //        timerService = new TimerServiceImpl();
    //    }
    //
    //    @Test
    //    public void shouldScheduleOneTask() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 1000);
    //        Thread.sleep(100);
    //        verify(timerTaskA).run();
    //    }
    //
    //    @Test
    //    public void shouldScheduleOneTaskPeriodically() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //        Thread.sleep(300);
    //        verify(timerTaskA, times(2)).run();
    //    }
    //
    //    @Test
    //    public void shouldScheduleMultipleTasks() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 1000);
    //        timerService.periodicSchedule(timerTaskB, 0, 1000);
    //        Thread.sleep(100);
    //        verify(timerTaskA).run();
    //        verify(timerTaskB).run();
    //    }
    //
    //    @Test
    //    public void shouldScheduleMultipleTasksPeriodically() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //        timerService.periodicSchedule(timerTaskB, 0, 200);
    //        Thread.sleep(300);
    //        verify(timerTaskA, times(2)).run();
    //        verify(timerTaskB, times(2)).run();
    //    }
    //
    //    @Test
    //    public void shouldCancelOneTask() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //        Thread.sleep(100);
    //        verify(timerTaskA).run();
    //
    //        timerService.cancelSchedule(timerTaskA);
    //
    //        Thread.sleep(200);
    //        verify(timerTaskA).run();
    //    }
    //
    //    @Test
    //    public void shouldCancelMultipleTasks() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //        timerService.periodicSchedule(timerTaskB, 0, 200);
    //        Thread.sleep(100);
    //        verify(timerTaskA).run();
    //        verify(timerTaskB).run();
    //
    //        timerService.cancelSchedule(timerTaskA);
    //        timerService.cancelSchedule(timerTaskB);
    //
    //        Thread.sleep(300);
    //        verify(timerTaskA).run();
    //        verify(timerTaskB).run();
    //    }
    //
    //    @Test
    //    public void shouldTerminateAllTimerTasksAtGarbageCollection() throws Exception
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //        timerService.periodicSchedule(timerTaskB, 0, 200);
    //        Thread.sleep(100);
    //        verify(timerTaskA).run();
    //        verify(timerTaskB).run();
    //
    //        Method finalize = TimerServiceImpl.class.getDeclaredMethod("finalize");
    //        finalize.setAccessible(true);
    //        finalize.invoke(timerService);
    //
    //        Thread.sleep(300);
    //        verify(timerTaskA).run();
    //        verify(timerTaskB).run();
    //    }
    //
    //    @Test
    //    public void shouldScheduleOneTaskOnlyOnce() throws InterruptedException
    //    {
    //        timerService.periodicSchedule(timerTaskA, 0, 200);
    //
    //        try
    //        {
    //            timerService.periodicSchedule(timerTaskA, 0, 1000);
    //            Fail.failBecauseExceptionWasNotThrown(IllegalArgumentException.class);
    //        }
    //        catch (IllegalArgumentException e)
    //        {
    //            assertThat(e).hasMessage("TimerTask already has a schedule.");
    //        }
    //    }
    //
    //    @Test
    //    public void shouldNotCancelUnscheduledTask()
    //    {
    //        timerService.cancelSchedule(timerTaskA);
    //        verifyZeroInteractions(timerTaskA);
    //    }
}
