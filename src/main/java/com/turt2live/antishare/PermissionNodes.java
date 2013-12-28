/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare;

import org.bukkit.GameMode;

/**
 * Holds all permission nodes in AntiShare
 *
 * @author turt2live
 */
public class PermissionNodes {

    /**
     * Holds multiple permissions for illegal/legal checks
     *
     * @author turt2live
     */
    public static class PermissionPackage {
        public final String allow, deny, region;

        PermissionPackage(String allow, String deny, String region) {
            this.allow = allow;
            this.deny = deny;
            this.region = region;
        }
    }

    // CONFIGURATION NODES
    public static final String CONFIG_ALLOW_NODE_START = "AntiShare.allow";
    public static final String CONFIG_DENY_NODE_START = "AntiShare.deny";

    // Allow Nodes
    public static final String ALLOW_BLOCK_PLACE = "AntiShare.allow.block_place";
    public static final String ALLOW_BLOCK_BREAK = "AntiShare.allow.block_break";
    public static final String ALLOW_DEATH = "AntiShare.allow.death";
    public static final String ALLOW_PICKUP = "AntiShare.allow.pickup_item";
    public static final String ALLOW_DROP = "AntiShare.allow.drop_item";
    public static final String ALLOW_USE = "AntiShare.allow.use_item";
    public static final String ALLOW_INTERACT = "AntiShare.allow.interact_item";
    public static final String ALLOW_COMMANDS = "AntiShare.allow.commands";
    public static final String ALLOW_COMBAT_PLAYERS = "AntiShare.allow.hit_players";
    public static final String ALLOW_COMBAT_MOBS = "AntiShare.allow.hit_mobs";
    public static final String ALLOW_MOB_CREATION = "AntiShare.allow.create";
    public static final String ALLOW_EAT = "AntiShare.allow.eat";

    // Deny Nodes
    public static final String DENY_BLOCK_PLACE = "AntiShare.deny.block_place";
    public static final String DENY_BLOCK_BREAK = "AntiShare.deny.block_break";
    public static final String DENY_DEATH = "AntiShare.deny.death";
    public static final String DENY_PICKUP = "AntiShare.deny.pickup_item";
    public static final String DENY_DROP = "AntiShare.deny.drop_item";
    public static final String DENY_USE = "AntiShare.deny.use_item";
    public static final String DENY_INTERACT = "AntiShare.deny.interact_item";
    public static final String DENY_COMMANDS = "AntiShare.deny.commands";
    public static final String DENY_COMBAT_PLAYERS = "AntiShare.deny.hit_players";
    public static final String DENY_COMBAT_MOBS = "AntiShare.deny.hit_mobs";
    public static final String DENY_MOB_CREATION = "AntiShare.deny.create";
    public static final String DENY_EAT = "AntiShare.deny.eat";

    // Admin Nodes
    public static final String GET_NOTIFICATIONS = "AntiShare.getNotifications";
    public static final String SILENT_NOTIFICATIONS = "AntiShare.silent";
    public static final String BREAK_ANYTHING = "AntiShare.getDrops";
    public static final String MIRROR = "AntiShare.mirror";
    public static final String FREE_PLACE = "AntiShare.free_place";
    public static final String RELOAD = "AntiShare.reload";
    public static final String NO_GAMEMODE_COOLDOWN = "AntiShare.cooldownbypass";
    public static final String CHECK = "AntiShare.check";
    public static final String ITEM_FRAMES = "AntiShare.itemframes";
    public static final String CREATE_CUBOID = "AntiShare.cuboid";
    public static final String ALLOW_EMPTY_INVENTORY = "AntiShare.gamemode.emptyinv";

    // General Nodes
    public static final String AFFECT_SURVIVAL = "AntiShare.affect.survival";
    public static final String AFFECT_CREATIVE = "AntiShare.affect.creative";
    public static final String AFFECT_ADVENTURE = "AntiShare.affect.survival";

    // Inventories
    public static final String NO_SWAP = "AntiShare.inventories.no_swap";
    public static final String MAKE_ANYTHING = "AntiShare.inventories.allowcrafting";

    // Tool Nodes
    public static final String TOOL_GET = "AntiShare.tool.get";
    public static final String TOOL_USE = "AntiShare.tool.use";

