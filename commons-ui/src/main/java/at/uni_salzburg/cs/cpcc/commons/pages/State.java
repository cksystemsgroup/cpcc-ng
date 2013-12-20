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
package at.uni_salzburg.cs.cpcc.commons.pages;

import javax.inject.Inject;

import org.apache.tapestry5.StreamResponse;
import org.apache.tapestry5.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import at.uni_salzburg.cs.cpcc.core.services.JsonStreamResponse;
import at.uni_salzburg.cs.cpcc.ros.services.RosNodeService;

/**
 * State page for map updating.
 */
public class State
{
    private static final Logger LOG = LoggerFactory.getLogger(State.class);

    //    @Inject
    //    private RosNodeStarter rosNodeStarter;
    //
    //    @Inject
    //    private JsonConverter mseConverter;

    @Inject
    private RosNodeService rns;

    /**
     * @return the JSON stream response.
     */
    public Object onActivate()
    {
        return onActivate(null);
    }

    /**
     * @param what the subset of the MSE to be emitted.
     * @return the JSON stream response.
     */
    public StreamResponse onActivate(final String what)
    {
        //        return new MseStreamResponse(mseConverter, rosNodeStarter.getMseListener().getMessage(),
        //            rosNodeStarter.getSseListener().getMessage(),
        //            what);

        JSONObject jsonObject = new JSONObject("{red: green}");
        return new JsonStreamResponse(jsonObject);
    }

}
