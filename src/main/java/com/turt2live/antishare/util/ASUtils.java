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
package com.turt2live.antishare.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Bed;
import org.bukkit.material.Door;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.generic.ASEntity;
import com.turt2live.antishare.util.generic.MobPattern;
import com.turt2live.antishare.util.generic.MobPattern.MobPatternType;

/**
 * Utilities
 * 
 * @author turt2live
 */
@SuppressWarnings ("deprecation")
public class ASUtils {

	public static enum EntityPattern{
		SNOW_GOLEM, IRON_GOLEM, WITHER;
	}

	/**
	 * Array of true block faces (none of the SOUTH_WEST-like ones)
	 */
	public static final List<BlockFace> TRUE_BLOCK_FACES = Collections.unmodifiableList(Arrays.asList(new BlockFace[] {BlockFace.DOWN, BlockFace.EAST, BlockFace.WEST, BlockFace.SOUTH, BlockFace.NORTH, BlockFace.UP}));
	private static final List<ASEntity> ENTITY_NAMES = new ArrayList<ASEntity>();
	private static MobPattern SNOW_GOLEM_PATTERN;
	private static MobPattern IRON_GOLEM_PATTERN;
	private static MobPattern WITHER_PATTERN;

	/**
	 * Sends a message to a player.<br>
	 * This will prefix "[AntiShare]" to the message and not send if the message is simply "no message".
	 * 
	 * @param target the player to send to
	 * @param message the message to send
	 * @param useSimpleNotice set to true if this method should use SimpleNotice if available
	 */
	public static void sendToPlayer(CommandSender target, String message, boolean useSimpleNotice){
		if(!message.equalsIgnoreCase("nomsg")
				&& !message.equalsIgnoreCase("no message")
				&& !message.equalsIgnoreCase("none")
				&& !message.equalsIgnoreCase("noshow")
				&& !message.equalsIgnoreCase("no show")){
			message = ChatColor.translateAlternateColorCodes('&', message);
			String prefix = "[AntiShare]";
			try{
				prefix = AntiShare.getInstance().getPrefix();
			}catch(NullPointerException e){}
			prefix = ChatColor.translateAlternateColorCodes('&', prefix);
			if(!ChatColor.stripColor(message).startsWith(ChatColor.stripColor(prefix))){
				message = ChatColor.GRAY + prefix + " " + ChatColor.WHITE + message;
			}
			/* SimpleNotice support provided by feildmaster.
			 * Support adapted by krinsdeath and further
			 * modified by turt2live for AntiShare.
			 */
			if(target instanceof Player){
				if(((Player) target).getListeningPluginChannels().contains("SimpleNotice")
						&& useSimpleNotice
						&& AntiShare.getInstance().isSimpleNoticeEnabled(target.getName())){
					((Player) target).sendPluginMessage(AntiShare.getInstance(), "SimpleNotice", message.getBytes(java.nio.charset.Charset.forName("UTF-8")));
				}else{
					target.sendMessage(message);
				}
			}else{
				target.sendMessage(message);
			}
		}
	}

	/**
	 * Gets a boolean from a String
	 * 
	 * @param value the String
	 * @return the boolean (or null if not found)
	 */
	public static Boolean getBoolean(String value){
		if(value == null || value.trim().length() == 0){
			return null;
		}
		if(value.equalsIgnoreCase("true") || value.equalsIgnoreCase("t") || value.equalsIgnoreCase("on")
				|| value.equalsIgnoreCase("active") || value.equalsIgnoreCase("1")){
			return true;
		}else if(value.equalsIgnoreCase("false") || value.equalsIgnoreCase("f") || value.equalsIgnoreCase("off")
				|| value.equalsIgnoreCase("inactive") || value.equalsIgnoreCase("0")){
			return false;
		}
		return null;
	}

	/**
	 * Gets a GameMode from a String
	 * 
	 * @param value the string
	 * @return the GameMode (or null if not found)
	 */
	public static GameMode getGameMode(String value){
		if(value == null){
			return null;
		}
		if(value.equalsIgnoreCase("creative") || value.equalsIgnoreCase("c") || value.equalsIgnoreCase("1")){
			return GameMode.CREATIVE;
		}else if(value.equalsIgnoreCase("survival") || value.equalsIgnoreCase("s") || value.equalsIgnoreCase("0")){
			return GameMode.SURVIVAL;
		}else if(value.equalsIgnoreCase("adventure") || value.equalsIgnoreCase("a") || value.equalsIgnoreCase("2")){
			return GameMode.ADVENTURE;
		}
		return null;
	}

