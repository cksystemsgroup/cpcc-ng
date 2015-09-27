// This code is part of the CPCC-NG project.
//
// Copyright (c) 2014 Clemens Krainer <clemens.krainer@gmail.com>
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

package cpcc.commons.services;

import java.text.FieldPosition;
import java.text.Format;
import java.text.ParsePosition;

import org.apache.tapestry5.ioc.Messages;

/**
 * EnumFormatter
 */
@SuppressWarnings("serial")
public class EnumFormatter extends Format
{
    private Messages messages;

    /**
     * @param messages the message catalog.
     */
    public EnumFormatter(Messages messages)
    {
        this.messages = messages;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        pos.setBeginIndex(0);
        pos.setEndIndex(0);

        if (obj instanceof Enum)
        {
            Enum<?> e = (Enum<?>) obj;
            toAppendTo.append(messages.get(e.name()));
        }

        return toAppendTo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object parseObject(String source, ParsePosition pos)
    {
        return null;
    }
}
