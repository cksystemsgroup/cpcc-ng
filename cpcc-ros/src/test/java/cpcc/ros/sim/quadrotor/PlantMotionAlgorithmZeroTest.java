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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;

/**
 * PlantMotionAlgorithmZeroTest implementation.
 */
public class PlantMotionAlgorithmZeroTest
{
    private Logger logger;

    @BeforeEach
    public void setUp()
    {
        logger = mock(Logger.class);
    }

    static Stream<Arguments> takeOffDataProvider()
    {
        return Stream.of(
            arguments(0.0, 0.0, 0.0, 2.4),
            arguments(25.0, 500.0, 30.0, 0.0),
            arguments(50.0, 1000.0, 0.0, -2.4));
    }

    @ParameterizedTest
    @MethodSource("takeOffDataProvider")
    public void shouldTakeOff(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.TAKE_OFF, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("takeOffDataProvider")
    public void shouldLand(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.LAND, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyNoInteractions(logger);
    }

    @ParameterizedTest
    @MethodSource("takeOffDataProvider")
    public void shouldFly(double time, double expDistance, double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.FLIGHT, 1000.0, 30.0, 5.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(50.0, offset(1E-6));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-6));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-6));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-6));

        verifyNoInteractions(logger);
    }

    @Test
    public void shouldReduceAccelerationOnQuickFlights()
    {
        PlantMotionAlgorithmZero sut = new PlantMotionAlgorithmZero(logger, State.FLIGHT, 100.0, 10.0, 1.0);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(24.494897, offset(1E-6));

        verify(logger).warn("{}: maximum acceleration reduced from {} m/s^2 to {} m/s^2",
            State.FLIGHT, 2.6666666666666665, 1.0);

        verify(logger).warn("{}: total time prolonged from {} s to {} s",
            State.FLIGHT, 15.0, 24.49489742783178);

        verify(logger).warn("{}: maximum velocity reduced from {} m/s to {} m/s",
            State.FLIGHT, 10.0, 6.123724356957945);
    }
}
