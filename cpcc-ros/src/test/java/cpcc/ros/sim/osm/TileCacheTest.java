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
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.mockserver.model.Header.header;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.stream.Stream;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockserver.client.MockServerClient;
import org.mockserver.junit.jupiter.MockServerExtension;
import org.mockserver.model.MediaType;

/**
 * Tile Cache Test
 */
@ExtendWith(MockServerExtension.class)
public class TileCacheTest
{
    private static final String USER_AGENT_HEADER = "User-Agent";

    private static final String USER_AGENT = "TileCache/1.0";

    private static final String RESPONSE_DATA_01 = "this is just a test 01.";
    private static final String RESPONSE_DATA_02 = "this is just a test 02.";
    private static final String RESPONSE_DATA_03 = "this is just a test 03.";
    private static final String RESPONSE_DATA_04 = "this is just a test 04.";

    private Configuration config;
    private TileCache sut;
    private TileCache sutSpy;
    private File tempDirectory;
    private MockServerClient client;

    @BeforeEach
    public void setUp(MockServerClient client) throws Exception
    {
        this.client = client;

        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/18/1234/5523.png")
                    .withHeader(header(USER_AGENT_HEADER, USER_AGENT)))
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.PNG)
                    .withBody(RESPONSE_DATA_01));

        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/18/1234/5524.png")
                    .withHeader(header(USER_AGENT_HEADER, USER_AGENT)))
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.PNG)
                    .withBody(RESPONSE_DATA_02));

        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/18/1233/5523.png")
                    .withHeader(header(USER_AGENT_HEADER, USER_AGENT)))
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.PNG)
                    .withBody(RESPONSE_DATA_03));

        client
            .when(
                request()
                    .withMethod("GET")
                    .withPath("/18/1233/5524.png")
                    .withHeader(header(USER_AGENT_HEADER, USER_AGENT)))
            .respond(
                response()
                    .withStatusCode(200)
                    .withContentType(MediaType.PNG)
                    .withBody(RESPONSE_DATA_04));

        File path = new File("target/");
        tempDirectory = Files.createTempDirectory(path.toPath(), "tmp-test").toFile();
        assertThat(tempDirectory).exists();

        config = Mockito.mock(Configuration.class);
        when(config.getTileCacheBaseDir())
            .thenReturn(tempDirectory.getAbsolutePath());
        when(config.getTileServerUrl())
            .thenReturn("http://localhost:" + client.getPort() + "/%1$d/%2$d/%3$d.png");

        sut = new TileCache(config);
        sutSpy = spy(sut);
    }

    @AfterEach
    public void tearDown() throws IOException
    {
        FileUtils.forceDelete(tempDirectory);
    }

    static Stream<Arguments> tileCoordinatesDataProvider()
    {
        return Stream.of(
            arguments(18, 1234, 5523, RESPONSE_DATA_01),
            arguments(18, 1234, 5524, RESPONSE_DATA_02),
            arguments(18, 1233, 5523, RESPONSE_DATA_03),
            arguments(18, 1233, 5524, RESPONSE_DATA_04));
    }

    @ParameterizedTest
    @MethodSource("tileCoordinatesDataProvider")
    public void shouldCreateTileCacheDirectoryIfNotPresent(int zoom, int x, int y) throws IOException
    {
        FileUtils.deleteDirectory(tempDirectory);
        assertThat(tempDirectory).doesNotExist();

        File file = sut.getTile(zoom, x, y);

        assertThat(file.exists()).isTrue();
        FileUtils.forceDelete(file);
        assertThat(file).doesNotExist();
        assertThat(tempDirectory).exists();

        file = sut.getTile(zoom, x, y);

        assertThat(file.exists()).isTrue();
        assertThat(tempDirectory).exists();
    }

    @ParameterizedTest
    @MethodSource("tileCoordinatesDataProvider")
    public void shouldCreateTileCacheFile(int zoom, int x, int y, String expectedData) throws Exception
    {
        File file = sut.getTile(zoom, x, y);

        String content = IOUtils.toString(new FileInputStream(file), "UTF-8");
        assertThat(content).isNotNull().isEqualTo(expectedData);
    }

    @ParameterizedTest
    @MethodSource("tileCoordinatesDataProvider")
    public void shouldLoadCachedTileCacheFile(int zoom, int x, int y, String expectedData) throws Exception
    {
        File file1 = sutSpy.getTile(zoom, x, y);
        String content1 = IOUtils.toString(new FileInputStream(file1), "UTF-8");
        assertThat(content1).isNotNull().isEqualTo(expectedData);

        File file2 = sutSpy.getTile(zoom, x, y);
        String content2 = IOUtils.toString(new FileInputStream(file2), "UTF-8");
        assertThat(content2).isNotNull().isEqualTo(expectedData);

        verify(sutSpy, times(2)).getTile(zoom, x, y);
        verify(sutSpy).downloadFile(anyString(), any(File.class));
        verifyNoMoreInteractions(sutSpy);
    }

    @ParameterizedTest
    @MethodSource("tileCoordinatesDataProvider")
    public void shouldHandleIncorrectDownloadUrl(int zoom, int x, int y) throws Exception
    {
        try
        {
            sut.downloadFile("bugger://noUrl/noPath", null);
            Fail.failBecauseExceptionWasNotThrown(IOException.class);
        }
        catch (IOException e)
        {
            assertThat(e).hasMessage("unknown protocol: bugger");
        }
    }

    static Stream<Arguments> notExistingTileCoordinatesDataProvider()
    {
        return Stream.of(
            arguments(31, 1234, 5523),
            arguments(31, 1234, 5524),
            arguments(31, 1233, 5523),
            arguments(31, 1233, 5524));
    }

    @ParameterizedTest
    @MethodSource("notExistingTileCoordinatesDataProvider")
    public void shouldHandleDownLoadErrors(int zoom, int x, int y) throws IOException
    {
        try
        {
            sut.getTile(zoom, x, y);
            Fail.failBecauseExceptionWasNotThrown(IOException.class);
        }
        catch (FileNotFoundException e)
        {
            String msg = String.format("http://localhost:%d/%d/%d/%d.png", client.getPort(), zoom, x, y);
            assertThat(e).hasMessage(msg);
        }
    }
}
