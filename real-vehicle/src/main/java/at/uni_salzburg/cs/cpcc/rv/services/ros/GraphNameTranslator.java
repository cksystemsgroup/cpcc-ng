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
package at.uni_salzburg.cs.cpcc.rv.services.ros;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;
import org.ros.exception.RosRuntimeException;
import org.ros.namespace.GraphName;

/**
 * GraphNameTranslator
 */
public class GraphNameTranslator implements Translator<GraphName>
{
    private final String name;
    private final Class<GraphName> type;
    private final String messageKey;

    /**
     * GraphNameTranslator constructor.
     *
     * @param name the name.
     */
    public GraphNameTranslator(String name)
    {
        this.name = name;
        this.type = GraphName.class;
        this.messageKey = "graphname-format-exception";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toClient(GraphName value)
    {
        return value == null ? "" : value.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<GraphName> getType()
    {
        return type;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getMessageKey()
    {
        return messageKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public GraphName parseClient(Field field, String clientValue, String message) throws ValidationException
    {
        try
        {
            return clientValue == null ? GraphName.empty() : GraphName.of(clientValue);
        }
        catch (RosRuntimeException e)
        {
            throw new ValidationException(message);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Field field, String message, MarkupWriter writer, FormSupport formSupport)
    {
        // Intentionally empty.
    }

}
