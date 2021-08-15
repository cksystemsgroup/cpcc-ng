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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Stream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpProcessor;
import org.apache.http.protocol.HttpProcessorBuilder;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpService;
import org.apache.http.protocol.ResponseConnControl;
import org.apache.http.protocol.ResponseContent;
import org.apache.http.protocol.ResponseDate;
import org.apache.http.protocol.ResponseServer;
import org.apache.http.protocol.UriHttpRequestHandlerMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.WGS84;
import cpcc.ros.test.ProcessUtils;
import cpcc.ros.test.RequestListenerThread;

/**
 * CameraTest
 */
public class CameraTest
{
    private static final String BLACK_TILE = "data/black_tile.png";
    private static final String BLUE_TILE = "data/blue_tile.png";
    private static final String CYAN_TILE = "data/cyan_tile.png";
    private static final String GREEN_TILE = "data/green_tile.png";
    private static final String PURPLE_TILE = "data/purple_tile.png";
    private static final String RED_TILE = "data/red_tile.png";
    private static final String WHITE_TILE = "data/white_tile.png";

    private static final String HEIGHT_0 = "data/height-0.png";
    private static final String HEIGHT_10 = "data/height-10.png";
    private static final String HEIGHT_20 = "data/height-20.png";
    private static final String HEIGHT_40 = "data/height-40.png";
    private static final String HEIGHT_90 = "data/height-90.png";
    private static final String HEIGHT_120 = "data/height-120.png";
    private static final String HEIGHT_150 = "data/height-150.png";
    private static final String HEIGHT_200 = "data/height-200.png";

    private static final File blackTileFile = new File(CameraTest.class.getResource(BLACK_TILE).getFile());
    private static final File blueTileFile = new File(CameraTest.class.getResource(BLUE_TILE).getFile());
    private static final File cyanTileFile = new File(CameraTest.class.getResource(CYAN_TILE).getFile());
    private static final File greenTileFile = new File(CameraTest.class.getResource(GREEN_TILE).getFile());
    private static final File purpleTileFile = new File(CameraTest.class.getResource(PURPLE_TILE).getFile());
    private static final File redTileFile = new File(CameraTest.class.getResource(RED_TILE).getFile());
    private static final File whiteTileFile = new File(CameraTest.class.getResource(WHITE_TILE).getFile());

    private static final String height0 = CameraTest.class.getResource(HEIGHT_0).getFile();
    private static final String height10 = CameraTest.class.getResource(HEIGHT_10).getFile();
    private static final String height20 = CameraTest.class.getResource(HEIGHT_20).getFile();
    private static final String height40 = CameraTest.class.getResource(HEIGHT_40).getFile();
    private static final String height90 = CameraTest.class.getResource(HEIGHT_90).getFile();
    private static final String height120 = CameraTest.class.getResource(HEIGHT_120).getFile();
    private static final String height150 = CameraTest.class.getResource(HEIGHT_150).getFile();
    private static final String height200 = CameraTest.class.getResource(HEIGHT_200).getFile();

    private Configuration config;
    private Camera camera;
    private GeodeticSystem gs;
    private RequestListenerThread requestListenerThread;
    private MyHttpFileHandler testFileHandler;
    private File tempDir;

