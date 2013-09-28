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

import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * OptionsScanner
 */
public class OptionsScanner
{
    @SuppressWarnings("serial")
    private static final Map<String, Token> CHAR_MAP = new HashMap<String, Token>()
    {
        {
            put(Symbol.EQUALS.getSymbolString(), Token.EQUALS);
            put(Symbol.LEFT_PAREN.getSymbolString(), Token.LEFT_PAREN);
            put(Symbol.RIGHT_PAREN.getSymbolString(), Token.RIGHT_PAREN);
            put(Symbol.SEMICOLON.getSymbolString(), Token.SEMICOLON);
            put(Symbol.COLON.getSymbolString(), Token.COLON);
            put(Symbol.COMMA.getSymbolString(), Token.COMMA);
        }
    };

    private Reader reader;
    private Character ch = null;
    private boolean firstRun = true;
    private int lineNumber = 1;
    private int columnNumber = 0;

    private static boolean isDigit(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c >= '0' && c <= '9';
    }

    private static boolean isLetter(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c == '_';
    }

    private static boolean isSpace(Character c)
    {
        if (c == null)
        {
            return false;
        }
        return c == ' ' || c == '\t' || c == '\r' || c == '\n';
    }

    private static boolean isSign(Character c)
    {
        return c == '-' || c == '+';
    }

    /**
     * @param reader the reader
     */
    public OptionsScanner(Reader reader)
    {
        this.reader = reader;
    }

    /**
     * @return the line number.
     */
    public int getLineNumber()
    {
        return lineNumber;
    }

    /**
     * @return the column number.
     */
    public int getColumnNumber()
    {
        return columnNumber;
    }

    /**
     * @return the next character.
     * @throws IOException thrown in case of errors.
     */
    private Character getChar() throws IOException
    {
        char[] cbuf = new char[1];
        int n = reader.read(cbuf);
        if (n <= 0)
        {
            return null;
        }

        ++columnNumber;
        if (cbuf[0] == '\n')
        {
            ++lineNumber;
            columnNumber = 0;
        }

        return cbuf[0];
    }

    /**
     * @return the next token.
     * @throws IOException thrown in case of errors.
     */
    public Token next() throws IOException
    {
        if (firstRun)
        {
            ch = getChar();
            firstRun = false;
        }

        while (isSpace(ch))
        {
            ch = getChar();
        }

        if (ch == null)
        {
            return new Token(Symbol.END, null, null);
        }

        if (isDigit(ch) || isSign(ch))
        {
            return scanNumber();
        }

        if (ch == '"' || ch == '\'')
        {
            return scanLiteral();
        }

        if (isLetter(ch))
        {
            return scanIdentifier();
        }

        if (ch == '=')
        {
            ch = getChar();
            return new Token(Symbol.EQUALS, "=", null);
        }

        return scanOneCharacterSymbol();
    }

    /**
     * @return the scanned symbol.
     * @throws IOException thrown in case of errors.
     */
    private Token scanOneCharacterSymbol() throws IOException
    {
        String cs = String.valueOf(ch);
        ch = getChar();

        Token token = CHAR_MAP.get(cs);

        if (token != null)
        {
            return token;
        }

        return new Token(Symbol.OTHER, cs, null);
    }

    /**
     * @return the parsed literal.
     * @throws IOException thrown in case of errors.
     */
    private Token scanLiteral() throws IOException
    {
        int delimiter = ch;

        StringBuilder b = new StringBuilder();
        for (ch = getChar(); ch != null && ch != delimiter; ch = getChar())
        {
            b.append(ch);
        }

        ch = getChar();

        return new Token(Symbol.LITERAL, b.toString(), null);
    }

    /**
     * @return the parsed identifier token.
     * @throws IOException thrown in case of errors.
     */
    private Token scanIdentifier() throws IOException
    {
        StringBuilder b = new StringBuilder();
        b.append(ch);

        for (ch = getChar(); isLetter(ch) || isDigit(ch); ch = getChar())
        {
            b.append(ch);
        }

        return new Token(Symbol.IDENT, b.toString(), null);
    }

    /**
     * @return the parsed number token
     * @throws IOException thrown in case of errors.
     */
    private Token scanNumber() throws IOException
    {
        StringBuilder b = new StringBuilder();
        boolean decimalSeparatorFound = false;

        do
        {
            b.append(ch);
            ch = getChar();
            if (ch == null)
            {
                break;
            }
            if (!decimalSeparatorFound && ch == '.')
            {
                b.append(ch);
                decimalSeparatorFound = true;
                ch = getChar();
            }
        } while (isDigit(ch));

        return new Token(Symbol.NUMBER, b.toString(), new BigDecimal(b.toString()));
    }
}
