// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.commons.services.SensorDescriptionListFormat;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;

public class SensorDescriptionListFormatTest
{
    private SensorDescriptionListFormat sut;

    @BeforeMethod
    public void setUp()
    {
        sut = new SensorDescriptionListFormat();
    }

    @DataProvider
    public Object[][] sensorDefinitionDataProvicer()
    {
        SensorDefinition s1 = mock(SensorDefinition.class);
        when(s1.getId()).thenReturn(1);
        when(s1.getDescription()).thenReturn("Altimeter");
        when(s1.getType()).thenReturn(SensorType.ALTIMETER);
        when(s1.getMessageType()).thenReturn("std_msgs/Float32");
        when(s1.getVisibility()).thenReturn(SensorVisibility.ALL_VV);
        when(s1.getParameters()).thenReturn("random=10:35");
        when(s1.getLastUpdate()).thenReturn(new Date(10001));

        SensorDefinition s2 = mock(SensorDefinition.class);
        when(s2.getId()).thenReturn(2);
        when(s2.getDescription()).thenReturn("Barometer");
        when(s2.getType()).thenReturn(SensorType.BAROMETER);
        when(s2.getMessageType()).thenReturn("std_msgs/Float32");
        when(s2.getVisibility()).thenReturn(SensorVisibility.NO_VV);
        when(s2.getParameters()).thenReturn("random=1050:1080");
        when(s2.getLastUpdate()).thenReturn(new Date(20002));

        return new Object[][]{
            new Object[]{null, ""},
            new Object[]{Collections.<SensorDefinition> emptyList(), ""},
            new Object[]{Arrays.asList(s1), "Altimeter"},
            new Object[]{Arrays.asList(s1, s2), "Altimeter, Barometer"},
        };
    }

    @Test(dataProvider = "sensorDefinitionDataProvicer")
    public void shouldFormatSensorDefinitionLists(List<SensorDefinition> sdList, String expected)
    {
        StringBuffer toAppendTo = new StringBuffer();

        sut.format(sdList, toAppendTo, null);

        assertThat(toAppendTo.toString()).isEqualTo(expected);
    }

    @Test(expectedExceptions = {NotImplementedException.class})
    public void shouldThrowExceptionOnParsing()
    {
        sut.parseObject(null, null);
    }
}
