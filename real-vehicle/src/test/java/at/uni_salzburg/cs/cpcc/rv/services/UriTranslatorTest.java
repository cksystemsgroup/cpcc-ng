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
package at.uni_salzburg.cs.cpcc.rv.services;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static com.googlecode.catchexception.CatchException.catchException;
import static com.googlecode.catchexception.CatchException.caughtException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.tapestry5.ValidationException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

/**
 * UriTranslatorTest
 */
public class UriTranslatorTest
{
    UriTranslator translator;

    @BeforeMethod
    public void setUp()
    {
        translator = spy(new UriTranslator(null));
    }
    
    @DataProvider
    public Object[][] nameDataProvider()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"/"},
            new Object[]{"/a"},
            new Object[]{"/abcdefg"},
        };
    };

    @Test(dataProvider = "nameDataProvider")
    public void shouldStoreNameCorrectly(String name)
    {
        translator = spy(new UriTranslator(name));
        verifyZeroInteractions(translator);
        assertThat(translator.getName()).isEqualTo(name);
        verify(translator).getName();
    }

    @DataProvider
    public Object[][] uriDataProvider() throws URISyntaxException
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{new URI("http://www.acme.com/index.html")},
            new Object[]{new URI("file:///tmp/notexistentfileordirectory")},
        };
    };

    @Test(dataProvider = "uriDataProvider")
    public void shouldConvertToClientCorrectly(URI uri)
    {
        if (uri == null)
        {
            assertThat(translator.toClient(uri)).isNotNull().isEmpty();
        }
        else
        {
            assertThat(translator.toClient(uri)).isEqualTo(uri.toString());
        }
    }

    @Test
    public void shouldBeOfTypeUriClass()
    {
        assertThat(translator.getType()).isInstanceOf(Class.class).isEqualTo(URI.class);
    }
    
    @Test
    public void shouldHaveASpecificMessageKey()
    {
        assertThat(translator.getMessageKey()).isEqualTo("uri-format-exception");
    }

    @Test(dataProvider = "uriDataProvider")
    public void shouldParseField(URI uri) throws ValidationException
    {
        translator.parseClient(null, uri == null ? null : uri.toString(), null);
    }
    
    @Test
    public void parseClientShouldThrowUSEOnInvalidUri() throws ValidationException
    {
        translator = new UriTranslator(null);
        String message = "an exception message";
        catchException(translator).parseClient(null, "lala:\\buggerit", message);
        assertThat(caughtException()).isInstanceOf(ValidationException.class).hasMessage(message);
    }
    
    @Test
    public void shouldRenderNothing()
    {
        translator.render(null, null, null, null);
    }
}
