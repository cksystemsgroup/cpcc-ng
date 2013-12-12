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
package at.uni_salzburg.cs.cpcc.com.services;

import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;

/**
 * MyStatusLine
 */
public class MyStatusLine implements StatusLine
{
    private ProtocolVersion protocolVersion;
    private int statusCode;
    private String reasonPhrase;

    /**
     * @param protocolVersion the protocol version.
     * @param statusCode the status code.
     * @param reasonPhrase the reason phrase.
     */
    public MyStatusLine(ProtocolVersion protocolVersion, int statusCode, String reasonPhrase)
    {
        this.protocolVersion = protocolVersion;
        this.statusCode = statusCode;
        this.reasonPhrase = reasonPhrase;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProtocolVersion getProtocolVersion()
    {
        return protocolVersion;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getStatusCode()
    {
        return statusCode;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getReasonPhrase()
    {
        return reasonPhrase;
    }
}
