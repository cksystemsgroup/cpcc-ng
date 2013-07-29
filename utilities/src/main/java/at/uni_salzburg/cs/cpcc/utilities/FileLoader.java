/*
 * @(#) FileLoader.java
 *
 * This code is part of the JNavigator project.
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
package at.uni_salzburg.cs.cpcc.utilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class FileLoader
{
    public static String loadFileAsString(File file) throws IOException
    {
        FileReader reader = new FileReader(file);
        BufferedReader lineReader = new BufferedReader(reader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = lineReader.readLine()) != null)
        {
            sb.append(line);
            sb.append("\n");
        }
        lineReader.close();
        reader.close();
        return sb.toString();
    }
}
