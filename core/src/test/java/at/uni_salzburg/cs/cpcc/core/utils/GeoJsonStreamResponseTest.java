package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.services.Response;
import org.geojson.FeatureCollection;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GeoJsonStreamResponseTest
{

    @Test
    public void shouldReturnFeatureCollectionAsJsonStream() throws IOException
    {
        FeatureCollection featureCollection = new FeatureCollection();
        byte[] expected = new ObjectMapper().writeValueAsBytes(featureCollection);

        GeoJsonStreamResponse sut = new GeoJsonStreamResponse(featureCollection);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void shouldIgnoreCallsToPrepareResponse() throws IOException
    {
        FeatureCollection featureCollection = new FeatureCollection();
        byte[] expected = new ObjectMapper().writeValueAsBytes(featureCollection);

        GeoJsonStreamResponse sut = new GeoJsonStreamResponse(featureCollection);

        Response response = mock(Response.class);
        sut.prepareResponse(response);

        verifyZeroInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo("application/json");
        assertThat(actual).isEqualTo(expected);
    }

}
