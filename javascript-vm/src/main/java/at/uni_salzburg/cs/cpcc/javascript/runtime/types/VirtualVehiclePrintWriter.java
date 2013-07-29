/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2012  Clemens Krainer
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

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.ScriptableObject;

import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;

/**
 * VirtualVehiclePrintWriter
 */
public class VirtualVehiclePrintWriter extends ScriptableObject
{

    private static final long serialVersionUID = -3020497671453909858L;
    private VirtualVehicleFile file;
    private String csn;

    private void setPrototype()
    {
        setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getContextScope(Context.getCurrentContext()),
            "PrintWriter"));
    }

    /**
     * Construct a <code>VirtualVehiclePrintWriter</code> object.
     */
    public VirtualVehiclePrintWriter()
    {
        setPrototype();
    }

    /**
     * @name VirtualVehiclePrintWriter
     * @class Immutable container for virtual vehicle print output.
     * @param {File} file The file object containing the abstract path name.
     * @throws FileNotFoundException
     * @throws UnsupportedEncodingException
     */
    public void jsConstructor(VirtualVehicleFile file, String csn) throws FileNotFoundException,
        UnsupportedEncodingException
    {
        this.file = file;
        this.csn = csn != null && "undefined".equals(csn) ? null : csn;
        file.getDelegate().delete();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName()
    {
        return "PrintWriter";
    }

    /**
     * @return
     * @throws IOException
     */
    @SuppressWarnings("resource")
    private PrintWriter getDelegate() throws IOException
    {
        FileOutputStream os = new FileOutputStream(file.getDelegate(), true);
        return csn == null ? new PrintWriter(os) : new PrintWriter(new OutputStreamWriter(os, csn));
    }

    /**
     * Prints the provided argument, followed by a newline.
     * 
     * @memberOf PrintWriter#
     * @name println
     * @function
     * @memberOf _global_
     * @param {Object} arg The object to be printed.
     * @throws IOException
     */
    public void jsFunction_println(Object arg) throws IOException
    {
        PrintWriter delegate = getDelegate();
        delegate.println(arg);
        delegate.close();
    }

    /**
     * Prints the provided argument.
     * 
     * @memberOf PrintWriter#
     * @name println
     * @function
     * @memberOf _global_
     * @param {Object} arg The object to be printed.
     * @throws IOException
     */
    public void jsFunction_print(Object arg) throws IOException
    {
        PrintWriter delegate = getDelegate();
        delegate.print(arg);
        delegate.close();
    }

    /**
     * Prints the provided argument in the specified format.
     * 
     * @memberOf PrintWriter#
     * @name printf
     * @function
     * @memberOf _global_
     * @param {String} format The formatting string.
     * @param {Object} args The objects to be printed.
     * @throws IOException
     */
    public void jsFunction_printf(String format, Object args) throws IOException
    {
        jsFunction_print(String.format(format, args));
    }

}
