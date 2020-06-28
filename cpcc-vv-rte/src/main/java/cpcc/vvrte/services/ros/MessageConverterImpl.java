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

package cpcc.vvrte.services.ros;

import static sensor_msgs.NavSatStatus.SERVICE_COMPASS;
import static sensor_msgs.NavSatStatus.SERVICE_GALILEO;
import static sensor_msgs.NavSatStatus.SERVICE_GLONASS;
import static sensor_msgs.NavSatStatus.SERVICE_GPS;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.NativeObject;
import org.mozilla.javascript.ScriptableObject;

/**
 * MessageConverterImpl
 */
public class MessageConverterImpl implements MessageConverter
{
    private static final String SENSOR_MSGS_NAV_SAT_FIX = "sensor_msgs/NavSatFix";
    private static final String SENSOR_MSGS_IMAGE = "sensor_msgs/Image";
    private static final String STD_MSGS_FLOAT32 = "std_msgs/Float32";
    private static final String VALUE = "value";
    private static final String DATA = "data";
    private static final String STEP = "step";
    private static final String WIDTH = "width";
    private static final String HEIGHT = "height";
    private static final String ENCODING = "encoding";
    private static final String MESSAGE_TYPE = "messageType";

    private static final Map<String, MessageConverter> CONVERTER_MAP = Collections.unmodifiableMap(Stream
        .of(Pair.of("MessageImpl<std_msgs/Float32>", new Float32MessageConverter()),
            Pair.of("MessageImpl<sensor_msgs/Image>", new ImageMessageConverter()),
            Pair.of("MessageImpl<sensor_msgs/NavSatFix>", new NavSatFixMessageConverter()))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

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
            o.put(MESSAGE_TYPE, o, SENSOR_MSGS_IMAGE);
            o.put(ENCODING, o, m.getEncoding());
            o.put(HEIGHT, o, m.getHeight());
            o.put(WIDTH, o, m.getWidth());
            o.put(STEP, o, m.getStep());

            int offset = m.getData().arrayOffset();
            int length = m.getData().array().length;
            byte[] buf = Arrays.copyOfRange(m.getData().array(), offset, length);
            o.put(DATA, o, buf);
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
            o.put(MESSAGE_TYPE, o, STD_MSGS_FLOAT32);
            o.put(VALUE, o, Float.valueOf(m.getData()));
            return o;
        }
    }

    /**
     * NavSatFixMessageConverter
     */
    private static class NavSatFixMessageConverter implements MessageConverter
    {
        private static final Map<Byte, String> COVARIANCE_TYPE_MAP = Collections.unmodifiableMap(Stream
            .of(Pair.of(sensor_msgs.NavSatFix.COVARIANCE_TYPE_UNKNOWN, "unknown"),
                Pair.of(sensor_msgs.NavSatFix.COVARIANCE_TYPE_APPROXIMATED, "approximated"),
                Pair.of(sensor_msgs.NavSatFix.COVARIANCE_TYPE_DIAGONAL_KNOWN, "diagonal_known"),
                Pair.of(sensor_msgs.NavSatFix.COVARIANCE_TYPE_KNOWN, "known"))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        private static final Map<Byte, String> STATUS_MAP = Collections.unmodifiableMap(Stream
            .of(Pair.of(sensor_msgs.NavSatStatus.STATUS_NO_FIX, "no_fix"),
                Pair.of(sensor_msgs.NavSatStatus.STATUS_FIX, "fix"),
                Pair.of(sensor_msgs.NavSatStatus.STATUS_SBAS_FIX, "sbas_fix"),
                Pair.of(sensor_msgs.NavSatStatus.STATUS_GBAS_FIX, "gbas_fix"))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        private static final String GPS = "gps";
        private static final String GLONASS = "glonass";
        private static final String COMPASS = "compass";
        private static final String GALILEO = "galileo";

        private static final Map<Integer, String[]> SERVICE_MAP = Collections.unmodifiableMap(Stream
            .of(Pair.of(Integer.valueOf(0),
                new String[]{}),
                Pair.of(Integer.valueOf(SERVICE_GPS),
                    new String[]{GPS}),
                Pair.of(Integer.valueOf(SERVICE_GLONASS),
                    new String[]{GLONASS}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS),
                    new String[]{GPS, GLONASS}),
                Pair.of(Integer.valueOf(SERVICE_COMPASS),
                    new String[]{COMPASS}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_COMPASS),
                    new String[]{GPS, COMPASS}),
                Pair.of(Integer.valueOf(SERVICE_GLONASS | SERVICE_COMPASS),
                    new String[]{GLONASS, COMPASS}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_COMPASS),
                    new String[]{GPS, GLONASS, COMPASS}),
                Pair.of(Integer.valueOf(SERVICE_GALILEO),
                    new String[]{GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_GALILEO),
                    new String[]{GPS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GLONASS | SERVICE_GALILEO),
                    new String[]{GLONASS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_GALILEO),
                    new String[]{GPS, GLONASS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{COMPASS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GPS, COMPASS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GLONASS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GLONASS, COMPASS, GALILEO}),
                Pair.of(Integer.valueOf(SERVICE_GPS | SERVICE_GLONASS | SERVICE_COMPASS | SERVICE_GALILEO),
                    new String[]{GPS, GLONASS, COMPASS, GALILEO}))
            .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

        /**
         * {@inheritDoc}
         */
        @Override
        public NativeObject convertMessageToJS(org.ros.internal.message.Message message)
        {
            sensor_msgs.NavSatFix m = (sensor_msgs.NavSatFix) message;

            NativeObject o = new NativeObject();
            o.put(MESSAGE_TYPE, o, SENSOR_MSGS_NAV_SAT_FIX);
            o.put("lat", o, m.getLatitude());
            o.put("lng", o, m.getLongitude());
            o.put("alt", o, m.getAltitude());
            o.put("covariance", o, new NativeArray(convertToDoubleArray(m.getPositionCovariance())));
            o.put("covarianceType", o, COVARIANCE_TYPE_MAP.get(m.getPositionCovarianceType()));
            o.put("service", o, new NativeArray(SERVICE_MAP.get((int) m.getStatus().getService())));
            o.put("status", o, STATUS_MAP.get(m.getStatus().getStatus()));
            return o;
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
}
