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

import java.io.IOException;
import java.net.InetSocketAddress;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.localserver.LocalTestServer;
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
    private RealVehicle realVehicle;
    private LocalTestServer server;
    private HttpRequestHandler handler;

    @BeforeMethod
    public void setUp() throws Exception
    {
        handler = mock(HttpRequestHandler.class);

        doAnswer(new Answer<Object>()
        {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable
            {
                Object[] args = invocation.getArguments();
                // HttpRequest request = (HttpRequest) args[0];
                HttpResponse response = (HttpResponse) args[1];
                // HttpContext context = (HttpContext) args[2];

                final int statusCode = response.getStatusLine().getStatusCode();
                final String reasonPhrase = "buggerit";
                final ProtocolVersion protocolVersion = response.getProtocolVersion();

                response.setStatusLine(new MyStatusLine(protocolVersion, statusCode, reasonPhrase));
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
        };
    }

    @Test(dataProvider = "byteArrayDataProvider")
    public void shouldTransferDataChunk(byte[] data) throws ClientProtocolException, IOException
    {
        CommunicationService com = new CommunicationServiceImpl();

        CommunicationResponse response = com.transfer(realVehicle, Connector.MIGRATE, data);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isNotNull().isEqualTo(Status.OK);
        assertThat(response.getContent()).isNotNull().isEqualTo("buggerit");
    }

    /**
     * MyStatusLine
     */
    private static class MyStatusLine implements StatusLine
    {
        private ProtocolVersion protocolVersion;
        private int statusCode;
        private String reasonPhrase;

        /**
         * @param protocolVersion the protocol version.
         * @param statusCode the status code.
         * @param reasonPhrase the reason phrase.
         */
        public MyStatusLine(ProtocolVersion protocolVersion, int statusCode, String reasonPhrase)
        {
            this.protocolVersion = protocolVersion;
            this.statusCode = statusCode;
            this.reasonPhrase = reasonPhrase;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public ProtocolVersion getProtocolVersion()
        {
            return protocolVersion;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int getStatusCode()
        {
            return statusCode;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getReasonPhrase()
        {
            return reasonPhrase;
        }
    }
}
