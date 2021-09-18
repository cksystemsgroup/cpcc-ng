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

package cpcc.rv.base.services;

import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleState;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.services.RealVehicleRepository;
import cpcc.core.services.jobs.TimeService;
import cpcc.vvrte.entities.Task;

class PositionContributorTest
{
    private static final long NOW = 1000000000L;
    private static final long NEW_TIME = NOW - 1L;
    private static final long TIMEOUT_TIME = 1L;

    private TimeService timeService;
    private RealVehicleRepository rvRepo;
    private PositionContributor sut;

    @BeforeEach
    void setUp()
    {
        timeService = mock(TimeService.class);
        when(timeService.currentTimeMillis()).thenReturn(NOW);

        rvRepo = mock(RealVehicleRepository.class);

        sut = new PositionContributor(timeService, rvRepo);
    }

    @Test
    void shouldNotContributeOnNullPosition()
    {
        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, null, null);

        verifyNoInteractions(featureCollection);
    }

    static Stream<Arguments> realVehicleDataProvider()
    {
        RealVehicle rv01 = mock(RealVehicle.class);
        when(rv01.getType()).thenReturn(RealVehicleType.BOAT);
        when(rv01.getId()).thenReturn(12345);
        when(rv01.getName()).thenReturn("RV01");
        when(rv01.toString()).thenReturn("RV01");

        RealVehicleState rvs01 = mock(RealVehicleState.class);
        when(rvs01.getLastUpdate()).thenReturn(new Date(NEW_TIME));
        when(rvs01.toString()).thenReturn("State One");

        RealVehicleState rvs02 = mock(RealVehicleState.class);
        when(rvs02.getLastUpdate()).thenReturn(new Date(TIMEOUT_TIME));
        when(rvs02.toString()).thenReturn("State Two");

        Task task1 = mock(Task.class);
        when(task1.toString()).thenReturn("Task One");

        return Stream.of(
            arguments(null, null, Collections.<Task> emptyList(), "{"
                + "\"type\":\"Feature\","
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]},"
                + "\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[111.1,222.2,333.3]},"
                + "\"rvType\":\"UNKNOWN\",\"rvName\":\"unknown\",\"rvState\":\"none\",\"rvHeading\":0,"
                + "\"rvId\":-1,\"type\":\"rvPosition\",\"rvTime\":1000000000}"
                + "}"),

            arguments(rv01, rvs01, Collections.<Task> emptyList(), "{"
                + "\"type\":\"Feature\","
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]},"
                + "\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[111.1,222.2,333.3]},"
                + "\"rvType\":\"BOAT\",\"rvName\":\"RV01\",\"rvState\":\"idle\",\"rvHeading\":0,"
                + "\"rvId\":12345,\"type\":\"rvPosition\",\"rvTime\":1000000000}"
                + "}"),

            arguments(rv01, rvs02, Collections.<Task> emptyList(), "{"
                + "\"type\":\"Feature\","
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]},"
                + "\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[111.1,222.2,333.3]},"
                + "\"rvType\":\"BOAT\",\"rvName\":\"RV01\",\"rvState\":\"idle\",\"rvHeading\":0,"
                + "\"rvId\":12345,\"type\":\"rvPosition\",\"rvTime\":1000000000}"
                + "}"),

            arguments(rv01, null, Collections.<Task> emptyList(), "{"
                + "\"type\":\"Feature\","
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]},"
                + "\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[111.1,222.2,333.3]},"
                + "\"rvType\":\"BOAT\",\"rvName\":\"RV01\",\"rvState\":\"idle\",\"rvHeading\":0,"
                + "\"rvId\":12345,\"type\":\"rvPosition\",\"rvTime\":1000000000}"
                + "}"),

            arguments(rv01, rvs01, Arrays.asList(task1), "{"
                + "\"type\":\"Feature\","
                + "\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]},"
                + "\"properties\":{"
                + "\"rvPosition\":{\"coordinates\":[111.1,222.2,333.3]},"
                + "\"rvType\":\"BOAT\",\"rvName\":\"RV01\",\"rvState\":\"busy\",\"rvHeading\":0,"
                + "\"rvId\":12345,\"type\":\"rvPosition\",\"rvTime\":1000000000}"
                + "}"));
    }

    @ParameterizedTest
    @MethodSource("realVehicleDataProvider")
    void shouldContribute(RealVehicle realVehicle, RealVehicleState state, List<Task> taskList, String expected)
        throws JsonProcessingException, JSONException
    {
        PolarCoordinate position = new PolarCoordinate(222.2, 111.1, 333.3);

        if (realVehicle != null)
        {
            when(rvRepo.findOwnRealVehicle()).thenReturn(realVehicle);
            when(rvRepo.findRealVehicleStateById(realVehicle.getId())).thenReturn(state);
        }

        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, position, taskList);

        ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);

        verify(featureCollection).add(captor.capture());

        Feature argument = captor.getValue();

        String actual = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(argument);
        System.out.println("actual: " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);

    }
}
