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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * OptionsParserService
 */
public interface OptionsParserService
{
    /**
     * @param options the options string.
     * @return a map of parsed options.
     * @throws IOException thrown in case of errors.
     * @throws ParseException thrown in case of errors.
     */
    Collection<Option> parse(String options) throws IOException, ParseException;

    /**
     * @param config the configuration as a string.
     * @return the configuration as a map.
     * @throws ParseException thrown in case of errors.
     * @throws IOException thrown in case of errors.
     */
    Map<String, List<String>> parseConfig(String config) throws IOException, ParseException;

    /**
     * @param source the source.
     * @param format the localized message format.
     * @param e the exception
     * @return the formatted error message.
     */
    String formatParserErrorMessage(String source, String format, ParseException e);
}
