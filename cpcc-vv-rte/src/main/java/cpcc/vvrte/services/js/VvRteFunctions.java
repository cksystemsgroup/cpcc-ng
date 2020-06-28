// This code is part of the CPCC-NG project.
//
// Copyright (c) 2013 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.services.js;

import java.io.PrintStream;
import java.io.Serializable;

import cpcc.core.utils.NullPrintStream;

/**
 * VvRteFunctions
 */
public final class VvRteFunctions implements Serializable
{
    private static final long serialVersionUID = -5543843858420988049L;

    static final String[] FUNCTIONS = {"getVvRte", "getStdOut"};

    private static BuiltInFunctions vvRte;
    private static PrintStream stdOut = new NullPrintStream();

    /**
     * Private constructor.
     */
    private VvRteFunctions()
    {
        // intentionally empty
    }

    /**
     * @return the vvRte
     */
    public static BuiltInFunctions getVvRte()
    {
        return vvRte;
    }

    /**
     * @param vvRte the vvRte to set
     */
    public static void setVvRte(BuiltInFunctions vvRte)
    {
        VvRteFunctions.vvRte = vvRte;
    }

    /**
     * @return the standard output print stream.
     */
    public static PrintStream getStdOut()
    {
        return stdOut;
    }

    /**
     * @param stdOut the standard output print stream to set
     */
    public static void setStdOut(PrintStream stdOut)
    {
        VvRteFunctions.stdOut = stdOut;
    }

}
