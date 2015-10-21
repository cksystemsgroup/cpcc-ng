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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

import java.util.Arrays;
import java.util.List;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.utils.PolarCoordinate;
import cpcc.vvrte.task.Task;

/**
 * TaskTest
 */
public class TaskTest
{
    Task sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new Task();
    }

    @DataProvider
    public Object[][] positionDataProvider()
    {
        return new Object[][]{
            new Object[]{new PolarCoordinate(37.1234, -122.0898, 0.0)},
            new Object[]{new PolarCoordinate(37.1234, 122.0898, 100.0)},
            new Object[]{new PolarCoordinate(-37.1234, -122.0898, -100.0)},
            new Object[]{new PolarCoordinate(-37.1234, 122.0898, 1.0)},
        };
    };

    @Test(dataProvider = "positionDataProvider")
    public void shouldStorePosition(PolarCoordinate position)
    {
        sut.setPosition(position);
        assertThat(sut.getPosition()).isEqualTo(position);
    }

    @DataProvider
    public Object[][] distanceDataProvider()
    {
        return new Object[][]{
            new Object[]{3.9},
            new Object[]{null},
            new Object[]{3.333},
            new Object[]{7.1111},
            new Object[]{8.3},
        };
    };

    @Test(dataProvider = "distanceDataProvider")
    public void shouldStoreDistanceToTarget(Double expected)
    {
        sut.setDistanceToTarget(expected);
        assertThat(sut.getDistanceToTarget()).isEqualTo(expected);
    }

    @Test
    public void shouldHaveDefaultCreationTime()
    {
        long now = System.currentTimeMillis();
        assertThat(now - sut.getCreationTime()).isGreaterThanOrEqualTo(0).isLessThan(1000);
    }

    @DataProvider
    public Object[][] timeDataProvider()
    {
        return new Object[][]{
            new Object[]{-1L},
            new Object[]{1381003668231L},
            new Object[]{1000000000000L},
            new Object[]{2222222222222L},
            new Object[]{1213141516171L},
        };
    };

    @Test(dataProvider = "timeDataProvider")
    public void shouldStoreCreationTime(long time)
    {
        sut.setCreationTime(time);
        assertThat(sut.getCreationTime()).isEqualTo(time);
    }

    @DataProvider
    public static Object[][] toleranceDistanceDataProvider()
    {
        return new Object[][]{
            new Object[]{-1.0, 3.0},
            new Object[]{1.0, 3.0},
            new Object[]{2.0, 3.0},
            new Object[]{2.9, 3.0},
            new Object[]{3.0, 3.0},
            new Object[]{3.1, 3.1},
            new Object[]{10, 10.0},
        };
    }

    @Test(dataProvider = "toleranceDistanceDataProvider")
    public void shouldStoreTolerance(double tolerance, double expectedTolerance)
    {
        sut.setTolerance(tolerance);
        assertThat(sut.getTolerance()).isEqualTo(expectedTolerance);
    }

    @Test
    public void shouldHaveDefaultForLastInTaskGroup()
    {
        assertThat(sut.isLastInTaskGroup()).isTrue();
    }

    @DataProvider
    public static Object[][] booleanDataProvider()
    {
        return new Object[][]{
            new Object[]{Boolean.FALSE},
            new Object[]{Boolean.TRUE},
        };
    }

    @Test(dataProvider = "booleanDataProvider")
    public void shouldStoreIsLastInTaskGroup(boolean lastInTaskGroup)
    {
        sut.setLastInTaskGroup(lastInTaskGroup);
        assertThat(sut.isLastInTaskGroup()).isEqualTo(lastInTaskGroup);
    }

    @DataProvider
    public Object[][] sensorListDataProvider()
    {
        return new Object[][]{
            new Object[]{Arrays.asList(mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class))},
            new Object[]{Arrays.asList(mock(SensorDefinition.class), mock(SensorDefinition.class),
                mock(SensorDefinition.class), mock(SensorDefinition.class))},
        };
    }

    @Test(dataProvider = "sensorListDataProvider")
    public void shouldStoreSensorList(List<SensorDefinition> sensorList)
    {
        sut.setSensors(sensorList);
        assertThat(sut.getSensors()).isNotNull().hasSize(sensorList.size());
        assertThat(sut.getSensors()).containsExactly(sensorList.toArray(new SensorDefinition[0]));
    }

    @Test
    public void shouldWaitForCompletion()
    {
        TaskFinisher finisher = new TaskFinisher(sut, 1000);
        finisher.start();

        long start = System.nanoTime();
        sut.awaitCompletion();
        long end = System.nanoTime();

        assertThat(end - start).isGreaterThan(900000000);
        assertThat(end - start).isLessThanOrEqualTo(1100000000);
    }

    @Test
    public void shouldNotWaitForCompletionOfFinishedTask()
    {
        sut.setCompleted();

        TaskFinisher finisher = new TaskFinisher(sut, 1000);
        finisher.start();

        long start = System.nanoTime();
        sut.awaitCompletion();
        long end = System.nanoTime();

        assertThat(sut.isCompleted()).isTrue();
        assertThat(end - start).isLessThanOrEqualTo(100000000);
    }

    /**
     * TaskFinisher
     */
    private class TaskFinisher extends Thread
    {
        private Task task;
        private long time;

        /**
         * @param task the task
         * @param time the waiting time
         */
        public TaskFinisher(Task task, long time)
        {
            this.task = task;
            this.time = time;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            try
            {
                Thread.sleep(time);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
            task.setCompleted();
        }
    }

    //    private boolean completed = false;
    //    private Thread waitingThread;
}
