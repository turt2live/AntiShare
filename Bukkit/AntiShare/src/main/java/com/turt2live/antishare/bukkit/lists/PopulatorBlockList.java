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
import com.turt2live.antishare.bukkit.impl.derived.BlockInformation;
import com.turt2live.antishare.object.DerivedRejectable;
import com.turt2live.antishare.object.Rejectable;
import org.bukkit.Material;

import java.util.List;

/**
 * Populates lists of blocks
 *
 * @author turt2live
 */
public class PopulatorBlockList<T extends Rejectable> implements Populator<T> {

    @Override
    public void populateList(BukkitList<T> list, List<String> strings) {
        if (list == null || strings == null) throw new IllegalArgumentException();

        list.included.clear();
        list.includedGeneric.clear();
        list.negated.clear();
        list.negatedGeneric.clear();

        for (String value : strings) {
            String[] parts = value.split(":");
            boolean remove = false;

            if (parts[0].equalsIgnoreCase("all")) {
                list.negated.clear();
                list.included.clear();
                for (Material material : Material.values()) {
                    BlockInformation info = new BlockInformation(material, (short) -1); // -1 will restrict everything
                    if (!list.included.contains(info)) list.included.add(info);
                }
                continue;
            } else if (parts[0].equalsIgnoreCase("none")) {
                // Transfer already included blocks to negation list
                for (DerivedRejectable info : list.included) {
                    if (!list.negated.contains(info))
                        list.negated.add(info);
                }
                list.included.clear();
                continue;
            } else if (parts[0].startsWith("-")) remove = true;

            Material material = AntiShare.getInstance().getMaterialProvider().fromString(parts[0]);
            short d = -1;
            if (parts.length >= 2) {
                String data = parts[1];
                try {
                    d = Short.parseShort(data);
                } catch (NumberFormatException e) {
                    AntiShare.getInstance().getLogger().warning("Unknown value: " + value + ". Assuming " + material.name() + " and all sub types.");
                    d = -1;
                }
            }

            if (material == Material.AIR) {
                AntiShare.getInstance().getLogger().warning("Unknown value: " + value + ", skipping.");
                continue;
            }

            BlockInformation info = new BlockInformation(material, d);
            if (!remove) {
                if (!list.included.contains(info)) list.included.add(info);
                if (list.negated.contains(info)) list.negated.remove(info);
            } else {
                if (list.included.contains(info)) list.included.remove(info);
                if (!list.negated.contains(info)) list.negated.add(info);
            }
        }
    }
}
