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

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.GeoJsonObject;
import org.json.JSONException;
import org.mockito.ArgumentCaptor;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.entities.VirtualVehicleState;
import cpcc.vvrte.services.db.VvRteRepository;
import cpcc.vvrte.services.json.VvGeoJsonConverter;

public class VirtualVehicleContributorTest
{
    private VvGeoJsonConverter vjc;
    private VvRteRepository vvRepo;
    private VirtualVehicleContributor sut;

    @BeforeMethod
    public void setUp()
    {
        Map<VirtualVehicleState, Integer> vvStats = new HashMap<>();
        vvStats.put(VirtualVehicleState.DEFECTIVE, 1230);
        vvStats.put(VirtualVehicleState.FINISHED, 2230);
        vvStats.put(VirtualVehicleState.INIT, 3230);
        vvStats.put(VirtualVehicleState.RUNNING, 4230);
        vvStats.put(VirtualVehicleState.TASK_COMPLETION_AWAITED, 5230);
        vvStats.put(VirtualVehicleState.INTERRUPTED, 6230);
        vvStats.put(VirtualVehicleState.MIGRATION_INTERRUPTED_RCV, 13230);
        vvStats.put(VirtualVehicleState.MIGRATION_INTERRUPTED_SND, 7230);
        vvStats.put(VirtualVehicleState.MIGRATING_RCV, 8230);
        vvStats.put(VirtualVehicleState.MIGRATING_SND, 9230);
        vvStats.put(VirtualVehicleState.MIGRATION_AWAITED_SND, 10230);
        vvStats.put(VirtualVehicleState.MIGRATION_COMPLETED_RCV, 11230);
        vvStats.put(VirtualVehicleState.MIGRATION_COMPLETED_SND, 12230);

        vjc = mock(VvGeoJsonConverter.class);

        vvRepo = mock(VvRteRepository.class);
        when(vvRepo.getVvStatistics()).thenReturn(vvStats);

        sut = new VirtualVehicleContributor(vvRepo, vjc);
    }

    public static final String EMPTY_VV_FEATURE = "{\"type\":\"Feature\""
        + ",\"properties\":{\"vvsMigrating\":51150,\"vvsDefective\":1230,\"vvsDormant\":5460,\"vvsTotal\":93990"
        + ",\"type\":\"vvs\",\"vvsActive\":9460,\"vvsInterrupted\":26690}"
        + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":[]}}";

    @DataProvider
    public Object[][] noVehiclesDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{Collections.<VirtualVehicle> emptyList()},
        };
    }

    @Test(dataProvider = "noVehiclesDataProvider")
    public void shouldAddEmptyFeatureOnEmptyVirtualVehicleList(List<VirtualVehicle> emptyList)
        throws JsonProcessingException, JSONException
    {
        when(vvRepo.findAllActiveVehicles(10)).thenReturn(emptyList);

        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, null, null);

        ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);

        verify(featureCollection).add(captor.capture());

        Feature argument = captor.getValue();

        String actual = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(argument);
        System.out.println("actual: " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(EMPTY_VV_FEATURE, actual, false);
        JSONAssert.assertEquals(actual, EMPTY_VV_FEATURE, false);
    }

    @DataProvider
    public Object[][] dataProvider()
    {
        VirtualVehicle vv1 = mock(VirtualVehicle.class);
        VirtualVehicle vv2 = mock(VirtualVehicle.class);

        Feature fe1 = new Feature();
        fe1.setId("erwtw");
        fe1.setProperty("type", "vv");
        fe1.setProperty("name", "vv0001");
        fe1.setProperty("state", VirtualVehicleState.RUNNING.name());

        Feature fe2 = new Feature();
        fe2.setId("xcvbav");
        fe2.setProperty("type", "vv");
        fe2.setProperty("name", "vv0002");
        fe2.setProperty("state", VirtualVehicleState.INIT.name());

        List<GeoJsonObject> go1 = new ArrayList<>();
        go1.add(fe1);

        List<GeoJsonObject> go2 = new ArrayList<>();
        go2.add(fe2);

        List<GeoJsonObject> go3 = new ArrayList<>();
        go3.add(fe1);
        go3.add(fe2);

        return new Object[][]{
            new Object[]{Arrays.asList(vv1), go1, "{\"type\":\"Feature\""
                + ",\"properties\":{\"vvsMigrating\":51150,\"vvsDefective\":1230,\"vvsDormant\":5460"
                + ",\"vvsTotal\":93990,\"type\":\"vvs\",\"vvsActive\":9460,\"vvsInterrupted\":26690}"
                + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                + "{\"type\":\"Feature\""
                + ",\"properties\":{\"name\":\"vv0001\",\"state\":\"RUNNING\",\"type\":\"vv\"},\"id\":\"erwtw\"}"
                + "]}}"},

            new Object[]{Arrays.asList(vv2), go2, "{\"type\":\"Feature\""
                + ",\"properties\":{\"vvsMigrating\":51150,\"vvsDefective\":1230,\"vvsDormant\":5460"
                + ",\"vvsTotal\":93990,\"type\":\"vvs\",\"vvsActive\":9460,\"vvsInterrupted\":26690}"
                + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                + "{\"type\":\"Feature\""
                + ",\"properties\":{\"name\":\"vv0002\",\"state\":\"INIT\",\"type\":\"vv\"},\"id\":\"xcvbav\"}"
                + "]}}"},

            new Object[]{Arrays.asList(vv1, vv2), go3, "{\"type\":\"Feature\""
                + ",\"properties\":{\"vvsMigrating\":51150,\"vvsDefective\":1230,\"vvsDormant\":5460"
                + ",\"vvsTotal\":93990,\"type\":\"vvs\",\"vvsActive\":9460,\"vvsInterrupted\":26690}"
                + ",\"geometry\":{\"type\":\"GeometryCollection\",\"geometries\":["
                + "{\"type\":\"Feature\""
                + ",\"properties\":{\"name\":\"vv0001\",\"state\":\"RUNNING\",\"type\":\"vv\"},\"id\":\"erwtw\"},"
                + "{\"type\":\"Feature\""
                + ",\"properties\":{\"name\":\"vv0002\",\"state\":\"INIT\",\"type\":\"vv\"},\"id\":\"xcvbav\"}"
                + "]}}"},
        };
    }

    @Test(dataProvider = "dataProvider")
    public void shouldContribute(List<VirtualVehicle> vvList, List<GeoJsonObject> featureList, String expected)
        throws JsonProcessingException, JSONException
    {
        when(vvRepo.findAllVehicles()).thenReturn(vvList);
        when(vjc.toGeometryObjectsList(anyListOf(VirtualVehicle.class))).thenReturn(featureList);

        FeatureCollection featureCollection = mock(FeatureCollection.class);

        sut.contribute(featureCollection, null, null);

        ArgumentCaptor<Feature> captor = ArgumentCaptor.forClass(Feature.class);

        verify(featureCollection).add(captor.capture());

        Feature argument = captor.getValue();

        String actual = new ObjectMapper().disable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(argument);
        // System.out.println("actual: " + actual.replace("\"", "\\\""));

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }
}
