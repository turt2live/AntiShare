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

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.impl.derived.DerivedItemStack;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.AItem;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.DerivedRejectable;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.inventory.ItemStack;

/**
 * A Bukkit ItemStack
 *
 * @author turt2live
 */
public class BukkitItem extends BukkitObject implements AItem {

    private ItemStack stack;

    public BukkitItem(ItemStack stack) {
        if (stack == null) throw new IllegalArgumentException();

        this.stack = stack;
    }

    /**
     * Gets the Bukkit ItemStack
     *
     * @return the Bukkit ItemStack
     */
    public ItemStack getStack() {
        return stack;
    }

    @Override
    public TrackedState canUse(APlayer player) {
        return permissionCheck(RejectionList.ListType.ITEM_USE, player);
    }

    @Override
    public TrackedState canDrop(APlayer player) {
        return permissionCheck(RejectionList.ListType.ITEM_DROP, player);
    }

    @Override
    public TrackedState canPickup(APlayer player) {
        return permissionCheck(RejectionList.ListType.ITEM_PICKUP, player);
    }

    @Override
    public TrackedState canDieWith(APlayer player) {
        return permissionCheck(RejectionList.ListType.DEATH, player);
    }

    @Override
    protected String getFriendlyName() {
        return AntiShare.getInstance().getMaterialProvider().getPlayerFriendlyName(stack);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BukkitItem)) return false;

        BukkitItem that = (BukkitItem) o;

        if (!stack.equals(that.stack)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return stack.hashCode();
    }

    @Override
    public DerivedRejectable getGeneric() {
        return new DerivedItemStack(new ItemStack(stack.getType()));
    }

    @Override
    public DerivedRejectable getSpecific() {
        return new DerivedItemStack(stack.clone());
    }

    @Override
    public boolean hasGeneric() {
        return true;
    }
}
