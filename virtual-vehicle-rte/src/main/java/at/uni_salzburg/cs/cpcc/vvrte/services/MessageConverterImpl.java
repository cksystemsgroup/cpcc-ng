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

import static sensor_msgs.NavSatStatus.SERVICE_COMPASS;
import static sensor_msgs.NavSatStatus.SERVICE_GALILEO;
import static sensor_msgs.NavSatStatus.SERVICE_GLONASS;
import static sensor_msgs.NavSatStatus.SERVICE_GPS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

/**
 * MessageConverterImpl
 */
public class MessageConverterImpl implements MessageConverter
{
    @SuppressWarnings("serial")
    private static final Map<String, MessageConverter> CONVERTER_MAP = new HashMap<String, MessageConverter>()
    {
        {
            put("MessageImpl<sensor_msgs/Image>", new ImageMessageConverter());
            put("MessageImpl<std_msgs/Float32>", new Float32MessageConverter());
            put("MessageImpl<sensor_msgs/NavSatFix>", new NavSatFixMessageConverter());
        }
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public ScriptableObject convertMessageToJS(org.ros.internal.message.Message message)
    {
        if (!CONVERTER_MAP.containsKey(message.toString()))
        {
            return null;
        }

        return CONVERTER_MAP.get(message.toString()).convertMessageToJS(message);
    }

    /**
     * ImageMessageConverter
     */
    private static class ImageMessageConverter implements MessageConverter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public NativeObject convertMessageToJS(org.ros.internal.message.Message message)
        {
            sensor_msgs.Image m = (sensor_msgs.Image) message;

            NativeObject o = new NativeObject();
            o.put("encoding", o, m.getEncoding());
            o.put("height", o, m.getHeight());
            o.put("width", o, m.getWidth());
            o.put("step", o, m.getStep());

            int offset = m.getData().arrayOffset();
            int length = m.getData().array().length;
            byte[] buf = Arrays.copyOfRange(m.getData().array(), offset, length);
            o.put("data", o, buf);
            return o;
        }
    }

    /**
     * Float32MessageConverter
     */
    private static class Float32MessageConverter implements MessageConverter
    {
        /**
         * {@inheritDoc}
         */
        @Override
        public ScriptableObject convertMessageToJS(org.ros.internal.message.Message message)
        {
            std_msgs.Float32 m = (std_msgs.Float32) message;
            NativeObject o = new NativeObject();
            o.put("value", o, Float.valueOf(m.getData()));
            return o;
        }
    }

    /**
     * NavSatFixMessageConverter
     */
    private static class NavSatFixMessageConverter implements MessageConverter
    {

        @SuppressWarnings("serial")
        private static final Map<Byte, String> COVARIANCE_TYPE_MAP = new HashMap<Byte, String>()
        {
            {
                put(sensor_msgs.NavSatFix.COVARIANCE_TYPE_UNKNOWN, "unknown");
                put(sensor_msgs.NavSatFix.COVARIANCE_TYPE_APPROXIMATED, "approximated");
                put(sensor_msgs.NavSatFix.COVARIANCE_TYPE_DIAGONAL_KNOWN, "diagonal_known");
                put(sensor_msgs.NavSatFix.COVARIANCE_TYPE_KNOWN, "known");
            }
        };

        @SuppressWarnings("serial")
        private static final Map<Byte, String> STATUS_MAP = new HashMap<Byte, String>()
        {
            {
                put(sensor_msgs.NavSatStatus.STATUS_NO_FIX, "no_fix");
                put(sensor_msgs.NavSatStatus.STATUS_FIX, "fix");
                put(sensor_msgs.NavSatStatus.STATUS_SBAS_FIX, "sbas_fix");
                put(sensor_msgs.NavSatStatus.STATUS_GBAS_FIX, "gbas_fix");
            }
        };

        private static final String GPS = "gps";
        private static final String GLONASS = "glonass";
        private static final String COMPASS = "compass";
        private static final String GALILEO = "galileo";

        @SuppressWarnings("serial")
        private static final Map<Integer, String[]> SERVICE_MAP = new HashMap<Integer, String[]>()
        {
            {
                put(Integer.valueOf(0),
                    new String[]{});
                put(Integer.valueOf(SERVICE_GPS),
                    new String[]{GPS});
                put(Integer.valueOf(SERVICE_GLONASS),
                    new String[]{GLONASS});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS),
                    new String[]{GPS, GLONASS});
                put(Integer.valueOf(SERVICE_COMPASS),
                    new String[]{COMPASS});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_COMPASS),
                    new String[]{GPS, COMPASS});
                put(Integer.valueOf(SERVICE_GLONASS | SERVICE_COMPASS),
                    new String[]{GLONASS, COMPASS});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_COMPASS),
                    new String[]{GPS, GLONASS, COMPASS});
                put(Integer.valueOf(SERVICE_GALILEO),
                    new String[]{GALILEO});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_GALILEO),
                    new String[]{GPS, GALILEO});
                put(Integer.valueOf(SERVICE_GLONASS | SERVICE_GALILEO),
                    new String[]{GLONASS, GALILEO});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_GALILEO),
                    new String[]{GPS, GLONASS, GALILEO});
                put(Integer.valueOf(SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{COMPASS, GALILEO});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GPS, COMPASS, GALILEO});
                put(Integer.valueOf(SERVICE_GLONASS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GLONASS, COMPASS, GALILEO});
                put(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GPS, GLONASS, COMPASS, GALILEO});
            }
        };

        /**
         * {@inheritDoc}
         */
        @Override
        public NativeObject convertMessageToJS(org.ros.internal.message.Message message)
        {
            sensor_msgs.NavSatFix m = (sensor_msgs.NavSatFix) message;

            NativeObject o = new NativeObject();
            o.put("lat", o, m.getLatitude());
            o.put("lng", o, m.getLongitude());
            o.put("alt", o, m.getAltitude());
            o.put("covariance", o, new NativeArray(convertToDoubleArray(m.getPositionCovariance())));
            o.put("covarianceType", o, COVARIANCE_TYPE_MAP.get(m.getPositionCovarianceType()));
            o.put("service", o, new NativeArray(SERVICE_MAP.get((int) m.getStatus().getService())));
            o.put("status", o, STATUS_MAP.get(m.getStatus().getStatus()));
            return o;
        }
    }

    /**
     * @param a the array of double values.
     * @return the converted list of Double objects.
     */
    private static Double[] convertToDoubleArray(double[] a)
    {
        Double[] result = new Double[a.length];
        for (int k = 0, l = a.length; k < l; ++k)
        {
            result[k] = Double.valueOf(a[k]);
        }
        return result;
    }
}
