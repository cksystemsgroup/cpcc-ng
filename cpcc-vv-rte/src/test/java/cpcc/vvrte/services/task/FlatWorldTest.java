// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.CartesianCoordinate;

/**
 * FlatWorldTest implementation.
 */
class FlatWorldTest
{
    private FlatWorld sut;

    @BeforeEach
    void setUp()
    {
        sut = new FlatWorld();
    }

    @Test
    void shouldThrowExceptionOnCallOfRectangularToPolarCoordinatesA()
    {
        try
        {
            sut.rectangularToPolarCoordinates(1.1, 2.2, 3.3);
        }
        catch (IllegalAccessError e)
        {
            assertThat(e).hasMessage("Not allowed in Flat World!");
        }
    }

    @Test
    void shouldThrowExceptionOnCallOfRectangularToPolarCoordinatesB()
    {
        try
        {
            sut.rectangularToPolarCoordinates(null);
        }
        catch (IllegalAccessError e)
        {
            assertThat(e).hasMessage("Not allowed in Flat World!");
        }
    }

    @Test
    void shouldThrowExceptionOnCallOfWalk()
    {
        try
        {
            sut.walk(null, 0, 0, 0);
        }
        catch (IllegalAccessError e)
        {
            assertThat(e).hasMessage("Not allowed in Flat World!");
        }
    }

    @Test
    void shouldThrowExceptionOnCallOfCalculateDistance()
    {
        try
        {
            sut.calculateDistance(null, null);
        }
        catch (IllegalAccessError e)
        {
            assertThat(e).hasMessage("Not allowed in Flat World!");
        }
    }

    static Stream<Arguments> conversionDataProvider()
    {
        return Stream.of(
            arguments(1.1, 2.2, 3.3, new CartesianCoordinate(2.2, 1.1, 3.3)));
    }

    @ParameterizedTest
    @MethodSource("conversionDataProvider")
    void shouldConvertPolarToRectangularCoordinatesA(double latitude, double longitude, double altitude,
        CartesianCoordinate expected)
    {
        CartesianCoordinate actual = sut.polarToRectangularCoordinates(latitude, longitude, altitude);

        assertThat(actual.getX()).isEqualTo(longitude, offset(1E-9));
        assertThat(actual.getY()).isEqualTo(latitude, offset(1E-9));
        assertThat(actual.getZ()).isEqualTo(altitude, offset(1E-9));
    }

    @ParameterizedTest
    @MethodSource("conversionDataProvider")
    void shouldConvertPolarToRectangularCoordinatesB(double latitude, double longitude, double altitude,
        CartesianCoordinate expected)
    {
        PolarCoordinate pos = new PolarCoordinate(latitude, longitude, altitude);

        CartesianCoordinate actual = sut.polarToRectangularCoordinates(pos);

        assertThat(actual.getX()).isEqualTo(longitude, offset(1E-9));
        assertThat(actual.getY()).isEqualTo(latitude, offset(1E-9));
        assertThat(actual.getZ()).isEqualTo(altitude, offset(1E-9));
    }
}
