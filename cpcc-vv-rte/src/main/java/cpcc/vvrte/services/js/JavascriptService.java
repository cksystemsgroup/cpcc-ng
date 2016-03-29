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

package cpcc.vvrte.services.js;

import java.io.IOException;

import cpcc.vvrte.entities.VirtualVehicle;

/**
 * JavascriptService
 */
public interface JavascriptService
{
    /**
     * @param vehicle the Virtual Vehicle.
     * @param useContinuation use the continuation, if possible.
     * @return the executing worker.
     * @throws IOException thrown in case of errors.
     */
    JavascriptWorker createWorker(VirtualVehicle vehicle, boolean useContinuation) throws IOException;

    /**
     * @param string the regular expression of the name of the classes to permit access to.
     */
    void addAllowedClassRegex(String string);

    /**
     * @param script the JavaScript code.
     * @param apiVersion the API version.
     * @return the verification result.
     * @throws IOException thrown in case of errors.
     */
    Object[] codeVerification(String script, int apiVersion) throws IOException;
}
