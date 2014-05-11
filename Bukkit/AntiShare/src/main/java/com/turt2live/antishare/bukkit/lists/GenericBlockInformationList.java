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
import com.turt2live.antishare.bukkit.MaterialProvider;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

/**
 * Generic class for common block actions
 *
 * @author turt2live
 */
public class GenericBlockInformationList {

    protected List<BlockInformation> includedBlocks = new ArrayList<BlockInformation>();
    protected List<BlockInformation> negatedBlocks = new ArrayList<BlockInformation>();
    protected final MaterialProvider provider;

    public GenericBlockInformationList(MaterialProvider provider) {
        if (provider == null) throw new IllegalArgumentException();
        this.provider = provider;
    }

    // TODO: ALL lists should implement this world stuffs!

    public void populateBlocks(List<String> strings) {
        includedBlocks.clear();
        negatedBlocks.clear();
        for (String value : strings) {
            String[] parts = value.split(":");
            boolean remove = false;

            // Check for world
            String world = null;
            if (parts[0].contains(";")) {
                String[] parts1 = parts[0].split(";");
                world = parts1[0];
            }

            if (parts[0].equalsIgnoreCase("all")) {
                negatedBlocks.clear();
                includedBlocks.clear();
                for (Material material : Material.values()) {
                    BlockInformation info = new BlockInformation(material, (short) -1, null); // -1 and no world will restrict everything
                    if (!includedBlocks.contains(info)) includedBlocks.add(info);
                }
                continue;
            } else if (parts[0].equalsIgnoreCase("none")) {
                // Transfer already included blocks to negation list
                for (BlockInformation info : includedBlocks) {
                    if (!negatedBlocks.contains(info))
                        negatedBlocks.add(info);
                }
                includedBlocks.clear();
                continue;
            } else if (parts[0].startsWith("-")) remove = true;

            Material material = provider.fromString(parts[0]);
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

            BlockInformation info = new BlockInformation(material, d, world);
            if (!remove) {
                if (!includedBlocks.contains(info)) includedBlocks.add(info);
                if (negatedBlocks.contains(info)) negatedBlocks.remove(info);
            } else {
                if (includedBlocks.contains(info)) includedBlocks.remove(info);
                if (!negatedBlocks.contains(info)) negatedBlocks.add(info);
            }
        }
    }

    protected TrackedState getState(BlockInformation information) {
        boolean included = includedBlocks.contains(information);
        boolean negated = negatedBlocks.contains(information);

        if (included == negated) return TrackedState.NOT_PRESENT;
        else if (included) return TrackedState.INCLUDED;
        else return TrackedState.NEGATED;
    }

    protected List<BlockInformation> generateBlockInfo(Block block) {
        List<BlockInformation> infos = new ArrayList<BlockInformation>();
        String world = block.getWorld().getName();

        Material material = block.getType();
        if (material != Material.AIR) {
            byte data = block.getData();

            // Very specific : World and data
            BlockInformation search = new BlockInformation(material, (short) data, world);
            search.priority = 1;
            infos.add(search);

            // Less specific : World and any data
            data = -1;
            search = new BlockInformation(material, (short) data, world);
            search.priority = 2;
            infos.add(search);

            // Specific : No world and data
            data = block.getData();
            search = new BlockInformation(material, (short) data, null);
            search.priority = 3;
            infos.add(search);

            // Not-specific: No world and any data
            data = -1;
            search = new BlockInformation(material, (short) data, null);
            search.priority = 4;
            infos.add(search);
        }

        return infos;
    }

}
