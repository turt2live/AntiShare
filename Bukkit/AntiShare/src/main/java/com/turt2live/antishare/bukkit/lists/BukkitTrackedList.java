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
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class BukkitTrackedList implements TrackedTypeList, RejectionList<ABlock> {

    private List<BlockInformation> includedBlocks = new ArrayList<BlockInformation>();
    private List<BlockInformation> negatedBlocks = new ArrayList<BlockInformation>();
    private final MaterialProvider provider;
    private ListType type = ListType.CUSTOM;

    public BukkitTrackedList(MaterialProvider provider) {
        this(provider, null);
    }

    public BukkitTrackedList(MaterialProvider provider, ListType type) {
        if (provider == null) throw new IllegalArgumentException();

        this.provider = provider;
        if (type == null) type = ListType.CUSTOM;
        this.type = type;
    }

    @Override
    public boolean isTracked(ABlock block) {
        return getState(block) == TrackedState.INCLUDED;
    }

    @Override
    public boolean isBlocked(ABlock block) {
        return isTracked(block);
    }

    @Override
    public TrackedState getState(ABlock ablock) {
        if (ablock == null || !(ablock instanceof BukkitBlock)) return TrackedState.NOT_PRESENT;
        Block block = ((BukkitBlock) ablock).getBlock();

        BlockInformation generic = new BlockInformation(block.getType(), (short) -1);
        BlockInformation specific = new BlockInformation(block.getType(), block.getData());

        TrackedState stage1;
        boolean included = includedBlocks.contains(generic);
        boolean negated = negatedBlocks.contains(generic);

        if (included == negated) stage1 = TrackedState.NOT_PRESENT;
        else if (included) stage1 = TrackedState.INCLUDED;
        else stage1 = TrackedState.NEGATED;

        TrackedState stage2;
        included = includedBlocks.contains(specific);
        negated = negatedBlocks.contains(specific);

        if (included == negated) stage2 = TrackedState.NOT_PRESENT;
        else if (included) stage2 = TrackedState.INCLUDED;
        else stage2 = TrackedState.NEGATED;

        // Copied from block permissions logic

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
        return stage2; // [RE] Remaining cases are all stage2 favoured
    }

    @Override
    public ListType getType() {
        return type;
    }

    public void populateBlocks(List<String> strings) {
        includedBlocks.clear();
        negatedBlocks.clear();
        for (String value : strings) {
            String[] parts = value.split(":");
            boolean remove = false;

            if (parts[0].equalsIgnoreCase("all")) {
                negatedBlocks.clear();
                includedBlocks.clear();
                for (Material material : Material.values()) {
                    BlockInformation info = new BlockInformation(material, (short) -1); // -1 will restrict everything
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

            BlockInformation info = new BlockInformation(material, d);
            if (!remove) {
                if (!includedBlocks.contains(info)) includedBlocks.add(info);
                if (negatedBlocks.contains(info)) negatedBlocks.remove(info);
            } else {
                if (includedBlocks.contains(info)) includedBlocks.remove(info);
                if (!negatedBlocks.contains(info)) negatedBlocks.add(info);
            }
        }
    }

}
