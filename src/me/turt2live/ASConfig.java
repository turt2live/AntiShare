package me.turt2live;

import java.io.File;
import java.io.IOException;

public class ASConfig {

	private AntiShare	plugin;

	public ASConfig(AntiShare plugin) {
		this.plugin = plugin;
	}

	public void create() {
		File d = plugin.getDataFolder();
		d.mkdirs();
		File f2 = new File(plugin.getDataFolder(), "config.yml");
		if (!f2.exists()) try {
			f2.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (get("events.block_break") == null) set("events.block_break", "none");
		if (get("events.block_place") == null) set("events.block_place", "57 41 42");
		if (get("events.death") == null) set("events.death", "*");
		if (get("events.drop_item") == null) set("events.drop_item", "*");
		if (get("events.interact") == null) set("events.interact", "23 61 62 54 342");

		if (get("messages.block_break") == null) set("messages.block_break", "You can't do that!");
		if (get("messages.block_place") == null) set("messages.block_place", "You can't do that!");
		if (get("messages.death") == null) set("messages.death", "You can't do that!");
		if (get("messages.drop_item") == null) set("messages.drop_item", "You can't do that!");
		if (get("messages.interact") == null) set("messages.interact", "You can't do that!");
		if (get("messages.eggs") == null) set("messages.eggs", "You can't use the spawn eggs!");
		if (get("messages.inventory_swap") == null) set("messages.inventory_swap", "Your inventory has been changed.");
		if (get("messages.creativeModeBlock") == null) set("messages.creativeModeBlock", "You cannot break that block! It was placed by a creative mode player.");
		if (get("messages.bedrock") == null) set("messages.bedrock", "You cannot play with bedrock!");

		if (get("other.only_if_creative") == null) set("other.only_if_creative", true);
		if (get("other.allow_eggs") == null) set("other.allow_eggs", false);
		if (get("other.inventory_swap") == null) set("other.inventory_swap", true);
		if (get("other.track_blocks") == null) set("other.track_blocks", true);
		if (get("other.allow_bedrock") == null) set("other.allow_bedrock", false);
		header();
		save();
	}

	public void save() {
		plugin.saveConfig();
	}

	public void set(String path, Object value) {
		plugin.getConfig().set(path, value);
	}

	public Object get(String path) {
		return plugin.getConfig().get(path);
	}

	public void reload() {
		plugin.reloadConfig();
	}

	public void load() {
		plugin.reloadConfig();
	}

	private void header() {
		plugin.getConfig().options().header("AntiShare Configuration:\n" +
				"Events:\n" +
				"	'block_place' - Blocks/items to deny for block placing\n" +
				"	'block_break' - Blocks/items to deny for block breaking\n" +
				"	'death' - Blocks/items to not allow to drop on death\n" +
				"	'drop_item' - Block/items to not allow to drop when a player presses (default) Q\n" +
				"	'interact' - Blocks/items to deny interactions to. (Left/Right click)\n" +
				"	-- Want all blocks/items to be denied? Put a *\n" +
				"	-- Want no blocks/items to be denied? Put: none\n" +
				"	-- Make sure item lists are space-seperated, not commas, periods, or fancy other things!" +
				"Messages:\n" +
				"	All messages are when they are declined an action.\n" +
				"	Chat colors are supported using the & sign (eg: &f = white)\n" +
				"Other:\n" +
				"	'only_if_creative' - Auto-decline if they are in creative, permissions still apply.\n" +
				"		(eg: A player doesn't have the allow or decline permission to place, and is in creative, places a block: declined)\n" +
				"	'allow_eggs' - If false then eggs cannot be used (the ones that spawn mobs, like creepers)\n" +
				"	'inventory_swap' - If true then creative and survival inventories will be swapped\n" +
				"	'track_blocks' - If true then creative mode blocks will be tracked where only creative mode players can break them\n" +
				" 						(unless they have the permission \"AntiShare.blockBypass\")" +
				"	'allow_bedrock' - If false then bedrock will never be broken or placed\n" +
				"Permissions:\n" +
				"	'AntiShare.*' - Deny all events\n" +
				"	'AntiShare.place' - Deny block placing\n" +
				"	'AntiShare.break' - Deny block breaking\n" +
				"	'AntiShare.death' - Deny item drops on death\n" +
				"	'AntiShare.drop' - Deny item dropping\n" +
				"	'AntiShare.interact' - Deny interactions\n" +
				"	'AntiShare.eggs' - Deny eggs that spawn mobs\n" +
				"	-- If you want to allow an event, change the node to 'AntiShare.allow' (eg: 'AntiShare.allow.place' would allow placing)\n" +
				"	'AntiShare.reload' - Permission to use /antishare\n" +
				"	'AntiShare.noswap' - If true, the player is exempt from inventory swapping, otherwise they will have their inventory switched\n" +
				"	'AntiShare.blockBypass' - If true, then the player can bypass the \"Creative mode players can only break this block\" message\n" +
				"	'AntiShare.freePlace' - If true, then the player can place a \"creative-mode\" block without it being registered as a \"creative-mode\" block\n" +
				"	'AntiShare.bedrock' - If true, then the player can break and place bedrock\n" +
				"	'AntiShare.admin' - Includes \"AntiShare.allow.*\", \"AntiShare.reload\", \"AntiShare.blockBypass\", \"AntiShare.freePlace\", \"AntiShare.bedrock\".\n" +
				"Commands:\n" +
				"	'/antishare' - Reloads configuration\n" +
				"		Aliases: '/as', '/antis', '/ashare'\n" +
				"Notes:\n" +
				"	- Permissions default to all deny as true while all allow as OP or higher.\n" +
				"	- 'only_if_creative' will override the permissions meaning if a player is in survival-mode while 'only_if_creative' is true then \n" +
				"	  the deny permissions will be ignored (eg: That survival player places a block: it is placed)\n" +
				"	- The default settings decline interactions with furnaces, chests, dispensers, and chest-minecarts\n" +
				"	- The event 'block_break' is set to 'none' because in creative-mode, no blocks are dropped when broken");
	}
}
