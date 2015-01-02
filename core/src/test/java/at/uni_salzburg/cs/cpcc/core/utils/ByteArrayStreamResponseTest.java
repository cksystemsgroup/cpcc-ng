package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.services.Response;
import org.testng.annotations.Test;

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

        verifyZeroInteractions(response);

        byte[] actual = IOUtils.toByteArray(sut.getStream());

        assertThat(sut.getContentType()).isEqualTo(CONTENT_TYPE_TEXT);
        assertThat(actual).isEqualTo(expected);
    }
}
