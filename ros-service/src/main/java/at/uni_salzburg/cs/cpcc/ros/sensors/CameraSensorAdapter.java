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

package at.uni_salzburg.cs.cpcc.ros.sensors;

import org.ros.message.MessageListener;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Subscriber;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sensor_msgs.CameraInfo;
import at.uni_salzburg.cs.cpcc.ros.base.RosTopic;

/**
 * CameraSensor
 */
public class CameraSensorAdapter extends AbstractSensorAdapter implements ImageProvider
{
    private static final Logger LOG = LoggerFactory.getLogger(CameraSensorAdapter.class);
    private RosTopic infoTopic;
    private sensor_msgs.CameraInfo cameraInfo;
    private sensor_msgs.Image image;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(ConnectedNode connectedNode)
    {
        super.onStart(connectedNode);
        LOG.debug("onStart()");

        Subscriber<sensor_msgs.CameraInfo> cameraInfoSubscriber =
            connectedNode.newSubscriber(getInfoTopic().getName(), sensor_msgs.CameraInfo._TYPE);

        cameraInfoSubscriber.addMessageListener(new MessageListener<sensor_msgs.CameraInfo>()
        {
            @Override
            public void onNewMessage(sensor_msgs.CameraInfo message)
            {
                cameraInfo = message;
            }
        });

        Subscriber<sensor_msgs.Image> imageSubscriber =
            connectedNode.newSubscriber(getTopic().getName(), sensor_msgs.Image._TYPE);

        imageSubscriber.addMessageListener(new MessageListener<sensor_msgs.Image>()
        {
            @Override
            public void onNewMessage(sensor_msgs.Image message)
            {
                image = message;
            }
        });

        setStartCompleted();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SensorType getType()
    {
        return SensorType.CAMERA;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public sensor_msgs.Image getImage()
    {
        return image;
    }

    /**
     * {@inheritDoc}
     */
    public CameraInfo getCameraInfo()
    {
        return cameraInfo;
    }

    /**
     * @return the info topic.
     */
    public RosTopic getInfoTopic()
    {
        return infoTopic;
    }

    /**
     * @param infoTopic the info topic.
     */
    public void setInfoTopic(RosTopic infoTopic)
    {
        this.infoTopic = infoTopic;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public sensor_msgs.Image getValue()
    {
        return image;
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
