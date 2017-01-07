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

package cpcc.ros.sim.osm;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.assertj.core.api.Fail;
import org.easymock.EasyMock;
import org.mockito.Mockito;
import org.powermock.api.easymock.PowerMock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.testng.PowerMockTestCase;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * Tile Cache Test
 */
@PrepareForTest(HttpClientBuilder.class)
public class TileCacheTest extends PowerMockTestCase
{
    private static final String USER_AGENT = "TileCache/1.0";

    private Configuration config;

    private TileCache sut;

    private HttpClientBuilder httpClientBuilderMock;

    private File tempDirectory;

    private CloseableHttpClient client;

    private HttpEntity entity;

    private CloseableHttpResponse response;

    private String responseData = "this is just a test.";

    private StatusLine statusLine200ok = new StatusLine()
    {
        @Override
        public int getStatusCode()
        {
            return HttpStatus.SC_OK;
        }

        @Override
        public String getReasonPhrase()
        {
            return "200 OK";
        }

        @Override
        public ProtocolVersion getProtocolVersion()
        {
            return new ProtocolVersion("HTTP", 1, 0);
        }
    };

    private StatusLine statusLine503error = new StatusLine()
    {
        @Override
        public int getStatusCode()
        {
            return HttpStatus.SC_SERVICE_UNAVAILABLE;
        }

        @Override
        public String getReasonPhrase()
        {
            return "503 Service Unavailable";
        }

        @Override
        public ProtocolVersion getProtocolVersion()
        {
            return new ProtocolVersion("HTTP", 1, 0);
        }
    };

    @BeforeMethod
    public void setUp() throws Exception
    {
        File path = new File("target/");
        tempDirectory = Files.createTempDirectory(path.toPath(), "tmp-test").toFile();
        assertThat(tempDirectory).exists();

        config = PowerMockito.mock(Configuration.class);
        PowerMockito.doReturn(tempDirectory.getAbsolutePath()).when(config).getTileCacheBaseDir();
        PowerMockito.doReturn("http://my.tile.server/%1$d/%2$d/%3$d.png").when(config).getTileServerUrl();

        sut = new TileCache(config);

        entity = Mockito.mock(HttpEntity.class);
        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseData.getBytes("UTF-8")));

        response = PowerMockito.mock(CloseableHttpResponse.class);
        PowerMockito.when(response.getStatusLine()).thenReturn(statusLine200ok);
        PowerMockito.when(response.getEntity()).thenReturn(entity);

        client = PowerMockito.mock(CloseableHttpClient.class);
        PowerMockito.doReturn(response).when(client).execute(any(HttpUriRequest.class));

        httpClientBuilderMock = PowerMock.createMock(HttpClientBuilder.class);

        PowerMock.mockStatic(HttpClientBuilder.class);
        EasyMock.expect(HttpClientBuilder.create()).andReturn(httpClientBuilderMock).anyTimes();
        PowerMock.replay(HttpClientBuilder.class);
        EasyMock.expect(httpClientBuilderMock.setUserAgent(USER_AGENT)).andReturn(httpClientBuilderMock).anyTimes();
        PowerMock.replay(HttpClientBuilder.class);
        EasyMock.expect(httpClientBuilderMock.build()).andReturn(client).anyTimes();
        PowerMock.replay(HttpClientBuilder.class, httpClientBuilderMock);
    }

    @AfterMethod
    public void tearDown() throws IOException
    {
        FileUtils.forceDelete(tempDirectory);
    }

    @DataProvider
    public Object[][] tileCoordinatesDataProvider()
    {
        return new Object[][]{
            new Object[]{18, 1234, 5523},
            new Object[]{18, 1234, 5524},
            new Object[]{18, 1233, 5523},
            new Object[]{18, 1233, 5524},
        };
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldCreateTileCacheDirectoryIfNotPresent(int zoom, int x, int y) throws IOException
    {
        FileUtils.deleteDirectory(tempDirectory);
        assertThat(tempDirectory.exists()).isFalse();
        File file = sut.getTile(zoom, x, y);
        assertThat(file.exists()).isTrue();
        assertThat(tempDirectory.exists()).isTrue();
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldCreateTileCacheFile(int zoom, int x, int y) throws Exception
    {
        assertThat(sut).isNotNull();

        File file = sut.getTile(zoom, x, y);

        String content = IOUtils.toString(new FileInputStream(file), "UTF-8");
        assertThat(content).isNotNull().isEqualTo(responseData);
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldLoadCachedTileCacheFile(int zoom, int x, int y) throws Exception
    {
        assertThat(sut).isNotNull();

        File file1 = sut.getTile(zoom, x, y);
        String content1 = IOUtils.toString(new FileInputStream(file1), "UTF-8");
        assertThat(content1).isNotNull().isEqualTo(responseData);
        Mockito.verify(client).execute(any(HttpUriRequest.class));

        File file2 = sut.getTile(zoom, x, y);
        String content2 = IOUtils.toString(new FileInputStream(file2), "UTF-8");
        assertThat(content2).isNotNull().isEqualTo(responseData);
        Mockito.verify(client).execute(any(HttpUriRequest.class));
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldRecognizeExistingTileCacheFolder(int zoom, int x, int y) throws Exception
    {
        assertThat(sut).isNotNull();

        File file1 = sut.getTile(zoom, x, y);
        String content1 = IOUtils.toString(new FileInputStream(file1), "UTF-8");
        assertThat(content1).isNotNull().isEqualTo(responseData);
        Mockito.verify(client).execute(any(HttpUriRequest.class));

        when(entity.getContent()).thenReturn(new ByteArrayInputStream(responseData.getBytes("UTF-8")));

        File file2 = sut.getTile(zoom, x, y + 1);
        String content2 = IOUtils.toString(new FileInputStream(file2), "UTF-8");
        assertThat(content2).isNotNull().isEqualTo(responseData);
        Mockito.verify(client, times(2)).execute(any(HttpUriRequest.class));
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldHandleIOEAtDownload(int zoom, int x, int y) throws Exception
    {
        String msg = "thrown on purpose";
        assertThat(sut).isNotNull();
        PowerMockito.doThrow(new IOException(msg)).when(client).execute(any(HttpUriRequest.class));

        try
        {
            sut.getTile(zoom, x, y);
            Fail.failBecauseExceptionWasNotThrown(IOException.class);
        }
        catch (IOException e)
        {
            assertThat(e).hasMessage(msg);
        }
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldHandleDownLoadErrors(int zoom, int x, int y) throws IOException
    {
        PowerMockito.when(response.getStatusLine()).thenReturn(statusLine503error);

        try
        {
            sut.getTile(zoom, x, y);
            Fail.failBecauseExceptionWasNotThrown(IOException.class);
        }
        catch (IOException e)
        {
            String msg = String.format("Can not load URL 'http://my.tile.server/%d/%d/%d.png' "
                + "code=503 (503 Service Unavailable)", zoom, x, y);
            assertThat(e).hasMessage(msg);
        }
    }

    @Test(dataProvider = "tileCoordinatesDataProvider")
    public void shouldHandleNullResponseWithoutExceptions(int zoom, int x, int y) throws IOException
    {
        PowerMockito.when(response.getEntity()).thenReturn(null);
        //        when(entity.getContent()).thenReturn(null);

        sut.getTile(zoom, x, y);
    }
}
