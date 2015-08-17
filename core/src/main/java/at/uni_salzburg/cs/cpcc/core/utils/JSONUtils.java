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

package at.uni_salzburg.cs.cpcc.core.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.tapestry5.json.JSONObject;

/**
 * JSONUtils
 */
public final class JSONUtils
{
    private JSONUtils()
    {
        // intentionally empty.
    }

    /**
     * @param obj the JSON object.
     * @return the JSON object as byte array.
     * @throws IOException thrown in case of errors.
     */
    public static byte[] toByteArray(JSONObject obj) throws IOException
    {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
        Writer osw = new OutputStreamWriter(bos, "UTF-8");
        PrintWriter writer = new PrintWriter(osw);
        obj.print(writer, true);
        writer.close();
        osw.close();
        bos.close();
        return bos.toByteArray();
    }

    /**
     * @param map the map to be converted.
     * @return the map as a JSON array.
     */
    public static String toJsonString(Map<String, String> map)
    {
        boolean first = true;
        
        StringBuilder sb = new StringBuilder("{");

        for (Entry<String, String> e : map.entrySet())
        {
            if (first)
            {
                first = false;
            }
            else
            {
                sb.append(',');
            }

            sb.append("\"").append(e.getKey()).append("\":").append(e.getValue());
        }
        
        sb.append("}");
        
        return sb.toString();
    }
}
