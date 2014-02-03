/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.vvrte.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

import org.geojson.LngLatAlt;

import at.uni_salzburg.cs.cpcc.core.utils.PolarCoordinate;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * PolygonZone
 */
public class PolygonZone
{
    private static final double EPSILON = 1E-9;

    private TwoTuple[] vertices;
    private double maxLat = Double.NEGATIVE_INFINITY;
    private double minLat = Double.POSITIVE_INFINITY;
    private double maxLon = Double.NEGATIVE_INFINITY;
    private double minLon = Double.POSITIVE_INFINITY;

    /**
     * @param v the vertices of the polygon as polar coordinates.
     */
    public PolygonZone(PolarCoordinate[] v)
    {
        vertices = new TwoTuple[v.length];

        for (int k = 0, l = v.length; k < l; ++k)
        {
            double x = v[k].getLatitude();
            double y = v[k].getLongitude();
            vertices[k] = new TwoTuple(x, y);
        }

        findBoundingBox();
    }

    /**
     * @param v the vertices of the polygon as polar coordinates.
     */
    public PolygonZone(List<LngLatAlt> v)
    {
        vertices = new TwoTuple[v.size()];

        for (int k = 0, l = v.size(); k < l; ++k)
        {
            double x = v.get(k).getLatitude();
            double y = v.get(k).getLongitude();
            vertices[k] = new TwoTuple(x, y);
        }

        findBoundingBox();
    }

    /**
     * @param vertices the vertices of the polygon as two-tuples.
     */
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public PolygonZone(TwoTuple[] vertices)
    {
        this.vertices = vertices;
        findBoundingBox();
    }

    /**
     * Find the bounding box of the polygon.
     */
    private void findBoundingBox()
    {
        for (int k = 0, l = vertices.length; k < l; ++k)
        {
            if (vertices[k].x < minLat)
            {
                minLat = vertices[k].x;
            }
            if (vertices[k].x > maxLat)
            {
                maxLat = vertices[k].x;
            }
            if (vertices[k].y < minLon)
            {
                minLon = vertices[k].y;
            }
            if (vertices[k].y > maxLon)
            {
                maxLon = vertices[k].y;
            }
        }

        // TODO setDepotPosition(getCenterOfGravity());
    }

    /**
     * @param p the coordinate to be checked.
     * @return true if the coordinate is inside the polygon, false otherwise.
     */
    public boolean isInside(PolarCoordinate p)
    {
        if (p == null)
        {
            return false;
        }
        return isInside(p.getLatitude(), p.getLongitude());
    }

    /**
     * @param cx the X-coordinate of a position.
     * @param cy the Y-coordinate of a position.
     * @return true if the position is inside the polygon, false otherwise.
     */
    public boolean isInside(double cx, double cy)
    {

        if (isOutsideBoundingBox(cx, cy))
        {
            return false;
        }

        int right = 0;
        int left = 0;
        for (int i = 0, l = vertices.length; i < l; ++i)
        {
            double ax = vertices[i].x;
            double ay = vertices[i].y;
            double bx = i + 1 == l ? vertices[0].x : vertices[i + 1].x;
            double by = i + 1 == l ? vertices[0].y : vertices[i + 1].y;

            if (pointIsAVertice(ax, ay, cx, cy))
            {
                return true;
            }

            ay = adjustVerticalVerticeCoordinate(cy, ay);
            by = adjustVerticalVerticeCoordinate(cy, by);

            if (thereIsNoPointOfIntersection(ay, by, cy))
            {
                continue;
            }

            if (segmentIsRightOfPoint(ax, bx, cx))
            {
                ++right;
                continue;
            }

            if (segmentIsLeftOfPoint(ax, bx, cx))
            {
                ++left;
                continue;
            }

            double k = (by - ay) / (bx - ax);
            double x = ax + (cy - ay) / k;
            if (cx <= x)
            {
                ++right;
            }
            else
            {
                ++left;
            }
        }

        return testForUneven(right, left);
    }

    /**
     * @param right the right counter.
     * @param left the left counter.
     * @return true if right or left are uneven.
     */
    private boolean testForUneven(int right, int left)
    {
        if (right != 0)
        {
            return right % 2 != 0;
        }

        return left % 2 != 0;
    }

