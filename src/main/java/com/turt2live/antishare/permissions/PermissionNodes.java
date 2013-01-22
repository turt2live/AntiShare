/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.permissions;

/**
 * Holds all permission nodes in AntiShare
 * 
 * @author turt2live
 */
public class PermissionNodes {

	// CONFIGURATION NODES
	public static final String CONFIG_ALLOW_NODE_START = "AntiShare.allow";
	public static final String CONFIG_DENY_NODE_START = "AntiShare.deny";

	// Allow Nodes
	public static final String ALLOW_BLOCK_PLACE = "AntiShare.allow.block_place";
	public static final String ALLOW_BLOCK_BREAK = "AntiShare.allow.block_break";
	public static final String ALLOW_DEATH = "AntiShare.allow.death";
	public static final String ALLOW_PICKUP = "AntiShare.allow.pickup_item";
	public static final String ALLOW_DROP = "AntiShare.allow.drop_item";
	public static final String ALLOW_RIGHT_CLICK = "AntiShare.allow.right_click";
	public static final String ALLOW_USE = "AntiShare.allow.use_item";
	public static final String ALLOW_COMMANDS = "AntiShare.allow.commands";
	public static final String ALLOW_COMBAT_PLAYERS = "AntiShare.allow.hit_players";
	public static final String ALLOW_COMBAT_MOBS = "AntiShare.allow.hit_mobs";
	public static final String ALLOW_MOB_CREATION = "AntiShare.allow.create";

	// Deny Nodes
	public static final String DENY_BLOCK_PLACE = "AntiShare.deny.block_place";
	public static final String DENY_BLOCK_BREAK = "AntiShare.deny.block_break";
	public static final String DENY_DEATH = "AntiShare.deny.death";
	public static final String DENY_PICKUP = "AntiShare.deny.pickup_item";
	public static final String DENY_DROP = "AntiShare.deny.drop_item";
	public static final String DENY_RIGHT_CLICK = "AntiShare.deny.right_click";
	public static final String DENY_USE = "AntiShare.deny.use_item";
	public static final String DENY_COMMANDS = "AntiShare.deny.commands";
	public static final String DENY_COMBAT_PLAYERS = "AntiShare.deny.hit_players";
	public static final String DENY_COMBAT_MOBS = "AntiShare.deny.hit_mobs";
	public static final String DENY_MOB_CREATION = "AntiShare.deny.create";

	// Admin Nodes
	public static final String GET_NOTIFICATIONS = "AntiShare.getNotifications";
	public static final String SILENT_NOTIFICATIONS = "AntiShare.silent";
	public static final String BREAK_ANYTHING = "AntiShare.getDrops";
	public static final String MIRROR = "AntiShare.mirror";
	public static final String FREE_PLACE = "AntiShare.free_place";
	public static final String RELOAD = "AntiShare.reload";
	public static final String NO_GM_CD = "AntiShare.cooldownbypass";
	public static final String CHECK = "AntiShare.check";
	public static final String ITEM_FRAMES = "AntiShare.itemframes";
	public static final String CREATE_CUBOID = "AntiShare.cuboid";

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
	public static final String REGION_USE = "AntiShare.regions.use_item";
	public static final String REGION_LIST = "AntiShare.regions.list";

	// World Split Nodes
	public static final String WORLD_SPLIT_NO_SPLIT_CREATIVE = "AntiShare.worldsplit.creative";
	public static final String WORLD_SPLIT_NO_SPLIT_SURVIVAL = "AntiShare.worldsplit.survival";

	// Money Nodes
	public static final String MONEY_NO_FINE = "AntiShare.money.nofine";
	public static final String MONEY_NO_REWARD = "AntiShare.money.noreward";

}
