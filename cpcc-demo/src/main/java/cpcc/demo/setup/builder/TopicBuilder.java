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
 * TopicBuilder implementation.
 */
public class TopicBuilder
{
    private Topic topic;

    /**
     * Public default constructor.
     */
    public TopicBuilder()
    {
        this.topic = new Topic();
    }

    /**
     * @param id the topic ID.
     * @return this instance.
     */
    public TopicBuilder setId(int id)
    {
        topic.setId(id);
        return this;
    }

    /**
     * @param adapterClassName the adapter class name to set
     * @return this instance.
     */
    public TopicBuilder setAdapterClassName(String adapterClassName)
    {
        topic.setAdapterClassName(adapterClassName);
        return this;
    }

    /**
     * @param category the category to set
     * @return this instance.
     */
    public TopicBuilder setCategory(TopicCategory category)
    {
        topic.setCategory(category);
        return this;
    }

    /**
     * @param messageType the message type.
     * @return this instance.
     */
    public TopicBuilder setMessageType(String messageType)
    {
        topic.setMessageType(messageType);
        return this;
    }

    /**
     * @param nodeType the node type.
     * @return this instance.
     */
    public TopicBuilder setNodeType(RosNodeType nodeType)
    {
        topic.setNodeType(nodeType);
        return this;
    }

    /**
     * @param subpath the topic sub-path.
     * @return this instance.
     */
    public TopicBuilder setSubPach(String subpath)
    {
        topic.setSubpath(subpath);
        return this;
    }

    /**
     * @return the newly created topic.
     */
    public Topic build()
    {
        return topic;
    }

}
