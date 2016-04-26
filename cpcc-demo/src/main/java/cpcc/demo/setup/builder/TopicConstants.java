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

import cpcc.core.entities.RosNodeType;
import cpcc.core.entities.Topic;
import cpcc.core.entities.TopicCategory;

/**
 * TopicConstants implementation.
 */
public final class TopicConstants
{
    public static final Topic T_CAMERA = new TopicBuilder()
        .setId(1)
        .setAdapterClassName("cpcc.ros.sensors.ImageSensorAdapter")
        .setCategory(TopicCategory.CAMERA)
        .setMessageType("sensor_msgs/Image")
        .setNodeType(RosNodeType.PUBLISHER)
        .setSubPach("image")
        .build();

    public static final Topic T_CAMERA_INFO = new TopicBuilder()
        .setId(2)
        .setAdapterClassName("cpcc.ros.sensors.CameraInfoSensorAdapter")
        .setCategory(TopicCategory.CAMERA_INFO)
        .setMessageType("sensor_msgs/CameraInfo")
        .setNodeType(RosNodeType.PUBLISHER)
        .setSubPach("camera_info")
        .build();

    public static final Topic T_RV_GPS_POSITION_PROVIDER = new TopicBuilder()
        .setId(5)
        .setAdapterClassName("cpcc.ros.sensors.GpsSensorAdapter")
        .setCategory(TopicCategory.GPS_POSITION_PROVIDER)
        .setMessageType("sensor_msgs/NavSatFix")
        .setNodeType(RosNodeType.PUBLISHER)
        .setSubPach("gps")
        .build();

    public static final Topic T_SIMPLE_WAYPOINT_CONTROLLER = new TopicBuilder()
        .setId(6)
        .setAdapterClassName("cpcc.ros.actuators.SimpleWayPointControllerAdapter")
        .setCategory(TopicCategory.WAYPOINT_CONTROLLER)
        .setMessageType("big_actor_msgs/LatLngAlt")
        .setNodeType(RosNodeType.SUBSCRIBER)
        .setSubPach("waypoint")
        .build();

    public static final Topic T_ALTITUDE_OVER_GROUND = new TopicBuilder()
        .setId(7)
        .setAdapterClassName("cpcc.ros.sensors.AltimeterAdapter")
        .setCategory(TopicCategory.ALTITUDE_OVER_GROUND)
        .setMessageType("std_msgs/Float32")
        .setNodeType(RosNodeType.PUBLISHER)
        .setSubPach("sonar")
        .build();

    public static final Topic T_GS_GPS_POSITION_PROVIDER = new TopicBuilder()
        .setId(12)
        .setAdapterClassName("cpcc.ros.sensors.GroundStationGpsPositionAdapter")
        .setCategory(TopicCategory.GPS_POSITION_PROVIDER)
        .setMessageType("sensor_msgs/NavSatFix")
        .setNodeType(RosNodeType.PUBLISHER)
        .build();

    private TopicConstants()
    {
        // Intentionally empty.
    }
}
