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

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * CommunicationResponse
 */
public class CommunicationResponse
{
    /**
     * Status
     */
    public enum Status
    {
        NOT_OK,
        OK
    }

    private Status status;
    private byte[] content;

    /**
     * @return the current status.
     */
    public Status getStatus()
    {
        return status;
    }

    /**
     * @param status the status to set.
     */
    public void setStatus(Status status)
    {
        this.status = status;
    }

    /**
     * @return the response content as a string.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP", justification = "Exposed on purpose")
    public byte[] getContent()
    {
        return content;
    }

    /**
     * @param content the response content to set.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose")
    public void setContent(byte[] content)
    {
        this.content = content;
    }
}
