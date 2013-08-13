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
package at.uni_salzburg.cs.cpcc.rv.pages;

import java.util.Date;

import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.ioc.annotations.Symbol;

/**
 * Start page of application real-vehicle.
 */
public class Index
{
    @Property
    @Inject
    @Symbol(SymbolConstants.TAPESTRY_VERSION)
    private String tapestryVersion;

//    @InjectComponent
//    private Zone zone;

//    @Persist
//    @Property
//    private int clickCount;

//    @Inject
//    private AlertManager alertManager;

    /**
     * @return the current time.
     */
    public Date getCurrentTime()
    {
        return new Date();
    }

//    void onActionFromIncrement()
//    {
//        alertManager.info("Increment clicked");
//
//        clickCount++;
//    }
//
//    Object onActionFromIncrementAjax()
//    {
//        clickCount++;
//
//        alertManager.info("Increment (via Ajax) clicked");
//
//        return zone;
//    }
}