    @BeforeEach
    public void setUp() throws Exception
    {
        int port = ProcessUtils.getRandomPortNumber(30000, 60000);

        gs = new WGS84();

        tempDir = File.createTempFile("camera", "");
        tempDir.delete();
        FileUtils.forceMkdir(tempDir);

        config = mock(Configuration.class);
        when(config.getCameraHeight()).thenReturn(240);
        when(config.getCameraWidth()).thenReturn(320);
        when(config.getTileHeight()).thenReturn(256);
        when(config.getTileWidth()).thenReturn(256);
        when(config.getOriginPosition()).thenReturn(null);
        when(config.getCameraApertureAngle()).thenReturn(2.0);
        when(config.getTileServerUrl()).thenReturn("http://localhost:" + port + "/%1$d/%2$d/%3$d.png");
        when(config.getZoomLevel()).thenReturn(18);
        when(config.getGeodeticSystem()).thenReturn(gs);
        when(config.getTileCacheBaseDir()).thenReturn(tempDir.getAbsolutePath());

        camera = new Camera(config);

        testFileHandler = new MyHttpFileHandler();
        testFileHandler.getResponses().put("/18/41920/101290.png", new Object[]{"image/png", blackTileFile});
        testFileHandler.getResponses().put("/18/41920/101291.png", new Object[]{"image/png", blueTileFile});
        testFileHandler.getResponses().put("/18/41920/101292.png", new Object[]{"image/png", cyanTileFile});
        testFileHandler.getResponses().put("/18/41920/101293.png", new Object[]{"image/png", greenTileFile});
        testFileHandler.getResponses().put("/18/41920/101294.png", new Object[]{"image/png", purpleTileFile});

        testFileHandler.getResponses().put("/18/41921/101290.png", new Object[]{"image/png", redTileFile});
        testFileHandler.getResponses().put("/18/41921/101291.png", new Object[]{"image/png", whiteTileFile});
        testFileHandler.getResponses().put("/18/41921/101292.png", new Object[]{"image/png", blackTileFile});
        testFileHandler.getResponses().put("/18/41921/101293.png", new Object[]{"image/png", blueTileFile});
        testFileHandler.getResponses().put("/18/41921/101294.png", new Object[]{"image/png", cyanTileFile});

        testFileHandler.getResponses().put("/18/41922/101290.png", new Object[]{"image/png", greenTileFile});
        testFileHandler.getResponses().put("/18/41922/101291.png", new Object[]{"image/png", purpleTileFile});
        testFileHandler.getResponses().put("/18/41922/101292.png", new Object[]{"image/png", redTileFile});
        testFileHandler.getResponses().put("/18/41922/101293.png", new Object[]{"image/png", whiteTileFile});
        testFileHandler.getResponses().put("/18/41922/101294.png", new Object[]{"image/png", blackTileFile});

        testFileHandler.getResponses().put("/18/41923/101290.png", new Object[]{"image/png", blueTileFile});
        testFileHandler.getResponses().put("/18/41923/101291.png", new Object[]{"image/png", cyanTileFile});
        testFileHandler.getResponses().put("/18/41923/101292.png", new Object[]{"image/png", greenTileFile});
        testFileHandler.getResponses().put("/18/41923/101293.png", new Object[]{"image/png", purpleTileFile});
        testFileHandler.getResponses().put("/18/41923/101294.png", new Object[]{"image/png", redTileFile});

        testFileHandler.getResponses().put("/18/41924/101290.png", new Object[]{"image/png", whiteTileFile});
        testFileHandler.getResponses().put("/18/41924/101291.png", new Object[]{"image/png", blackTileFile});
        testFileHandler.getResponses().put("/18/41924/101292.png", new Object[]{"image/png", blueTileFile});
        testFileHandler.getResponses().put("/18/41924/101293.png", new Object[]{"image/png", cyanTileFile});
        testFileHandler.getResponses().put("/18/41924/101294.png", new Object[]{"image/png", greenTileFile});
        // whiteTileFile
        testFileHandler.getResponses().put("/18/41925/101290.png", new Object[]{"image/png", purpleTileFile});
        testFileHandler.getResponses().put("/18/41925/101291.png", new Object[]{"image/png", redTileFile});
        testFileHandler.getResponses().put("/18/41925/101292.png", new Object[]{"image/png", whiteTileFile});
        testFileHandler.getResponses().put("/18/41925/101293.png", new Object[]{"image/png", blackTileFile});
        testFileHandler.getResponses().put("/18/41925/101294.png", new Object[]{"image/png", blueTileFile});

        testFileHandler.getResponses().put("/18/41926/101290.png", new Object[]{"image/png", cyanTileFile});
        testFileHandler.getResponses().put("/18/41926/101291.png", new Object[]{"image/png", greenTileFile});
        testFileHandler.getResponses().put("/18/41926/101292.png", new Object[]{"image/png", purpleTileFile});
        testFileHandler.getResponses().put("/18/41926/101293.png", new Object[]{"image/png", redTileFile});
        testFileHandler.getResponses().put("/18/41926/101294.png", new Object[]{"image/png", whiteTileFile});

        HttpProcessor httpproc = HttpProcessorBuilder.create()
            .add(new ResponseDate())
            .add(new ResponseServer("Test/1.1"))
            .add(new ResponseContent())
            .add(new ResponseConnControl()).build();

        UriHttpRequestHandlerMapper reqistry = new UriHttpRequestHandlerMapper();
        reqistry.register("*", testFileHandler);

        HttpService httpService = new HttpService(httpproc, reqistry);

        requestListenerThread = new RequestListenerThread(port, httpService);
        requestListenerThread.setDaemon(false);
        requestListenerThread.start();
    }

