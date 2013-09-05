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

    public Token(Token token)
    {
        this.symbol = token.getSymbol();
        this.itemString = token.getItemString();
        this.number = token.getNumber();
    }

    public Token(Symbol symbol, String itemString, BigDecimal number)
    {
        this.symbol = symbol;
        this.itemString = itemString;
        this.number = number;
    }

    public Symbol getSymbol() {
            return symbol;
    }

    public void setSymbol(Symbol symbol) {
            this.symbol = symbol;
    }

    public String getItemString() {
            return itemString;
    }

    public void setItemString(String itemString) {
            this.itemString = itemString;
    }

    public BigDecimal getNumber() {
            return number;
    }

    public void setNumber(BigDecimal number) {
            this.number = number;
    }

    public Token copyFields(Token master) {
            this.symbol = master.symbol;
            this.itemString = master.itemString;
            this.number = master.number;
            return this;
    }
}
