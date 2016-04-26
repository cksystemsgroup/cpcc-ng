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

import cpcc.core.entities.DeviceType;

/**
 * DeviceTypeConstants implementation.
 */
public final class DeviceTypeConstants
{
    public static final DeviceType GS_GPS = new DeviceTypeBuilder()
        .setId(10)
        .setName("Ground Station GPS Position")
        .setMainTopic(TopicConstants.T_GS_GPS_POSITION_PROVIDER)
        .build();

    public static final DeviceType QUADROTOR = new DeviceTypeBuilder()
        .setId(4)
        .setName("Simulated Quadrotor")
        .setClassName("cpcc.ros.sim.quadrotor.NodeGroup")
        .setMainTopic(TopicConstants.T_SIMPLE_WAYPOINT_CONTROLLER)
        .setSubTopics(Arrays.asList(TopicConstants.T_RV_GPS_POSITION_PROVIDER, TopicConstants.T_ALTITUDE_OVER_GROUND))
        .build();

    public static final DeviceType T_SIMULATED_BELLY_MOUNTED_CAMERA = new DeviceTypeBuilder()
        .setId(1)
        .setName("Simulated Belly Mounted Camera")
        .setClassName("cpcc.ros.sim.osm.NodeGroup")
        .setMainTopic(TopicConstants.T_CAMERA)
        .setSubTopics(Arrays.asList(TopicConstants.T_CAMERA_INFO))
        .build();

    private DeviceTypeConstants()
    {
        // intentionally empty.
    }
}
