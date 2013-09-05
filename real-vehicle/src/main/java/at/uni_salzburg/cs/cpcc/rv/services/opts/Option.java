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

import java.util.List;
import java.util.Map.Entry;

/**
 * Option
 */
public class Option implements Entry<String, List<Token>>
{
    private String key;
    private List<Token> value;

    /**
     * Constructor
     */
    public Option(String key, List<Token> tokens)
    {
        this.key = key;
        this.value = tokens;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getKey()
    {
        return key;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Token> getValue()
    {
        return value;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Token> setValue(List<Token> value)
    {
        this.value = value;
        return value;
    }
}
