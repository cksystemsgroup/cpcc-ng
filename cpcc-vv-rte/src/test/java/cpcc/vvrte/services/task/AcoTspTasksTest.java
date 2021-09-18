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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.PolarCoordinate;
import cpcc.vvrte.entities.Task;

/**
 * AcoTspTasksTest implementation.
 */
class AcoTspTasksTest
{
    private AcoTspTasks sut;

    @BeforeEach
    void setUp()
    {
        sut = new AcoTspTasks();
    }

    static Stream<Arguments> pathDataProvider()
    {
        Task taskA = mock(Task.class);
        when(taskA.getPosition()).thenReturn(new PolarCoordinate(47.1, 13.2, 5.0));
        when(taskA.toString()).thenReturn("taskA");

        Task taskB = mock(Task.class);
        when(taskB.getPosition()).thenReturn(new PolarCoordinate(47.9, 13.45, 5.0));
        when(taskB.toString()).thenReturn("taskB");

        Task taskC = mock(Task.class);
        when(taskC.getPosition()).thenReturn(new PolarCoordinate(47.8, 13.2, 5.0));
        when(taskC.toString()).thenReturn("taskC");

        Task taskD = mock(Task.class);
        when(taskD.getPosition()).thenReturn(new PolarCoordinate(47.3, 13.5, 5.0));
        when(taskD.toString()).thenReturn("taskD");

        Task taskE = mock(Task.class);
        when(taskE.getPosition()).thenReturn(new PolarCoordinate(47.3, 13.8, 5.0));
        when(taskE.toString()).thenReturn("taskE");

        Task taskF = mock(Task.class);
        when(taskF.getPosition()).thenReturn(new PolarCoordinate(47.9, 13.7, 5.0));
        when(taskF.toString()).thenReturn("taskF");

        return Stream.of(
            arguments(
                new PolarCoordinate(47.4, 13.2, 5.0),
                Arrays.asList(taskA, taskB, taskC, taskD, taskE, taskF),
                Arrays.asList(taskA, taskD, taskE, taskF, taskB, taskC)),
            arguments(
                new PolarCoordinate(47.4, 13.2, 5.0),
                Arrays.asList(taskA, taskC, taskB, taskD),
                Arrays.asList(taskA, taskD, taskB, taskC)));
    }

    @ParameterizedTest
    @MethodSource("pathDataProvider")
    void shouldCalculateBestPathWithoutDepot(PolarCoordinate position, List<Task> path, List<Task> expected)
    {
        List<Task> actual = sut.calculateBestPath(position, path);

        assertThat(actual).has(new TspListCondition<Task>(expected));
    }

    static Stream<Arguments> pathWithDepotDataProvider()
    {
        Task taskA = mock(Task.class);
        when(taskA.getPosition()).thenReturn(new PolarCoordinate(47.1, 13.2, 5.0));
        when(taskA.toString()).thenReturn("taskA");

        Task taskB = mock(Task.class);
        when(taskB.getPosition()).thenReturn(new PolarCoordinate(47.9, 13.45, 5.0));
        when(taskB.toString()).thenReturn("taskB");

        Task taskC = mock(Task.class);
        when(taskC.getPosition()).thenReturn(new PolarCoordinate(47.8, 13.2, 5.0));
        when(taskC.toString()).thenReturn("taskC");

        Task taskD = mock(Task.class);
        when(taskD.getPosition()).thenReturn(new PolarCoordinate(47.3, 13.5, 5.0));
        when(taskD.toString()).thenReturn("taskD");

        Task taskE = mock(Task.class);
        when(taskE.getPosition()).thenReturn(new PolarCoordinate(47.3, 13.8, 5.0));
        when(taskE.toString()).thenReturn("taskE");

        Task taskF = mock(Task.class);
        when(taskF.getPosition()).thenReturn(new PolarCoordinate(47.9, 13.7, 5.0));
        when(taskF.toString()).thenReturn("taskF");

        return Stream.of(
            arguments(
                new PolarCoordinate(47.4, 13.2, 5.0),
                // new PolarCoordinate(47.75, 13.1, 5.0),
                Arrays.asList(taskA, taskB, taskC, taskD, taskE, taskF),
                Arrays.asList(taskA, taskD, taskE, taskF, taskB, taskC)),
            arguments(
                new PolarCoordinate(47.4, 13.2, 5.0),
                // new PolarCoordinate(47.75, 13.1, 5.0),
                Arrays.asList(taskA, taskC, taskB, taskD),
                Arrays.asList(taskA, taskD, taskB, taskC)));
    }

    @ParameterizedTest
    @MethodSource("pathWithDepotDataProvider")
    void shouldCalculateBestPathWithDepot(PolarCoordinate position, List<Task> path, List<Task> expected)
    {
        List<Task> actual = sut.calculateBestPath(position, path);

        assertThat(actual).has(new TspListCondition<Task>(expected));
    }

    @Test
    void shouldReturnUnchangedPathIfDepotPositionIsNull()
    {
        List<Task> path = new ArrayList<>();

        List<Task> actual = sut.calculateBestPath(null, path);

        assertThat(actual).isSameAs(path);
    }

    static Stream<Arguments> shortPathDataProvider()
    {
        Task taskA = mock(Task.class);
        when(taskA.getPosition()).thenReturn(new PolarCoordinate(47.1, 13.2, 5.0));
        when(taskA.toString()).thenReturn("taskA");

        return Stream.of(
            arguments(Collections.<Task> emptyList()),
            arguments(Arrays.asList(taskA)));
    }

    @ParameterizedTest
    @MethodSource("shortPathDataProvider")
    void shouldReturnUnchangedPathIfPathHasLessThanTwoTasks(List<Task> path)
    {
        List<Task> actual = sut.calculateBestPath(null, path);

        assertThat(actual).isSameAs(path);
    }
}
