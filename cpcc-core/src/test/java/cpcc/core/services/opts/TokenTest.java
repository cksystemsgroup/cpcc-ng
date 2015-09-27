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

import static org.fest.assertions.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import cpcc.core.services.opts.Symbol;
import cpcc.core.services.opts.Token;

/**
 * TokenTest
 */
public class TokenTest
{
    @DataProvider
    public Object[][] tokenDataProvider()
    {
        return new Object[][]
        {
            new Object[]{Symbol.EQUALS, Symbol.EQUALS.getSymbolString(), null},
            new Object[]{Symbol.LEFT_PAREN, Symbol.LEFT_PAREN.getSymbolString(), null},
            new Object[]{Symbol.RIGHT_PAREN, Symbol.RIGHT_PAREN.getSymbolString(), null},
            new Object[]{Symbol.SEMICOLON, Symbol.SEMICOLON.getSymbolString(), null},
            new Object[]{Symbol.COLON, Symbol.COLON.getSymbolString(), null},
            new Object[]{Symbol.COMMA, Symbol.COMMA.getSymbolString(), null},
            new Object[]{Symbol.NUMBER, "3.14", new BigDecimal(3.14)},
            new Object[]{Symbol.IDENT, "anIdentifier", null},
            new Object[]{Symbol.LITERAL, "aLiteral", null},
        };
    }

    @Test(dataProvider = "tokenDataProvider")
    public void shouldStoreData(Symbol symbol, String itemString, BigDecimal number)
    {
        Token token = new Token(symbol, itemString, number);

        assertThat(token.getSymbol()).isEqualTo(symbol);
        assertThat(token.getItemString()).isEqualTo(itemString);
        assertThat(token.getNumber()).isEqualTo(number);

        if (symbol == Symbol.NUMBER)
        {
            assertThat(token.getValue()).isEqualTo(number);
        }
        else
        {
            assertThat(token.getValue()).isEqualTo(itemString);
        }
    }

    @Test(dataProvider = "tokenDataProvider")
    public void shouldCopyDataViaConstructor(Symbol symbol, String itemString, BigDecimal number)
    {
        Token helperToken = new Token(symbol, itemString, number);
        Token token = new Token(helperToken);

        assertThat(token.getSymbol()).isEqualTo(symbol);
        assertThat(token.getItemString()).isEqualTo(itemString);
        assertThat(token.getNumber()).isEqualTo(number);

        if (symbol == Symbol.NUMBER)
        {
            assertThat(token.getValue()).isEqualTo(number);
        }
        else
        {
            assertThat(token.getValue()).isEqualTo(itemString);
        }
    }

    @Test(dataProvider = "tokenDataProvider")
    public void shouldCopyData(Symbol symbol, String itemString, BigDecimal number)
    {
        Token helperToken = new Token(symbol, itemString, number);
        Token token = new Token(Symbol.END, "", null);
        token.copyFields(helperToken);

        assertThat(token.getSymbol()).isEqualTo(symbol);
        assertThat(token.getItemString()).isEqualTo(itemString);
        assertThat(token.getNumber()).isEqualTo(number);

        if (symbol == Symbol.NUMBER)
        {
            assertThat(token.getValue()).isEqualTo(number);
        }
        else
        {
            assertThat(token.getValue()).isEqualTo(itemString);
        }
    }
}
