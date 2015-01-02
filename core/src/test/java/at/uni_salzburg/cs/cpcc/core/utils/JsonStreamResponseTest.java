package at.uni_salzburg.cs.cpcc.core.utils;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyZeroInteractions;

import java.io.IOException;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.Response;
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
