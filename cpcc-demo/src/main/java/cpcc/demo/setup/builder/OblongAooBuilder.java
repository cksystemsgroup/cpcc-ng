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

import org.geojson.Feature;
import org.geojson.FeatureCollection;
import org.geojson.LngLatAlt;
import org.geojson.Point;
import org.geojson.Polygon;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import cpcc.core.entities.PolarCoordinate;
import cpcc.core.utils.GeodeticSystem;
import cpcc.core.utils.WGS84;

/**
 * Oblong Area Of Operation Builder implementation.
 */
public class OblongAooBuilder
{
    private static final double MIN_ALT = 0.0;
    private static final double MAX_ALT = 60.0;

    private GeodeticSystem gs = new WGS84();

    private int x;
    private int y;
    private int nrCellsWide = -1;
    private int nrCellsHigh = -1;
    private double width;
    private double height;
    private PolarCoordinate center;

    /**
     * @param newNrCellsWide the numbers of cells the map covers in width
     * @return this instance.
     */
    public OblongAooBuilder setNrCellsWide(int newNrCellsWide)
    {
        this.nrCellsWide = newNrCellsWide;
        return this;
    }

    /**
     * @param newNrCellsHigh the numbers of cells the map covers in heigth
     * @return this instance.
     */
    public OblongAooBuilder setNrCellsHigh(int newNrCellsHigh)
    {
        this.nrCellsHigh = newNrCellsHigh;
        return this;
    }

    /**
     * @param newX the X index of the current cell
     * @return this instance.
     */
    public OblongAooBuilder setX(int newX)
    {
        this.x = newX;
        return this;
    }

    /**
     * @param newY the Y index of the current cell
     * @return this instance.
     */
    public OblongAooBuilder setY(int newY)
    {
        this.y = newY;
        return this;
    }

    /**
     * @param newWidth the cell width
     * @return this instance.
     */
    public OblongAooBuilder setWidth(double newWidth)
    {
        this.width = newWidth;
        return this;
    }

    /**
     * @param newHeight the cell height
     * @return this instance.
     */
    public OblongAooBuilder setHeight(double newHeight)
    {
        this.height = newHeight;
        return this;
    }

    /**
     * @param newCenter the map center.
     * @return this instance.
     */
    public OblongAooBuilder setCenter(PolarCoordinate newCenter)
    {
        this.center = newCenter;
        return this;
    }

    /**
     * @return the Area Of Operation as a JSON string.
     * @throws JsonProcessingException in case of errors.
     */
    public String build() throws JsonProcessingException
    {
        FeatureCollection fc = new FeatureCollection();

        Point depotLocation = calculateAooFeature(fc);

        fc.setProperty("center", depotLocation.getCoordinates());
        fc.setProperty("zoom", 18);
        fc.setProperty("layer", "No map");

        Feature depotFeature = new Feature();
        depotFeature.setProperty("type", "depot");
        depotFeature.setGeometry(depotLocation);
        fc.add(depotFeature);

        return new ObjectMapper()
            .disable(SerializationFeature.INDENT_OUTPUT)
            .writeValueAsString(fc);
    }

    /**
     * @param fc the feature collection to add the AOO.
     * @return the RV depot position.
     */
    private Point calculateAooFeature(FeatureCollection fc)
    {
        if (nrCellsHigh < 1 || nrCellsWide < 1)
        {
            return new Point(center.getLongitude(), center.getLatitude());
        }

        PolarCoordinate origin = gs.walk(center, 0.5 * nrCellsHigh * height, -0.5 * nrCellsWide * width, 0.0);
        PolarCoordinate ll = gs.walk(origin, -y * height, x * width, 0.0);
        PolarCoordinate lr = gs.walk(origin, -y * height, (x + 1) * width, 0.0);
        PolarCoordinate ul = gs.walk(origin, -(y + 1) * height, x * width, 0.0);
        PolarCoordinate ur = gs.walk(origin, -(y + 1) * height, (x + 1) * width, 0.0);
        PolarCoordinate ct = gs.walk(origin, -(y + 0.5) * height, (x + 0.5) * width, 0.0);

        Polygon aoo = new Polygon(
            new LngLatAlt(ll.getLongitude(), ll.getLatitude()),
            new LngLatAlt(ul.getLongitude(), ul.getLatitude()),
            new LngLatAlt(ur.getLongitude(), ur.getLatitude()),
            new LngLatAlt(lr.getLongitude(), lr.getLatitude()),
            new LngLatAlt(ll.getLongitude(), ll.getLatitude()));

        Feature aooFeature = new Feature();
        aooFeature.setProperty("minAlt", MIN_ALT);
        aooFeature.setProperty("maxAlt", MAX_ALT);
        aooFeature.setGeometry(aoo);
        fc.add(aooFeature);

        return new Point(ct.getLongitude(), ct.getLatitude());
    }

}
