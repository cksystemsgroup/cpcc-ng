/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.vvrte.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.fest.assertions.api.Assertions.offset;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;

import org.apache.commons.lang.ArrayUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.mozilla.javascript.ScriptableObject;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.sun.xml.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

/**
 * MessageConverterTest
 */
public class MessageConverterTest
{
    private MessageConverter conv;

    @BeforeMethod
    public void setUp()
    {
        conv = new MessageConverterImpl();
    }

    @DataProvider
    public static Object[][] validFloatDataProvicer()
    {
        return new Object[][]{
            new Object[]{1.2f},
            new Object[]{-1.2f},
            new Object[]{0.0f},
            new Object[]{11.9f},
            new Object[]{-11.9f},
        };
    }

    @Test(dataProvider = "validFloatDataProvicer")
    public void shouldConvertFloat32(float value)
    {
        std_msgs.Float32 msg = mock(std_msgs.Float32.class);
        when(msg.toString()).thenReturn("MessageImpl<std_msgs/Float32>");
        when(msg.getData()).thenReturn(value);

        ScriptableObject obj = conv.convertMessageToJS(msg);

        assertThat(obj).isNotNull();
        assertThat((Float) obj.get("value")).isNotNull().isEqualTo(value);
    }
    
    @DataProvider
    public static Object[][] validNavSatFixDataProvicer()
    {
        return new Object[][]{
            new Object[]{47.9, 13.2, 12.0, new double[]{1.0, 2.0, 3.0}, (byte) 1, (byte) 2, (short) 1},
            new Object[]{-47.9, -122.2, 12.0, new double[]{2.0, 3.1, 1.3}, (byte) 2, (byte) 3, (short) 15},
        };
    }

    @Test(dataProvider = "validNavSatFixDataProvicer")
    public void shouldConvertNavSatFix(double lat, double lon, double alt, double[] cov, byte covType, byte statusByte,
        short service)
    {
        sensor_msgs.NavSatStatus status = mock(sensor_msgs.NavSatStatus.class);
        when(status.getStatus()).thenReturn(statusByte);
        when(status.getService()).thenReturn(service);

        sensor_msgs.NavSatFix msg = mock(sensor_msgs.NavSatFix.class);
        when(msg.toString()).thenReturn("MessageImpl<sensor_msgs/NavSatFix>");
        when(msg.getLatitude()).thenReturn(lat);
        when(msg.getLongitude()).thenReturn(lon);
        when(msg.getAltitude()).thenReturn(alt);
        when(msg.getPositionCovariance()).thenReturn(cov);
        when(msg.getPositionCovarianceType()).thenReturn(covType);
        when(msg.getStatus()).thenReturn(status);

        ScriptableObject obj = conv.convertMessageToJS(msg);

        assertThat(obj).isNotNull();
        assertThat((Double) obj.get("lat")).isNotNull().isEqualTo(lat, offset(1E-9));
        assertThat((Double) obj.get("lon")).isNotNull().isEqualTo(lon, offset(1E-9));
        assertThat((Double) obj.get("alt")).isNotNull().isEqualTo(alt, offset(1E-9));
    }

    @DataProvider
    public static Object[][] validImageDataProvider()
    {
        return new Object[][]{
            new Object[]{"JPG", 480, 640, 1280, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 1},
            new Object[]{"PNG", 481, 641, 1281, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 3},
            new Object[]{"PNG", 479, 639, 1279, new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 0},
        };
    }
    
    @Test(dataProvider = "validImageDataProvider")
    public void shouldConvertImage(String encoding, int height, int width, int step, final byte[] bufArray, int bufOffset)
    {
        byte[] data = Arrays.copyOfRange(bufArray, bufOffset, bufArray.length);
        
        ChannelBuffer buf = mock(ChannelBuffer.class);
        when(buf.array()).thenReturn(bufArray);
        when(buf.arrayOffset()).thenReturn(bufOffset);
        
        doAnswer(new Answer<Object>() {
            public Object answer(InvocationOnMock invocation) {
                Object[] args = invocation.getArguments();
                int offset = (Integer)args[0];
                byte[] buf = (byte[])args[1];
                System.arraycopy(bufArray, offset, buf, 0, bufArray.length-offset);
                return null;
            }
        }).when(buf).getBytes(anyInt(), any(byte[].class));

        sensor_msgs.Image msg = mock(sensor_msgs.Image.class);
        when(msg.toString()).thenReturn("MessageImpl<sensor_msgs/Image>");
        when(msg.getEncoding()).thenReturn(encoding);
        when(msg.getHeight()).thenReturn(height);
        when(msg.getWidth()).thenReturn(width);
        when(msg.getStep()).thenReturn(step);
        when(msg.getData()).thenReturn(buf);

        ScriptableObject obj = conv.convertMessageToJS(msg);

        assertThat(obj).isNotNull();
        assertThat((String) obj.get("encoding")).isNotNull().isEqualTo(encoding);
        assertThat((Integer) obj.get("height")).isNotNull().isEqualTo(height);
        assertThat((Integer) obj.get("width")).isNotNull().isEqualTo(width);
        assertThat((Integer) obj.get("step")).isNotNull().isEqualTo(step);
        assertThat((byte[]) obj.get("data")).isNotNull().isEqualTo(data);
    }
    
    @Test
    public void shouldNotConvertUnknownMessageType()
    {
        std_msgs.Header msg = mock(std_msgs.Header.class);
        
        ScriptableObject obj = conv.convertMessageToJS(msg);
        
        assertThat(obj).isNull();
    }
}