	/**
	 * Converts a block to a string<br>
	 * This returns the format 'id:data', data will be zero if no
	 * data is found, or the data is actually zero. You can set 'zero'
	 * in the parameters to false to just get the block ID. If 'zero' is
	 * true and there is data, the correct format (id:data) will be returned.
	 * 
	 * @param block the block
	 * @param zero true to add zero
	 * @return the block as a string
	 */
	public static String blockToString(Block block, boolean zero){
		if(block == null){
			return null;
		}
		String typeId = "";
		String data = "";
		typeId = Integer.toString(block.getTypeId());
		if(block.getType().getMaxDurability() > 0){
			data = "0";
		}else if(block.getData() > 0){
			data = Byte.toString(block.getData());
		}else{
			data = "0";
		}
		return typeId + (data.equals("0") && zero ? "" : ":" + data);
	}

	/**
	 * Converts a material to a string<br>
	 * This returns the format 'id:data', data will be zero if no
	 * data is found, or the data is actually zero. You can set 'zero'
	 * in the parameters to false to just get the material ID. If 'zero' is
	 * true and there is data, the correct format (id:data) will be returned.<br>
	 * <b>Worth Noting:</b> this (if zero is false) will return a :* id, such as
	 * 1:* if you pass it Material.STONE.
	 * 
	 * @param material the material
	 * @param zero true to add zero
	 * @return the material as a string
	 */
	public static String materialToString(Material material, boolean zero){
		if(material == null){
			return null;
		}
		StringBuilder ret = new StringBuilder();
		ret.append(material.getId());
		if(!zero){
			ret.append(":");
			ret.append("*");
		}
		return ret.toString();
	}

	/**
	 * Converts words to ID. Eg: "light blue wool" -> "wool:3"
	 * 
	 * @param input the raw input
	 * @return the wool ID (with data value) or null if not wool
	 */
	public static String getWool(String input){
		if(input == null || !input.toLowerCase().contains("wool")){
			return null;
		}

		String color = input.replace("wool", "").trim().toLowerCase();
		if(color.length() == 0){
			color = "white";
		}
		color = color.replaceAll(" ", "_");
		color = color.replace("orange", "1");
		color = color.replace("white", "0");
		color = color.replace("magenta", "2");
		color = color.replace("light_blue", "3");
		color = color.replace("yellow", "4");
		color = color.replace("lime", "5");
		color = color.replace("pink", "6");
		color = color.replace("light_gray", "8");
		color = color.replace("gray", "7");
		color = color.replace("cyan", "9");
		color = color.replace("purple", "10");
		color = color.replace("blue", "11");
		color = color.replace("brown", "12");
		color = color.replace("green", "13");
		color = color.replace("red", "14");
		color = color.replace("black", "15");

		return Material.WOOL.getId() + ":" + color;
	}

	/**
	 * Gets an entity name from an entity object
	 * 
	 * @param entity the entity
	 * @return the name, or null if invalid
	 */
	public static String getEntityName(Entity entity){
		if(entity == null){
			return null;
		}
		return getEntityName(entity.getClass().getSimpleName().replace("Craft", ""));
	}

	/**
	 * Determines if the passed name is valid
	 * 
	 * @param entity the potential entity
	 * @return the name, or null if invalid
	 */
	public static String getEntityName(String entity){
		for(ASEntity asentity : allEntities()){
			if(asentity.getProperName().equalsIgnoreCase(entity) || asentity.getGivenName().equalsIgnoreCase(entity)){
				return asentity.getProperName();
			}
		}
		return null;
	}

