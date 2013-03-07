/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.entity.EntityType;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.ASMaterialList;

/**
 * AntiShare configuration
 * 
 * @author turt2live
 */
public class ASConfig {

	public static class InventoryCleanupSettings {
		public final boolean archive, enabled, removeOldWorlds;
		public final int after;

		InventoryCleanupSettings(boolean enabled, boolean archive, int after, boolean removeOldWorlds){
			this.archive = archive;
			this.enabled = enabled;
			this.after = after;
			this.removeOldWorlds = removeOldWorlds;
		}
	}

	public static class CooldownSettings {
		public final boolean enabled;
		public final int seconds;

		CooldownSettings(boolean enabled, int seconds){
			this.enabled = enabled;
			this.seconds = seconds;
		}
	}

	public static class NaturalSettings {
		public final boolean allowMismatchedGM;
		public final boolean breakAsPiston, breakAsAttached, breakAsWater, breakAsBomb;
		public final boolean emptyInventories;
		public final boolean removeAttached;
		public final boolean breakSand;

		NaturalSettings(boolean mismatch, boolean piston, boolean attached, boolean water, boolean bombs, boolean empty, boolean removeAttached, boolean breakSand){
			this.allowMismatchedGM = mismatch;
			this.breakAsPiston = piston;
			this.breakAsAttached = attached;
			this.breakAsWater = water;
			this.emptyInventories = empty;
			this.removeAttached = removeAttached;
			this.breakSand = breakSand;
			this.breakAsBomb = bombs;
		}
	}

	public static class GameModeChangeSettings {
		public final boolean changeLevel, changeBalance, changeInventory, changeEnder;

		GameModeChangeSettings(boolean level, boolean balance, boolean inventory, boolean ender){
			this.changeBalance = balance;
			this.changeEnder = ender;
			this.changeInventory = inventory;
			this.changeLevel = level;
		}
	}

	public static class InteractionSettings {
		public final boolean deny, drop;

		InteractionSettings(boolean deny, boolean drop){
			this.deny = deny;
			this.drop = drop;
		}
	}

	public static class NotifySettings {
		public final boolean enabled, admins, console;

		NotifySettings(boolean enabled, boolean admins, boolean console){
			this.enabled = enabled;
			this.admins = admins;
			this.console = console;
		}
	}

	public static class FeatureSettings {
		public final boolean inventories, fines;

		FeatureSettings(boolean inventories, boolean fines){
			this.inventories = inventories;
			this.fines = fines;
		}
	}

	public final ASMaterialList blockBreak, blockPlace, death, pickup, drop, use, craft, trackedCreative, trackedSurvival, trackedAdventure, eat;
	public final List<String> commands;
	public final List<EntityType> interactMobs, attackMobs, craftedMobs;
	public final boolean adventureEqCreative, perWorldInventories, updateChecker, magicSpells, logBlockSpam, potions, thrownPotions;
	public final InventoryCleanupSettings inventoryCleanupSettings;
	public final CooldownSettings cooldownSettings;
	public final NaturalSettings naturalSettings;
	public final GameModeChangeSettings gamemodeChangeSettings;
	public final InteractionSettings survivalBreakCreative, creativeBreakSurvival, survivalBreakAdventure, creativeBreakAdventure, adventureBreakSurvival, adventureBreakCreative;
	public final NotifySettings notificationSettings;
	public final EnhancedConfiguration rawConfiguration;
	public final FeatureSettings features;

	private AntiShare p = AntiShare.p;

