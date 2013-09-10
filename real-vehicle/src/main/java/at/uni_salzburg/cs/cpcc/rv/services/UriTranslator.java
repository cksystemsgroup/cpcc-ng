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
package at.uni_salzburg.cs.cpcc.rv.services;

import java.net.URI;
import java.net.URISyntaxException;

import org.apache.tapestry5.Field;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.services.FormSupport;

/**
 * URITranslator
 */
public class UriTranslator implements Translator<URI>
{
    private final String name;
    private final Class<URI> type;
    private final String messageKey;

    /**
     * URITranslator constructor.
     *
     * @param name the name.
     */
    public UriTranslator(String name)
    {
        this.name = name;
        this.type = URI.class;
        this.messageKey = "uri-format-exception";
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
    public String toClient(URI value)
    {
        return value == null ? "" : value.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<URI> getType()
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
    public URI parseClient(Field field, String clientValue, String message) throws ValidationException
    {
        try
        {
            return clientValue == null ? null : new URI(clientValue);
        }
        catch (URISyntaxException e)
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
