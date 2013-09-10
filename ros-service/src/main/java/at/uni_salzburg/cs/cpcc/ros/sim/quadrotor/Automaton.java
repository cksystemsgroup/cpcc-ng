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
package at.uni_salzburg.cs.cpcc.ros.sim.quadrotor;

import java.util.HashMap;
import java.util.Map;

/**
 * Automaton
 */
public class Automaton
{
    @SuppressWarnings("serial")
    private static final Map<State, Map<Event, State>> MAP =
        new HashMap<State, Map<Event, State>>()
        {
            {
                put(State.OFFLINE, new HashMap<Event, State>()
                {
                    {
                        put(Event.UNLOCK, State.READY);
                        put(Event.BATTERY_LOW, State.DENY);
                    }
                });
                put(State.READY, new HashMap<Event, State>()
                {
                    {
                        put(Event.LOCK, State.OFFLINE);
                        put(Event.BATTERY_LOW, State.DENY);
                        put(Event.START, State.TAKE_OFF);
                    }
                });
                put(State.TAKE_OFF, new HashMap<Event, State>()
                {
                    {
                        put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
                        put(Event.STOP, State.LAND);
                        put(Event.REACHED, State.HOVER);
                    }
                });
                put(State.LAND, new HashMap<Event, State>()
                {
                    {
                        put(Event.LANDED, State.READY);
                    }
                });
                put(State.HOVER, new HashMap<Event, State>()
                {
                    {
                        put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
                        put(Event.FLY_TO, State.FLIGHT);
                    }
                });
                put(State.FLIGHT, new HashMap<Event, State>()
                {
                    {
                        put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
                        put(Event.REACHED, State.HOVER);
                    }
                });
                put(State.DEPOT_FLIGHT, new HashMap<Event, State>()
                {
                    {
                        put(Event.REACHED, State.DEPOT_LAND);
                    }
                });
                put(State.DEPOT_LAND, new HashMap<Event, State>()
                {
                    {
                        put(Event.LANDED, State.DENY);
                    }
                });
                put(State.DENY, new HashMap<Event, State>());
            }
        };

    private State currentState;

    /**
     * Construct the automaton.
     */
    public Automaton()
    {
        reset();
    }

    /**
     * Reset the automaton.
     */
    private void reset()
    {
        currentState = State.OFFLINE;
    }

    /**
     * @param event the event.
     * @return the new state or null.
     */
    public State transition(Event event)
    {
        if (!MAP.get(currentState).containsKey(event))
        {
            return null;
        }
        currentState = MAP.get(currentState).get(event);
        return currentState;
    }

    /**
     * @return the current state.
     */
    public State getCurrentState()
    {
        return currentState;
    }
}
