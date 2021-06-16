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
import org.testng.annotations.Test;

public class ResourceStreamResponseTest
{
    private static final String CONTENT_TYPE_PNG = "application/png";
    private static final String PNG_RESOURCE_NAME = "cpcc/core/utils/red_tile.png";

    @Test
    public void shouldReturnResourceAsStream() throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        byte[] expected = IOUtils.toByteArray(classLoader.getResourceAsStream(PNG_RESOURCE_NAME));
        assertThat(expected).hasSize(731);

        ResourceStreamResponse sut = new ResourceStreamResponse(CONTENT_TYPE_PNG, PNG_RESOURCE_NAME);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo(CONTENT_TYPE_PNG);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIgnoreCallsToPrepareResponse() throws IOException
    {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        byte[] expected = IOUtils.toByteArray(classLoader.getResourceAsStream(PNG_RESOURCE_NAME));
        assertThat(expected).hasSize(731);

        ResourceStreamResponse sut = new ResourceStreamResponse(CONTENT_TYPE_PNG, PNG_RESOURCE_NAME);

        Response response = mock(Response.class);
        sut.prepareResponse(response);

        verifyZeroInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo(CONTENT_TYPE_PNG);
        assertThat(actual).isEqualTo(expected);
    }
}
