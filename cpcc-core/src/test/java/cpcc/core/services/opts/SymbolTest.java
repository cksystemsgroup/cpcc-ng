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

package cpcc.core.services.opts;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * SymbolTest
 */
public class SymbolTest
{
    static Stream<Arguments>  symbolDataProvicer()
    {
        return Stream.of(
            arguments(Symbol.LEFT_PAREN, "("),
            arguments(Symbol.RIGHT_PAREN, ")"),
            arguments(Symbol.SEMICOLON, ";"),
            arguments(Symbol.COLON, ":"),
            arguments(Symbol.COMMA, ","),
            arguments(Symbol.EQUALS, "="));
    };

    @ParameterizedTest
    @MethodSource("symbolDataProvicer")
    public void shouldHaveANotNullSymbolString(Symbol symbol, String string)
    {
        assertThat(string).isEqualTo(symbol.getSymbolString());
    }

    @ParameterizedTest
    @MethodSource("symbolDataProvicer")
    public void shouldConvertStringToSymbol(Symbol symbol, String string)
    {
        assertThat(Symbol.getSymbol(string)).isEqualTo(symbol);
    }

    static Stream<Arguments> nullStringSymbolDataProvicer()
    {
        return Stream.of(
            arguments(Symbol.NUMBER),
            arguments(Symbol.IDENT),
            arguments(Symbol.LITERAL),
            arguments(Symbol.END),
            arguments(Symbol.OTHER));
    };

    @ParameterizedTest
    @MethodSource("nullStringSymbolDataProvicer")
    public void shouldHaveANullSymbolString(Symbol symbol)
    {
        assertThat(symbol.getSymbolString()).isNull();
    }

    static Stream<Arguments> nonSymbolDataProvicer()
    {
        return Stream.of(
            arguments((String) null),
            arguments(""),
            arguments("NOSYMBOL"));
    };

    @ParameterizedTest
    @MethodSource("nonSymbolDataProvicer")
    public void shouldReturnNullForNonSymbolStrings(String symbol)
    {
        assertThat(Symbol.getSymbol(symbol)).isNull();
    }
}
