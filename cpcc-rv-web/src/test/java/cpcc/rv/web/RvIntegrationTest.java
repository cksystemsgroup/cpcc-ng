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

package cpcc.rv.web;

import java.io.File;
import java.util.stream.Stream;

import org.apache.tapestry5.test.SeleniumTestCase;
import org.testng.ITestContext;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.testng.xml.XmlTest;

/**
 * RvIntegrationTest implementation.
 */
public class RvIntegrationTest extends SeleniumTestCase
{
    private static final String DB_PREFIX = "target/integration_test";
    private static final String[] DB_FILE_NAMES = {DB_PREFIX + ".mv.db", DB_PREFIX + ".trace.db"};

    @BeforeTest(groups = "beforeStartup")
    public void setUp(final ITestContext testContext, XmlTest xmlTest) throws Exception
    {
        Stream.of(DB_FILE_NAMES).map(x -> new File(x)).filter(File::exists).forEach(File::delete);
    }

    @Test
    public void persist_entities()
    {
        open("/");
        assertEquals(getText("//span[@id='name']").length(), 0);

        clickAndWait("link=create item");
        assertText("//span[@id='name']", "name");
    }
}
