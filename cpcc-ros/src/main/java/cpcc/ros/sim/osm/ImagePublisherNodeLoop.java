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

package cpcc.ros.sim.osm;

import java.io.IOException;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.concurrent.CancellableLoop;
import org.ros.message.MessageFactory;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cpcc.core.entities.PolarCoordinate;
import sensor_msgs.CameraInfo;
import sensor_msgs.Image;
import sensor_msgs.NavSatFix;

/**
 * Image Publisher Node Loop
 */
public class ImagePublisherNodeLoop extends CancellableLoop
{
    private static final Logger LOG = LoggerFactory.getLogger(ImagePublisherNodeLoop.class);

    private Configuration config;
    private ConnectedNode connectedNode;
    private Publisher<Image> imagePublisher;
    private Publisher<CameraInfo> infoPublisher;
    private Camera camera;
    private NavSatFix message;

    /**
     * @param config the configuration.
     * @param connectedNode the connected node.
     */
    public ImagePublisherNodeLoop(Configuration config, ConnectedNode connectedNode)
    {
        this.config = config;
        this.connectedNode = connectedNode;

        camera = new Camera(config);

        imagePublisher =
            connectedNode.newPublisher(config.getTopicRoot() + "/image", sensor_msgs.Image._TYPE);

        infoPublisher =
            connectedNode.newPublisher(config.getTopicRoot() + "/camera_info", sensor_msgs.CameraInfo._TYPE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void loop() throws InterruptedException
    {
        if (message == null)
        {
            return;
        }

        PolarCoordinate position =
            new PolarCoordinate(message.getLatitude(), message.getLongitude(), message.getAltitude());

        MessageFactory factory = connectedNode.getTopicMessageFactory();

        try
        {
            byte[] image = camera.getImage(position);
            sensor_msgs.Image imageMessage = factory.newFromType(sensor_msgs.Image._TYPE);
            imageMessage.setIsBigendian((byte) 0);
            imageMessage.setData(ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), image));
            imageMessage.setEncoding("png");
            imageMessage.setHeight(config.getCameraHeight());
            imageMessage.setWidth(config.getCameraWidth());
            imageMessage.setStep(0);
            imagePublisher.publish(imageMessage);

            sensor_msgs.CameraInfo info = factory.newFromType(sensor_msgs.CameraInfo._TYPE);
            info.setHeight(config.getCameraHeight());
            info.setWidth(config.getCameraWidth());
            infoPublisher.publish(info);
        }
        catch (IOException e)
        {
            LOG.error("Can not get camera image.", e);
        }

        Thread.sleep(1000);
    }

    /**
     * @return the received message.
     */
    public NavSatFix getMessage()
    {
        return message;
    }

    /**
     * @param message the message to set.
     */
    public void setMessage(NavSatFix message)
    {
        this.message = message;
    }
}