    /**
     * @param ax the X-coordinate of the polygon segment begin
     * @param bx the X-coordinate of the polygon segment end
     * @param cx the X-coordinate of the test point
     * @return true if the segment is on the left side of the point
     */
    private boolean segmentIsLeftOfPoint(double ax, double bx, double cx)
    {
        return cx >= ax && cx >= bx;
    }

    /**
     * @param ax the X-coordinate of the polygon segment begin
     * @param bx the X-coordinate of the polygon segment end
     * @param cx the X-coordinate of the test point
     * @return true if the segment is on the right side of the point
     */
    private boolean segmentIsRightOfPoint(double ax, double bx, double cx)
    {
        return cx <= ax && cx <= bx;
    }

    /**
     * @param ax the X-coordinate of the polygon vertice
     * @param cx the Y-coordinate of the test point
     * @param ay the X-coordinate of the polygon vertice
     * @param cy the Y-coordinate of the test point
     * @return
     */
    private boolean pointIsAVertice(double ax, double ay, double cx, double cy)
    {
        return cx == ax && cy == ay;
    }

    /**
     * @param ay the Y-coordinate of the polygon segment begin
     * @param by the Y-coordinate of the polygon segment end
     * @param cy the Y-coordinate of the test point
     * @return true if there is no point of intersection, false otherwise.
     */
    private boolean thereIsNoPointOfIntersection(double ay, double by, double cy)
    {
        return (cy < ay && cy < by) || (cy > ay && cy > by);
    }

    /**
     * @param cy the test point Y-coordinate.
     * @param ny the node Y-coordinate.
     * @return the adjusted node Y-coordinate.
     */
    private double adjustVerticalVerticeCoordinate(double cy, double ny)
    {
        return cy == ny ? ny + EPSILON : ny;
    }

    /**
     * @param cx the X-coordinate of a position.
     * @param cy the Y-coordinate of a position.
     * @return true if the position is outside the bounding box, false otherwise.
     */
    private boolean isOutsideBoundingBox(double cx, double cy)
    {
        return cx < minLat || cx > maxLat || cy < minLon || cy > maxLon;
    }

    /**
     * @return the coordinates of the center of gravity.
     */
    public PolarCoordinate getCenterOfGravity()
    {
        BigDecimal x = new BigDecimal(0), y = new BigDecimal(0);
        BigDecimal doubleArea = new BigDecimal(0);

        for (int k = 0, l = vertices.length - 1; k < l; ++k)
        {
            BigDecimal ax = new BigDecimal(vertices[k].x);
            BigDecimal ay = new BigDecimal(vertices[k].y);
            BigDecimal bx = new BigDecimal(vertices[k + 1].x);
            BigDecimal by = new BigDecimal(vertices[k + 1].y);
            BigDecimal t = ax.multiply(by).subtract(bx.multiply(ay));
            x = x.add(ax.add(bx).multiply(t));
            y = y.add(ay.add(by).multiply(t));
            doubleArea = doubleArea.add(ax.multiply(by).subtract(bx.multiply(ay)));
        }

        double sixTimesArea = 3.0 * doubleArea.doubleValue();
        return new PolarCoordinate(x.doubleValue() / sixTimesArea, y.doubleValue() / sixTimesArea, 0);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString()
    {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        for (TwoTuple t : vertices)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                b.append(", ");
            }
            b.append(t);
        }

        return String.format(Locale.US, "vertices: %s", b.toString());
    }

    /**
     * TwoTuple
     */
    public static class TwoTuple
    {
        private double x;
        private double y;

        /**
         * @param x the X-coordinate.
         * @param y the y-coordinate.
         */
        public TwoTuple(double x, double y)
        {
            this.x = x;
            this.y = y;
        }

        /**
         * @return the X-value.
         */
        public double getX()
        {
            return x;
        }

        /**
         * @return the Y-value
         */
        public double getY()
        {
            return y;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString()
        {
            return String.format(Locale.US, "(%.8f, %.8f)", x, y);
        }
    }
}
