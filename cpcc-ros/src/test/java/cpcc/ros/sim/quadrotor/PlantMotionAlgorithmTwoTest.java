// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.ros.sim.quadrotor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * PlantMotionAlgorithmTwoTest implementation.
 */
public class PlantMotionAlgorithmTwoTest
{
    private Logger logger;

    @BeforeMethod
    public void setUp()
    {
        logger = mock(Logger.class);
    }

    @DataProvider
    public Object[][] takeOffDataProvider()
    {
        return new Object[][]{
            // Total Time,  maxV, maxA, Distance, Time, s(t), v(t), a(t)
            new Object[]{34.641, 50.0, 5.0, 1000.0, -0.001, 0.00, 0.00, 0.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 0.000, 0.00, 0.00, 5.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 1.732, 7.25, 8.227, 4.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 3.464, 28.00, 15.588, 4.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 5.196, 60.75, 22.084, 3.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 6.928, 104.00, 27.713, 3.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 8.660, 156.25, 32.476, 2.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 10.392, 215.98, 36.373, 2.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 12.124, 281.74, 39.404, 1.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 13.856, 351.98, 41.569, 1.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 15.588, 425.23, 42.868, 0.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 17.321, 500.02, 43.301, 0.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 19.053, 574.76, 42.868, -0.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 20.785, 648.01, 41.569, -1.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 22.517, 718.26, 39.404, -1.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 24.249, 784.01, 36.373, -2.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 25.981, 843.75, 32.476, -2.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 27.713, 896.00, 27.713, -3.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 29.445, 939.25, 22.084, -3.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 31.177, 972.00, 15.588, -4.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 32.909, 992.75, 8.227, -4.50},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 34.641, 1000.00, 0.000, -5.00},
            new Object[]{34.641, 50.0, 5.0, 1000.0, 34.642, 1000.00, 0.000, 0.00},
        };
    }

    @Test(dataProvider = "takeOffDataProvider")
    public void shouldFly(double totalTime, double maxV, double maxA, double distance, double time, double expDistance,
        double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmTwo sut = new PlantMotionAlgorithmTwo(logger, distance, maxA);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(totalTime, offset(1E-2));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-2));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-2));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-2));

        verify(logger).info(anyString(), any(), any(), any(), any());
    }

    @Test
    public void shouldWriteDataToFile() throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter("target/plantAltgorithmTwo.csv"))
        {
            writer.println("time;distance;velocity;acceleration");

            PlantMotionAlgorithmTwo sut = new PlantMotionAlgorithmTwo(logger, 1000.0, 5.0);

            double totalTime = sut.getTotalTime();
            double time = 0.0;

            while (time <= totalTime)
            {
                writer.printf("%.3f;%.3f;%.3f;%.3f\n",
                    time, sut.distance(time), sut.velocity(time), sut.acceleration(time));
                time += 0.02;
            }
        }
    }
}
