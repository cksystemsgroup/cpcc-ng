/*
 * This code is part of the JNavigator project.
 *
 * Copyright (c) 2009-2013 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.utilities;

/**
 * This class implements a 3-tuple to describe a position in a orthogonal coordinate system.
 */
public class CartesianCoordinate
{
    /**
     * The x value of this vector.
     */
    private double x;

    /**
     * The y value of this vector.
     */
    private double y;

    /**
     * The z value of this vector.
     */
    private double z;

    /**
     * Construct a null position vector, i.e. x, y and z are zero.
     */
    public CartesianCoordinate()
    {
        x = 0;
        y = 0;
        z = 0;
    }

    /**
     * Construct a position vector as a copy of another position vector.
     * 
     * @param p the position vector to be copied.
     */
    public CartesianCoordinate(CartesianCoordinate p)
    {
        this.x = p.x;
        this.y = p.y;
        this.z = p.z;
    }

    /**
     * Construct a position vector with the given values for x, y and z.
     * 
     * @param x the x coordinate.
     * @param y the y coordinate.
     * @param z the z coordinate.
     */
    public CartesianCoordinate(double x, double y, double z)
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * @return the x coordinate.
     */
    public double getX()
    {
        return x;
    }

    /**
     * @param x the new x coordinate
     */
    public void setX(double x)
    {
        this.x = x;
    }

    /**
     * @return the y coordinate.
     */
    public double getY()
    {
        return y;
    }

    /**
     * @param y the new y coordinate.
     */
    public void setY(double y)
    {
        this.y = y;
    }

    /**
     * @return the z coordinate.
     */
    public double getZ()
    {
        return z;
    }

    /**
     * @param z the new z coordinate
     */
    public void setZ(double z)
    {
        this.z = z;
    }

    /**
     * Copy the values of another CartesianCoordinate vector to this vector.
     * 
     * @param p the other CartesianCoordinate vector.
     */
    public void set(CartesianCoordinate p)
    {
        x = p.x;
        y = p.y;
        z = p.z;
    }

    /**
     * @return the norm of the position vector.
     */
    public double norm()
    {
        return Math.sqrt(x * x + y * y + z * z);
    }

    /**
     * @param p another vector to be added to this vector.
     * @return the resulting vector
     */
    public CartesianCoordinate add(CartesianCoordinate p)
    {
        return new CartesianCoordinate(x + p.x, y + p.y, z + p.z);
    }

    /**
     * @param p another vector to be subtracted of this vector.
     * @return the resulting vector
     */
    public CartesianCoordinate subtract(CartesianCoordinate p)
    {
        return new CartesianCoordinate(x - p.x, y - p.y, z - p.z);
    }

    /**
     * @param scalar the scalar this position is multiplied by.
     * @return the resulting vector
     */
    public CartesianCoordinate multiply(double scalar)
    {
        return new CartesianCoordinate(x * scalar, y * scalar, z * scalar);
    }

    /**
     * Perform a scalar multiplication with another vector.
     * 
     * @param p the other vector
     * @return the result
     */
    public double multiply(CartesianCoordinate p)
    {
        return x * p.x + y * p.y + z * p.z;
    }

    /**
     * Perform a cross multiplication with another vector.
     * 
     * @param p the other vector
     * @return the result
     */
    public CartesianCoordinate crossProduct(CartesianCoordinate p)
    {
        return new CartesianCoordinate(y * p.z - z * p.y, z * p.x - x * p.z, x * p.y - y * p.x);
    }

    /**
     * Return the normalized current vector, i.e. a vector in the same direction having length one.
     * 
     * @return the normalized current vector.
     */
    public CartesianCoordinate normalize()
    {
        double n = norm();

        if (n == 0)
        {
            return new CartesianCoordinate(1, 0, 0);
        }

        return multiply(1 / n);
    }

    /**
     * {@inheritDoc}
     */
    public String toString()
    {
        return "(" + x + "m, " + y + "m, " + z + "m)";
    }
}
