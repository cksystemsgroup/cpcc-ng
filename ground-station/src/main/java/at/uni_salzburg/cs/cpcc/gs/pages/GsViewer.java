/*
 * This code is part of the CPCC-NG project.
 *
 * Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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
package at.uni_salzburg.cs.cpcc.gs.pages;

import javax.inject.Inject;

import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;

import at.uni_salzburg.cs.cpcc.commons.pages.Viewer;

/**
 * GsViewer
 */
public class GsViewer extends Viewer
{
    @Inject
    private JavaScriptSupport js;

    /**
     * import the Map JavaScript stack.
     */
    @SetupRender
    public void importStack()
    {
        super.importStack();
        js.importStack("RealVehicle");
        js.addScript("realVehicleInit();");
    }
}
