package com.turt2live.antishare.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.metrics.tracker.DonutTracker;
import com.turt2live.metrics.tracker.Tracker;

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
		public final boolean breakAsPiston, breakAsAttached, breakAsWater;
		public final boolean emptyInventories;
		public final boolean removeAttached;
		public final boolean breakSand;

		NaturalSettings(boolean mismatch, boolean piston, boolean attached, boolean water, boolean empty, boolean removeAttached, boolean breakSand){
			this.allowMismatchedGM = mismatch;
			this.breakAsPiston = piston;
			this.breakAsAttached = attached;
			this.breakAsWater = water;
			this.emptyInventories = empty;
			this.removeAttached = removeAttached;
			this.breakSand = breakSand;
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

	public final List<Material> blockBreak, blockPlace, death, pickup, drop, use, craft, trackedCreative, trackedSurvival, trackedAdventure;
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
		blockBreak = stringToMaterialList(config.getList("lists.break", p.getConfig().getList("lists.break")));
		blockPlace = stringToMaterialList(config.getList("lists.place", p.getConfig().getList("lists.place")));
		death = stringToMaterialList(config.getList("lists.death", p.getConfig().getList("lists.death")));
		pickup = stringToMaterialList(config.getList("lists.pickup", p.getConfig().getList("lists.pickup")));
		drop = stringToMaterialList(config.getList("lists.drop", p.getConfig().getList("lists.drop")));
		use = stringToMaterialList(config.getList("lists.use", p.getConfig().getList("lists.use")));
		craft = stringToMaterialList(config.getList("lists.crafting", p.getConfig().getList("lists.crafting")));
		trackedCreative = stringToMaterialList(config.getList("tracking.creative", p.getConfig().getList("tracking.creative")));
		trackedSurvival = stringToMaterialList(config.getList("tracking.survival", p.getConfig().getList("tracking.survival")));
		trackedAdventure = stringToMaterialList(config.getList("tracking.adventure", p.getConfig().getList("tracking.adventure")));
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

		// Update trackers
		for(Material material : trackedCreative){
			// We know all of these casts
			DonutTracker tracker = (DonutTracker) AntiShare.TRACKED_MATERIALS.getTracker(material);
			Tracker wedge = tracker.getMinorWedge("creative");
			wedge.increment();
		}
		for(Material material : trackedSurvival){
			// We know all of these casts
			DonutTracker tracker = (DonutTracker) AntiShare.TRACKED_MATERIALS.getTracker(material);
			Tracker wedge = tracker.getMinorWedge("survival");
			wedge.increment();
		}
		for(Material material : trackedAdventure){
			// We know all of these casts
			DonutTracker tracker = (DonutTracker) AntiShare.TRACKED_MATERIALS.getTracker(material);
			Tracker wedge = tracker.getMinorWedge("adventure");
			wedge.increment();
		}
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

	private List<Material> stringToMaterialList(List<?> list){
		List<Material> materials = new ArrayList<Material>();
		for(Object o : list){
			if(!(o instanceof String)){
				continue;
			}
			String string = (String) o;
			string = string.trim();
			String testString = string.replace(" ", "");
			if(string.equalsIgnoreCase("all")){
				materials.clear();
				for(Material m : Material.values()){
					materials.add(m);
				}
				break;
			}else if(string.equalsIgnoreCase("none")){
				materials.clear();
				break;
			}else if(testString.equalsIgnoreCase("furnace") || testString.equalsIgnoreCase("burningfurnace")
					|| testString.equalsIgnoreCase(String.valueOf(Material.FURNACE.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.BURNING_FURNACE.getId()))){
				materials.add(Material.FURNACE);
				materials.add(Material.BURNING_FURNACE);
				continue;
			}else if(testString.equalsIgnoreCase("sign") || testString.equalsIgnoreCase("wallsign") || testString.equalsIgnoreCase("signpost")
					|| testString.equalsIgnoreCase(String.valueOf(Material.SIGN.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.WALL_SIGN.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.SIGN_POST.getId()))){
				materials.add(Material.SIGN);
				materials.add(Material.WALL_SIGN);
				materials.add(Material.SIGN_POST);
				continue;
			}else if(testString.equalsIgnoreCase("brewingstand") || testString.equalsIgnoreCase("brewingstanditem")
					|| testString.equalsIgnoreCase(String.valueOf(Material.BREWING_STAND.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.BREWING_STAND_ITEM.getId()))){
				materials.add(Material.BREWING_STAND);
				materials.add(Material.BREWING_STAND_ITEM);
				continue;
			}else if(testString.equalsIgnoreCase("enderportal") || testString.equalsIgnoreCase("enderportalframe")
					|| testString.equalsIgnoreCase(String.valueOf(Material.ENDER_PORTAL.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.ENDER_PORTAL_FRAME.getId()))){
				materials.add(Material.ENDER_PORTAL);
				materials.add(Material.ENDER_PORTAL_FRAME);
				continue;
			}else if(testString.equalsIgnoreCase("skull") || testString.equalsIgnoreCase("skullitem") || testString.equalsIgnoreCase("mobskull")
					|| testString.equalsIgnoreCase(String.valueOf(Material.SKULL.getId()))
					|| testString.equalsIgnoreCase(String.valueOf(Material.SKULL_ITEM.getId()))){
				materials.add(Material.SKULL);
				materials.add(Material.SKULL_ITEM);
				continue;
			}
			Material material = Material.matchMaterial(string);
			if(material == null){
				p.getLogger().warning(p.getMessages().getMessage("unknown-material", string));
				continue;
			}
			materials.add(material);
		}
		return Collections.unmodifiableList(materials);
	}

}
