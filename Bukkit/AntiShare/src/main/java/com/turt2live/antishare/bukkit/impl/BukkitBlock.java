package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.*;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.engine.RejectionList;
import org.bukkit.block.Block;

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

    private TrackedState permissionCheck(RejectionList.ListType type, APlayer player) {
        String allowPermission = PermissionNodes.getPermissionNode(true, type);
        String denyPermission = PermissionNodes.getPermissionNode(false, type);

        boolean allow = player.hasPermission(allowPermission);
        boolean deny = player.hasPermission(denyPermission);
        TrackedState stageOne = TrackedState.NOT_PRESENT;

        if ((allow && deny) || (!allow && !deny)) stageOne = TrackedState.NOT_PRESENT;
        else if (allow) stageOne = TrackedState.INCLUDED;
        else if (deny) stageOne = TrackedState.NEGATED;

        allowPermission = PermissionNodes.getPermissionNode(true, type) + "." + getFriendlyName();
        denyPermission = PermissionNodes.getPermissionNode(false, type) + "." + getFriendlyName();

        allow = player.hasPermission(allowPermission);
        deny = player.hasPermission(denyPermission);
        TrackedState stageTwo = TrackedState.NOT_PRESENT;

        if ((allow && deny) || (!allow && !deny)) stageTwo = TrackedState.NOT_PRESENT;
        else if (allow) stageTwo = TrackedState.INCLUDED;
        else if (deny) stageTwo = TrackedState.NEGATED;

        /*
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
        return block.getType().name().toLowerCase(); // TODO: Better friendly name
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
