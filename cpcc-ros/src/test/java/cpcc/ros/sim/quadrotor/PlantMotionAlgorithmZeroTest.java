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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;

import org.slf4j.Logger;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * PlantMotionAlgorithmZeroTest implementation.
 */
public class PlantMotionAlgorithmZeroTest
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
            new Object[]{0.0, 0.0, 0.0, 2.4},
            new Object[]{25.0, 500.0, 30.0, 0.0},
            new Object[]{50.0, 1000.0, 0.0, -2.4},
        };
    }

    @Test(dataProvider = "takeOffDataProvider")
    public void shouldTakeOff(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.TAKE_OFF, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyZeroInteractions(logger);
    }

    @Test(dataProvider = "takeOffDataProvider")
    public void shouldLand(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.LAND, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyZeroInteractions(logger);
    }

    @Test(dataProvider = "takeOffDataProvider")
    public void shouldFly(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.FLIGHT, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyZeroInteractions(logger);
    }

    @Test
    public void shouldReduceAccelerationOnQuickFlights()
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.FLIGHT, 100.0, 10.0, 1.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(24.494897, offset(1E-6));

        verify(logger).warn("FLIGHT: maximum acceleration reduced from 2.667 m/s^2 to 1.000 m/s^2");
        verify(logger).warn("FLIGHT: total time prolonged from 15.000 s to 24.495 s");
        verify(logger).warn("FLIGHT: maximum velocity reduced from 10.000 m/s to 6.124 m/s");

    }
}
