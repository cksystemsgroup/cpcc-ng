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

package at.uni_salzburg.cs.cpcc.core.services.opts;

import java.util.Collection;
import java.util.Locale;

/**
 * ParseException
 */
@SuppressWarnings("serial")
public class ParseException extends Exception
{
    private int lineNumber;
    private int columnNumber;

    /**
     * @param expected the expected symbol(s).
     * @param symbol the actual symbol.
     * @param lineNumber the current line number.
     * @param columnNumber the current column number.
     */
    public ParseException(Collection<Symbol> expected, Symbol symbol, int lineNumber, int columnNumber)
    {
        super(String.format(Locale.US, "Expected a %s but got a %s in line %d column %d",
            joinIt(expected), symbol.name(), lineNumber, columnNumber));
        this.lineNumber = lineNumber;
        this.columnNumber = columnNumber;
    }

    /**
     * @param symbols the symbols.
     * @return the joint string of symbol names.
     */
    private static String joinIt(Collection<Symbol> symbols)
    {
        StringBuilder b = new StringBuilder();
        boolean first = true;
        int size = symbols.size() - 1;
        for (Symbol e : symbols)
        {
            if (first)
            {
                first = false;
            }
            else
            {
                b.append(--size > 0 ? ", " : " or ");
            }
            b.append(e.name());
        }
        return b.toString();
    }

    /**
     * @return the lineNumber
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * @return the columnNumber
     */
    public int getColumnNumber()
    {
        return columnNumber;
    }

}
