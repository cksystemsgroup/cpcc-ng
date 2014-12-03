/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.commons.pages.configuration;

import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.services.RequestGlobals;

import at.uni_salzburg.cs.cpcc.commons.services.ConfigurationSynchronizer;
import at.uni_salzburg.cs.cpcc.core.utils.ByteArrayStreamResponse;

/**
 * ConfigurationUpdate
 */
public class ConfigurationUpdate
{
    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private ConfigurationSynchronizer synchronizer;

    @CommitAfter
    private Object onActivate() throws Exception
    {
        InputStream inputStream = requestGlobals.getHTTPServletRequest().getInputStream();
        byte[] requestData = IOUtils.toByteArray(inputStream);
        byte[] responseData = synchronizer.updateOwnConfig(requestData);
        return new ByteArrayStreamResponse("text/plain", responseData);
    }
}
