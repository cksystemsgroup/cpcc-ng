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
import static org.fest.assertions.api.Assertions.offset;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.fest.assertions.api.Fail;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
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
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldParseNullOptions() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse(null);
        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    public void shouldParseCorrectOptions() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("bugger=lala looney=3.141592 caspar='xxx uu'");

        assertThat(result).isNotNull().hasSize(3);

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

        assertThat(bugger).isNotNull();
        List<Token> buggerList = bugger.getValue();
        assertThat(buggerList).isNotNull().hasSize(1);
        assertThat(buggerList.get(0).getSymbol()).isNotNull().isEqualTo(Symbol.IDENT);
        assertThat(buggerList.get(0).getItemString()).isNotNull().isEqualTo("lala");

        assertThat(looney).isNotNull();
        List<Token> looneyList = looney.getValue();
        assertThat(looneyList).isNotNull().hasSize(1);
        assertThat(looneyList.get(0).getSymbol()).isNotNull().isEqualTo(Symbol.NUMBER);
        assertThat(looneyList.get(0).getItemString()).isNotNull().isEqualTo("3.141592");
        assertThat(looneyList.get(0).getNumber().doubleValue()).isEqualTo(3.141592, offset(1E-8));

        assertThat(caspar).isNotNull();
        List<Token> casparList = caspar.getValue();
        assertThat(casparList).isNotNull().hasSize(1);
        assertThat(casparList.get(0).getSymbol()).isNotNull().isEqualTo(Symbol.LITERAL);
        assertThat(casparList.get(0).getItemString()).isNotNull().isEqualTo("xxx uu");
    }

    @Test
    public void shouldParseCorrectOptionList() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("bugger=(lala,'looney',3.141592)");
        assertThat(result).isNotNull().hasSize(1);

        Option entry = result.iterator().next();
        assertThat(entry.getKey()).isNotNull().isEqualTo("bugger");
        assertThat(entry.getValue()).isNotNull().hasSize(3);

        assertThat(entry.getValue().get(0).getSymbol()).isNotNull().isEqualTo(Symbol.IDENT);
        assertThat(entry.getValue().get(0).getItemString()).isNotNull().isEqualTo("lala");

        assertThat(entry.getValue().get(1).getSymbol()).isNotNull().isEqualTo(Symbol.LITERAL);
        assertThat(entry.getValue().get(1).getItemString()).isNotNull().isEqualTo("looney");

        assertThat(entry.getValue().get(2).getSymbol()).isNotNull().isEqualTo(Symbol.NUMBER);
        assertThat(entry.getValue().get(2).getItemString()).isNotNull().isEqualTo("3.141592");
        assertThat(entry.getValue().get(2).getNumber().doubleValue()).isEqualTo(3.141592, offset(1E-8));
    }

    @Test
    public void shouldParseCorrectGpsCoordinates() throws IOException, ParseException
    {
        Collection<Option> result = svc.parse("origin=(37.86644;-122.30954;0)");
        assertThat(result).isNotNull().hasSize(1);

        Option entry = result.iterator().next();
        assertThat(entry.getKey()).isNotNull().isEqualTo("origin");
        assertThat(entry.getValue()).isNotNull().hasSize(3);

        assertThat(entry.getValue().get(0).getSymbol()).isNotNull().isEqualTo(Symbol.NUMBER);
        assertThat(entry.getValue().get(0).getItemString()).isNotNull().isEqualTo("37.86644");
        assertThat(entry.getValue().get(0).getNumber().doubleValue()).isEqualTo(37.86644, offset(1E-8));

        assertThat(entry.getValue().get(1).getSymbol()).isNotNull().isEqualTo(Symbol.NUMBER);
        assertThat(entry.getValue().get(1).getItemString()).isNotNull().isEqualTo("-122.30954");
        assertThat(entry.getValue().get(1).getNumber().doubleValue()).isEqualTo(-122.30954, offset(1E-8));

        assertThat(entry.getValue().get(2).getSymbol()).isNotNull().isEqualTo(Symbol.NUMBER);
        assertThat(entry.getValue().get(2).getItemString()).isNotNull().isEqualTo("0");
        assertThat(entry.getValue().get(2).getNumber().doubleValue()).isEqualTo(0, offset(1E-8));
    }

    @Test()
    public void shouldFailOnInvalidItem() throws IOException, ParseException
    {
        try
        {
            svc.parse("1.bugger=nix");
            Fail.failBecauseExceptionWasNotThrown(ParseException.class);
        }
        catch (ParseException e)
        {
            assertThat(e).hasMessage("Expected a IDENT but got a NUMBER in line 1 column 3");
        }
    }

    @Test()
    public void shouldFailOnMissingEqualSymbol() throws IOException, ParseException
    {
        String source = "bugger nix";

        try
        {
            svc.parse(source);
            Fail.failBecauseExceptionWasNotThrown(ParseException.class);
        }
        catch (ParseException e)
        {
            assertThat(e).hasMessage("Expected a EQUALS but got a IDENT in line 1 column 10");
            assertThat(svc.formatParserErrorMessage(source, "%s", e)).isEqualTo("bugger nix<*>\n");
        }
    }

    @Test
    public void shouldFailOnInvalidList() throws IOException, ParseException
    {
        String source = "bugger=(nix";

        try
        {
            svc.parse(source);
            Fail.failBecauseExceptionWasNotThrown(ParseException.class);
        }
        catch (ParseException e)
        {
            assertThat(e).hasMessage("Expected a RIGHT_PAREN but got a END in line 1 column 11");
            assertThat(svc.formatParserErrorMessage(source, "%s", e)).isEqualTo("bugger=(nix<*>\n");
        }
    }

    @Test
    public void shouldFailOnAdditionalEqualsSymbol() throws IOException, ParseException
    {
        String source = "bugger==nix=a";

        try
        {
            svc.parse(source);
            Fail.failBecauseExceptionWasNotThrown(ParseException.class);
        }
        catch (ParseException e)
        {
            assertThat(e).hasMessage("Expected a LITERAL, IDENT or NUMBER but got a EQUALS in line 1 column 9");
            assertThat(svc.formatParserErrorMessage(source, "%s", e)).isEqualTo("bugger==n<*>ix=a\n");
        }
    }

    @Test
    public void shouldParseConfigurationCorrectly() throws IOException, ParseException
    {
        String source = "lala=13.4\npos=(47.1,13.2,10)";
        Map<String, List<String>> result = svc.parseConfig(source);

        assertThat(result.keySet()).containsExactly("lala", "pos");
        assertThat(result.get("lala")).containsExactly("13.4");
        assertThat(result.get("pos")).containsExactly("47.1", "13.2", "10");
    }

    @Test()
    public void shouldFailOnMalformedList() throws IOException, ParseException
    {
        String source = "bugger=(1,=";

        try
        {
            svc.parse(source);
            Fail.failBecauseExceptionWasNotThrown(ParseException.class);
        }
        catch (ParseException e)
        {
            assertThat(e).hasMessage("Expected a LITERAL, IDENT or NUMBER but got a EQUALS in line 1 column 11");
            assertThat(svc.formatParserErrorMessage(source, "%s", e)).isEqualTo("bugger=(1,=<*>\n");
        }
    }

    @DataProvider
    public Object[][] errorMessageDataProvider()
    {
        return new Object[][]{
            new Object[]{
                "bugger=(nix",
                "%s",
                "bugger=(nix<*>\n",
                new ParseException(Arrays.asList(Symbol.COMMA, Symbol.RIGHT_PAREN), Symbol.END, 1, 11)
            },
            new Object[]{
                "bugger\n=\n(nix",
                "%s",
                "bugger\n=\n(nix<*>\n",
                new ParseException(Arrays.asList(Symbol.RIGHT_PAREN), Symbol.END, 3, 4)
            },
        };
    };

    @Test(dataProvider = "errorMessageDataProvider")
    public void shouldFormatParserErrorMessageCorrectly(String source, String format, String expected, ParseException e)
    {
        String result = svc.formatParserErrorMessage(source, format, e);
        assertThat(result).isNotNull().isEqualTo(expected);
    }
}
