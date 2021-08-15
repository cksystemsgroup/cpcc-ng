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

package cpcc.core.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.ValueEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import cpcc.core.entities.SensorDefinition;

public class SensorDefinitionSelectHelpersTest
{
    private static final String SENSOR_DEFINOTION_DESCRIPTION = "name1";
    private SensorDefinitionSelectHelpers sut;
    private ValueEncoder<SensorDefinition> encoder;
    private SensorDefinition sensorDefinition;

    @BeforeEach
    public void setUp()
    {
        sensorDefinition = mock(SensorDefinition.class);
        when(sensorDefinition.getDescription()).thenReturn(SENSOR_DEFINOTION_DESCRIPTION);

        QueryManager qm = Mockito.mock(QueryManager.class);
        when(qm.findSensorDefinitionByDescription(SENSOR_DEFINOTION_DESCRIPTION)).thenReturn(sensorDefinition);

        sut = new SensorDefinitionSelectHelpers(qm);
        encoder = sut.valueEncoder();
    }

    @Test
    public void shouldReturnNullForClientValueNull()
    {
        SensorDefinition actual = encoder.toValue(null);
        assertThat(actual).isNull();
    }

    @Test
    public void shouldReturnSensorDefinitionClientValueNotNull()
    {
        SensorDefinition actual = encoder.toValue(SENSOR_DEFINOTION_DESCRIPTION);
        assertThat(actual).isEqualTo(sensorDefinition);
    }

    @Test
    public void shouldReturnEmptyStringForClientValueNull()
    {
        String actual = encoder.toClient(null);
        assertThat(actual).isEqualTo(StringUtils.EMPTY);
    }

    @Test
    public void shouldReturnNonEmptyStringForClientValueNotNull()
    {
        String actual = encoder.toClient(sensorDefinition);
        assertThat(actual).isEqualTo(SENSOR_DEFINOTION_DESCRIPTION);
    }

    @Test
    public void shouldCreateEmptySelectModel()
    {
        SelectModel actual = SensorDefinitionSelectHelpers.selectModel(Arrays.asList(new SensorDefinition[0]));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(0);
    }

    @Test
    public void shouldCreateSelectModelForOneSensorDefinition()
    {
        SelectModel actual = SensorDefinitionSelectHelpers.selectModel(Arrays.asList(sensorDefinition));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(1);
    }
}
