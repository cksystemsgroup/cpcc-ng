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

package at.uni_salzburg.cs.cpcc.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Response;

/**
 * JsonStreamResponse
 */
public class JsonStreamResponse implements StreamResponse
{
    private JSONObject jsonObject;

    /**
     * @param jsonObject the JSON object to be streamed.
     */
    public JsonStreamResponse(JSONObject jsonObject)
    {
        this.jsonObject = jsonObject;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getContentType()
    {
        return "application/json";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getStream() throws IOException
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream(1024);
        OutputStreamWriter streamWriter = new OutputStreamWriter(out, "UTF-8");
        PrintWriter writer = new PrintWriter(streamWriter);
        jsonObject.print(writer);
        writer.close();
        out.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void prepareResponse(Response response)
    {
        // intentionally empty.
    }

}
