// ====================================================================
// Licensed to the Apache Software Foundation (ASF) under one
// or more contributor license agreements. See the NOTICE file
// distributed with this work for additional information
// regarding copyright ownership. The ASF licenses this file
// to you under the Apache License, Version 2.0 (the
// "License"); you may not use this file except in compliance
// with the License. You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing,
// software distributed under the License is distributed on an
// "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
// KIND, either express or implied. See the License for the
// specific language governing permissions and limitations
// under the License.
// ====================================================================
//
// This software consists of voluntary contributions made by many
// individuals on behalf of the Apache Software Foundation. For more
// information on the Apache Software Foundation, please see
// <http://www.apache.org/>.
//
//
// This code has been taken from this code example:
//
// http://hc.apache.org/httpcomponents-core-4.3.x/httpcore/examples/org/apache/http/examples/ElementalHttpServer.java
//
// The example code has been modified for the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
//

package cpcc.ros.test;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.http.ConnectionClosedException;
import org.apache.http.HttpConnectionFactory;
import org.apache.http.HttpException;
import org.apache.http.HttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnection;
import org.apache.http.impl.DefaultBHttpServerConnectionFactory;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpService;

/**
 * RequestListenerThread
 */
public class RequestListenerThread extends Thread
{
    private final HttpConnectionFactory<DefaultBHttpServerConnection> connectionFactory;
    private final ServerSocket serverSocket;
    private final HttpService httpService;

    /**
     * @param port the listener port.
     * @param httpService the HTTP service.
     * @throws IOException thrown in case of errors.
     */
    public RequestListenerThread(final int port, final HttpService httpService) throws IOException
    {
        connectionFactory = DefaultBHttpServerConnectionFactory.INSTANCE;
        serverSocket = new ServerSocket(port);
        this.httpService = httpService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run()
    {
        System.out.println("Listening on port " + serverSocket.getLocalPort());
        while (!Thread.interrupted())
        {
            try
            {
                Socket socket = serverSocket.accept();
                System.out.println("Incoming connection from " + socket.getInetAddress());
                HttpServerConnection conn = connectionFactory.createConnection(socket);

                Thread t = new WorkerThread(httpService, conn);
                t.setDaemon(true);
                t.start();
            }
            catch (InterruptedIOException ex)
            {
                break;
            }
            catch (IOException e)
            {
                System.err.println("I/O error initialising connection thread: " + e.getMessage());
                break;
            }
        }
    }

    /**
     * WorkerThread
     */
    static class WorkerThread extends Thread
    {
        private final HttpService httpservice;
        private final HttpServerConnection serverConnection;

        /**
         * @param httpservice the HTTP service.
         * @param conn the server connection.
         */
        public WorkerThread(final HttpService httpservice, final HttpServerConnection serverConnection)
        {
            super();
            this.httpservice = httpservice;
            this.serverConnection = serverConnection;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void run()
        {
            System.out.println("New connection thread");
            HttpContext context = new BasicHttpContext(null);
            try
            {
                while (!Thread.interrupted() && serverConnection.isOpen())
                {
                    httpservice.handleRequest(serverConnection, context);
                }
            }
            catch (ConnectionClosedException ex)
            {
                System.err.println("Client closed connection");
            }
            catch (IOException ex)
            {
                System.err.println("I/O error: " + ex.getMessage());
            }
            catch (HttpException ex)
            {
                System.err.println("Unrecoverable HTTP protocol violation: " + ex.getMessage());
            }
            finally
            {
                try
                {
                    this.serverConnection.shutdown();
                }
                catch (IOException ignore)
                {
                }
            }
        }
    }
}
