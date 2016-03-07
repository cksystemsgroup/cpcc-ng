// This code is part of the CPCC-NG project.
//
// Copyright (c) 2009-2016 Clemens Krainer <clemens.krainer@gmail.com>
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

import javax.inject.Inject;

import org.apache.commons.lang3.StringUtils;
import org.apache.tapestry5.StreamResponse;

import cpcc.core.utils.DownloadStreamResponse;
import cpcc.vvrte.entities.VirtualVehicle;
import cpcc.vvrte.services.db.VvRteRepository;

/**
 * VvDownload implementation.
 */
public class VvDownload
{
    @Inject
    private VvRteRepository repository;

    StreamResponse onActivate(Integer vvId) throws IOException
    {
        VirtualVehicle vv = repository.findVirtualVehicleById(vvId);

        if (vv == null || StringUtils.isBlank(vv.getCode()))
        {
            return null;
        }

        String fileName = String.format("vv-%s-%d.js", vv.getName().replaceAll("\\s+", "_"), vv.getId());
        return new DownloadStreamResponse("text/javascript", vv.getCode().getBytes("UTF-8"), fileName);
    }
}
