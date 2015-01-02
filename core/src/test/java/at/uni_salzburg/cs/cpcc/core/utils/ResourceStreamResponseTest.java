package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.services.Response;
import org.testng.annotations.Test;

public class ResourceStreamResponseTest
{
    private static final String CONTENT_TYPE_PNG = "application/png";
    private static final String PNG_RESOURCE_NAME = "at/uni_salzburg/cs/cpcc/core/utils/red_tile.png";

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
