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

import org.mozilla.javascript.NativeJavaObject;
import org.mozilla.javascript.Scriptable;

/**
 * Sandbox Native Java Object
 */
public class SandboxNativeJavaObject extends NativeJavaObject
{
    private static final long serialVersionUID = -839055571346169008L;

    /**
     * @param scope the scope.
     * @param javaObject the Java object.
     * @param staticType the static type.
     */
    public SandboxNativeJavaObject(Scriptable scope, Object javaObject, Class<?> staticType)
    {
        super(scope, javaObject, staticType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object get(String name, Scriptable start)
    {
        return "getClass".equals(name)
            ? NOT_FOUND
            : super.get(name, start);
    }
}
