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

/**
 * Symbol
 */
public enum Symbol
{
    LEFT_PAREN("("),
    RIGHT_PAREN(")"),
    SEMICOLON(";"),
    COLON(":"),
    COMMA(","),
    EQUALS("="),
    NUMBER(null),
    IDENT(null),
    LITERAL(null),
    END(null),
    OTHER(null);

    private String symbolString;

    /**
     * @param symbolString the symbol string.
     */
    Symbol(String symbolString)
    {
        this.symbolString = symbolString;
    }

    /**
     * @return the symbol string.
     */
    public String getSymbolString()
    {
        return symbolString;
    }

    /**
     * @param ss the symbol as a string.
     * @return the symbol.
     */
    public static Symbol getSymbol(String ss)
    {
        if (ss == null)
        {
            return null;
        }
        for (Symbol s : values())
        {
            if (s.symbolString != null && s.symbolString.equalsIgnoreCase(ss))
            {
                return s;
            }
        }
        return null;
    }

}