	public ASConfig(EnhancedConfiguration config){
		rawConfiguration = config;
		potions = config.getBoolean("lists.no-potions", p.getConfig().getBoolean("lists.no-potions"));
		thrownPotions = config.getBoolean("lists.no-thrown-potions", p.getConfig().getBoolean("lists.no-thrown-potions"));
		blockBreak = new ASMaterialList(config.getList("lists.break", p.getConfig().getList("lists.break")));
		blockPlace = new ASMaterialList(config.getList("lists.place", p.getConfig().getList("lists.place")));
		death = new ASMaterialList(config.getList("lists.death", p.getConfig().getList("lists.death")));
		pickup = new ASMaterialList(config.getList("lists.pickup", p.getConfig().getList("lists.pickup")));
		drop = new ASMaterialList(config.getList("lists.drop", p.getConfig().getList("lists.drop")));
		use = new ASMaterialList(config.getList("lists.use", p.getConfig().getList("lists.use")));
		eat = new ASMaterialList(config.getList("lists.eat", p.getConfig().getList("lists.eat")));
		craft = new ASMaterialList(config.getList("lists.crafting", p.getConfig().getList("lists.crafting")));
		trackedCreative = new ASMaterialList(config.getList("tracking.creative", p.getConfig().getList("tracking.creative")));
		trackedSurvival = new ASMaterialList(config.getList("tracking.survival", p.getConfig().getList("tracking.survival")));
		trackedAdventure = new ASMaterialList(config.getList("tracking.adventure", p.getConfig().getList("tracking.adventure")));
		commands = toStringList(config.getList("lists.commands", p.getConfig().getList("lists.commands")));
		interactMobs = stringToEntityList(config.getList("lists.interact-mobs", p.getConfig().getList("lists.interact-mobs")));
		attackMobs = stringToEntityList(config.getList("lists.attack-mobs", p.getConfig().getList("lists.attack-mobs")));
		craftedMobs = stringToEntityList(config.getList("lists.craft-mob", p.getConfig().getList("lists.craft-mob")));
		adventureEqCreative = config.getBoolean("settings.adventure-is-creative", p.getConfig().getBoolean("settings.adventure-is-creative"));
		perWorldInventories = config.getBoolean("settings.use-per-world-inventories", p.getConfig().getBoolean("settings.use-per-world-inventories"));
		magicSpells = config.getBoolean("hooks.magicspells.block-creative", p.getConfig().getBoolean("hooks.magicspells.block-creative"));
		logBlockSpam = config.getBoolean("hooks.logblock.stop-spam", p.getConfig().getBoolean("settings.logblock.stop-spam"));
		updateChecker = !config.getBoolean("other.ignore-updates", p.getConfig().getBoolean("other.ignore-updates"));
		inventoryCleanupSettings = new InventoryCleanupSettings(config.getBoolean("settings.cleanup.inventories.enabled", p.getConfig().getBoolean("settings.cleanup.inventories.enabled")),
				!config.getString("settings.cleanup.inventories.method", p.getConfig().getString("settings.cleanup.inventories.method")).equalsIgnoreCase("delete"),
				config.getInt("settings.cleanup.inventories.after", p.getConfig().getInt("settings.cleanup.inventories.after")),
				config.getBoolean("settings.cleanup.inventories.remove-old-worlds", p.getConfig().getBoolean("settings.cleanup.inventories.remove-old-worlds")));
		cooldownSettings = new CooldownSettings(config.getBoolean("settings.cooldown.enabled", p.getConfig().getBoolean("settings.cooldown.enabled")),
				config.getInt("settings.cooldown.wait-time-seconds", p.getConfig().getInt("settings.cooldown.wait-time-seconds")));
		naturalSettings = new NaturalSettings(config.getBoolean("settings.natural-protection.allow-mismatch-gamemode", p.getConfig().getBoolean("settings.natural-protection.allow-mismatch-gamemode")),
				config.getBoolean("settings.natural-protection.break-as-gamemode.pistons", p.getConfig().getBoolean("settings.natural-protection.break-as-gamemode.pistons")),
				config.getBoolean("settings.natural-protection.break-as-gamemode.attached-blocks", p.getConfig().getBoolean("settings.natural-protection.break-as-gamemode.attached-blocks")),
				config.getBoolean("settings.natural-protection.break-as-gamemode.water", p.getConfig().getBoolean("settings.natural-protection.break-as-gamemode.water")),
				config.getBoolean("settings.natural-protection.break-as-gamemode.blown-up", p.getConfig().getBoolean("settings.natural-protection.break-as-gamemode.blown-up")),
				config.getBoolean("settings.natural-protection.empty-inventories", p.getConfig().getBoolean("settings.natural-protection.empty-inventories")),
				config.getBoolean("settings.natural-protection.remove-attached-blocks", p.getConfig().getBoolean("settings.natural-protection.remove-attached-blocks")),
				config.getBoolean("settings.natural-protection.break-as-gamemode.falling-blocks", p.getConfig().getBoolean("settings.natural-protection.break-as-gamemode.falling-blocks")));
		survivalBreakCreative = new InteractionSettings(config.getBoolean("interaction.survival-breaking-creative.deny", p.getConfig().getBoolean("interaction.survival-breaking-creative.deny")),
				config.getBoolean("interaction.survival-breaking-creative.drop-items", p.getConfig().getBoolean("interaction.survival-breaking-creative.drop-items")));
		creativeBreakSurvival = new InteractionSettings(config.getBoolean("interaction.creative-breaking-survival.deny", p.getConfig().getBoolean("interaction.creative-breaking-survival.deny")),
				config.getBoolean("interaction.creative-breaking-survival.drop-items", p.getConfig().getBoolean("interaction.creative-breaking-survival.drop-items")));
		survivalBreakAdventure = new InteractionSettings(config.getBoolean("interaction.survival-breaking-adventure.deny", p.getConfig().getBoolean("interaction.survival-breaking-adventure.deny")),
				config.getBoolean("interaction.survival-breaking-adventure.drop-items", p.getConfig().getBoolean("interaction.survival-breaking-adventure.drop-items")));
		adventureBreakCreative = new InteractionSettings(config.getBoolean("interaction.adventure-breaking-creative.deny", p.getConfig().getBoolean("interaction.adventure-breaking-creative.deny")),
				config.getBoolean("interaction.adventure-breaking-creative.drop-items", p.getConfig().getBoolean("interaction.adventure-breaking-creative.drop-items")));
		adventureBreakSurvival = new InteractionSettings(config.getBoolean("interaction.adventure-breaking-survival.deny", p.getConfig().getBoolean("interaction.adventure-breaking-survival.deny")),
				config.getBoolean("interaction.adventure-breaking-survival.drop-items", p.getConfig().getBoolean("interaction.adventure-breaking-survival.drop-items")));
		creativeBreakAdventure = new InteractionSettings(config.getBoolean("interaction.creative-breaking-adventure.deny", p.getConfig().getBoolean("interaction.creative-breaking-adventure.deny")),
				config.getBoolean("interaction.creative-breaking-adventure.drop-items", p.getConfig().getBoolean("interaction.creative-breaking-adventure.drop-items")));
		gamemodeChangeSettings = new GameModeChangeSettings(config.getBoolean("settings.gamemode-change.change-level", p.getConfig().getBoolean("settings.gamemode-change.change-level")),
				config.getBoolean("settings.gamemode-change.change-economy-balance", p.getConfig().getBoolean("settings.gamemode-change.change-economy-balance")),
				config.getBoolean("settings.gamemode-change.change-inventory", p.getConfig().getBoolean("settings.gamemode-change.change-inventory")),
				config.getBoolean("settings.gamemode-change.change-ender-chest", p.getConfig().getBoolean("settings.gamemode-change.change-ender-chest")));
		notificationSettings = new NotifySettings(config.getBoolean("settings.notify.use", p.getConfig().getBoolean("settings.notify.use")),
				config.getBoolean("settings.notify.with-permission", p.getConfig().getBoolean("settings.notify.with-permission")),
				config.getBoolean("settings.notify.console", p.getConfig().getBoolean("settings.notify.console")));
		features = new FeatureSettings(config.getBoolean("settings.features.use-inventories", p.getConfig().getBoolean("settings.features.use-inventories")),
				config.getBoolean("settings.features.use-fines-rewards", p.getConfig().getBoolean("settings.features.use-fines-rewards")));
	}

