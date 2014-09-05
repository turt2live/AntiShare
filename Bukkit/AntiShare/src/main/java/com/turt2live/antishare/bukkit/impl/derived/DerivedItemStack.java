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

package com.turt2live.antishare.bukkit.impl.derived;

import com.turt2live.antishare.object.DerivedRejectable;
import org.bukkit.inventory.ItemStack;

/**
 * A bukkit item stack derivation
 *
 * @author turt2live
 */
public class DerivedItemStack implements DerivedRejectable {

    private ItemStack stack;

    public DerivedItemStack(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException();

        this.stack = stack;
    }

    /**
     * Gets a clone of the underlying stack
     *
     * @return the underlying stack, cloned
     */
    public ItemStack getStack() {
        return stack.clone();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DerivedItemStack)) return false;

        DerivedItemStack that = (DerivedItemStack) o;

        return stack.equals(that.stack);

    }

    @Override
    public int hashCode() {
        return stack.hashCode();
    }

    @Override
    public String toString() {
        return "DerivedItemStack{" +
                "stack=" + stack +
                '}';
    }
}