	/**
	 * Gets a list of all entities AntiShare can handle
	 * 
	 * @return a list of entities
	 */
	public static List<ASEntity> allEntities(){
		if(ENTITY_NAMES.size() <= 0){
			ENTITY_NAMES.add(new ASEntity("blaze", "blaze"));
			ENTITY_NAMES.add(new ASEntity("cavespider", "cave spider"));
			ENTITY_NAMES.add(new ASEntity("cave spider", "cave spider"));
			ENTITY_NAMES.add(new ASEntity("chicken", "chicken"));
			ENTITY_NAMES.add(new ASEntity("cow", "cow"));
			ENTITY_NAMES.add(new ASEntity("creeper", "creeper"));
			ENTITY_NAMES.add(new ASEntity("enderdragon", "ender dragon"));
			ENTITY_NAMES.add(new ASEntity("ender dragon", "ender dragon"));
			ENTITY_NAMES.add(new ASEntity("enderman", "enderman"));
			ENTITY_NAMES.add(new ASEntity("ghast", "ghast"));
			ENTITY_NAMES.add(new ASEntity("giant", "giant"));
			ENTITY_NAMES.add(new ASEntity("irongolem", "iron golem"));
			ENTITY_NAMES.add(new ASEntity("iron golem", "iron golem"));
			ENTITY_NAMES.add(new ASEntity("mushroomcow", "mooshroom"));
			ENTITY_NAMES.add(new ASEntity("mushroom cow", "mooshroom"));
			ENTITY_NAMES.add(new ASEntity("mooshroom", "mooshroom"));
			ENTITY_NAMES.add(new ASEntity("ocelot", "ocelot"));
			ENTITY_NAMES.add(new ASEntity("cat", "ocelot"));
			ENTITY_NAMES.add(new ASEntity("pig", "pig"));
			ENTITY_NAMES.add(new ASEntity("pigzombie", "pigman"));
			ENTITY_NAMES.add(new ASEntity("zombiepigman", "pigman"));
			ENTITY_NAMES.add(new ASEntity("pig zombie", "pigman"));
			ENTITY_NAMES.add(new ASEntity("zombie pigman", "pigman"));
			ENTITY_NAMES.add(new ASEntity("pigman", "pigman"));
			ENTITY_NAMES.add(new ASEntity("sheep", "sheep"));
			ENTITY_NAMES.add(new ASEntity("silverfish", "silverfish"));
			ENTITY_NAMES.add(new ASEntity("skeleton", "skeleton"));
			ENTITY_NAMES.add(new ASEntity("slime", "slime"));
			ENTITY_NAMES.add(new ASEntity("magmacube", "magma cube"));
			ENTITY_NAMES.add(new ASEntity("magma cube", "magma cube"));
			ENTITY_NAMES.add(new ASEntity("spider", "spider"));
			ENTITY_NAMES.add(new ASEntity("snowman", "snowman"));
			ENTITY_NAMES.add(new ASEntity("squid", "squid"));
			ENTITY_NAMES.add(new ASEntity("villager", "villager"));
			ENTITY_NAMES.add(new ASEntity("testificate", "villager"));
			ENTITY_NAMES.add(new ASEntity("wolf", "wolf"));
			ENTITY_NAMES.add(new ASEntity("zombie", "zombie"));
			ENTITY_NAMES.add(new ASEntity("witch", "witch"));
			ENTITY_NAMES.add(new ASEntity("wither", "wither boss"));
			ENTITY_NAMES.add(new ASEntity("witherboss", "wither boss"));
			ENTITY_NAMES.add(new ASEntity("wither boss", "wither boss"));
			ENTITY_NAMES.add(new ASEntity("bat", "bat"));
		}
		return ENTITY_NAMES;
	}

	/**
	 * Gets a list of online players with a defined Game Mode
	 * 
	 * @param gamemode the Game Mode
	 * @return the player names with that Game Mode (online only)
	 */
	public static List<String> findGameModePlayers(GameMode gamemode){
		List<String> affected = new ArrayList<String>();
		for(Player player : Bukkit.getOnlinePlayers()){
			if(player.getGameMode() == gamemode){
				affected.add(player.getName());
			}
		}
		return affected;
	}

	/**
	 * Generates a comma-separated list from a List
	 * 
	 * @param list the list
	 * @return the comma-separated String
	 */
	public static String commas(List<String> list){
		if(list == null){
			return "no one";
		}
		StringBuilder commas = new StringBuilder();
		for(String s : list){
			commas.append(s).append(", ");
		}
		String finalComma = commas.toString().trim();
		return finalComma.length() > 0 ? finalComma.substring(0, finalComma.length() - 1) : "no one";
	}

