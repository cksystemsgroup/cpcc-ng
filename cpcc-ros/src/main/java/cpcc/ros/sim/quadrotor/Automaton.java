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
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.tuple.Pair;

/**
 * Automaton
 */
public class Automaton
{
    private static final Map<State, Map<Event, State>> TRANSITION_MAP = Collections.unmodifiableMap(Stream
        .of(
            Pair.of(State.OFFLINE, Stream
                .of(Pair.of(Event.UNLOCK, State.READY),
                    Pair.of(Event.BATTERY_LOW, State.DENY))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.READY, Stream
                .of(Pair.of(Event.LOCK, State.OFFLINE),
                    Pair.of(Event.BATTERY_LOW, State.DENY),
                    Pair.of(Event.START, State.TAKE_OFF))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.TAKE_OFF, Stream
                .of(Pair.of(Event.BATTERY_LOW, State.DEPOT_FLIGHT),
                    Pair.of(Event.STOP, State.LAND),
                    Pair.of(Event.REACHED, State.HOVER))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.LAND, Stream
                .of(Pair.of(Event.LANDED, State.READY))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.HOVER, Stream
                .of(Pair.of(Event.BATTERY_LOW, State.DEPOT_FLIGHT),
                    Pair.of(Event.FLY_TO, State.FLIGHT))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.FLIGHT, Stream
                .of(Pair.of(Event.BATTERY_LOW, State.DEPOT_FLIGHT),
                    Pair.of(Event.REACHED, State.HOVER))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.DEPOT_FLIGHT, Stream
                .of(Pair.of(Event.REACHED, State.DEPOT_LAND))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.DEPOT_LAND, Stream
                .of(Pair.of(Event.LANDED, State.DENY))
                .collect(Collectors.toMap(Pair::getLeft, Pair::getRight))),

            Pair.of(State.DENY, Collections.<Event, State> emptyMap())

        ).collect(Collectors.toMap(Pair::getLeft, Pair::getRight)));

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
