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

import sensor_msgs.CameraInfo;
import sensor_msgs.Image;
import sensor_msgs.NavSatFix;
import cpcc.core.utils.PolarCoordinate;
import cpcc.ros.sim.AnonymousNodeMain;

/**
 * Image Publisher Node Loop
 */
public class ImagePublisherNodeLoop extends CancellableLoop
{
    private static final Logger LOG = LoggerFactory.getLogger(ImagePublisherNodeLoop.class);

    private Configuration config;
    private AnonymousNodeMain<NavSatFix> node;
    private ConnectedNode connectedNode;
    private Publisher<Image> imagePublisher;
    private Publisher<CameraInfo> infoPublisher;
    private Camera camera;

    /**
     * @param config the configuration.
     * @param node the node
     * @param connectedNode the connected node.
     */
    public ImagePublisherNodeLoop(Configuration config, AnonymousNodeMain<sensor_msgs.NavSatFix> node
        , ConnectedNode connectedNode)
    {
        this.config = config;
        this.node = node;
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
        NavSatFix msg = node.getReceivedMessage();

        if (msg == null)
        {
            return;
        }

        PolarCoordinate position = new PolarCoordinate(msg.getLatitude(), msg.getLongitude(), msg.getAltitude());

        MessageFactory factory = connectedNode.getTopicMessageFactory();

        try
        {
            byte[] image = camera.getImage(position);
            sensor_msgs.Image message = factory.newFromType(sensor_msgs.Image._TYPE);
            message.setIsBigendian((byte) 0);
            message.setData(ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), image));
            message.setEncoding("png");
            message.setHeight(config.getCameraHeight());
            message.setWidth(config.getCameraWidth());
            message.setStep(0);
            imagePublisher.publish(message);

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
}
