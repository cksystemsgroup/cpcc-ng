/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2013  Clemens Krainer
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
package at.uni_salzburg.cs.cpcc.javascript.runtime.types;

import java.util.Locale;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;

/**
 * LatLngAlt
 */
public class LatLngAlt extends ScriptableObject
{

    private static final long serialVersionUID = 2875994715294180671L;

    private double lat = Double.NaN;
    private double lng = Double.NaN;
    private double alt = Double.NaN;

    private void setPrototype()
    {
        setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getContextScope(Context.getCurrentContext()),
            "LatLngAlt"));
    }

    /**
     * Construct a <code>LatLngAlt</code> object.
     */
    public LatLngAlt()
    {
        setPrototype();
    }

    /**
     * Construct a <code>LatLngAlt</code> object with given coordinates.
     * 
     * @param lat latitude
     * @param lng longitude
     * @param alt altitude
     */
    public LatLngAlt(double lat, double lng, double alt)
    {
        setPrototype();
        jsConstructor(lat, lng, alt);
    }

    /**
     * Constructor creating the latitude-longitude-altitude tuple.
     * 
     * @name LatLngAlt
     * @class Immutable container for real-world coordinates latitude, longitude, and altitude.
     * @param {double} lat The latitude of the position.
     * @param {double} lng The longitude of the position.
     * @param {double} alt The altitude of the position.
     */
    public void jsConstructor(double lat, double lng, double alt)
    {
        this.lat = lat;
        this.lng = lng;
        this.alt = alt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName()
    {
        return "LatLngAlt";
    };

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj)
    {
        if (obj == null || !(obj instanceof LatLngAlt))
            return false;
        LatLngAlt other = (LatLngAlt) obj;
        return lat == other.lat && lng == other.lng && alt == other.alt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode()
    {
        return super.hashCode();
    }

    /**
     * Method returns the latitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name lat
     * @function
     * @return {double} The latitude
     */
    public double jsGet_lat()
    {
        return lat;
    }

    /**
     * Method sets the latitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name lat
     * @function
     * @param {double} The latitude
     */
    public void jsSet_lat(double lat)
    {
        this.lat = lat;
    }

    /**
     * Method returns the longitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name lng
     * @function
     * @return {double} The longitude
     */
    public double jsGet_lng()
    {
        return lng;
    }

    /**
     * Method sets the longitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name lng
     * @function
     * @param {double} The longitude
     */
    public void jsSet_lng(double lng)
    {
        this.lng = lng;
    }

    /**
     * Method returns the altitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name alt
     * @function
     * @return {double} The altitude
     */
    public double jsGet_alt()
    {
        return alt;
    }

    /**
     * Method sets the altitude of the position.
     * 
     * @memberOf LatLngAlt#
     * @name alt
     * @function
     * @param {double} The altitude
     */
    public void jsSet_alt(double alt)
    {
        this.alt = alt;
    }

    /**
     * Method compares the position against another position
     * 
     * @memberOf LatLngAlt#
     * @name equals
     * @function
     * @param {LatLngAlt} other The position to compare against.
     * @return {boolean} true if the positions are equal, false otherwise.
     */
    public boolean jsFunction_equals(LatLngAlt other)
    {
        return equals(other);
    }

    @Override
    public String toString()
    {
        return String.format(Locale.US, "(%.8f, %.8f, %.3f)", lat, lng, alt);
    }
}
