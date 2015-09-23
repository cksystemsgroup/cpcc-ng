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

package at.uni_salzburg.cs.cpcc.vvrte.services.js;

import static org.fest.assertions.api.Assertions.assertThat;

import java.util.HashSet;
import java.util.Set;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * SandboxClassShutterTest
 */
public class SandboxClassShutterTest
{
    private SandboxClassShutter shutter;

    @SuppressWarnings("serial")
    private static final Set<String> EXTRA_REGEX_DEFINED_CLASSES = new HashSet<String>()
    {
        {
            add("at\\.uni_salzburg\\.cs\\.cpcc\\.vvrte\\.services\\.js\\.\\$BuiltInFunctions_.*");
            add("at\\.uni_salzburg\\.cs\\.cpcc\\.vvrte\\.services\\.js\\.BuiltInFunctions_.*");
        }
    };
    
    private static final Set<String> EXTRA_ALLOWED_CLASSES = new HashSet<String>();

    @BeforeMethod
    public void setUp()
    {
        shutter = new SandboxClassShutter(EXTRA_ALLOWED_CLASSES, EXTRA_REGEX_DEFINED_CLASSES);
    }

    @DataProvider
    public Object[][] basicallyAllowedClasses()
    {
        return new Object[][]{
            new Object[]{java.io.PrintStream.class.getName()},
            new Object[]{at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions.class.getName()},
            new Object[]{java.util.ArrayList.class.getName()},
            new Object[]{java.util.List.class.getName()},
            new Object[]{java.util.Map.class.getName()},
            new Object[]{java.util.HashMap.class.getName()},
            new Object[]{java.util.Set.class.getName()},
            new Object[]{java.util.HashSet.class.getName()},
        };
    }

    @Test(dataProvider = "basicallyAllowedClasses")
    public void shouldAcceptAllowedClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isTrue();
    }

    @DataProvider
    public Object[][] someForbiddenClasses()
    {
        return new Object[][]{
            new Object[]{java.lang.Thread.class.getName()},
            new Object[]{java.lang.System.class.getName()},
            new Object[]{java.io.File.class.getName()},
            new Object[]{java.io.FileOutputStream.class.getName()},
            new Object[]{java.io.FileInputStream.class.getName()},
        };

    }

    @Test(dataProvider = "someForbiddenClasses")
    public void shouldNotAcceptOtherClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isFalse();
    }

    @DataProvider
    public Object[][] extraRegexDefinedClassesDataProvider()
    {
        return new Object[][]{
            new Object[]{"at.uni_salzburg.cs.cpcc.vvrte.services.js.$BuiltInFunctions_proxy12312412"},
            new Object[]{"at.uni_salzburg.cs.cpcc.vvrte.services.js.BuiltInFunctions_buggerit!"},
        };
    }

    @Test(dataProvider = "extraRegexDefinedClassesDataProvider")
    public void shouldAcceptAdditionalRegisteredRegexClasses(String fullClassName)
    {
        boolean result = shutter.visibleToScripts(fullClassName);
        assertThat(result).isTrue();
    }

}