	/**
	 * Gets the abbreviation for a game mode. Eg: CREATIVE = "GM = C" (if shortVersion=false) or CREATIVE = "C" (shortVersion=true)
	 * 
	 * @param gamemode the gamemode
	 * @param shortVersion true to use the single letter, false otherwise
	 * @return the short hand version, or null if invalid
	 */
	public static String gamemodeAbbreviation(GameMode gamemode, boolean shortVersion){
		if(gamemode == null){
			return null;
		}
		return (shortVersion ? "" : "GM = ") + gamemode.name().charAt(0);
	}

	/**
	 * Creates a file safe name from a string
	 * 
	 * @param name the string
	 * @return the file safe name
	 */
	public static String fileSafeName(String name){
		return name.replaceAll("[^0-9a-zA-Z]", "-");
	}

	/**
	 * Wipes a folder
	 * 
	 * @param folder the folder to wipe
	 * @param fileNames file names to wipe, can be null for "all"
	 */
	public static void wipeFolder(File folder, CopyOnWriteArrayList<String> fileNames){
		if(folder == null || !folder.exists()){
			return;
		}
		if(folder.listFiles() == null){
			return;
		}else{
			for(File file : folder.listFiles()){
				if(file.isDirectory()){
					wipeFolder(file, fileNames);
				}
				if(fileNames == null || fileNames.contains(file.getName())){
					file.delete();
				}
			}
		}
	}

	/**
	 * Gives a tool to a player
	 * 
	 * @param tool the tool
	 * @param player the player
	 */
	public static void giveTool(Material tool, Player player){
		giveTool(tool, player, 1);
	}

	/**
	 * Gives a tool to a player
	 * 
	 * @param tool the tool
	 * @param player the player
	 * @param slot the slot to place it in. <b>Starts at 1</b>
	 */
	public static void giveTool(Material tool, Player player, int slot){
		Inventory inv = player.getInventory();
		if(inv.firstEmpty() >= 0){
			ItemStack original = inv.getItem(slot - 1);
			if(original != null){
				original = original.clone();
			}
			inv.setItem(slot - 1, new ItemStack(tool));
			if(original != null){
				inv.addItem(original);
			}
			player.updateInventory();
		}
	}

	/**
	 * Determines the second block to a source block (like a bed)
	 * 
	 * @param block the source
	 * @return the second block, or null if not found
	 */
	public static Block multipleBlocks(Block block){
		if(block == null){
			return null;
		}
		switch (block.getType()){
		case WOODEN_DOOR:
		case IRON_DOOR_BLOCK:
			Door door = (Door) block.getState().getData();
			if(door.isTopHalf()){
				return block.getRelative(BlockFace.DOWN);
			}else{
				return block.getRelative(BlockFace.UP);
			}
		case BED_BLOCK:
			Bed bed = (Bed) block.getState().getData();
			if(bed.isHeadOfBed()){
				return block.getRelative(bed.getFacing().getOppositeFace());
			}else{
				return block.getRelative(bed.getFacing());
			}
		default:
			return null;
		}
	}

	/**
	 * Gets the mob pattern for the supplied entity, if found
	 * 
	 * @param pattern the pattern to look for
	 * @return the pattern. This will be null if the pattern is not found or unsupported
	 */
	public static MobPattern getMobPattern(EntityPattern pattern){
		if(pattern == null){
			return null;
		}
		switch (pattern){
		case SNOW_GOLEM:
			if(SNOW_GOLEM_PATTERN == null){
				SNOW_GOLEM_PATTERN = new MobPattern(MobPatternType.POLE, Material.SNOW_BLOCK, Material.PUMPKIN, Material.JACK_O_LANTERN);
			}
			return SNOW_GOLEM_PATTERN;
		case IRON_GOLEM:
			if(IRON_GOLEM_PATTERN == null){
				IRON_GOLEM_PATTERN = new MobPattern(MobPatternType.T_SHAPE, Material.IRON_BLOCK, Material.PUMPKIN, Material.JACK_O_LANTERN);
			}
			return IRON_GOLEM_PATTERN;
		case WITHER:
			if(WITHER_PATTERN == null){
				WITHER_PATTERN = new MobPattern(MobPatternType.T_SHAPE, Material.SOUL_SAND, Material.SKULL);
			}
			return WITHER_PATTERN;
		}
		return null;
	}

}
