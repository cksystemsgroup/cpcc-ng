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
import org.geojson.FeatureCollection;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class GeoJsonStreamResponseTest
{
    @Test
    public void shouldReturnFeatureCollectionAsJsonStream() throws IOException
    {
        FeatureCollection featureCollection = new FeatureCollection();
        byte[] expected = new ObjectMapper()
            .disable(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsBytes(featureCollection);

        GeoJsonStreamResponse sut = new GeoJsonStreamResponse(featureCollection);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIgnoreCallsToPrepareResponse() throws IOException
    {
        FeatureCollection featureCollection = new FeatureCollection();
        byte[] expected = new ObjectMapper()
            .disable(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsBytes(featureCollection);

        GeoJsonStreamResponse sut = new GeoJsonStreamResponse(featureCollection);

        Response response = mock(Response.class);
        sut.prepareResponse(response);

        verifyNoInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }

}
