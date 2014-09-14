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

package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.generics.GenericBlockStore;

/**
 * Represents a block store that is not persistent in any way. Any changes to this
 * block store are done in memory and are not saved to any form of persistent storage.
 * <br/>
 * There are no errors for saving/loading the block store. This would silently fail.
 *
 * @author turt2live
 */
public class MemoryBlockStore extends GenericBlockStore {

    /**
     * @deprecated Does nothing
     */
    @Override
    @Deprecated
    public void save() {
    }

    /**
     * @deprecated Does nothing
     */
    @Override
    @Deprecated
    public void load() {
    }

}
