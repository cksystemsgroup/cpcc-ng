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
import java.util.Collection;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

/**
 * OptionsParserServiceTest
 */
public class OptionsParserServiceTest
{
    private OptionsParserService svc;

    @BeforeClass
    public void setUp()
    {
        svc = new OptionsParserServiceImpl();
    }

    @Test
    public void shouldParseEmptyOptions() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("");
        Assert.assertTrue(result.size() == 0);
    }

    @Test
    public void shouldParseCorrectOptions() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("bugger=lala looney=3.141592 caspar='xxx uu'");
        Assert.assertTrue(result.size() == 3);

        Option bugger = null;
        Option looney = null;
        Option caspar = null;

        for (Option option : result)
        {
            if ("bugger".equals(option.getKey()))
            {
                bugger = option;
            }
            else if ("looney".equals(option.getKey()))
            {
                looney = option;
            }
            else if ("caspar".equals(option.getKey()))
            {
                caspar = option;
            }
        }

        Assert.assertNotNull(bugger);
        List<Token> buggerList = bugger.getValue();
        Assert.assertEquals(buggerList.size(), 1);
        Assert.assertEquals(buggerList.get(0).getSymbol(), Symbol.IDENT);
        Assert.assertEquals(buggerList.get(0).getItemString(), "lala");

        Assert.assertNotNull(looney);
        List<Token> looneyList = looney.getValue();
        Assert.assertEquals(looneyList.size(), 1);
        Assert.assertEquals(looneyList.get(0).getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(looneyList.get(0).getItemString(), "3.141592");
        Assert.assertEquals(looneyList.get(0).getNumber().doubleValue(), 3.141592, 1E-8);

        Assert.assertNotNull(caspar);
        List<Token> casparList = caspar.getValue();
        Assert.assertEquals(casparList.size(), 1);
        Assert.assertEquals(casparList.get(0).getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(casparList.get(0).getItemString(), "xxx uu");
    }

    @Test
    public void shouldParseCorrectOptionList() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("bugger=(lala,'looney',3.141592)");
        Assert.assertTrue(result.size() == 1);

        Option bugger = null;

        for (Option option : result)
        {
            if ("bugger".equals(option.getKey()))
            {
                bugger = option;
            }
        }

        Assert.assertNotNull(bugger);
        List<Token> buggerList = bugger.getValue();
        Assert.assertEquals(buggerList.size(), 3);
        Assert.assertEquals(buggerList.get(0).getSymbol(), Symbol.IDENT);
        Assert.assertEquals(buggerList.get(0).getItemString(), "lala");

        Assert.assertEquals(buggerList.get(1).getSymbol(), Symbol.LITERAL);
        Assert.assertEquals(buggerList.get(1).getItemString(), "looney");

        Assert.assertEquals(buggerList.get(2).getSymbol(), Symbol.NUMBER);
        Assert.assertEquals(buggerList.get(2).getItemString(), "3.141592");
        Assert.assertEquals(buggerList.get(2).getNumber().doubleValue(), 3.141592, 1E-8);
    }

    @Test()
    public void shouldFailOnInvalidItem() throws IOException
    {
        try
        {
            Collection<Option> result = svc.parse("1.bugger=nix");
            Assert.assertNull(result);
        }
        catch (ParseException e)
        {
            Assert.assertEquals(e.getMessage(), "Expected a IDENT but got a NUMBER in line 1 column 3");
        }
    }

    @Test()
    public void shouldFailOnMissingEqualSymbol() throws IOException
    {
        String source = "bugger nix";
        try
        {
            Collection<Option> result = svc.parse(source);
            Assert.assertNull(result);
        }
        catch (ParseException e)
        {
            Assert.assertEquals(e.getMessage(), "Expected a EQUALS but got a IDENT in line 1 column 10");
            Assert.assertEquals(svc.formatParserErrorMessage(source, "%s", e), "bugger nix<*>\n");
        }
    }

    @Test()
    public void shouldFailOnInvalidList() throws IOException
    {
        String source = "bugger=(nix";
        try
        {
            Collection<Option> result = svc.parse(source);
            Assert.assertNull(result);
        }
        catch (ParseException e)
        {
            Assert.assertEquals(e.getMessage(), "Expected a RIGHT_PAREN but got a END in line 1 column 11");
            Assert.assertEquals(svc.formatParserErrorMessage(source, "%s", e), "bugger=(nix<*>\n");
        }
    }

    @Test()
    public void shouldFailOnAdditionalEqualsSymbol() throws IOException
    {
        String source = "bugger==nix=a";
        try
        {
            Collection<Option> result = svc.parse(source);
            Assert.assertNull(result);
        }
        catch (ParseException e)
        {
            Assert.assertEquals(e.getMessage(),
                "Expected a LITERAL, IDENT or NUMBER but got a EQUALS in line 1 column 9");
            
            Assert.assertEquals(svc.formatParserErrorMessage(source, "%s", e), "bugger==n<*>ix=a\n");
        }
    }
}
