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

import java.io.File;
import java.io.FileNotFoundException;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeArray;
import org.mozilla.javascript.ScriptableObject;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import at.uni_salzburg.cs.cpcc.javascript.JSInterpreter;
import at.uni_salzburg.cs.cpcc.javascript.Utils;

/**
 * VirtualVehicleFile
 */
public class VirtualVehicleFile extends ScriptableObject
{

    private static final long serialVersionUID = -361941093499277654L;
    private String fileName;

    private void setPrototype()
    {
        setPrototype(ScriptableObject.getClassPrototype(JSInterpreter.getContextScope(Context.getCurrentContext()),
            "File"));
    }

    /**
     * Construct a <code>VirtualVehicleFile</code> object.
     */
    public VirtualVehicleFile()
    {
        setPrototype();
    }

    /**
     * @return the <code>File</code> delegate.
     */
    @SuppressFBWarnings("PATH_TRAVERSAL_IN")
    File getDelegate()
    {
        String prefix = JSInterpreter.getContextProperties().getProperty("virtual.vehicle.data.directory");
        return new File(prefix, this.fileName);
    }

    /**
     * @name File
     * @class Immutable container for virtual vehicle file I/O
     * @param name The name of the file
     * @throws FileNotFoundException thrown in case of errors.
     */
    public void jsConstructor(String name) throws FileNotFoundException
    {
        this.fileName = Utils.compactPath(name);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getClassName()
    {
        return "File";
    }

    /**
     * Deletes the file or directory denoted by this abstract pathname. If this pathname denotes a directory, then the
     * directory must be empty in order to be deleted.
     * 
     * @memberOf File#
     * @name delete
     * @function
     * @return true if and only if the file or directory is successfully deleted; false otherwise
     */
    public boolean jsFunction_delete()
    {
        return getDelegate().delete();
    }

    /**
     * Tests whether the file or directory denoted by this abstract pathname exists.
     * 
     * @memberOf File#
     * @name exists
     * @function
     * @return true if and only if the file or directory denoted by this abstract pathname exists; false
     *         otherwise
     */
    public boolean jsFunction_exists()
    {
        return getDelegate().exists();
    }

    /**
     * Returns the name of the file or directory denoted by this abstract pathname. This is just the last name in the
     * pathname's name sequence.
     * 
     * @memberOf File#
     * @name getName
     * @function
     * @return The name of the file or directory denoted by this abstract pathname.
     */
    public String jsFunction_getName()
    {
        return fileName;
    }

    /**
     * Tests whether the file or directory denoted by this abstract pathname is an existing directory
     * 
     * @memberOf File#
     * @name isDirectory
     * @function
     * @return true if the specified path is an existing directory.
     */
    public boolean jsFunction_isDirectory()
    {
        return getDelegate().isDirectory();
    }

    /**
     * Tests whether the file or directory denoted by this abstract pathname exists is an existing file.
     * 
     * @memberOf File#
     * @name isFile
     * @function
     * @return true if the specified path is an existing file.
     */
    public boolean jsFunction_isFile()
    {
        return getDelegate().isFile();
    }

    /**
     * Creates the directory named by this abstract pathname.
     * 
     * @memberOf File#
     * @name mkdir
     * @function
     * @return true if and only if the directory was created; false otherwise
     */
    public boolean jsFunction_mkdir()
    {
        return getDelegate().mkdir();
    }

    /**
     * Creates the directory named by this abstract pathname, including any necessary but nonexistent parent
     * directories.
     * 
     * @memberOf File#
     * @name mkdirs
     * @function
     * @return {boolean} true if and only if the directory was created, along with all necessary parent directories;
     *         false otherwise
     */
    public boolean jsFunction_mkdirs()
    {
        return getDelegate().mkdirs();
    }

    /**
     * Returns the length of the file denoted by this abstract pathname.
     * 
     * @memberOf File#
     * @name length
     * @function
     * @return The length, in bytes, of the file denoted by this abstract pathname.
     */
    public long jsFunction_length()
    {
        return getDelegate().length();
    }

    /**
     * Lists the directory named by this abstract pathname.
     * 
     * @memberOf File#
     * @name list
     * @function
     * @return the list of files in this directory.
     */
    public NativeArray jsFunction_list()
    {
        return new NativeArray(getDelegate().list());
    }

}
