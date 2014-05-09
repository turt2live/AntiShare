/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.events;

/**
 * A cancellable event
 *
 * @author turt2live
 */
public interface CancellableEvent extends Event {

    /**
     * Determines if this event is cancelled
     *
     * @return true if cancelled
     */
    public boolean isCancelled();

    /**
     * Sets this event's cancelled state
     *
     * @param cancelled the new state
     */
    public void setCancelled(boolean cancelled);

}
