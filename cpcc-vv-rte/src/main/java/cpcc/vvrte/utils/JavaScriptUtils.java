// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.vvrte.utils;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.NativeJSON;
import org.mozilla.javascript.ScriptableObject;

/**
 * Java Script utilities implementation.
 */
public final class JavaScriptUtils
{
    private JavaScriptUtils()
    {
        // Intentionally empty.
    }

    /**
     * @param obj the {@code ScriptableObject} to be converted.
     * @return the {@code ScriptableObject} as a {@code String}.
     */
    public static String toJsonString(ScriptableObject obj)
    {
        try
        {
            Context cx = Context.enter();
            cx.setOptimizationLevel(-1);
            ScriptableObject scope = cx.initStandardObjects();
            return (String) NativeJSON.stringify(cx, scope, obj, null, null);
        }
        finally
        {
            Context.exit();
        }
    }
}
