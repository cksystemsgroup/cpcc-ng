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

import static cpcc.demo.setup.builder.LambdaExceptionUtil.rethrowConsumer;

import java.io.IOException;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import cpcc.core.entities.Device;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.Topic;

/**
 * AbstractRealVehicleWriter implementation.
 */
public class AbstractRealVehicleWriter
{
    /**
     * @param writer the writer.
     * @param device the device to write.
     * @throws IOException in case of errors.
     */
    protected void writeDevice(Writer writer, Device device) throws IOException
    {
        writer.append(String.format("INSERT INTO DEVICES (ID,CONFIGURATION,TOPIC_ROOT,TYPE_ID) VALUES "
            + "(%1$d,'%2$s','%3$s',%4$d);%n"
            , device.getId()
            , device.getConfiguration()
            , device.getTopicRoot()
            , device.getType().getId()
            ));

        Stream.of(Arrays.asList(device.getType().getMainTopic()), device.getType().getSubTopics())
            .flatMap(List::stream)
            .forEach(rethrowConsumer(x -> writeMappingAttributes(writer, device, x)));
    }

    /**
     * @param writer the writer.
     * @param device the device to write.
     * @param topic the ROS topic.
     * @throws IOException in case of errors.
     */
    protected void writeMappingAttributes(Writer writer, Device device, Topic topic) throws IOException
    {
        SensorType sensorType = SensorConstants.TOPIC_SENSOR_MAP.get(topic.getCategory());
        List<SensorDefinition> sds = sensorType != null ? SensorConstants.byType(sensorType) : Collections.emptyList();

        writer.append(String.format("INSERT INTO MAPPING_ATTRIBUTES "
            + "(CONNECTED_TO_AUTOPILOT,VV_VISIBLE,TOPIC_ID,DEVICE_ID,SENSORDEFINITION_ID) "
            + "VALUES (1,0,%1$d,%2$d,%3$s);%n"
            , topic.getId()
            , device.getId()
            , sds.isEmpty() ? "null" : sds.get(0).getId()
            ));
    }
}
