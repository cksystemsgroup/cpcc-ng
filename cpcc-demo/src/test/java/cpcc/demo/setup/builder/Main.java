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

import java.io.File;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonProcessingException;

import cpcc.core.entities.PolarCoordinate;

/**
 * Main implementation.
 */
public class Main
{
    /**
     * @param args the program arguments.
     * @throws IOException in case of errors.
     */
    public static void main(String[] args) throws IOException
    {
        PolarCoordinate mapCenter = new PolarCoordinate(47.822120373304436, 13.040800037829925, 0.0);
        PolarCoordinate groundStationPosition = new PolarCoordinate(47.821922207617014, 13.040811717510222, 0.0);

        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 1, 1);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 500, 500, 1, 1);

        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 2, 2);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 3, 3);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 4, 4);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 5, 5);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 6, 6);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 7, 7);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 8, 8);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 9, 9);
        writeRectangularConfig(8000, mapCenter, groundStationPosition, 50, 50, 10, 10);
    }

    /**
     * @param basePort the base TCP port number.
     * @param mapCenter the center of the map.
     * @param groundStationPosition the position of the ground station.
     * @param cellWidth the map with of a cell in meters.
     * @param cellHeight the map height of a cell in meters.
     * @param nrCellsWide the number of cells in width.
     * @param nrCellsHigh the number of cells in heigth.
     * @throws IOException in case of errors.
     * @throws JsonProcessingException in case of errors.
     */
    private static void writeRectangularConfig(int basePort, PolarCoordinate mapCenter
        , PolarCoordinate groundStationPosition, int cellWidth, int cellHeight, int nrCellsWide, int nrCellsHigh)
        throws IOException, JsonProcessingException
    {
        File outputDir = new File(String.format("src/main/resources/setups/rect-%03dm-by-%03dm-%02dx%02d"
            , cellWidth, cellHeight, nrCellsWide, nrCellsHigh));

        new SquareDemoBuilder()
            .setBasePort(basePort)
            .setNrGroundStations(1)
            .setCellWidth(cellWidth)
            .setCellHeight(cellHeight)
            .setNumberOfCellsWide(nrCellsWide)
            .setNumberOfCellsHigh(nrCellsHigh)
            .setGroundStationPosition(groundStationPosition)
            .setCenter(mapCenter)
            .write(outputDir);
    }

}
