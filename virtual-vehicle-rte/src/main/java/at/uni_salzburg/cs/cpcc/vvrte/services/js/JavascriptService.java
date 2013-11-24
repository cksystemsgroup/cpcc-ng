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
package at.uni_salzburg.cs.cpcc.vvrte.services.js;

import java.io.IOException;

/**
 * JavascriptService
 */
public interface JavascriptService
{
    /**
     * @param script the script.
     * @param apiVersion the VVRTE API version.
     * @return the executing worker.
     * @throws IOException thrown in case of errors.
     */
    JavascriptWorker createWorker(String script, int apiVersion) throws IOException;

    /**
     * @param snapshot the frozen program.
     * @return the executing worker.
     */
    JavascriptWorker createWorker(byte[] snapshot);

    /**
     * @param string the name of the class to permit access to.
     */
    void addAllowedClass(String string);
    
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
