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

package cpcc.com.services;

import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.localserver.LocalServerTestBase;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.com.services.CommunicationResponse.Status;
import cpcc.core.entities.RealVehicle;

public class CommunicationServiceTest
{
    private static final String MIGRATE = "migrate";

    private static final String REASON_PHRASE = "this is the reason phrase.";

    private RealVehicle realVehicle;
    private HttpServer server;
    private HttpRequestHandler handler;
    private BasicHttpEntityEnclosingRequest request;
    private byte[] content;
    private boolean throwHttpException;
    private CommunicationServiceImpl com;

    @BeforeMethod
    public void setUp() throws Exception
    {
        content = null;
        throwHttpException = false;

        handler = mock(HttpRequestHandler.class);

        doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                request = (BasicHttpEntityEnclosingRequest) args[0];
                HttpResponse response = (HttpResponse) args[1];

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                IOUtils.copy(request.getEntity().getContent(), baos);
                content = baos.toByteArray();

                if (throwHttpException)
                {
                    throw new HttpException("HttpException thrown on purpose!");
                }

                final int statusCode = response.getStatusLine().getStatusCode();
                final String reasonPhrase = response.getStatusLine().getReasonPhrase();
                final ProtocolVersion protocolVersion = response.getProtocolVersion();

                response.setStatusLine(new MyStatusLine(protocolVersion, statusCode, reasonPhrase));

                HttpEntity entity = EntityBuilder.create()
                    .setContentType(ContentType.TEXT_PLAIN)
                    .setText(REASON_PHRASE)
                    .build();

                response.setEntity(entity);
                return null;
            }
        }).when(handler).handle(any(HttpRequest.class), any(HttpResponse.class), any(HttpContext.class));

        server = ServerBootstrap.bootstrap()
            .setServerInfo(LocalServerTestBase.ORIGIN)
            .registerHandler("/*", handler)
            .create();

        server.start();

        String serverUrl = "http://" + server.getInetAddress().getHostAddress() + ":" + server.getLocalPort();

        realVehicle = mock(RealVehicle.class);
        when(realVehicle.getUrl()).thenReturn(serverUrl + "/rv001");

        com = new CommunicationServiceImpl();
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        server.stop();
        server.awaitTermination(30, TimeUnit.SECONDS);
    }

    @DataProvider
    public Object[][] byteArrayDataProvider()
    {
        return new Object[][]{
            new Object[]{new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9}},
            new Object[]{new byte[]{9, 8, 7, 6, 5, 4, 3, 2, 1, 0, 9, 8, 7, 6, 5, 4, 3}},
            new Object[]{new byte[]{}},
        };
    }

    @Test(dataProvider = "byteArrayDataProvider")
    public void shouldTransferDataChunk(byte[] data) throws ClientProtocolException, IOException
    {
        CommunicationResponse response = com.transfer(realVehicle, MIGRATE, data);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull().isEqualTo(Status.OK);
        assertThat(response.getContent()).isNotNull().isEqualTo(REASON_PHRASE.getBytes());

        assertThat(request).isNotNull();
        assertThat(request.getEntity().getContentLength()).isEqualTo(data.length);
        assertThat(request.getEntity().getContentType().getValue()).isEqualTo("application/octet-stream");
        assertThat(request.getEntity().getContentEncoding()).isNull();
        assertThat(content).isEqualTo(data);
    }

    @Test(dataProvider = "byteArrayDataProvider")
    public void shouldDetectTransferProblems(byte[] data) throws ClientProtocolException, IOException
    {
        throwHttpException = true;

        CommunicationResponse response = com.transfer(realVehicle, MIGRATE, data);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull().isEqualTo(Status.NOT_OK);
        assertThat(response.getContent()).isNotNull().isEqualTo("HttpException thrown on purpose!".getBytes());
    }

    @Test
    public void shouldThrowExeptionWhenAddingConnectorsTwice()
    {
        com.addConnector("connector", "path");

        catchException(() -> com.addConnector("connector", "path"));

        assertThat((Throwable) caughtException()).isInstanceOf(IllegalStateException.class);
    }
}
