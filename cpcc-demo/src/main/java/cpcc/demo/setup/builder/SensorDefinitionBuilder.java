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

import java.util.Date;

import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;
import cpcc.core.entities.SensorVisibility;

/**
 * SensorDefinitionBuilder implementation.
 */
public class SensorDefinitionBuilder
{
    private SensorDefinition sd = new SensorDefinition();

    /**
     * @param id the id to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setId(Integer id)
    {
        sd.setId(id);
        return this;
    }

    /**
     * @param description the description to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setDescription(String description)
    {
        sd.setDescription(description);
        return this;
    }

    /**
     * @param type the type to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setType(SensorType type)
    {
        sd.setType(type);
        return this;
    }

    /**
     * @param parameters the parameters to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setParameters(String parameters)
    {
        sd.setParameters(parameters);
        return this;
    }

    /**
     * @param visibility the visibility to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setVisibility(SensorVisibility visibility)
    {
        sd.setVisibility(visibility);
        return this;
    }

    /**
     * @param now the last update time stamp to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setLastUpdate(Date now)
    {
        sd.setLastUpdate(now);
        return this;
    }

    /**
     * @param messageType the ROS message type to set
     * @return this instance.
     */
    public SensorDefinitionBuilder setMessageType(String messageType)
    {
        sd.setMessageType(messageType);
        return this;
    }

    /**
     * @param deleted set to true if this record is to be considered as deleted.
     * @return this instance.
     */
    public SensorDefinitionBuilder setDeleted(Boolean deleted)
    {
        sd.setDeleted(deleted);
        return this;
    }

    /**
     * @return the newly build sensor definition.
     */
    public SensorDefinition build()
    {
        return sd;
    }
}
