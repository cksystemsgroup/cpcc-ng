/*
 * This code is part of the CPCC-NG project.
 * Copyright (c) 2013  Clemens Krainer, Michael Lippautz
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
package at.uni_salzburg.cs.cpcc.javascript;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.junit.Test;

import at.uni_salzburg.cs.cpcc.javascript.runtime.base.NullPositionProvider;


public class BasicTest
{

    private JSInterpreter createJSI(String resourceName) throws IllegalAccessException, InstantiationException,
        InvocationTargetException, IOException
    {
        InputStream fis = Thread.currentThread().getContextClassLoader().getResourceAsStream(resourceName);
        JSInterpreterBuilder b = new JSInterpreterBuilder();
        b.addCodefile(resourceName, fis);
        // b.addProvidedPackage(packageScope, o);
        // b.addProvidedTypes(type);
        return b.build();
    }

    @Test
    public void testLoadVV() throws IOException, IllegalAccessException, InstantiationException,
        InvocationTargetException
    {
        String file = "test-js/simple-vv.js";
        JSInterpreter jsi = createJSI(file);
        jsi.setPositionProvider(new NullPositionProvider());
        jsi.start();
        jsi.close();
    }

    @Test
    public void testArithmeticParse() throws IOException, IllegalAccessException, InstantiationException,
        InvocationTargetException
    {
        String file = "test-js/basic-arithmetic.js";
        JSInterpreter jsi = createJSI(file);
        jsi.setPositionProvider(new NullPositionProvider());
        jsi.start();
        jsi.close();
    }

}
