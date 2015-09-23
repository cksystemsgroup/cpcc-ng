// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package at.uni_salzburg.cs.cpcc.commons.services;

import static org.fest.assertions.api.Assertions.assertThat;

import org.apache.tapestry5.ValidationException;
import org.ros.namespace.GraphName;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.commons.services.GraphNameTranslator;

/**
 * GraphNameTranslatorTest
 */
public class GraphNameTranslatorTest
{
    GraphNameTranslator translator;

    @BeforeMethod
    public void setUp()
    {
        translator = new GraphNameTranslator("noName");
    }

    @DataProvider
    public Object[][] nameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"name"},
            new Object[]{"othername"},
            new Object[]{"simpleName"},
        };
    };

    @Test(dataProvider = "nameDataProvider")
    public void shouldStoreNameCorrectly(String name)
    {
        GraphNameTranslator myTranslator = new GraphNameTranslator(name);
        assertThat(myTranslator.getName()).isEqualTo(name);
    }

    @Test(dataProvider = "nameDataProvider")
    public void shouldConvertToClientCorrectly(String name)
    {
        assertThat(translator.toClient(name == null ? null : GraphName.of(name))).isEqualTo(name == null ? "" : name);
    }

    @Test
    public void shouldHaveCorrectType()
    {
        assertThat(translator.getType()).isEqualTo(GraphName.class);
    }

    @Test
    public void shouldHaveCorrectMessageKey()
    {
        assertThat(translator.getMessageKey()).isEqualTo("graphname-format-exception");
    }

    @Test(dataProvider = "nameDataProvider")
    public void shouldParseClientCorrectly(String clientValue) throws ValidationException
    {
        assertThat(translator.parseClient(null, clientValue, null))
            .isNotNull()
            .isEqualTo(clientValue == null ? GraphName.empty() : GraphName.of(clientValue));
    }

    @Test(expectedExceptions = ValidationException.class)
    public void shouldThrowValidationExceptionForMalformedGraphNames() throws ValidationException
    {
        translator.parseClient(null, "xx xx", null);
    }

    @Test
    public void shouldRenderAnything()
    {
        translator.render(null, null, null, null);
    }
}
