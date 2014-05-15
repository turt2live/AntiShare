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

package com.turt2live.antishare.io.memory;

import com.turt2live.antishare.io.generics.GenericEntityManager;

/**
 * Represents an EntityManager which loads and saves no data. So long
 * as this manager is in memory, it's records will stay in-tact. Data
 * from this manager cannot be saved or loaded from anywhere besides memory.
 *
 * @author turt2live
 */
public class MemoryEntityManager extends GenericEntityManager {

    @Override
    public void save() {
        // Do nothing
    }

    @Override
    public void load() {
        // Do nothing
    }
}
