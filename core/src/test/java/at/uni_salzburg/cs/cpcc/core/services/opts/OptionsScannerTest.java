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
package at.uni_salzburg.cs.cpcc.core.services.opts;

import static org.fest.assertions.api.Assertions.assertThat;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.core.services.opts.OptionsScanner;
import at.uni_salzburg.cs.cpcc.core.services.opts.ParseException;
import at.uni_salzburg.cs.cpcc.core.services.opts.Symbol;
import at.uni_salzburg.cs.cpcc.core.services.opts.Token;

/**
 * OptionsScannerTest
 */
public class OptionsScannerTest
{
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanOneOption() throws IOException
    {
        Reader reader = new StringReader("bugger=LA_LA");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "LA_LA");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanOneLiteralOption() throws IOException
    {
        Reader reader = new StringReader("bugger=\"LA_LA\"");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "LA_LA");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanMultipleOptions() throws IOException
    {
        Reader reader = new StringReader("bugger=lala\nbugger2=lala2");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanOptionsLists() throws IOException
    {
        Reader reader = new StringReader("bugger=(lala,blbla,'nix xx')\nbugger2=('lala2';'xxx')\n");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LEFT_PAREN);
        Assert.assertEquals(token.getItemString(), "(");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.COMMA);
        Assert.assertEquals(token.getItemString(), ",");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "blbla");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.COMMA);
        Assert.assertEquals(token.getItemString(), ",");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "nix xx");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.RIGHT_PAREN);
        Assert.assertEquals(token.getItemString(), ")");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LEFT_PAREN);
        Assert.assertEquals(token.getItemString(), "(");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "lala2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.SEMICOLON);
        Assert.assertEquals(token.getItemString(), ";");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "xxx");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.RIGHT_PAREN);
        Assert.assertEquals(token.getItemString(), ")");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanNumberOptions01() throws IOException
    {
        Reader reader = new StringReader("bugger=+3.14\nbugger2=99999");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "+3.14");
        Assert.assertEquals(token.getNumber().doubleValue(), 3.14, 1E-5);
    
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger2");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "99999");
        Assert.assertEquals(token.getNumber().intValueExact(), 99999);
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanNumberOptions02() throws IOException
    {
        Reader reader = new StringReader("bugger=lala looney=3.141592 caspar='xxx uu'");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "bugger");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "lala");
        
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "looney");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");      
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "3.141592");
        Assert.assertEquals(token.getNumber().doubleValue(), 3.141592, 1E-8);
    

        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "caspar");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(token.getItemString(), "xxx uu");
    }
    
    /**
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    @Test
    public void shouldScanGpsCoordinates() throws IOException
    {
        Reader reader = new StringReader("origin=(37.86644;-122.30954;0)");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "origin");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.LEFT_PAREN);
        Assert.assertEquals(token.getItemString(), "(");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "37.86644");
        Assert.assertEquals(token.getNumber().doubleValue(), 37.86644, 1E-8);
    
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.SEMICOLON);
        Assert.assertEquals(token.getItemString(), ";");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "-122.30954");
        Assert.assertEquals(token.getNumber().doubleValue(), -122.30954, 1E-8);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.SEMICOLON);
        Assert.assertEquals(token.getItemString(), ";");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "0");
        Assert.assertEquals(token.getNumber().doubleValue(), 0, 1E-8);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.RIGHT_PAREN);
        Assert.assertEquals(token.getItemString(), ")");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    @Test
    public void shouldScanMalformedIdentifier() throws IOException
    {
        Reader reader = new StringReader("orig#in=3");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "orig");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.OTHER);
        Assert.assertEquals(token.getItemString(), "#");

        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "in");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.EQUALS);
        Assert.assertEquals(token.getItemString(), "=");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(token.getItemString(), "3");
        Assert.assertEquals(token.getNumber().doubleValue(), 3, 1E-8);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    @Test
    public void shouldScanIdentifier() throws IOException
    {
        Reader reader = new StringReader("orig");
        OptionsScanner scanner = new OptionsScanner(reader );
        
        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.IDENT);
        Assert.assertEquals(token.getItemString(), "orig");
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
        
        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    @DataProvider
    public Object[][] literalDataProvider()
    {
        return new Object[][]{
            new Object[]{"'orig'", Symbol.LITERAL, "orig", null},
            new Object[]{"'orig", Symbol.LITERAL, "orig", null},
            new Object[]{"\"orig", Symbol.LITERAL, "orig", null},
        };
    };
        
    @Test(dataProvider = "literalDataProvider")
    public void shouldScanLiteral(String string, Symbol sym, String scannedResult, Double number)
        throws IOException
    {
        Reader reader = new StringReader(string);
        OptionsScanner scanner = new OptionsScanner(reader);

        Token token = scanner.next();
        Assert.assertEquals(token.getSymbol(), sym);
        Assert.assertEquals(token.getItemString(), scannedResult);

        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);

        token = scanner.next();
        Assert.assertEquals(token.getSymbol(), Symbol.END);
    }
    
    @DataProvider
    public Object[][] wreckedItemsProvider()
    {
        return new Object[][]{
            new Object[]{"lala\u00fc\u00fc\u00fc", Symbol.IDENT, "lala", null, 5, 1},
            new Object[]{"\nblabla\u00fc\u00fc\u00fc", Symbol.IDENT, "blabla", null, 7, 2},
            new Object[]{"\t\rnix\u00fc", Symbol.IDENT, "nix", null, 6, 1},
        };
    };
    
    @Test(dataProvider = "wreckedItemsProvider")
    public void shouldNotScanUmlautAsItems(String string, Symbol sym, String scannedResult, Double number, int column, int line)
        throws IOException
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
