// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.rv.base.pages.update;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.commons.io.IOUtils;
import org.apache.tapestry5.json.JSONObject;
import org.apache.tapestry5.services.RequestGlobals;

import cpcc.core.utils.JsonStreamResponse;
import cpcc.rv.base.services.StateSynchronizer;

/**
 * ConfigurationUpdate
 */
public class UpdateConfiguration
{
    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private StateSynchronizer synchronizer;

    Object onActivate() throws IOException
    {
        InputStream inputStream = requestGlobals.getHTTPServletRequest().getInputStream();
        byte[] requestData = IOUtils.toByteArray(inputStream);

        synchronizer.importConfiguration(requestData);

        JSONObject jsonObject = new JSONObject("result", "OK");
        return new JsonStreamResponse(jsonObject);
    }
}
