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

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.bukkit.impl.derived.BlockInformation;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.*;
import com.turt2live.antishare.object.attribute.Facing;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.material.Attachable;
import org.bukkit.material.Directional;
import org.bukkit.material.MaterialData;

import java.util.List;

/**
 * Bukkit block
 *
 * @author turt2live
 */
public class BukkitBlock extends BukkitObject implements ABlock {

    private final Block block;

    public BukkitBlock(Block block) {
        if (block == null) throw new IllegalArgumentException();

        this.block = block;
    }

    @Override
    public boolean isContainer() {
        return VersionSelector.getMinecraft().getContainerTypes().contains(block.getType());
    }

    @Override
    public boolean isAttached(ABlock host) {
        if (host == null) return false;

        if (host instanceof BukkitBlock) {
            Block bkBlock = ((BukkitBlock) host).getBlock();
            BlockState state = bkBlock.getState();
            MaterialData data = state.getData();

            // Check one: The obvious
            if (data instanceof Attachable) {
                BlockFace attached = ((Attachable) data).getAttachedFace();
                Location attachedLoc = this.block.getRelative(attached).getLocation();
                Location sourceLocation = bkBlock.getLocation();

                if (attachedLoc.distanceSquared(sourceLocation) == 0) {
                    return true;
                }
            }

            // Check two: Things that break when their support goes missing
            Block under = this.block.getRelative(BlockFace.DOWN);
            if (under.getLocation().distanceSquared(bkBlock.getLocation()) == 0) {
                // Well, we know that we're above the source at least
                List<Material> canBeBroken = VersionSelector.getMinecraft().getBrokenOnTop();
                if (canBeBroken.contains(this.block.getType())) {
                    return true; // We'll go missing :(
                }
            }
        }

        return false;
    }

    @Override
    public Facing getFacingDirection() {
        BlockState state = block.getState();
        MaterialData data = state.getData();
        if (data != null && data instanceof Directional) {
            for (Facing facing : Facing.values()) {
                if (facing.name().equalsIgnoreCase(((Directional) data).getFacing().name())) {
                    return facing;
                }
            }
        }
        return null;
    }

    @Override
    public ChestType getChestType() {
        return VersionSelector.getMinecraft().getChestType(block);
    }

    @Override
    public ABlock getOtherChest() {
        switch (getChestType()) {
            case DOUBLE_NORMAL:
            case DOUBLE_TRAPPED:
                break;
            default:
                return null;
        }

        ABlock n = getRelative(Facing.NORTH),
                s = getRelative(Facing.SOUTH),
                e = getRelative(Facing.EAST),
                w = getRelative(Facing.WEST);

        if (n.getChestType() == getChestType()) return n;
        else if (s.getChestType() == getChestType()) return s;
        else if (e.getChestType() == getChestType()) return e;
        else if (w.getChestType() == getChestType()) return w;

        return null;
    }

    @Override
    public ASLocation getLocation() {
        return BukkitUtils.toLocation(block.getLocation());
    }

    @Override
    public AWorld getWorld() {
        return getLocation().world;
    }

    @Override
    public ABlock getRelative(Facing relative) {
        if (relative == null) throw new IllegalArgumentException();
        BlockFace face = BukkitUtils.getFacing(relative);
        return new BukkitBlock(block.getRelative(face));
    }

    @Override
    public TrackedState canPlace(APlayer player) {
        return permissionCheck(RejectionList.ListType.BLOCK_PLACE, player);
    }

    @Override
    public TrackedState canBreak(APlayer player) {
        return permissionCheck(RejectionList.ListType.BLOCK_BREAK, player);
    }

    @Override
    public TrackedState canInteract(APlayer player) {
        return permissionCheck(RejectionList.ListType.INTERACTION, player);
    }

    @Override
    protected String getFriendlyName() {
        return AntiShare.getInstance().getMaterialProvider().getPlayerFriendlyName(block);
    }

    /**
     * Gets the applicable Bukkit block
     *
     * @return the bukkit block
     */
    public Block getBlock() {
        return block;
    }

    @Override
    public String toString() {
        return "BukkitBlock{block=" + block.toString() + "}";
    }

    @Override
    public DerivedRejectable getGeneric() {
        return new BlockInformation(block.getType(), (short) -1);
    }

    @Override
    public DerivedRejectable getSpecific() {
        return new BlockInformation(block.getType(), block.getData());
    }

    @Override
    public boolean hasGeneric() {
        return true;
    }
}
