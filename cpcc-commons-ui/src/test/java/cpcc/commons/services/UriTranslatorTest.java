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

package cpcc.commons.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.stream.Stream;

import org.apache.tapestry5.ValidationException;
import org.assertj.core.api.Fail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * UriTranslatorTest
 */
class UriTranslatorTest
{
    UriTranslator translator;

    @BeforeEach
    void setUp()
    {
        translator = spy(new UriTranslator(null));
    }

    static Stream<Arguments> nameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("/"),
            arguments("/a"),
            arguments("/abcdefg"));
    };

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldStoreNameCorrectly(String name)
    {
        translator = spy(new UriTranslator(name));
        verifyNoInteractions(translator);
        assertThat(translator.getName()).isEqualTo(name);
        verify(translator).getName();
    }

    static Stream<Arguments> uriDataProvider() throws URISyntaxException
    {
        return Stream.of(
            arguments((URI) null),
            arguments(new URI("http://www.acme.com/index.html")),
            arguments(new URI("file:///tmp/notexistentfileordirectory")));
    };

    @ParameterizedTest
    @MethodSource("uriDataProvider")
    void shouldConvertToClientCorrectly(URI uri)
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
    void shouldBeOfTypeUriClass()
    {
        assertThat(translator.getType()).isInstanceOf(Class.class).isEqualTo(URI.class);
    }

    @Test
    void shouldHaveASpecificMessageKey()
    {
        assertThat(translator.getMessageKey()).isEqualTo("uri-format-exception");
    }

    @ParameterizedTest
    @MethodSource("uriDataProvider")
    void shouldParseField(URI uri)
    {
        try
        {
            translator.parseClient(null, uri == null ? null : uri.toString(), null);
        }
        catch (ValidationException e)
        {
            assertThat(e).isNull();
        }
    }

    @Test
    void parseClientShouldThrowUSEOnInvalidUri() throws ValidationException
    {
        translator = new UriTranslator(null);
        String message = "an exception message";

        try
        {
            translator.parseClient(null, "lala:\\buggerit", message);
            Fail.failBecauseExceptionWasNotThrown(ValidationException.class);
        }
        catch (ValidationException e)
        {
            assertThat(e).hasMessage(message);
        }
    }

    @Test
    void shouldRenderNothing()
    {
        try
        {
            translator.render(null, null, null, null);
        }
        catch (RuntimeException e)
        {
            assertThat(e).isNull();
        }
    }
}
