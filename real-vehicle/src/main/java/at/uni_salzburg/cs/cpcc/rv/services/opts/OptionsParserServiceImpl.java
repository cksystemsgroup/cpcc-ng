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
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * OptionsParserServiceImpl
 */
public class OptionsParserServiceImpl implements OptionsParserService
{

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<Option> parse(String options) throws IOException, ParseException
    {
        List<Option> optionsList = new ArrayList<Option>();

        OptionsScanner scanner = new OptionsScanner(new StringReader(options));

        Token token = scanner.next();

        while (token.getSymbol() != Symbol.END)
        {
            optionsList.add(parseOption(scanner, token));
            token = scanner.next();
        }

        return optionsList;
    }

    /**
     * @param scanner the scanner.
     * @param token the current token.
     * @return the parsed option.
     * @throws ParseException thrown in case of errors.
     */
    private Option parseOption(OptionsScanner scanner, Token token) throws IOException, ParseException
    {
        Token myToken = new Token(token);
        if (token.getSymbol().compareTo(Symbol.IDENT) != 0)
        {
            throw new ParseException(Symbol.IDENT, scanner, token);
        }

        token.copyFields(scanner.next());
        if (token.getSymbol().compareTo(Symbol.EQUALS) != 0)
        {
            throw new ParseException(Symbol.EQUALS, scanner, token);
        }

        token.copyFields(scanner.next());
        if (token.getSymbol().compareTo(Symbol.LEFT_PAREN) != 0)
        {
            return new Option(myToken.getItemString(), Arrays.asList(new Token(token)));
        }

        return parseList(scanner, myToken, token);
    }

    /**
     * @param scanner the scanner.
     * @param token the current token.
     * @return the parsed option.
     */
    private Option parseList(OptionsScanner scanner, Token myToken, Token token) throws IOException, ParseException
    {
        List<Token> tokenList = new ArrayList<Token>();
        
        while (token.getSymbol() != Symbol.END && token.getSymbol() != Symbol.RIGHT_PAREN)
        {
            token.copyFields(scanner.next());
            if (token.getSymbol() != Symbol.IDENT && token.getSymbol() != Symbol.LITERAL && token.getSymbol() != Symbol.NUMBER)
            {
                throw new ParseException(Symbol.IDENT, scanner, token);
            }
            tokenList.add(new Token(token));
            token.copyFields(scanner.next());
        }
        
        return new Option(myToken.getItemString(), tokenList);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public String formatParserErrorMessage(String source, String format, ParseException e)
    {
        StringBuilder b = new StringBuilder();
        String[] lines = source.split("\n");
        for (int k=0, s=lines.length; k < s; ++k)
        {
            if (e.getLineNumber()-1 == k)
            {
                b.append(lines[k].substring(0,e.getColumnNumber()));
                b.append("<*>");
                b.append(lines[k].substring(e.getColumnNumber()));
            }
            else
            {
                b.append(lines[k]);
                
            }
            b.append("\n");
        }
        
        return String.format(format, b.toString());
    }
}
