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
import static org.assertj.core.api.Assertions.offset;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * OptionsScannerTest
 */
class OptionsScannerTest
{
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanOneOption() throws IOException
    {
        Reader reader = new StringReader("bugger=LA_LA");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("LA_LA");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanOneLiteralOption() throws IOException
    {
        Reader reader = new StringReader("bugger=\"LA_LA\"");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LITERAL);
        assertThat(token.getItemString()).isEqualTo("LA_LA");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanMultipleOptions() throws IOException
    {
        Reader reader = new StringReader("bugger=lala\nbugger2=lala2");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("lala");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger2");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("lala2");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanOptionsLists() throws IOException
    {
        Reader reader = new StringReader("bugger=(lala,blbla,'nix xx')\nbugger2=('lala2';'xxx')\n");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LEFT_PAREN);
        assertThat(token.getItemString()).isEqualTo("(");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("lala");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.COMMA);
        assertThat(token.getItemString()).isEqualTo(",");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("blbla");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.COMMA);
        assertThat(token.getItemString()).isEqualTo(",");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LITERAL);
        assertThat(token.getItemString()).isEqualTo("nix xx");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.RIGHT_PAREN);
        assertThat(token.getItemString()).isEqualTo(")");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger2");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LEFT_PAREN);
        assertThat(token.getItemString()).isEqualTo("(");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LITERAL);
        assertThat(token.getItemString()).isEqualTo("lala2");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.SEMICOLON);
        assertThat(token.getItemString()).isEqualTo(";");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LITERAL);
        assertThat(token.getItemString()).isEqualTo("xxx");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.RIGHT_PAREN);
        assertThat(token.getItemString()).isEqualTo(")");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanNumberOptions01() throws IOException
    {
        Reader reader = new StringReader("bugger=+3.14\nbugger2=99999");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("+3.14");
        assertThat(token.getNumber().doubleValue()).isEqualTo(3.14, offset(1E-5));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger2");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("99999");
        assertThat(token.getNumber().intValueExact()).isEqualTo(99999);
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanNumberOptions02() throws IOException
    {
        Reader reader = new StringReader("bugger=lala looney=3.141592 caspar='xxx uu'");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("bugger");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("lala");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("looney");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("3.141592");
        assertThat(token.getNumber().doubleValue()).isEqualTo(3.141592, offset(1E-8));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("caspar");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LITERAL);
        assertThat(token.getItemString()).isEqualTo("xxx uu");
    }

    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    void shouldScanGpsCoordinates() throws IOException
    {
        Reader reader = new StringReader("origin=(37.86644;-122.30954;0)");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("origin");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.LEFT_PAREN);
        assertThat(token.getItemString()).isEqualTo("(");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("37.86644");
        assertThat(token.getNumber().doubleValue()).isEqualTo(37.86644, offset(1E-8));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.SEMICOLON);
        assertThat(token.getItemString()).isEqualTo(";");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("-122.30954");
        assertThat(token.getNumber().doubleValue()).isEqualTo(-122.30954, offset(1E-8));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.SEMICOLON);
        assertThat(token.getItemString()).isEqualTo(";");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("0");
        assertThat(token.getNumber().doubleValue()).isEqualTo(0, offset(1E-8));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.RIGHT_PAREN);
        assertThat(token.getItemString()).isEqualTo(")");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    @Test
    void shouldScanMalformedIdentifier() throws IOException
    {
        Reader reader = new StringReader("orig#in=3");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("orig");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.OTHER);
        assertThat(token.getItemString()).isEqualTo("#");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("in");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.EQUALS);
        assertThat(token.getItemString()).isEqualTo("=");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.NUMBER);
        assertThat(token.getItemString()).isEqualTo("3");
        assertThat(token.getNumber().doubleValue()).isEqualTo(3, offset(1E-8));

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    @Test
    void shouldScanIdentifier() throws IOException
    {
        Reader reader = new StringReader("orig");
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.IDENT);
        assertThat(token.getItemString()).isEqualTo("orig");

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    static Stream<Arguments> literalDataProvider()
    {
        return Stream.of(
            arguments("'orig'", Symbol.LITERAL, "orig", null),
            arguments("'orig", Symbol.LITERAL, "orig", null),
            arguments("\"orig", Symbol.LITERAL, "orig", null));
    };

    @ParameterizedTest
    @MethodSource("literalDataProvider")
    void shouldScanLiteral(String string, Symbol sym, String scannedResult, Double number)
        throws IOException
    {
        Reader reader = new StringReader(string);
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(sym);
        assertThat(token.getItemString()).isEqualTo(scannedResult);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);

        token = scanner.next();
        assertThat(token.getSymbol()).isEqualTo(Symbol.END);
    }

    static Stream<Arguments> wreckedItemsProvider()
    {
        return Stream.of(
            arguments("lala\u00fc\u00fc\u00fc", Symbol.IDENT, "lala", null, 5, 1),
            arguments("\nblabla\u00fc\u00fc\u00fc", Symbol.IDENT, "blabla", null, 7, 2),
            arguments("\t\rnix\u00fc", Symbol.IDENT, "nix", null, 6, 1));
    };

    @ParameterizedTest
    @MethodSource("wreckedItemsProvider")
    void shouldNotScanUmlautAsItems(String string, Symbol sym, String scannedResult, Double number, int column,
        int line) throws IOException
    {
        Reader reader = new StringReader(string);
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        assertThat(token.getSymbol()).isNotNull().isEqualTo(sym);
        assertThat(token.getItemString()).isNotNull().isEqualTo(scannedResult);

        assertThat(scanner.getColumnNumber()).isEqualTo(column);
        assertThat(scanner.getLineNumber()).isEqualTo(line);

        token = scanner.next();
        assertThat(token.getSymbol()).isNotNull().isEqualTo(Symbol.OTHER);
    }
}
