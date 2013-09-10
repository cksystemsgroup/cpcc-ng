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
package at.uni_salzburg.cs.cpcc.rv.services.opts;

import java.math.BigDecimal;

/**
 * Token
 */
public class Token
{
    private Symbol symbol;

    private String itemString;

    private BigDecimal number;

    /**
     * @param token the token.
     */
    public Token(Token token)
    {
        this.symbol = token.getSymbol();
        this.itemString = token.getItemString();
        this.number = token.getNumber();
    }

    /**
     * @param symbol the symbol.
     * @param itemString the item string.
     * @param number the number.
     */
    public Token(Symbol symbol, String itemString, BigDecimal number)
    {
        this.symbol = symbol;
        this.itemString = itemString;
        this.number = number;
    }

    /**
     * @return the symbol.
     */
    public Symbol getSymbol()
    {
        return symbol;
    }

    /**
     * @return the item string.
     */
    public String getItemString()
    {
        return itemString;
    }

    /**
     * @return the number.
     */
    public BigDecimal getNumber()
    {
        return number;
    }

    /**
     * @param master the master token.
     * @return this object.
     */
    public Token copyFields(Token master)
    {
        this.symbol = master.symbol;
        this.itemString = master.itemString;
        this.number = master.number;
        return this;
    }
}