    /**
     * Tear down the test setup.
     * 
     * @throws IOException
     */
    @AfterEach
    public void teadDown() throws IOException
    {
        requestListenerThread.interrupt();
        FileUtils.deleteDirectory(tempDir);
    }

    /**
     * MyHttpFileHandler
     */
    static class MyHttpFileHandler implements HttpRequestHandler
    {
        private Map<String, Object[]> responses = new HashMap<String, Object[]>();

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(HttpRequest request, HttpResponse response, HttpContext context)
            throws HttpException, IOException
        {
            String method = request.getRequestLine().getMethod().toUpperCase(Locale.ENGLISH);
            if (!"GET".equalsIgnoreCase(method))
            {
                throw new MethodNotSupportedException(method + " method not supported");
            }

            String target = URLDecoder.decode(request.getRequestLine().getUri(), "UTF-8");

            if (responses.containsKey(target))
            {
                Object[] responseContent = responses.get(target);
                ContentType contentType = ContentType.create((String) responseContent[0], (Charset) null);
                response.setStatusCode(HttpStatus.SC_OK);
                FileEntity body = new FileEntity((File) responseContent[1], contentType);
                response.setEntity(body);
                //System.out.println("Serving: url=" + target + ", file="
                //    + ((File) (responseContent[1])).getAbsolutePath());
            }
            else
            {
                response.setStatusCode(HttpStatus.SC_FORBIDDEN);
                StringEntity entity = new StringEntity(
                    "<html><body><h1>Access denied</h1></body></html>",
                    ContentType.create("text/html", "UTF-8"));
                response.setEntity(entity);
                // System.out.println("Cannot deliver file " + target);
            }
        }

        /**
         * @return the responses
         */
        public Map<String, Object[]> getResponses()
        {
            return responses;
        }
    }

    static Stream<Arguments> imageDataProvider()
    {
        return Stream.of(
            arguments(new PolarCoordinate(37.80881, -122.42669, 0.0), 320, 240, "PNG", height0),
            arguments(new PolarCoordinate(37.80881, -122.42669, 10.0), 320, 240, "PNG", height10),
            arguments(new PolarCoordinate(37.80881, -122.42669, 20.0), 320, 240, "PNG", height20),
            arguments(new PolarCoordinate(37.80881, -122.42669, 40.0), 320, 240, "PNG", height40),
            arguments(new PolarCoordinate(37.80881, -122.42669, 90.0), 320, 240, "PNG", height90),
            arguments(new PolarCoordinate(37.80881, -122.42669, 120.0), 320, 240, "PNG", height120),
            arguments(new PolarCoordinate(37.80881, -122.42669, 150.0), 320, 240, "PNG", height150),
            arguments(new PolarCoordinate(37.80881, -122.42669, 200.0), 320, 240, "PNG", height200),
            arguments(new PolarCoordinate(37.80881, -122.42669, 200.1), 320, 240, "PNG", height200),
            arguments(new PolarCoordinate(37.80881, -122.42669, 250.0), 320, 240, "PNG", height200));
    }

    @ParameterizedTest
    @MethodSource("imageDataProvider")
    public void shouldGetImageForGivenPositionAndHeight(PolarCoordinate position, int width, int height,
        String imageFormat, String imageName) throws IOException
    {
        byte[] buffer = camera.getImage(position);
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(buffer));
        assertThat(image.getHeight()).isEqualTo(height);
        assertThat(image.getWidth()).isEqualTo(width);

        byte[] reference = FileUtils.readFileToByteArray(new File(imageName));
        assertThat(buffer).isEqualTo(reference);
    }

    @Test
    public void shouldReturnNullImageOnNullPosition() throws IOException
    {
        byte[] buffer = camera.getImage(null);
        assertThat(buffer).isNotNull().hasSize(0);
    }

}
