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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.impl.derived.DerivedItemStack;
import com.turt2live.antishare.bukkit.util.ItemMatcher;
import com.turt2live.antishare.object.DerivedRejectable;
import com.turt2live.antishare.object.Rejectable;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class PopulatorItemList<T extends Rejectable> implements Populator<T> {

    @Override
    public void populateList(BukkitList<T> list, List<String> strings) {
        if (list == null || strings == null) throw new IllegalArgumentException();

        list.included.clear();
        list.includedGeneric.clear();
        list.negated.clear();
        list.negatedGeneric.clear();

        for (String entry : strings) {
            String workingCopy = entry;
            boolean negated = false;

            if (entry.startsWith("-")) {
                negated = true;
                workingCopy = entry.substring(1);
            }

            if (workingCopy.equalsIgnoreCase("all")) {
                list.included.clear();
                list.includedGeneric.clear();
                list.negated.clear();
                list.negatedGeneric.clear();

                for (Material material : Material.values()) {
                    ItemStack item = new ItemStack(material);
                    if (!contains2(list.includedGeneric, item)) list.includedGeneric.add(new DerivedItemStack(item));
                }
                continue;
            } else if (workingCopy.equalsIgnoreCase("none")) {
                list.negated.clear();
                list.negatedGeneric.clear();

                for (DerivedRejectable item : list.included)
                    if (!contains2(list.negated, item)) list.negated.add(item);
                for (DerivedRejectable item : list.includedGeneric)
                    if (!contains2(list.negated, item)) list.negated.add(item);

                list.included.clear();
                list.includedGeneric.clear();
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
                    if (contains2(list.includedGeneric, stack))
                        list.includedGeneric.remove(new DerivedItemStack(stack));
                    if (!contains2(list.negated, stack)) list.negated.add(new DerivedItemStack(stack));
                } else {
                    if (!contains2(list.includedGeneric, stack)) list.includedGeneric.add(new DerivedItemStack(stack));
                    if (contains2(list.negated, stack)) list.negated.remove(new DerivedItemStack(stack));
                }
            }

            if (negated) {
                if (contains(list.included, stack)) list.included.remove(new DerivedItemStack(stack));
                if (!contains(list.negated, stack)) list.negated.add(new DerivedItemStack(stack));
            } else {
                if (!contains(list.included, stack)) list.included.add(new DerivedItemStack(stack));
                if (contains(list.negated, stack)) list.negated.remove(new DerivedItemStack(stack));
            }
        }
    }

    private boolean contains(List<DerivedRejectable> list, ItemStack stack) {
        for (DerivedRejectable s : list) {
            if (s instanceof DerivedItemStack) {
                if (((DerivedItemStack) s).getStack().isSimilar(stack))
                    return true;
            }
        }
        return false;
    }

    private boolean contains2(List<DerivedRejectable> list, ItemStack stack) {
        for (DerivedRejectable s : list) {
            if (s instanceof DerivedItemStack) {
                if (((DerivedItemStack) s).getStack().getType() == stack.getType())
                    return true;
            }
        }
        return false;
    }

    private boolean contains2(List<DerivedRejectable> list, DerivedRejectable stack) {
        return stack instanceof DerivedItemStack && contains2(list, ((DerivedItemStack) stack).getStack());
    }

}