    // Region Nodes
    public static final String REGION_CREATE = "AntiShare.regions.create";
    public static final String REGION_DELETE = "AntiShare.regions.delete";
    public static final String REGION_EDIT = "AntiShare.regions.edit";
    public static final String REGION_ROAM = "AntiShare.regions.roam";
    public static final String REGION_THROW = "AntiShare.regions.throw_item";
    public static final String REGION_PICKUP = "AntiShare.regions.pickup_item";
    public static final String REGION_PLACE = "AntiShare.regions.place";
    public static final String REGION_BREAK = "AntiShare.regions.break";
    public static final String REGION_ATTACK_MOBS = "AntiShare.regions.attack_mobs";
    public static final String REGION_ATTACK_PLAYERS = "AntiShare.regions.attack_players";
    public static final String REGION_USE = "AntiShare.regions.use_item";
    public static final String REGION_INTERACT = "AntiShare.regions.interact_item";
    public static final String REGION_LIST = "AntiShare.regions.list";

    // World Split Nodes
    public static final String WORLD_SPLIT_NO_SPLIT_ADVENTURE = "AntiShare.worldsplit.adventure";
    public static final String WORLD_SPLIT_NO_SPLIT_CREATIVE = "AntiShare.worldsplit.creative";
    public static final String WORLD_SPLIT_NO_SPLIT_SURVIVAL = "AntiShare.worldsplit.survival";
    public static final String WORLD_SPLIT_NO_SPLIT = "AntiShare.worldsplit.*";

    // Money Nodes
    public static final String MONEY_NO_FINE = "AntiShare.money.nofine";
    public static final String MONEY_NO_REWARD = "AntiShare.money.noreward";

    // Plugin-Specific Nodes
    public static final String PLUGIN_MAGIC_SPELLS = "AntiShare.plugin.magicspells";
    public static final String PLUGIN_PLAYER_VAULTS = "AntiShare.plugin.playervaults";

    // PACKAGES
    public static final PermissionPackage PACK_BLOCK_BREAK = new PermissionPackage(ALLOW_BLOCK_BREAK, DENY_BLOCK_BREAK, REGION_BREAK);
    public static final PermissionPackage PACK_BLOCK_PLACE = new PermissionPackage(ALLOW_BLOCK_PLACE, DENY_BLOCK_PLACE, REGION_PLACE);
    public static final PermissionPackage PACK_DEATH = new PermissionPackage(ALLOW_DEATH, DENY_DEATH, null);
    public static final PermissionPackage PACK_PICKUP = new PermissionPackage(ALLOW_PICKUP, DENY_PICKUP, REGION_PICKUP);
    public static final PermissionPackage PACK_DROP = new PermissionPackage(ALLOW_DROP, DENY_DROP, REGION_THROW);
    public static final PermissionPackage PACK_USE = new PermissionPackage(ALLOW_USE, DENY_USE, REGION_USE);
    public static final PermissionPackage PACK_INTERACT = new PermissionPackage(ALLOW_INTERACT, DENY_INTERACT, REGION_INTERACT);
    public static final PermissionPackage PACK_COMMANDS = new PermissionPackage(ALLOW_COMMANDS, DENY_COMMANDS, null);
    public static final PermissionPackage PACK_COMBAT_PLAYERS = new PermissionPackage(ALLOW_COMBAT_PLAYERS, DENY_COMBAT_PLAYERS, REGION_ATTACK_PLAYERS);
    public static final PermissionPackage PACK_COMBAT_MOBS = new PermissionPackage(ALLOW_COMBAT_MOBS, DENY_COMBAT_MOBS, REGION_ATTACK_MOBS);
    public static final PermissionPackage PACK_MOB_MAKE = new PermissionPackage(ALLOW_MOB_CREATION, DENY_MOB_CREATION, null);
    public static final PermissionPackage PACK_EAT = new PermissionPackage(ALLOW_EAT, DENY_EAT, null);

    public static String getWorldSplitNode(GameMode side) {
        switch (side) {
            case SURVIVAL:
                return WORLD_SPLIT_NO_SPLIT_SURVIVAL;
            case CREATIVE:
                return WORLD_SPLIT_NO_SPLIT_CREATIVE;
            case ADVENTURE:
                return WORLD_SPLIT_NO_SPLIT_ADVENTURE;
            default:
                return WORLD_SPLIT_NO_SPLIT;
        }
    }

}
