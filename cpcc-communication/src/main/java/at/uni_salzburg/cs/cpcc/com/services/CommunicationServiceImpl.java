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

package at.uni_salzburg.cs.cpcc.com.services;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import at.uni_salzburg.cs.cpcc.com.services.CommunicationResponse.Status;
import at.uni_salzburg.cs.cpcc.core.entities.RealVehicle;

/**
 * CommunicationServiceImpl
 */
public class CommunicationServiceImpl implements CommunicationService
{
    private Map<String, String> connectorMap = new HashMap<>();

    /**
     * CommunicationServiceImpl
     */
    public CommunicationServiceImpl()
    {
        // Intentionally empty.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConnector(String connector, String path) throws IllegalStateException
    {
        if (connectorMap.containsKey(connector))
        {
            throw new IllegalStateException("Connector " + connector + " is already registered!");
        }

        connectorMap.put(connector, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CommunicationResponse transfer(RealVehicle realVehicle, String connector, byte[] data)
        throws ClientProtocolException, IOException
    {
        HttpPost request = new HttpPost(realVehicle.getUrl() + connectorMap.get(connector));

        HttpEntity entity = EntityBuilder.create().setBinary(data).build();
        request.setEntity(entity);

        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = httpclient.execute(request);

        HttpEntity responseEntity = response.getEntity();
        InputStream ins = responseEntity.getContent();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        IOUtils.copy(ins, baos);
        byte[] content = baos.toByteArray();

        boolean ok = response.getStatusLine().getStatusCode() == 200;

        CommunicationResponse r = new CommunicationResponse();
        r.setStatus(ok ? Status.OK : Status.NOT_OK);
        r.setContent(content);
        return r;
    }

}
