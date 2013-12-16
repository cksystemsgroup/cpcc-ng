/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package at.uni_salzburg.cs.cpcc.com.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.ContentEncodingHttpClient;
import org.apache.http.localserver.LocalTestServer;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;

public class CommunicationServiceTest
{
    private static final String REASON_PHRASE = "this is the reason phrase.";
    
    private RealVehicle realVehicle;
    private LocalTestServer server;
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
                // HttpContext context = (HttpContext) args[2];

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
                
                response.setEntity(entity );
                return null;
            }
        }).when(handler).handle(any(HttpRequest.class), any(HttpResponse.class), any(HttpContext.class));

        server = new LocalTestServer(null, null);
        server.register("/*", handler);
        server.start();
        InetSocketAddress addr = server.getServiceAddress();
        String serverUrl = "http://" + addr.getHostString() + ":" + addr.getPort();

        realVehicle = mock(RealVehicle.class);
        when(realVehicle.getUrl()).thenReturn(serverUrl + "/rv001");
        
        com = new CommunicationServiceImpl();
    }

    @AfterMethod
    public void tearDown() throws Exception
    {
        server.stop();
        server.awaitTermination(0);
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
        CommunicationResponse response = com.transfer(realVehicle, Connector.MIGRATE, data);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull().isEqualTo(Status.OK);
        assertThat(response.getContent()).isNotNull().isEqualTo(REASON_PHRASE);

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
        
        CommunicationResponse response = com.transfer(realVehicle, Connector.MIGRATE, data);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull().isEqualTo(Status.NOT_OK);
        assertThat(response.getContent()).isNotNull().isEqualTo("HttpException thrown on purpose!");
    }
}
