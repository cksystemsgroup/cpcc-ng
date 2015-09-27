// This code is part of the CPCC-NG project.
//
// Copyright (c) 2015 Clemens Krainer <clemens.krainer@gmail.com>
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
import java.util.List;

import org.apache.commons.lang.NotImplementedException;

import cpcc.core.entities.SensorDefinition;

/**
 * Sensor List Format
 */
public class SensorDescriptionListFormat extends Format
{
    private static final long serialVersionUID = -9169715362992900557L;

    /**
     * {@inheritDoc}
     */
    @Override
    public StringBuffer format(Object obj, StringBuffer toAppendTo, FieldPosition pos)
    {
        @SuppressWarnings("unchecked")
        List<SensorDefinition> sds = (List<SensorDefinition>) obj;

        if (sds != null)
        {
            boolean first = true;

            for (SensorDefinition sd : sds)
            {
                if (first)
                {
                    first = false;
                }
                else
                {
                    toAppendTo.append(", ");
                }

                toAppendTo.append(sd.getDescription());
            }
        }

        return toAppendTo;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object parseObject(String source, ParsePosition pos)
    {
        throw new NotImplementedException();
    }

}
