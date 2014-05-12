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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.impl.BukkitItem;
import com.turt2live.antishare.bukkit.util.ItemMatcher;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A bukkit item reject list
 *
 * @author turt2live
 */
public class BukkitItemList implements RejectionList<BukkitItem> {

    private List<ItemStack> included = new ArrayList<ItemStack>();
    private List<ItemStack> negated = new ArrayList<ItemStack>();
    private List<ItemStack> includedAll = new ArrayList<ItemStack>();
    private List<ItemStack> negatedAll = new ArrayList<ItemStack>();
    private ListType type;

    /**
     * Creates a new BukkitItemList
     *
     * @param type the list type, cannot be null
     */
    public BukkitItemList(ListType type) {
        if (type == null) throw new IllegalArgumentException();

        this.type = type;
    }

    /**
     * Loads a list of strings into the list
     *
     * @param strings the strings to load, cannot be null
     */
    public void load(List<String> strings) {
        if (strings == null) throw new IllegalArgumentException();

        for (String entry : strings) {
            String workingCopy = entry;
            boolean negated = false;

            if (entry.startsWith("-")) {
                negated = true;
                workingCopy = entry.substring(1);
            }

            if (workingCopy.equalsIgnoreCase("all")) {
                this.negatedAll.clear();
                this.negatedAll.clear();
                this.included.clear();
                this.includedAll.clear();

                for (Material material : Material.values()) {
                    ItemStack item = new ItemStack(material);
                    if (!contains2(this.includedAll, item)) this.includedAll.add(item);
                }
                continue;
            } else if (workingCopy.equalsIgnoreCase("none")) {
                this.negated.clear();
                this.negatedAll.clear();

                for (ItemStack item : this.included)
                    if (!contains2(this.negatedAll, item)) this.negatedAll.add(item);
                for (ItemStack item : this.includedAll)
                    if (!contains2(this.negatedAll, item)) this.negatedAll.add(item);

                this.included.clear();
                this.includedAll.clear();
                continue;
            }

            ItemStack stack = ItemMatcher.getItem(workingCopy);
            if (stack == null || stack.getType() == Material.AIR) {
                AntiShare.getInstance().getLogger().warning("Unknown item: " + workingCopy);
                continue;
            }
            stack.setAmount(1); // Override stack size

            if (AntiShare.getInstance().getMaterialProvider().hasAdditionalData(workingCopy)) {
                if (negated) {
                    if (contains2(this.includedAll, stack)) this.includedAll.remove(stack);
                    if (!contains2(this.negatedAll, stack)) this.negatedAll.add(stack);
                } else {
                    if (!contains2(this.includedAll, stack)) this.includedAll.add(stack);
                    if (contains2(this.negatedAll, stack)) this.negatedAll.remove(stack);
                }
            }

            if (negated) {
                if (contains(this.included, stack)) this.included.remove(stack);
                if (!contains(this.negated, stack)) this.negated.add(stack);
            } else {
                if (!contains(this.included, stack)) this.included.add(stack);
                if (contains(this.negated, stack)) this.negated.remove(stack);
            }
        }
    }

    private boolean contains(List<ItemStack> list, ItemStack stack) {
        for (ItemStack s : list) {
            if (s.isSimilar(stack))
                return true;
        }
        return false;
    }

    private boolean contains2(List<ItemStack> list, ItemStack stack) {
        for (ItemStack s : list) {
            if (s.getType() == stack.getType())
                return true;
        }
        return false;
    }

    @Override
    public boolean isBlocked(BukkitItem item) {
        return getState(item) == TrackedState.INCLUDED;
    }

    @Override
    public TrackedState getState(BukkitItem item) {
        if (item == null) throw new IllegalArgumentException();

        // Logic copied from permission check in BukkitObject

        ItemStack lookup = item.getStack();
        ItemStack noMeta = lookup.clone();
        noMeta.setItemMeta(AntiShare.getInstance().getServer().getItemFactory().getItemMeta(noMeta.getType()));

        TrackedState stage1 = TrackedState.NOT_PRESENT;
        boolean included = contains(this.included, noMeta) || contains2(this.includedAll, noMeta);
        boolean negated = contains(this.negated, noMeta) || contains2(this.negatedAll, noMeta);

        if (included == negated) stage1 = TrackedState.NOT_PRESENT;
        else if (included) stage1 = TrackedState.INCLUDED;
        else if (negated) stage1 = TrackedState.NEGATED;

        TrackedState stage2 = TrackedState.NOT_PRESENT;
        included = contains(this.included, lookup) || contains2(this.includedAll, lookup);
        negated = contains(this.negated, lookup) || contains2(this.negatedAll, lookup);

        if (included == negated) stage2 = TrackedState.NOT_PRESENT;
        else if (included) stage2 = TrackedState.INCLUDED;
        else if (negated) stage2 = TrackedState.NEGATED;

        /*
        Stage Three: Combination logic for merging stages one and two
        Logic:

        G = stage1, general scope
        S = stage2, specific scope

        if(G[allow] && S[allow])    [allow]  // Favour: G || S      [C2] <-- Covered by return, doesn't matter
        if(G[allow] && S[deny])     [deny]   // Favour: S           [RE]
        if(G[allow] && S[none])     [allow]  // Favour: G           [C1]

        if(G[deny] && S[allow])     [allow]  // Favour: S           [RE]
        if(G[deny] && S[deny])      [deny]   // Favour: G || S      [C2] <-- Covered by return, doesn't matter
        if(G[deny] && S[none])      [deny]   // Favour: G           [C1]

        if(G[none] && S[allow])     [allow]  // Favour: S           [RE]
        if(G[none] && S[deny])      [deny]   // Favour: S           [RE]
        if(G[none] && S[none])      [none]   // Favour: G || S      [C2] <-- Covered by return, doesn't matter
         */

        if (stage2 == TrackedState.NOT_PRESENT) return stage1; // [C1] In all cases, stageOne is favoured
        //if (stage2 == stage1) return stage1; // [C2] Doesn't matter
        return stage2; // [RE] Remaining cases are all stageTwo favoured
    }

    @Override
    public ListType getType() {
        return type;
    }
}
