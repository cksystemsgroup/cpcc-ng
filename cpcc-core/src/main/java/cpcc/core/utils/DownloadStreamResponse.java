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

package cpcc.core.utils;

import org.apache.tapestry5.http.services.Response;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * Download Stream Response implementation.
 */
public class DownloadStreamResponse extends ByteArrayStreamResponse
{
    private String fileName;

    /**
     * @param contentType the content type to be reported to the client.
     * @param data the data to be transmitted to the client.
     * @param fileName the download file name.
     */
    @SuppressFBWarnings(value = "EI_EXPOSE_REP2", justification = "Exposed on purpose!")
    public DownloadStreamResponse(String contentType, byte[] data, String fileName)
    {
        super(contentType, data);

        this.fileName = fileName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareResponse(Response response)
    {
        response.setHeader("content-disposition", "attachment; filename=\"" + fileName + "\"");
    }
}
