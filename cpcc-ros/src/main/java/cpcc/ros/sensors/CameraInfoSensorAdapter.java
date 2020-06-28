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

package cpcc.ros.sensors;

import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CameraInfoAdapter
 */
public class CameraInfoSensorAdapter extends AbstractSensorAdapter
{
    private static final Logger LOG = LoggerFactory.getLogger(CameraInfoSensorAdapter.class);

    private sensor_msgs.CameraInfo cameraInfo;

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorType getType()
    {
        return SensorType.CAMERA_INFO;
    }

    /**
     * @return the current camera info.
     */
    public sensor_msgs.CameraInfo getCameraInfo()
    {
        return cameraInfo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        super.onStart(connectedNode);
        LOG.debug("onStart()");

        Subscriber<sensor_msgs.CameraInfo> cameraInfoSubscriber =
            connectedNode.newSubscriber(getTopic().getName(), sensor_msgs.CameraInfo._TYPE);

        cameraInfoSubscriber.addMessageListener(message -> cameraInfo = message);

        setStartCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public sensor_msgs.CameraInfo getValue()
    {
        return getCameraInfo();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setValue(Object object)
    {
        throw new IllegalStateException();
    }
}
