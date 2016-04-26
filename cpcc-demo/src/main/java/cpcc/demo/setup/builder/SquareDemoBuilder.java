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
import static cpcc.demo.setup.builder.LambdaExceptionUtil.rethrowSupplier;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;

import com.fasterxml.jackson.core.JsonProcessingException;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.entities.RealVehicle;
import cpcc.core.entities.RealVehicleType;
import cpcc.core.entities.SensorType;

/**
 * SquareDemoBuilder implementation.
 */
public class SquareDemoBuilder
{
    private static final String HTTP_LOCALHOST_RV_URL_FORMAT = "http://localhost:%04d";

    private int nrGroundStations = 1;
    private PortManager pm;
    private double width;
    private double height;
    private int nrCellsWide;
    private int nrCellsHigh;
    private PolarCoordinate gsPosition;
    private PolarCoordinate center;

    /**
     * @param gStations the number of ground stations.
     * @return this instance.
     */
    public SquareDemoBuilder setNrGroundStations(int gStations)
    {
        this.nrGroundStations = gStations;
        return this;
    }

    /**
     * @param basePort the base TCP port number for the HTTP connectors..
     * @return this instance.
     */
    public SquareDemoBuilder setBasePort(int basePort)
    {
        pm = new PortManager(basePort);
        return this;
    }

    /**
     * @param newWidth the cell width in meters.
     * @return this instance.
     */
    public SquareDemoBuilder setCellWidth(double newWidth)
    {
        this.width = newWidth;
        return this;
    }

    /**
     * @param newHeight the cell height in meters.
     * @return this instance.
     */
    public SquareDemoBuilder setCellHeight(double newHeight)
    {
        this.height = newHeight;
        return this;
    }

    /**
     * @param newNrCellsWide the number of cells of the demo in horizontal direction.
     * @return this instance.
     */
    public SquareDemoBuilder setNumberOfCellsWide(int newNrCellsWide)
    {
        this.nrCellsWide = newNrCellsWide;
        return this;
    }

    /**
     * @param newNrCellsHigh the number of cells of the demo in vertical direction.
     * @return this instance.
     */
    public SquareDemoBuilder setNumberOfCellsHigh(int newNrCellsHigh)
    {
        this.nrCellsHigh = newNrCellsHigh;
        return this;
    }

    /**
     * @param newGsPosition the position of the ground station.
     * @return this instance.
     */
    public SquareDemoBuilder setGroundStationPosition(PolarCoordinate newGsPosition)
    {
        this.gsPosition = newGsPosition;
        return this;
    }

    /**
     * @param newCenter the center of all cells
     * @return this instance.
     */
    public SquareDemoBuilder setCenter(PolarCoordinate newCenter)
    {
        this.center = newCenter;
        return this;
    }

    /**
     * @param outputDirectory the output folder.
     * @throws IOException in case of errors.
     * @throws JsonProcessingException in case of errors.
     */
    public void write(File outputDirectory) throws IOException, JsonProcessingException
    {
        FileUtils.forceMkdir(outputDirectory);

        List<RealVehicle> rvs = new ArrayList<>();
        rvs.add(setUpGroundStation(1));
        rvs.addAll(IntStream
            .range(0, nrCellsWide)
            .mapToObj(x -> IntStream
                .range(0, nrCellsHigh)
                .mapToObj(y -> setupRealVehicle(x, y))
                .collect(Collectors.toList()))
            .flatMap(List::stream)
            .collect(Collectors.toList()));

        new GlobalSetupWriter().write(new File(outputDirectory, "db-setup-all.sql"), rvs);

        rvs.stream().forEach(rethrowConsumer(x -> new RealVehicleBasicWriter(pm.getBasePort())
            .write(new File(outputDirectory, "db-setup-" + x.getName() + "-rv.sql"), x)));

        rvs.stream().forEach(rethrowConsumer(x -> new RealVehicleCameraWriter()
            .write(new File(outputDirectory, "db-setup-" + x.getName() + "-cam.sql"), x)));
    }

    /**
     * @param gsid the ground station identification.
     * @return the ground station instance.
     */
    private RealVehicle setUpGroundStation(int gsid)
    {
        return new RealVehicleBuilder()
            .setId(gsid)
            .setDeleted(false)
            .setLastUpdate(new Date())
            .setName(String.format("GS%02d", gsid))
            .setAreaOfOperation(getGsAreaOfOperation(gsid))
            .setSensors(SensorConstants.byType(SensorType.GPS))
            .setState(null)
            .setType(RealVehicleType.GROUND_STATION)
            .setUrl(String.format(HTTP_LOCALHOST_RV_URL_FORMAT, pm.getRvPort(gsid)))
            .build();
    }

    /**
     * @param gsid the ground station identification.
     * @return the area of operations as a string.
     */
    private String getGsAreaOfOperation(int gsid)
    {
        try
        {
            return new OblongAooBuilder().setCenter(gsPosition).build();
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException("getGsAreaOfOperation " + gsid + " failed.", e);
        }
    }

    /**
     * @param x the X position of the map cell.
     * @param y the Y position of the map cell.
     * @return the real vehicle.
     */
    private RealVehicle setupRealVehicle(int x, int y)
    {
        int rvid = x + y * nrCellsHigh + 1;
        int id = nrGroundStations + rvid;

        return new RealVehicleBuilder()
            .setId(id)
            .setDeleted(false)
            .setLastUpdate(new Date())
            .setName(String.format("RV%02d", rvid))
            .setAreaOfOperation(getRvAreaOfOperation(x, y))
            .setSensors(SensorConstants.all())
            .setState(null)
            .setType(RealVehicleType.QUADROCOPTER)
            .setUrl(String.format(HTTP_LOCALHOST_RV_URL_FORMAT, pm.getRvPort(id)))
            .build();
    }

    /**
     * @param x the X position of the map cell.
     * @param y the Y position of the map cell.
     * @return the area of operations as a string.
     */
    private String getRvAreaOfOperation(int x, int y)
    {
        return rethrowSupplier(() -> new OblongAooBuilder()
            .setNrCellsWide(nrCellsWide)
            .setNrCellsHigh(nrCellsHigh)
            .setX(x)
            .setY(y)
            .setWidth(width)
            .setHeight(height)
            .setCenter(center)
            .build()).get();
    }

}
