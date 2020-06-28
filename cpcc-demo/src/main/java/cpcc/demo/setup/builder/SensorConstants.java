// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.demo.setup.builder;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;
import cpcc.core.entities.TopicCategory;

/**
 * Sensor Constants implementation.
 */
public final class SensorConstants
{
    private static final String SENSOR_MSGS_NAV_SAT_FIX = "sensor_msgs/NavSatFix";
    private static final String SENSOR_MSGS_IMAGE = "sensor_msgs/Image";
    private static final String STD_MSGS_FLOAT32 = "std_msgs/Float32";

    private static final Date now = new Date();

    private static final SensorDefinition[] SENSOR_DEFINITIONS = {
        new SensorDefinitionBuilder()
            .setId(1)
            .setDescription("Altimeter")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.ALTIMETER)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(2)
            .setDescription("Area of Operations")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.AREA_OF_OPERATIONS)
            .setVisibility(SensorVisibility.PRIVILEGED_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(3)
            .setDescription("Barometer")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.BAROMETER)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(4)
            .setDescription("Battery")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.BATTERY)
            .setVisibility(SensorVisibility.PRIVILEGED_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(5)
            .setDescription("Belly Mounted Camera 640x480")
            .setLastUpdate(now)
            .setMessageType(SENSOR_MSGS_IMAGE)
            .setParameters("width=640 height=480 yaw=0 down=1.571 alignment=''north''")
            .setType(SensorType.CAMERA)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        // new SensorDefinitionBuilder()
        //  .setId(6)
        //  .setDescription("FPV Camera 640x480")
        //  .setLastUpdate(now)
        //  .setMessageType("sensor_msgs/Image")
        //  .setParameters("width=640 height=480 yaw=0 down=0 alignment=''heading''")
        //  .setType(SensorType.CAMERA)
        //  .setVisibility(SensorVisibility.ALL_VV)
        //  .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(7)
            .setDescription("CO2")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.CO2)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(9)
            .setDescription("GPS")
            .setLastUpdate(now)
            .setMessageType(SENSOR_MSGS_NAV_SAT_FIX)
            .setParameters(null)
            .setType(SensorType.GPS)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(10)
            .setDescription("Hardware")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.HARDWARE)
            .setVisibility(SensorVisibility.PRIVILEGED_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(11)
            .setDescription("NOx")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.NOX)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build(),
        new SensorDefinitionBuilder()
            .setId(12)
            .setDescription("Thermometer")
            .setLastUpdate(now)
            .setMessageType(STD_MSGS_FLOAT32)
            .setParameters(null)
            .setType(SensorType.THERMOMETER)
            .setVisibility(SensorVisibility.ALL_VV)
            .setDeleted(false).build()
    };

    public static final Map<TopicCategory, SensorType> TOPIC_SENSOR_MAP = Collections.unmodifiableMap(Stream
        .of(Pair.of(TopicCategory.ALTITUDE_OVER_GROUND, SensorType.ALTIMETER),
            Pair.of(TopicCategory.CAMERA, SensorType.CAMERA),
            Pair.of(TopicCategory.CAMERA_INFO, SensorType.CAMERA),
            Pair.of(TopicCategory.GPS_POSITION_PROVIDER, SensorType.GPS))
        .collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

    private SensorConstants()
    {
        // Intentionally empty.
    }

    /**
     * @param type the required sensor types.
     * @return all sensor definitions specified in type.
     */
    public static List<SensorDefinition> byType(SensorType... type)
    {
        Set<SensorType> types = Stream.of(type).collect(Collectors.toSet());
        return Stream.of(SENSOR_DEFINITIONS).filter(x -> types.contains(x.getType())).collect(Collectors.toList());
    }

    /**
     * @return all sensor definitions.
     */
    public static List<SensorDefinition> all()
    {
        return Arrays.asList(SENSOR_DEFINITIONS);
    }
}
