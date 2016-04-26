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
import java.text.SimpleDateFormat;
import java.util.List;

import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorDefinition;
import cpcc.core.entities.SensorType;

/**
 * Global Setup Writer implementation.
 */
public class GlobalSetupWriter
{
    private static final String TIME_STAMP_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS'000000'";

    /**
     * @param file the output file.
     * @param rvs the list of real vehicles.
     * @throws IOException in case of errors.
     */
    public void write(File file, List<RealVehicle> rvs)
        throws IOException
    {
        try (OutputStream out = new FileOutputStream(file); Writer writer = new OutputStreamWriter(out, "UTF-8"))
        {
            SensorConstants.all().stream().forEach(rethrowConsumer(x -> writeSensor(writer, x)));
            writer.append("\n");

            rvs.stream().forEach(rethrowConsumer(x -> writeRealVehicle(writer, x)));
            writer.append("\n");

            rvs.stream().forEach(rethrowConsumer(x -> writeRvSensors(writer, x)));
            writer.append("\n");
        }
    }

    /**
     * @param writer the writer to use.
     * @param sensor the sensor definition.
     * @throws IOException in case of errors.
     */
    private static void writeSensor(Writer writer, SensorDefinition sensor) throws IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_DATE_FORMAT);

        writer.append(String.format("INSERT INTO SENSOR_DEFINITIONS ("
            + "ID,DESCRIPTION,LAST_UPDATE,MESSAGE_TYPE,PARAMETERS,TYPE,VISIBILITY,DELETED)%n"
            + "VALUES (%1$d,'%2$s',{ts '%3$s'},'%4$s',%5$s,'%6$s','%7$s',%8$d);%n"
            , sensor.getId()
            , sensor.getDescription()
            , sdf.format(sensor.getLastUpdate())
            , sensor.getMessageType()
            , sensor.getParameters() != null ? "'" + sensor.getParameters() + "'" : "null"
            , sensor.getType().name()
            , sensor.getVisibility().name()
            , sensor.getDeleted() ? 1 : 0));
    }

    /**
     * @param writer the writer to use.
     * @param rv the real vehicle.
     * @throws IOException in case of errors.
     */
    private static void writeRealVehicle(Writer writer, RealVehicle rv) throws IOException
    {
        SimpleDateFormat sdf = new SimpleDateFormat(TIME_STAMP_DATE_FORMAT);
        writer.append(String.format("INSERT INTO REAL_VEHICLES ("
            + "ID,LAST_UPDATE,NAME,URL,TYPE,AREA_OF_OPERATION,DELETED)%n"
            + "VALUES (%1$2d,{ts '%2$s'},'%3$s','%4$s','%5$s','%6$s',%7$d);%n"
            , rv.getId()
            , sdf.format(rv.getLastUpdate())
            , rv.getName()
            , rv.getUrl()
            , rv.getType().name()
            , rv.getAreaOfOperation()
            , rv.getDeleted() ? 1 : 0));
    }

    /**
     * @param writer the writer to use.
     * @param rv the real vehicle.
     * @throws IOException in case of errors.
     */
    private void writeRvSensors(Writer writer, RealVehicle rv) throws IOException
    {
        List<SensorDefinition> sds = rv.getType() == RealVehicleType.GROUND_STATION
            ? SensorConstants.byType(SensorType.GPS)
            : SensorConstants.byType(SensorType.GPS, SensorType.ALTIMETER, SensorType.CAMERA);

        sds.forEach(rethrowConsumer(x -> writeSensorConnection(writer, rv, x)));
    }

    /**
     * @param writer the writer to use.
     * @param rv the real vehicle.
     * @param sensor the sensor definition.
     * @throws IOException in case of errors.
     */
    private void writeSensorConnection(Writer writer, RealVehicle rv, SensorDefinition sensor) throws IOException
    {
        writer.append(String.format("INSERT INTO REAL_VEHICLES_SENSOR_DEFINITIONS "
            + "(REAL_VEHICLES_ID,SENSORS_ID) VALUES (%1$2d,%2$2d);%n"
            , rv.getId(), sensor.getId()));
    }
}
