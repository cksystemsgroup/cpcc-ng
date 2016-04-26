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

package cpcc.demo.setup.builder;

import static cpcc.demo.setup.builder.LambdaExceptionUtil.rethrowConsumer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import cpcc.core.entities.Device;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;

/**
 * Real Vehicle Camera Writer implementation.
 */
public class RealVehicleCameraWriter extends AbstractRealVehicleWriter
{
    private static final String RV_CONFIG_FORMAT = "gps=''/%1$s/gps'' osmZoomLevel=21 "
        + "osmTileServerUrl=''http://khm0.googleapis.com/kh?v=192&hl=en-US&x=%%2$s&y=%%3$s&z=%%1$s&token=108840''";

    /**
     * @param file the output file
     * @param rv the real vehicle.
     * @throws IOException in case of errors.
     */
    public void write(File file, RealVehicle rv) throws IOException
    {
        List<Device> devices = rv.getType() == RealVehicleType.GROUND_STATION
            ? Collections.emptyList()
            : setupRvDevices(rv);

        try (OutputStream out = new FileOutputStream(file); Writer writer = new OutputStreamWriter(out, "UTF-8"))
        {
            devices.stream().forEach(rethrowConsumer(x -> writeDevice(writer, x)));
        }
    }

    /**
     * @param rv the real vehicle.
     * @return the list of devices.
     * @throws IOException in case of errors.
     */
    private List<Device> setupRvDevices(RealVehicle rv) throws IOException
    {
        String configuration = String.format(RV_CONFIG_FORMAT, rv.getName().toLowerCase());

        return Arrays.asList(new DeviceBuilder()
            .setId(2)
            .setConfiguration(configuration)
            .setTopicRoot(String.format("/%1$s/camera", rv.getName().toLowerCase()))
            .setType(DeviceTypeConstants.T_SIMULATED_BELLY_MOUNTED_CAMERA)
            .build());
    }

}
