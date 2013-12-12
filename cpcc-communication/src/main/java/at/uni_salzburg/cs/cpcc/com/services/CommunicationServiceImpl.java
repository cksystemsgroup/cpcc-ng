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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationRequest.Connector;
import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.persistence.entities.RealVehicle;

/**
 * CommunicationServiceImpl
 */
public class CommunicationServiceImpl implements CommunicationService
{
    @SuppressWarnings("serial")
    private static final Map<Connector, String> CONNECTOR_MAP = new HashMap<Connector, String>()
    {
        {
            put(Connector.MIGRATE, "/migrate");
        }
    };

    /**
     * CommunicationServiceImpl
     */
    public CommunicationServiceImpl()
    {
        // intentionally empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommunicationResponse transfer(RealVehicle realVehicle, Connector connector, byte[] data)
        throws ClientProtocolException, IOException
    {
        HttpPost request = new HttpPost(realVehicle.getUrl() + CONNECTOR_MAP.get(connector));

        HttpEntity entity = EntityBuilder.create().setBinary(data).build();
        request.setEntity(entity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);

        String content = response.getStatusLine().getReasonPhrase();

        boolean ok = response.getStatusLine().getStatusCode() == 200;

        CommunicationResponse r = new CommunicationResponse();
        r.setStatus(ok ? Status.OK : Status.NOT_OK);
        r.setContent(content);

        return r;
    }

}
