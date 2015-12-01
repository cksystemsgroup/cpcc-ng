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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.Point;
import org.json.JSONException;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.services.CoreGeoJsonConverter;
import cpcc.vvrte.task.Task;

public class SensorsContributorTest
{
    private SensorsContributor sut;
    private CoreGeoJsonConverter jsonConverter;
    private SensorDefinition s1;
    private SensorDefinition s2;
    private Task task;
    private Point point;

    @BeforeMethod
    public void setUp()
    {
        s1 = mock(SensorDefinition.class);
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);

        s2 = mock(SensorDefinition.class);
        when(s2.getType()).thenReturn(SensorType.GPS);

        task = mock(Task.class);
        when(task.getSensors()).thenReturn(Arrays.asList(s1, s2));
        when(task.getLatitude()).thenReturn(1.2);
        when(task.getLongitude()).thenReturn(3.4);
        when(task.getAltitude()).thenReturn(5.6);

        point = new Point(111.1, 222.2, 333.3);

        jsonConverter = mock(CoreGeoJsonConverter.class);
        when(jsonConverter.toPoint(task)).thenReturn(point);

        sut = new SensorsContributor(jsonConverter);
    }

    public static final String EMPTY_SENSOR_FEATURE = "{\"type\":\"Feature\""
        + ",\"properties\":{\"type\":\"sensors\"}"
        + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":[]}}";

    @Test
    public void shouldContributeEmptyFeatureCollectionWhenNoTaskExecutes()
        throws JsonProcessingException, JSONException
    {
        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, null, Collections.<Task> emptyList());

        ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);

        verify(featureCollection).add(captor.capture());

        Feature argument = captor.getValue();

        String actual = new ObjectMapper().writeValueAsString(argument);
        System.out.println("actual: " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(EMPTY_SENSOR_FEATURE, actual, false);
        JSONAssert.assertEquals(actual, EMPTY_SENSOR_FEATURE, false);
    }

    public static final String EXPECTED_01 = "{\"type\":\"Feature\""
        + ",\"properties\":{\"type\":\"sensors\"}"
        + ",\"geometry\":{\"type\":\"GeometryCollection\""
        + ",\"geometries\":["
        + "{\"type\":\"Feature\",\"properties\":{\"sensorList\":\"ALTIMETER,GPS\",\"type\":\"rvSensor\"}"
        + ",\"geometry\":{\"type\":\"Point\",\"coordinates\":[111.1,222.2,333.3]}}"
        + "]}}";

    @Test
    public void shouldContributePointFeature() throws JsonProcessingException, JSONException
    {
        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, null, Arrays.asList(task));

        ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);

        verify(featureCollection).add(captor.capture());

        Feature argument = captor.getValue();

        String actual = new ObjectMapper().writeValueAsString(argument);
        // System.out.println("actual: " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(EXPECTED_01, actual, false);
        JSONAssert.assertEquals(actual, EXPECTED_01, false);
    }

}
