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

package cpcc.core.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * MappingAttributesPK
 */
@SuppressFBWarnings("SE_BAD_FIELD")
@Embeddable
public class MappingAttributesPK implements Serializable
{
    private static final long serialVersionUID = -1018515034103162936L;

    @ManyToOne
    @JoinColumn(name = "DEVICE_ID", referencedColumnName = "ID")
    private Device device;

    @ManyToOne
    @JoinColumn(name = "TOPIC_ID", referencedColumnName = "ID")
    private Topic topic;

    /**
     * Constructor
     */
    public MappingAttributesPK()
    {
        // intentionally empty
    }

    /**
     * @param device the device.
     * @param topic the topic.
     */
    public MappingAttributesPK(Device device, Topic topic)
    {
        this.device = device;
        this.topic = topic;
    }

    /**
     * @return the device
     */
    public Device getDevice()
    {
        return device;
    }

    /**
     * @param device the device to set
     */
    public void setDevice(Device device)
    {
        this.device = device;
    }

    /**
     * @return the topic
     */
    public Topic getTopic()
    {
        return topic;
    }

    /**
     * @param topic the topic to set
     */
    public void setTopic(Topic topic)
    {
        this.topic = topic;
    }

}
