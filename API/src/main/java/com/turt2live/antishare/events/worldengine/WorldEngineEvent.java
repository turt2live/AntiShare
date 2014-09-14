/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.events.worldengine;

import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.events.Event;

/**
 * A generic WorldEngineEvent
 *
 * @author turt2live
 */
public abstract class WorldEngineEvent implements Event {

    private WorldEngine engine;

    /**
     * Creates a new WorldEngineEvent
     *
     * @param engine the engine involved, cannot be null
     */
    public WorldEngineEvent(WorldEngine engine) {
        if (engine == null) throw new IllegalArgumentException("engine cannot be null");

        this.engine = engine;
    }

    /**
     * Gets the applicable world engine
     *
     * @return the world engine
     */
    public WorldEngine getEngine() {
        return engine;
    }

}
