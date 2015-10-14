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

package cpcc.ros.sim.quadrotor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Automaton
 */
public class Automaton
{
    @SuppressWarnings("serial")
    private static final Map<State, Map<Event, State>> TRANSITION_MAP = new HashMap<State, Map<Event, State>>()
    {
        {
            Map<Event, State> m = new HashMap<Event, State>();
            m.put(Event.UNLOCK, State.READY);
            m.put(Event.BATTERY_LOW, State.DENY);
            put(State.OFFLINE, m);

            m = new HashMap<Event, State>();
            m.put(Event.LOCK, State.OFFLINE);
            m.put(Event.BATTERY_LOW, State.DENY);
            m.put(Event.START, State.TAKE_OFF);
            put(State.READY, m);

            m = new HashMap<Event, State>();
            m.put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
            m.put(Event.STOP, State.LAND);
            m.put(Event.REACHED, State.HOVER);
            put(State.TAKE_OFF, m);

            m = new HashMap<Event, State>();
            m.put(Event.LANDED, State.READY);
            put(State.LAND, m);

            m = new HashMap<Event, State>();
            m.put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
            m.put(Event.FLY_TO, State.FLIGHT);
            put(State.HOVER, m);

            m = new HashMap<Event, State>();
            m.put(Event.BATTERY_LOW, State.DEPOT_FLIGHT);
            m.put(Event.REACHED, State.HOVER);
            put(State.FLIGHT, m);

            m = new HashMap<Event, State>();
            m.put(Event.REACHED, State.DEPOT_LAND);
            put(State.DEPOT_FLIGHT, m);

            m = new HashMap<Event, State>();
            m.put(Event.LANDED, State.DENY);
            put(State.DEPOT_LAND, m);

            put(State.DENY, Collections.<Event, State> emptyMap());
        }
    };

    private State currentState;

    /**
     * Construct the automaton.
     */
    public Automaton()
    {
        currentState = State.OFFLINE;
    }

    /**
     * @param event the event.
     * @return the new state or null.
     */
    public State transition(Event event)
    {
        if (!TRANSITION_MAP.get(currentState).containsKey(event))
        {
            return null;
        }

        currentState = TRANSITION_MAP.get(currentState).get(event);
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
