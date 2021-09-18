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
import static org.assertj.core.api.Assertions.failBecauseExceptionWasNotThrown;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.apache.tapestry5.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.ros.namespace.GraphName;

/**
 * GraphNameTranslatorTest
 */
class GraphNameTranslatorTest
{
    GraphNameTranslator translator;

    @BeforeEach
    void setUp()
    {
        translator = new GraphNameTranslator("noName");
    }

    static Stream<Arguments> nameDataProvider()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("name"),
            arguments("othername"),
            arguments("simpleName"));
    };

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldStoreNameCorrectly(String name)
    {
        GraphNameTranslator myTranslator = new GraphNameTranslator(name);
        assertThat(myTranslator.getName()).isEqualTo(name);
    }

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldConvertToClientCorrectly(String name)
    {
        assertThat(translator.toClient(name == null ? null : GraphName.of(name))).isEqualTo(name == null ? "" : name);
    }

    @Test
    void shouldHaveCorrectType()
    {
        assertThat(translator.getType()).isEqualTo(GraphName.class);
    }

    @Test
    void shouldHaveCorrectMessageKey()
    {
        assertThat(translator.getMessageKey()).isEqualTo("graphname-format-exception");
    }

    @ParameterizedTest
    @MethodSource("nameDataProvider")
    void shouldParseClientCorrectly(String clientValue) throws ValidationException
    {
        assertThat(translator.parseClient(null, clientValue, null))
            .isNotNull()
            .isEqualTo(clientValue == null ? GraphName.empty() : GraphName.of(clientValue));
    }

    void shouldThrowValidationExceptionForMalformedGraphNames()
    {
        try
        {
            translator.parseClient(null, "xx xx", null);
            failBecauseExceptionWasNotThrown(ValidationException.class);
        }
        catch (ValidationException e)
        {
            assertThat(e).hasMessage("");
        }
    }

    @Test
    void shouldRenderAnything()
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
