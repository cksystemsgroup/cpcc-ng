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

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;

/**
 * PlantMotionAlgorithmTwoTest implementation.
 */
public class PlantMotionAlgorithmOneTest
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
            // Total Time,  maxV, maxA, Distance, Time, s(t), v(t), a(t)
            arguments(55.333, 20.00, 5.00, 1000.00, -0.001, 0.000, 0.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 0.000, 0.000, 0.000, 5.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 2.767, 16.930, 11.441, 3.271),
            arguments(55.333, 20.00, 5.00, 1000.00, 5.533, 58.897, 18.099, 1.542),
            arguments(55.333, 20.00, 5.00, 1000.00, 6.700, 80.896, 19.472, 0.813),
            arguments(55.333, 20.00, 5.00, 1000.00, 8.00, 106.67, 20.00, 0.00),
            arguments(55.333, 20.00, 5.00, 1000.00, 8.300, 112.667, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 11.067, 168.000, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 13.833, 223.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 16.600, 278.667, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 19.367, 334.000, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 22.133, 389.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 24.900, 444.667, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 27.667, 500.000, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 30.433, 555.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 33.200, 610.667, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 35.967, 666.000, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 38.733, 721.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 41.500, 776.667, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 44.267, 832.000, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 47.033, 887.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 47.333, 893.333, 20.000, 0.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 48.633, 919.098, 19.472, -0.812),
            arguments(55.333, 20.00, 5.00, 1000.00, 49.800, 941.103, 18.099, -1.542),
            arguments(55.333, 20.00, 5.00, 1000.00, 52.567, 983.070, 11.441, -3.271),
            arguments(55.333, 20.00, 5.00, 1000.00, 55.333, 1000.000, 0.000, -5.000),
            arguments(55.333, 20.00, 5.00, 1000.00, 55.334, 1000.000, 0.000, 0.000));
    }

    @ParameterizedTest
    @MethodSource("takeOffDataProvider")
    public void shouldFly(double totalTime, double maxV, double maxA, double distance, double time, double expDistance,
        double expVelocity, double expAcceleration)
    {
        PlantMotionAlgorithmOne sut = new PlantMotionAlgorithmOne(logger, distance, maxV, maxA);

        assertThat(sut.getTotalTime()).describedAs("Total flight time").isEqualTo(totalTime, offset(1E-2));

        assertThat(sut.velocity(time)).describedAs("Velocity").isEqualTo(expVelocity, offset(1E-2));
        assertThat(sut.distance(time)).describedAs("Distance").isEqualTo(expDistance, offset(1E-2));
        assertThat(sut.acceleration(time)).describedAs("Acceleration").isEqualTo(expAcceleration, offset(1E-2));

        verify(logger).info("One: dist={}, maxV={}, maxA={}, totalTime={}, timeOne={}, timeTwo={}",
            distance, maxV, maxA, 55.333333333333336, 8.0, 47.333333333333336);
    }

    @Test
    public void shouldWriteDataToFileA() throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter("target/plantAltgorithmOneA.csv"))
        {
            writer.println("time;distance;velocity;acceleration");

            PlantMotionAlgorithmOne sut = new PlantMotionAlgorithmOne(logger, 1000.0, 20.0, 5.0);

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

    @Test
    public void shouldWriteDataToFileB() throws FileNotFoundException
    {
        try (PrintWriter writer = new PrintWriter("target/plantAltgorithmOneB.csv"))
        {
            writer.println("time;distance;velocity;acceleration");

            PlantMotionAlgorithmOne sut = new PlantMotionAlgorithmOne(logger, 10.0, 20.0, 5.0);

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
