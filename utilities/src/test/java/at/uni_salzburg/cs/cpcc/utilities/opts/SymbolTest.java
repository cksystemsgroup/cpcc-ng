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
package at.uni_salzburg.cs.cpcc.utilities.opts;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import at.uni_salzburg.cs.cpcc.utilities.opts.Symbol;

/**
 * SymbolTest
 */
public class SymbolTest
{
    @DataProvider
    public Object[][] symbolDataProvicer()
    {
        return new Object[][]{
            new Object[]{Symbol.LEFT_PAREN, "("},
            new Object[]{Symbol.RIGHT_PAREN, ")"},
            new Object[]{Symbol.SEMICOLON, ";"},
            new Object[]{Symbol.COLON, ":"},
            new Object[]{Symbol.COMMA, ","},
            new Object[]{Symbol.EQUALS, "="},
        };
    };

    @Test(dataProvider = "symbolDataProvicer")
    public void shouldHaveANotNullSymbolString(Symbol symbol, String string)
    {
        assertEquals(string, symbol.getSymbolString());
    }

    @Test(dataProvider = "symbolDataProvicer")
    public void shouldConvertStringToSymbol(Symbol symbol, String string)
    {
        assertTrue(Symbol.getSymbol(string) == symbol);
    }

    @DataProvider
    public Object[][] nullStringSymbolDataProvicer()
    {
        return new Object[][]{
            new Object[]{Symbol.NUMBER},
            new Object[]{Symbol.IDENT},
            new Object[]{Symbol.LITERAL},
            new Object[]{Symbol.END},
            new Object[]{Symbol.OTHER},
        };
    };

    @Test(dataProvider = "nullStringSymbolDataProvicer")
    public void shouldHaveANullSymbolString(Symbol symbol)
    {
        assertNull(symbol.getSymbolString());
    }
    
    @DataProvider
    public Object[][] nonSymbolDataProvicer()
    {
        return new Object[][]{
            new Object[]{null},
            new Object[]{""},
            new Object[]{"NOSYMBOL"},
        };
    };
    
    @Test(dataProvider = "nonSymbolDataProvicer")
    public void shouldReturnNullForNonSymbolStrings(String symbol)
    {
        assertNull(Symbol.getSymbol(symbol));
    }
}
