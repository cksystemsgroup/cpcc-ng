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
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import org.geojson.FeatureCollection;
import org.geojson.Point;

import com.fasterxml.jackson.databind.ObjectMapper;

import cpcc.core.entities.Device;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;

/**
 * Real Vehicle Basic Writer implementation.
 */
public class RealVehicleBasicWriter extends AbstractRealVehicleWriter
{
    private static final String GS_CONFIG_FORMAT = "position=(%1$.6f,%2$.6f,0)";
    private static final String RV_CONFIG_FORMAT = "origin=(%1$.6f;%2$.6f;0) "
        + "maxVelocity=20 maxAcceleration=5 precision=10 updateCycle=100 idlePower=20 hoverPower=55 mass=2.2 "
        + "takeOffHeight=10 takeOffVelocity=2 takeOffAcceleration=2 landingVelocity=1.5 landingAcceleration=2";

    private PortManager pm;

    /**
     * @param basePort the TCP bvase port number.
     */
    public RealVehicleBasicWriter(int basePort)
    {
        pm = new PortManager(basePort);
    }

    /**
     * @param file the file to write to.
     * @param rv the Real Vehicle to export.
     * @throws IOException in case of errors.
     */
    public void write(File file, RealVehicle rv) throws IOException
    {
        List<Device> devices = rv.getType() == RealVehicleType.GROUND_STATION
            ? setupGsDevices(rv)
            : setupRvDevices(rv);

        try (OutputStream out = new FileOutputStream(file);
            Writer writer = new OutputStreamWriter(out, StandardCharsets.UTF_8))
        {
            writeParameters(writer, rv);
            devices.stream().forEach(rethrowConsumer(x -> writeDevice(writer, x)));
        }
    }

    /**
     * @param rv the real vehicle.
     * @return the list of devices.
     * @throws IOException in case of errors.
     */
    private List<Device> setupGsDevices(RealVehicle rv) throws IOException
    {
        FeatureCollection fc = new ObjectMapper().readValue(rv.getAreaOfOperation(), FeatureCollection.class);

        String configuration = fc.getFeatures().stream()
            .filter(x -> x.getGeometry() instanceof Point)
            .map(x -> (Point) x.getGeometry())
            .map(Point::getCoordinates)
            .map(x -> String.format(GS_CONFIG_FORMAT, x.getLatitude(), x.getLongitude()))
            .findFirst()
            .orElse(String.format(GS_CONFIG_FORMAT, 0.0, 0.0));

        return Arrays.asList(new DeviceBuilder()
            .setId(1)
            .setConfiguration(configuration)
            .setTopicRoot(String.format("/%1$s/gps", rv.getName().toLowerCase()))
            .setType(DeviceTypeConstants.GS_GPS)
            .build());
    }

    /**
     * @param rv the real vehicle.
     * @return the list of devices.
     * @throws IOException in case of errors.
     */
    private List<Device> setupRvDevices(RealVehicle rv) throws IOException
    {
        FeatureCollection fc = new ObjectMapper().readValue(rv.getAreaOfOperation(), FeatureCollection.class);

        String configuration = fc.getFeatures().stream()
            .filter(x -> x.getGeometry() instanceof Point)
            .map(x -> (Point) x.getGeometry())
            .map(Point::getCoordinates)
            .map(x -> String.format(RV_CONFIG_FORMAT, x.getLatitude(), x.getLongitude()))
            .findFirst()
            .orElse(String.format(RV_CONFIG_FORMAT, 0.0, 0.0));

        return Arrays.asList(new DeviceBuilder()
            .setId(1)
            .setConfiguration(configuration)
            .setTopicRoot(String.format("/%1$s", rv.getName().toLowerCase()))
            .setType(DeviceTypeConstants.QUADROTOR)
            .build());
    }

    /**
     * @param writer the writer to use.
     * @param rv the real vehicle.
     * @throws IOException in case of errors.
     */
    private void writeParameters(Writer writer, RealVehicle rv) throws IOException
    {
        writer.append(String.format("INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES "
            + "(1,'realVehicleName',0,'%1$s');%n", rv.getName()));

        writer.append(String.format("INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES "
            + "(2,'masterServerURI',0,'http://localhost:%1$d');%n", pm.getRosPort(rv.getId())));

        writer.append("INSERT INTO PARAMETERS (ID,NAME,SORT,STRING_VALUE) VALUES (3,'useInternalRosCore',0,'true');\n\n");
    }

}