	private List<String> toStringList(List<?> list){
		List<String> strings = new ArrayList<String>();
		for(Object o : list){
			if(!(o instanceof String)){
				continue;
			}
			String s = ((String) o).toLowerCase();
			if(s.startsWith("/")){
				s = s.substring(1);
			}
			strings.add(s);
		}
		return Collections.unmodifiableList(strings);
	}

	private List<EntityType> stringToEntityList(List<?> list){
		List<EntityType> entities = new ArrayList<EntityType>();
		for(Object o : list){
			if(!(o instanceof String)){
				continue;
			}
			String string = (String) o;
			string = string.trim();
			if(string.equalsIgnoreCase("all")){
				entities.clear();
				for(EntityType e : EntityType.values()){
					entities.add(e);
				}
				break;
			}else if(string.equalsIgnoreCase("none")){
				entities.clear();
				break;
			}
			String modified = string.toLowerCase().replace(" ", "");
			if(modified.equalsIgnoreCase("irongolem")){
				modified = "villagergolem";
			}else if(modified.equalsIgnoreCase("snowgolem")){
				modified = "snowman";
			}else if(modified.equalsIgnoreCase("wither")){
				modified = "witherboss";
			}else if(modified.equalsIgnoreCase("players") || modified.equalsIgnoreCase("player")){
				entities.add(EntityType.PLAYER);
				continue;
			}
			EntityType entity = EntityType.fromName(modified);
			if(entity == null){
				p.getLogger().warning(p.getMessages().getMessage("unknown-entity", string));
				continue;
			}
			entities.add(entity);
		}
		return Collections.unmodifiableList(entities);
	}

}
