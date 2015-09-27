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
            add(cpcc.vvrte.services.js.BuiltInFunctions.class.getName());
            add(java.util.ArrayList.class.getName());
            add(java.util.List.class.getName());
            add(java.util.Map.class.getName());
            add(java.util.HashMap.class.getName());
            add(java.util.Set.class.getName());
            add(java.util.HashSet.class.getName());
            add(cpcc.core.utils.NullPrintStream.class.getName());
        }
    };

    private Set<String> allowedClasses = new HashSet<String>();

    private Set<String> regexDefinedClasses;

    /**
     * @param extraAllowedClasses the extra allowed class names.
     * @param regexDefinedClasses the regular expression defined class names.
     */
    public SandboxClassShutter(Set<String> extraAllowedClasses, Set<String> regexDefinedClasses)
    {
        allowedClasses.addAll(extraAllowedClasses);
        allowedClasses.addAll(ALLOWED_CLASSES);
        this.regexDefinedClasses = regexDefinedClasses;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean visibleToScripts(String fullClassName)
    {
        boolean result = allowedClasses.contains(fullClassName);
        if (!result)
        {
            result = checkIfRegexDefinedClassesMatch(fullClassName);
        }
        return result;
    }

    /**
     * @param fullClassName the fully qualified class name.
     * @return true if any registered regular expression defined class name matches.
     */
    private boolean checkIfRegexDefinedClassesMatch(String fullClassName)
    {
        for (String classRegex : regexDefinedClasses)
        {
            if (fullClassName.matches(classRegex))
            {
                allowedClasses.add(fullClassName);
                return true;
            }
        }
        return false;
    }

}
