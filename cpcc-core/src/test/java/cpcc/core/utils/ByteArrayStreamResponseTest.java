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
import static org.mockito.Mockito.verifyNoInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.http.services.Response;
import org.junit.jupiter.api.Test;

public class ByteArrayStreamResponseTest
{
    private static final String CONTENT_TYPE_TEXT = "application/text";

    @Test
    public void shouldReturnByteArrayAsStream() throws IOException
    {
        byte[] expected = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74};

        ByteArrayStreamResponse sut = new ByteArrayStreamResponse(CONTENT_TYPE_TEXT, expected);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo(CONTENT_TYPE_TEXT);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIgnoreCallsToPrepareResponse() throws IOException
    {
        byte[] expected = new byte[]{65, 66, 67, 68, 69, 70, 71, 72, 73, 74};

        ByteArrayStreamResponse sut = new ByteArrayStreamResponse(CONTENT_TYPE_TEXT, expected);

        Response response = mock(Response.class);
        sut.prepareResponse(response);

        verifyNoInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo(CONTENT_TYPE_TEXT);
        assertThat(actual).isEqualTo(expected);
    }
}
