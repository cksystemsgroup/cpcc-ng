// This code is part of the CPCC-NG project.
//
// Copyright (c) 2016 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.pages.vv;

import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;

import org.apache.tapestry5.hibernate.annotations.CommitAfter;
import org.apache.tapestry5.http.services.RequestGlobals;
import org.apache.tapestry5.util.TextStreamResponse;

import cpcc.vvrte.services.VirtualVehicleMigrator;

/**
 * Vehicle migration acknowledge end-point.
 */
public class VvMigrationAck
{
    @Inject
    private RequestGlobals requestGlobals;

    @Inject
    private VirtualVehicleMigrator migrator;

    @CommitAfter
    Object onActivate() throws IOException
    {
        InputStream inputStream = requestGlobals.getHTTPServletRequest().getInputStream();
        migrator.ackChunk(inputStream);
        return new TextStreamResponse("text/plain", "OK");
    }
}
