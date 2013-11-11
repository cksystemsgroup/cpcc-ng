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

import java.util.HashSet;
import java.util.Set;

import org.mozilla.javascript.ClassShutter;

/**
 * SandboxClassShutter
 */
public class SandboxClassShutter implements ClassShutter
{
    @SuppressWarnings("serial")
    private static final Set<String> ALLOWED_CLASSES = new HashSet<String>()
    {
        {
            add(java.io.PrintStream.class.getName());
            add(at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions.class.getName());
            add("at.uni_salzburg.cs.cpcc.vvrte.services.js.JavascriptServiceTest$MyBuiltInFunctions");
            add(java.util.ArrayList.class.getName());
            add(java.util.List.class.getName());
            add(java.util.Map.class.getName());
            add(java.util.HashMap.class.getName());
            add(java.util.Set.class.getName());
            add(java.util.HashSet.class.getName());
        }
    };

    private Set<String> allowedClasses = new HashSet<String>();

    /**
     * @param extraAllowedClasses the extra allowed class names.
     */
    public SandboxClassShutter(Set<String> extraAllowedClasses)
    {
        allowedClasses.addAll(extraAllowedClasses);
        allowedClasses.addAll(ALLOWED_CLASSES);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visibleToScripts(String fullClassName)
    {
        return allowedClasses.contains(fullClassName);
    }
}
