package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.*;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.engine.RejectionList;
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
public class BukkitBlock implements ABlock {

    private final Block block;

    public BukkitBlock(Block block) {
        if (block == null) throw new IllegalArgumentException();

        this.block = block;
    }

    @Override
    public boolean isAttached(ABlock host) {
        if(host==null)return false;

        if(host instanceof BukkitBlock){
            Block bkBlock = ((BukkitBlock)host).getBlock();
            BlockState state = bkBlock.getState();
            MaterialData data = state.getData();

            // Check one: The obvious
            if(data instanceof Attachable){
                BlockFace attached = ((Attachable) data).getAttachedFace();
                Location attachedLoc = this.block.getRelative(attached).getLocation();
                Location sourceLocation = bkBlock.getLocation();

                if(attachedLoc.distanceSquared(sourceLocation)==0){
                    return true;
                }
            }

            // Check two: Things that break when their support goes missing
            Block under = this.block.getRelative(BlockFace.DOWN);
            if(under.getLocation().distanceSquared(bkBlock.getLocation())==0){
                // Well, we know that we're above the source at least
                List<Material> canBeBroken = VersionSelector.getMinecraft().getBrokenOnTop();
                if(canBeBroken.contains(this.block.getType())){
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
    public ASLocation getLocation() {
        return BukkitUtils.toLocation(block.getLocation());
    }

    @Override
    public AWorld getWorld() {
        return getLocation().world;
    }

    @Override
    public TrackedState canPlace(APlayer player) {
        return permissionCheck(RejectionList.ListType.BLOCK_PLACE, player);
    }

    @Override
    public TrackedState canBreak(APlayer player) {
        return permissionCheck(RejectionList.ListType.BLOCK_BREAK, player);
    }

    private TrackedState permissionCheck(RejectionList.ListType type, APlayer player) {
        // Stage One: Check general permissions
        boolean allow = player.hasPermission(PermissionNodes.getPermissionNode(true, type));
        boolean deny = player.hasPermission(PermissionNodes.getPermissionNode(false, type));
        TrackedState stageOne = TrackedState.NOT_PRESENT;

        if (allow == deny) stageOne = TrackedState.NOT_PRESENT;
        else if (allow) stageOne = TrackedState.INCLUDED;
        else if (deny) stageOne = TrackedState.NEGATED;

        // Stage Two: Check specific permissions
        allow = player.hasPermission(PermissionNodes.getPermissionNode(true, type) + "." + getFriendlyName());
        deny = player.hasPermission(PermissionNodes.getPermissionNode(false, type) + "." + getFriendlyName());
        TrackedState stageTwo = TrackedState.NOT_PRESENT;

        if (allow == deny) stageTwo = TrackedState.NOT_PRESENT;
        else if (allow) stageTwo = TrackedState.INCLUDED;
        else if (deny) stageTwo = TrackedState.NEGATED;

        /*
        Stage Three: Combination logic for merging stages one and two
        Logic:

        G = stageOne, general scope
        S = stageTwo, specific scope

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

        if (stageTwo == TrackedState.NOT_PRESENT) return stageOne; // [C1] In all cases, stageOne is favoured
        //if (stageTwo == stageOne) return stageOne; // [C2] Doesn't matter
        return stageTwo; // [RE] Remaining cases are all stageTwo favoured
    }

    // Used for permission checks
    private String getFriendlyName() {
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
}
