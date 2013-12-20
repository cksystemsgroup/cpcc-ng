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
package at.uni_salzburg.cs.cpcc.ros.sim.osm;

import java.io.IOException;
import java.nio.ByteOrder;

import org.jboss.netty.buffer.ChannelBuffers;
import org.ros.concurrent.CancellableLoop;
import org.ros.node.ConnectedNode;
import org.ros.node.topic.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
import at.uni_salzburg.cs.cpcc.ros.sim.AnonymousNodeMain;

/**
 * ImagePublisherNode
 */
public class ImagePublisherNode extends AnonymousNodeMain<sensor_msgs.NavSatFix>
{
    private static final Logger LOG = LoggerFactory.getLogger(ImagePublisherNode.class);
    private Configuration config;
    private Camera camera;

    /**
     * @param config the configuration.
     */
    public ImagePublisherNode(Configuration config)
    {
        this.config = config;
        camera = new Camera(config);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onStart(final ConnectedNode connectedNode)
    {
        LOG.info("onStart");

        final Publisher<sensor_msgs.Image> imagePublisher =
            connectedNode.newPublisher(config.getTopicRoot() + "/image", sensor_msgs.Image._TYPE);

        final Publisher<sensor_msgs.CameraInfo> infoPublisher =
            connectedNode.newPublisher(config.getTopicRoot() + "/camera_info", sensor_msgs.CameraInfo._TYPE);

        CancellableLoop loop = new CancellableLoop()
        {
            @Override
            protected void loop() throws InterruptedException
            {
                if (getReceivedMessage() == null)
                {
                    return;
                }
                PolarCoordinate position =
                    new PolarCoordinate(getReceivedMessage().getLatitude(), getReceivedMessage().getLongitude(),
                        getReceivedMessage().getAltitude());
                try
                {
                    byte[] image = camera.getImage(position);
                    sensor_msgs.Image message =
                        connectedNode.getTopicMessageFactory().newFromType(sensor_msgs.Image._TYPE);
                    message.setIsBigendian((byte) 0);
                    message.setData(ChannelBuffers.copiedBuffer(ByteOrder.nativeOrder(), image));
                    message.setEncoding("png");
                    message.setHeight(config.getCameraHeight());
                    message.setWidth(config.getCameraWidth());
                    message.setStep(0);
                    imagePublisher.publish(message);

                    sensor_msgs.CameraInfo info =
                        connectedNode.getTopicMessageFactory().newFromType(sensor_msgs.CameraInfo._TYPE);
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
        };

        connectedNode.executeCancellableLoop(loop);
    }

}
