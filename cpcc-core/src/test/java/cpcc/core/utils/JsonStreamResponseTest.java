// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.http.services.Response;
import org.apache.tapestry5.json.JSONObject;
import org.testng.annotations.Test;

public class JsonStreamResponseTest
{
    @Test
    public void shouldReturnJsonObjectAsStream() throws IOException
    {
        JSONObject obj = new JSONObject("{\"a\":10}");
        byte[] expected = obj.toCompactString().getBytes("UTF-8");

        JsonStreamResponse sut = new JsonStreamResponse(obj);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIgnoreCallsToPrepareResponse() throws IOException
    {
        JSONObject obj = new JSONObject("{\"a\":10}");
        byte[] expected = obj.toCompactString().getBytes("UTF-8");

        JsonStreamResponse sut = new JsonStreamResponse(obj);

        Response response = mock(Response.class);
        sut.prepareResponse(response);

        verifyZeroInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }
}
