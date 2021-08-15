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

import cpcc.core.entities.DeviceType;

public class DeviceTypeSelectHelpersTest
{
    private static final String DEVICE_TYPE_NAME = "name1";
    private DeviceTypeSelectHelpers sut;
    private ValueEncoder<DeviceType> encoder;
    private DeviceType deviceType;

    @BeforeEach
    public void setUp()
    {
        deviceType = mock(DeviceType.class);
        when(deviceType.getName()).thenReturn(DEVICE_TYPE_NAME);

        QueryManager qm = Mockito.mock(QueryManager.class);
        when(qm.findDeviceTypeByName(DEVICE_TYPE_NAME)).thenReturn(deviceType);

        sut = new DeviceTypeSelectHelpers(qm);
        encoder = sut.valueEncoder();
    }

    @Test
    public void shouldReturnNullForClientValueNull()
    {
        DeviceType actual = encoder.toValue(null);
        assertThat(actual).isNull();
    }

    @Test
    public void shouldReturnDeviceTypeClientValueNotNull()
    {
        DeviceType actual = encoder.toValue(DEVICE_TYPE_NAME);
        assertThat(actual).isEqualTo(deviceType);
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
        String actual = encoder.toClient(deviceType);
        assertThat(actual).isEqualTo(DEVICE_TYPE_NAME);
    }

    @Test
    public void shouldCreateEmptySelectModel()
    {
        SelectModel actual = DeviceTypeSelectHelpers.selectModel(Arrays.asList(new DeviceType[0]));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(0);
    }

    @Test
    public void shouldCreateSelectModelForOneDeviceType()
    {
        SelectModel actual = DeviceTypeSelectHelpers.selectModel(Arrays.asList(deviceType));
        assertThat(actual).isNotNull();
        assertThat(actual.getOptions()).hasSize(1);
    }
}
