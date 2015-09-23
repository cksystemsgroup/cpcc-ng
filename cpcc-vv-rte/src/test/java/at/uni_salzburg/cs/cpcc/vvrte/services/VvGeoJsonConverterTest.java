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

package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicle;
import at.uni_salzburg.cs.cpcc.vvrte.entities.VirtualVehicleState;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VvGeoJsonConverterTest
{
    private VvGeoJsonConverter sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new VvGeoJsonConverterImpl();
    }

    @DataProvider
    public Object[][] vvDataProvider()
    {
        RealVehicle rv01 = new RealVehicle();

        VirtualVehicle vv01 = new VirtualVehicle();
        vv01.setApiVersion(1);
        vv01.setCode("Code01");
        vv01.setContinuation("Continuation01".getBytes());
        vv01.setEndTime(new Date(1439669289111L));
        vv01.setId(1);
        vv01.setMigrationDestination(rv01);
        vv01.setMigrationStartTime(new Date(1439669289222L));
        vv01.setName("RV01");
        vv01.setStartTime(new Date(1439669289333L));
        vv01.setState(VirtualVehicleState.INIT);
        vv01.setUuid("0123456-789-123");

        return new Object[][]{
            new Object[]{
                vv01,
                "{\"type\":\"Feature\",\"properties\":{\"name\":\"RV01\",\"state\":\"init\",\"type\":\"vv\"},"
                    + "\"id\":\"012345...\"}"},
        };
    }

    @Test(dataProvider = "vvDataProvider")
    public void shouldConvertToJson(VirtualVehicle virtualVehicle, String expected)
        throws JsonProcessingException, JSONException
    {
        Feature feature = sut.toFeature(virtualVehicle);

        String actual = new ObjectMapper().writeValueAsString(feature);

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

    @DataProvider
    public Object[][] vvListDataProvider()
    {
        RealVehicle rv01 = new RealVehicle();

        VirtualVehicle vv01 = new VirtualVehicle();
        vv01.setApiVersion(1);
        vv01.setCode("Code01");
        vv01.setContinuation("Continuation01".getBytes());
        vv01.setEndTime(new Date(1439669289111L));
        vv01.setId(1);
        vv01.setMigrationDestination(rv01);
        vv01.setMigrationStartTime(new Date(1439669289222L));
        vv01.setName("RV01");
        vv01.setStartTime(new Date(1439669289333L));
        vv01.setState(VirtualVehicleState.INIT);
        vv01.setUuid("0123456-789-123");

        VirtualVehicle vv02 = new VirtualVehicle();
        vv02.setApiVersion(2);
        vv02.setCode("Code02");
        vv02.setContinuation("Continuation02".getBytes());
        vv02.setEndTime(new Date(1439669289222L));
        vv02.setId(2);
        vv02.setMigrationDestination(rv01);
        vv02.setMigrationStartTime(new Date(1439669289333L));
        vv02.setName("RV02");
        vv02.setStartTime(new Date(1439669289444L));
        vv02.setState(VirtualVehicleState.RUNNING);
        vv02.setUuid("0123456-789-321");

        return new Object[][]{
            new Object[]{
                Arrays.asList(vv01),
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"RV01\",\"state\":\"init\",\"type\":\"vv\"},"
                    + "\"id\":\"012345...\"}]}"},
            new Object[]{
                Arrays.asList(vv01, vv02),
                "{\"type\":\"FeatureCollection\",\"features\":["
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"RV01\",\"state\":\"init\",\"type\":\"vv\"},"
                    + "\"id\":\"012345...\"},"
                    + "{\"type\":\"Feature\",\"properties\":{\"name\":\"RV02\",\"state\":\"running\",\"type\":\"vv\"},"
                    + "\"id\":\"012345...\"}]}"},
        };
    }

    @Test(dataProvider = "vvListDataProvider")
    public void shouldConvertListToJsonArray(List<VirtualVehicle> virtualVehicleList, String expected)
        throws JsonProcessingException, JSONException
    {
        List<Feature> featureList = sut.toFeatureList(virtualVehicleList);

        FeatureCollection featureCollection = new FeatureCollection();
        featureCollection.addAll(featureList);

        String actual = new ObjectMapper().writeValueAsString(featureCollection);

        JSONAssert.assertEquals(expected, actual, false);
        JSONAssert.assertEquals(actual, expected, false);
    }

}
