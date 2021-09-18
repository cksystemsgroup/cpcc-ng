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

package cpcc.core.services.jobs;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.Date;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class TimeServiceTest
{
    private TimeServiceImpl sut;

    @BeforeEach
    void setUp()
    {
        sut = new TimeServiceImpl();
    }

    static Stream<Arguments> sequenceDataProvider()
    {
        return Stream.of(
            arguments(0),
            arguments(1),
            arguments(2),
            arguments(3),
            arguments(4),
            arguments(5),
            arguments(6),
            arguments(7),
            arguments(8),
            arguments(9));
    }

    @ParameterizedTest
    @MethodSource("sequenceDataProvider")
    void shouldCreateDateObjects(int number)
    {
        Date expected = new Date();
        Date actual = sut.newDate();

        assertThat(actual).isNotNull();
        assertThat(actual.getTime() - expected.getTime()).describedAs("time accuracy").isLessThan(10L);
    }

    @ParameterizedTest
    @MethodSource("sequenceDataProvider")
    void shouldReturnTheCurrentTime(int number)
    {
        long expected = System.currentTimeMillis();
        long actual = sut.currentTimeMillis();

        assertThat(actual - expected).describedAs("time accuracy").isLessThan(10L);
    }
}
