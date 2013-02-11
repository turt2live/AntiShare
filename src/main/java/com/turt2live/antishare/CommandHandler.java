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
package com.turt2live.antishare;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.cuboid.Cuboid;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.DisplayableInventory;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;
import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;
import com.turt2live.antishare.manager.CuboidManager;
import com.turt2live.antishare.manager.InventoryManager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.manager.RegionManager;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.regions.Region;
import com.turt2live.antishare.regions.RegionKey;
import com.turt2live.antishare.util.ASUtils;
import com.turt2live.materials.MaterialAPI;

/**
 * Command Handler
 * 
 * @author turt2live
 */
public class CommandHandler implements CommandExecutor {

	private final AntiShare plugin = AntiShare.getInstance();

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		if(sender instanceof BlockCommandSender){
			return false;
		}
		if(command.getName().equalsIgnoreCase("AntiShare")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("version")){
					ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "Version: " + ChatColor.GOLD + plugin.getDescription().getVersion() + ChatColor.YELLOW + " Build: " + ChatColor.GOLD + plugin.getBuild(), false);
					return true;
				}else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
					if(plugin.getPermissions().has(sender, PermissionNodes.RELOAD)){
						ASUtils.sendToPlayer(sender, Localization.getMessage(LocaleMessage.RELOADING), true);
						plugin.reload();
						ASUtils.sendToPlayer(sender, ChatColor.GREEN + Localization.getMessage(LocaleMessage.RELOADED), true);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("mirror")){
					// Sanity Check
					if(!(sender instanceof Player)){
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
					}else{
						if(plugin.getPermissions().has(sender, PermissionNodes.MIRROR)){
							if(!plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.ERROR_INVENTORIES), true);
								return true;
							}
							if(args.length < 2){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as mirror <player> [enderchest/normal] [gamemode] [world]"), true);
							}else{
								// Setup
								String playername = args[1];
								OfflinePlayer player = plugin.getServer().getPlayer(playername);
								// Find online player first, then we look for offline players
								if(player == null){
									for(OfflinePlayer player2 : plugin.getServer().getOfflinePlayers()){
										if(player2.getName().equalsIgnoreCase(playername) || player2.getName().toLowerCase().startsWith(playername.toLowerCase())){
											player = player2;
											break;
										}
									}
								}

								// Sanity check
								if(player == null){
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_NO_PLAYER, playername), true);
									return true;
								}

								// Ender chest check
								boolean isEnder = false;
								if(args.length > 2){
									if(args[2].equalsIgnoreCase("ender") || args[2].equalsIgnoreCase("enderchest")){
										isEnder = true;
									}else if(args[2].equalsIgnoreCase("normal") || args[2].equalsIgnoreCase("player")){
										isEnder = false;
									}else{
										isEnder = false;
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_ASSUME, Localization.getMessage(LocaleMessage.DICT_INVENTORY), args[2], "normal"), true);
									}
								}

								// Per specific game mode
								GameMode gamemode = player.isOnline() ? ((Player) player).getGameMode() : GameMode.SURVIVAL;
								if(args.length > 3){
									GameMode temp = ASUtils.getGameMode(args[3]);
									if(temp != null){
										gamemode = temp;
									}else{
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_ASSUME, "Game Mode", args[3], gamemode.name().toLowerCase()), true);
									}
								}

								// World check
								World world = player.isOnline() ? ((Player) player).getWorld() : plugin.getServer().getWorlds().get(0);
								if(args.length > 4){
									World temp = Bukkit.getWorld(args[4]);
									if(temp == null){
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_ASSUME, Localization.getMessage(LocaleMessage.DICT_WORLD), args[4], world.getName()), true);
									}else{
										world = temp;
									}
								}

								// Load all inventories
								if(player.isOnline()){
									Player p = (Player) player;
									((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).savePlayer(p);
								}
								ASInventory chosen = null;
								List<ASInventory> inventories = ASInventory.generateInventory(player.getName(), isEnder ? InventoryType.ENDER : InventoryType.PLAYER);
								if(inventories != null){
									for(ASInventory inventory : inventories){
										if(inventory.getGameMode() == gamemode){
											if(inventory.getWorld().getName().equals(world.getName())){
												chosen = inventory;
												break;
											}
										}
									}
								}
								if(chosen == null){
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_INV_MISSING), true);
									return true;
								}

								// Create title
								String title = player.getName() + " | " + ASUtils.gamemodeAbbreviation(gamemode, false) + " | " + world.getName();

								// Create displayable inventory
								DisplayableInventory display = new DisplayableInventory(chosen, title);

								// Show inventory
								if(isEnder){
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + Localization.getMessage(LocaleMessage.MIRROR_WELCOME_ENDER, player.getName()), true);
									ASUtils.sendToPlayer(sender, Localization.getMessage(LocaleMessage.MIRROR_EDIT), true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + Localization.getMessage(LocaleMessage.MIRROR_WELCOME, player.getName()), true);
									ASUtils.sendToPlayer(sender, Localization.getMessage(LocaleMessage.MIRROR_EDIT), true);
								}
								((Player) sender).openInventory(display.getInventory()); // Creates the "live editing" window
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
						}
					}
					return true;
				}else if(args[0].equalsIgnoreCase("region")){
					if(plugin.getPermissions().has(sender, PermissionNodes.REGION_CREATE)){
						// Sanity Check
						if(sender instanceof Player){
							Player player = (Player) sender;
							if(args.length < 3){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as region <gamemode> <name>"), true);
							}else{
								String regionName = args[2];
								GameMode gamemode = ASUtils.getGameMode(args[1]);
								if(gamemode != null){
									if(!((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).isRegionNameTaken(regionName)){
										if(((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).isCuboidComplete(player.getName())){
											Cuboid cuboid = ((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).getCuboid(player.getName());
											((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).addRegion(cuboid, player.getName(), regionName, gamemode);
											ASUtils.sendToPlayer(sender, ChatColor.GREEN + Localization.getMessage(LocaleMessage.REGION_CREATED), true);
										}else{
											ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_NO_CUBOID_TOOL), true);
										}
									}else{
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_NAME_IN_USE), true);
									}
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, "Game Mode", args[1]), true);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("rmregion") || args[0].equalsIgnoreCase("removeregion")){
					if(plugin.getPermissions().has(sender, PermissionNodes.REGION_DELETE)){
						// Sanity check
						if(sender instanceof Player){
							// Remove region
							if(args.length == 1){
								Location location = ((Player) sender).getLocation();
								Region region = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(location);
								if(region != null){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).removeRegion(region.getName());
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.REGION_REMOVED), true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_REGION_STAND_MISSING), true);
								}
							}else{
								String regionName = args[1];
								Region region = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(regionName);
								if(region != null){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).removeRegion(region.getName());
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.REGION_REMOVED), true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_REGION_MISSING), true);
								}
							}
						}else{
							// Remove region
							if(args.length > 1){
								String regionName = args[1];
								Region region = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(regionName);
								if(region != null){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).removeRegion(region.getName());
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.REGION_REMOVED), true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_REGION_MISSING), true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as rmregion <name>"), true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("editregion")){
					if(plugin.getPermissions().has(sender, PermissionNodes.REGION_EDIT)){
						// Check validity of key
						boolean valid = false;
						if(args.length >= 3){
							if(RegionKey.isKey(args[2])){
								if(!RegionKey.requiresValue(RegionKey.getKey(args[2]))){
									valid = true; // we have at least 3 values in args[] and the key does not need a value
								}
							}
						}
						if(args.length >= 4){
							valid = true;
						}
						if(!valid){
							// Show help
							if(args.length >= 2){
								if(args[1].equalsIgnoreCase("help")){
									String key = Localization.getMessage(LocaleMessage.DICT_KEY).toLowerCase();
									String value = Localization.getMessage(LocaleMessage.DICT_VALUE).toLowerCase();
									key = key.substring(0, 1).toUpperCase() + key.substring(1);
									value = value.substring(0, 1).toUpperCase() + value.substring(1);
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "/as editregion <name> <key> <value>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "name " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "<any name>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "ShowEnterMessage " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "true/false", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "ShowExitMessage " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "true/false", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "EnterMessage " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "<enter message>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "ExitMessage " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "<exit message>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "inventory " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "'none'/'set'", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "gamemode " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "survival/creative", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + key + ": " + ChatColor.WHITE + "area " + ChatColor.AQUA + value + ": " + ChatColor.WHITE + "No Value", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as editregion <name> <key> <value>"), true);
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.EXTENDED_HELP, "/as editregion help"), true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as editregion <name> <key> <value>"), true);
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.EXTENDED_HELP, "/as editregion help"), true);
							}
						}else{
							// Setup
							String name = args[1];
							String key = args[2];
							String value = args.length > 3 ? args[3] : "";

							// Merge message
							if(args.length > 4){
								for(int i = 4; i < args.length; i++){ // Starts at args[4]
									value = value + args[i] + " ";
								}
								value = value.substring(0, value.length() - 1);
							}

							// Check region
							if(((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(name) == null){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_REGION_MISSING), true);
							}else{
								// Update region if needed
								if(RegionKey.isKey(key)){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).updateRegion(((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(name), RegionKey.getKey(key), value, sender);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, Localization.getMessage(LocaleMessage.DICT_KEY), key), true);
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.EXTENDED_HELP, "/as editregion help"), true);
								}
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("listregions")){
					if(plugin.getPermissions().has(sender, PermissionNodes.REGION_LIST)){
						// Sanity check on page number
						int page = 1;
						if(args.length >= 2){
							try{
								page = Integer.parseInt(args[1]);
							}catch(NumberFormatException e){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, Localization.getMessage(LocaleMessage.DICT_NUMBER), args[1]), true);
								return true;
							}
						}

						// Setup
						page = Math.abs(page);
						int resultsPerPage = 6; // Put as a variable for ease of changing
						Set<Region> set = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getAllRegions();
						List<Region> regions = new ArrayList<Region>();
						regions.addAll(set);

						// Check for empty list
						if(regions.size() <= 0){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NO_REGIONS), true);
							return true;
						}

						// Math
						Double maxPagesD = Math.ceil(regions.size() / resultsPerPage);
						if(maxPagesD < 1){
							maxPagesD = 1.0;
						}
						int maxPages = maxPagesD.intValue();
						if(maxPagesD < page){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_NO_PAGE, String.valueOf(page), String.valueOf(maxPages)), true);
							return true;
						}

						// Generate pages
						String pagenation = ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "AntiShare " + Localization.getMessage(LocaleMessage.DICT_REGIONS) + " " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + maxPages + ChatColor.DARK_GREEN + " ]=======";
						ASUtils.sendToPlayer(sender, pagenation, false);
						for(int i = (page - 1) * resultsPerPage; i < (resultsPerPage < regions.size() ? resultsPerPage * page : regions.size()); i++){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_AQUA + "#" + (i + 1) + " " + ChatColor.GOLD + regions.get(i).getName() + ChatColor.YELLOW + " Creator: " + ChatColor.AQUA + regions.get(i).getOwner() + ChatColor.YELLOW + " World: " + ChatColor.AQUA + regions.get(i).getWorldName(), false);
						}
						ASUtils.sendToPlayer(sender, pagenation, false);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("tool")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
						}else{
							// Setup
							Player player = (Player) sender;
							PlayerInventory inventory = player.getInventory();

							// Check inventory
							if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
								if(inventory.contains(AntiShare.ANTISHARE_TOOL)){
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.HAVE_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_TOOL.name())), true);
								}else{
									ASUtils.giveTool(AntiShare.ANTISHARE_TOOL, player);
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.GET_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_TOOL.name())), true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NEED_INV_SPACE, String.valueOf(1)), true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("settool")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
						}else{
							// Setup
							Player player = (Player) sender;
							PlayerInventory inventory = player.getInventory();

							// Check inventory
							if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
								if(inventory.contains(AntiShare.ANTISHARE_SET_TOOL)){
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.HAVE_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_SET_TOOL.name())), true);
								}else{
									ASUtils.giveTool(AntiShare.ANTISHARE_SET_TOOL, player);
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.GET_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_SET_TOOL.name())), true);
								}
								ASUtils.sendToPlayer(player, ChatColor.AQUA + Localization.getMessage(LocaleMessage.TOOL_SET, MaterialAPI.capitalize(AntiShare.ANTISHARE_SET_TOOL.name())), true);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NEED_INV_SPACE, String.valueOf(1)), true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("toolbox")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
						}else{
							// Setup
							Player player = (Player) sender;
							PlayerInventory inventory = player.getInventory();

							// Find clear spots
							int clearSpots = 0;
							for(ItemStack stack : inventory.getContents()){
								if(stack == null || stack.getType() == Material.AIR){
									clearSpots++;
								}
							}

							// Check inventory
							if(clearSpots >= 3){
								if(!inventory.contains(AntiShare.ANTISHARE_TOOL)){
									ASUtils.giveTool(AntiShare.ANTISHARE_TOOL, player, 1);
								}
								if(!inventory.contains(AntiShare.ANTISHARE_SET_TOOL)){
									ASUtils.giveTool(AntiShare.ANTISHARE_SET_TOOL, player, 2);
								}
								if(plugin.getPermissions().has(sender, PermissionNodes.CREATE_CUBOID)){
									if(!inventory.contains(AntiShare.ANTISHARE_CUBOID_TOOL)){
										ASUtils.giveTool(AntiShare.ANTISHARE_CUBOID_TOOL, player, 3);
									}
								}else{
									ASUtils.sendToPlayer(player, ChatColor.RED + Localization.getMessage(LocaleMessage.NO_CUBOID_TOOL), true);
								}
								ASUtils.sendToPlayer(player, ChatColor.YELLOW + Localization.getMessage(LocaleMessage.TOOL_GENERIC, MaterialAPI.capitalize(AntiShare.ANTISHARE_TOOL.name())), false);
								ASUtils.sendToPlayer(player, ChatColor.GOLD + Localization.getMessage(LocaleMessage.TOOL_SET, MaterialAPI.capitalize(AntiShare.ANTISHARE_SET_TOOL.name())), false);
								ASUtils.sendToPlayer(player, ChatColor.YELLOW + Localization.getMessage(LocaleMessage.TOOL_CUBOID, MaterialAPI.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name())), false);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NEED_INV_SPACE, String.valueOf(3)), true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("money")){
					if(args.length < 2){
						ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as money <on/off/status>"), true);
					}else{
						if(args[1].equalsIgnoreCase("status") || args[1].equalsIgnoreCase("state")){
							String state = !((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).isSilent(sender.getName()) ? ChatColor.GREEN + Localization.getMessage(LocaleMessage.DICT_GETTING) : ChatColor.RED + Localization.getMessage(LocaleMessage.DICT_NOT_GETTING);
							state = state + ChatColor.WHITE;
							ASUtils.sendToPlayer(sender, Localization.getMessage(LocaleMessage.FINE_REWARD_TOGGLE, state), true);
							return true;
						}
						if(ASUtils.getBoolean(args[1]) == null){
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as money <on/off/status>"), true);
							return true;
						}
						boolean silent = !ASUtils.getBoolean(args[1]);
						if(silent){
							((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).addToSilentList(sender.getName());
						}else{
							((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).removeFromSilentList(sender.getName());
						}
						String state = !silent ? ChatColor.GREEN + Localization.getMessage(LocaleMessage.DICT_GETTING) : ChatColor.RED + Localization.getMessage(LocaleMessage.DICT_NOT_GETTING);
						state = state + ChatColor.WHITE;
						ASUtils.sendToPlayer(sender, Localization.getMessage(LocaleMessage.FINE_REWARD, state), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("simplenotice") || args[0].equalsIgnoreCase("sn")){
					if(sender instanceof Player){
						Player player = (Player) sender;
						if(player.getListeningPluginChannels().contains("SimpleNotice")){
							if(plugin.isSimpleNoticeEnabled(player.getName())){
								plugin.disableSimpleNotice(player.getName());
								ASUtils.sendToPlayer(player, ChatColor.RED + Localization.getMessage(LocaleMessage.SN_OFF), false);
							}else{
								plugin.enableSimpleNotice(player.getName());
								ASUtils.sendToPlayer(player, ChatColor.GREEN + Localization.getMessage(LocaleMessage.SN_ON), false);
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SN_MISSING), false);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("check") || args[0].equalsIgnoreCase("gamemode") || args[0].equalsIgnoreCase("gm")){
					if(plugin.getPermissions().has(sender, PermissionNodes.CHECK)){
						GameMode gm = null;
						if(args.length > 1 && !args[1].equalsIgnoreCase("all")){
							gm = ASUtils.getGameMode(args[1]);
							if(gm == null){
								Player player = plugin.getServer().getPlayer(args[1]);
								if(player != null){
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " " + Localization.getMessage(LocaleMessage.DICT_IS_IN) + " " + ChatColor.GOLD + player.getGameMode().name(), false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.ERROR_UNKNOWN, "Game Mode", args[1]), true);
								}
								return true;
							}
						}
						if(gm == null){
							for(GameMode gamemode : GameMode.values()){
								if(ASUtils.findGameModePlayers(gamemode).size() > 0){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + gamemode.name() + ": " + ChatColor.YELLOW + ASUtils.commas(ASUtils.findGameModePlayers(gamemode)), false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + gamemode.name() + ": " + ChatColor.YELLOW + Localization.getMessage(LocaleMessage.DICT_NO_ONE), false);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.GOLD + gm.name() + ": " + ChatColor.YELLOW + ASUtils.commas(ASUtils.findGameModePlayers(gm)), false);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("tools")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_USE)){
						boolean on = false;
						if(plugin.isToolEnabled(sender.getName())){
							plugin.disableTools(sender.getName());
						}else{
							plugin.enableTools(sender.getName());
							on = true;
						}
						ASUtils.sendToPlayer(sender, on ? (ChatColor.GREEN + Localization.getMessage(LocaleMessage.TOOL_ENABLE)) : (ChatColor.RED + Localization.getMessage(LocaleMessage.TOOL_DISABLE)), true);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("cuboid")){
					if(!plugin.getPermissions().has(sender, PermissionNodes.CREATE_CUBOID)){
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + Localization.getMessage(LocaleMessage.NO_PERMISSION), true);
						return true;
					}
					if(args.length > 1){
						if(args[1].equalsIgnoreCase("clear")){
							if(((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).isCuboidComplete(sender.getName())){
								((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).removeCuboid(sender.getName());
								ASUtils.sendToPlayer(sender, ChatColor.GREEN + Localization.getMessage(LocaleMessage.CUBOID_REMOVED), true);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.CUBOID_MISSING), true);
							}
						}else if(args[1].equalsIgnoreCase("tool")){
							if(!(sender instanceof Player)){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NOT_A_PLAYER), true);
							}else{
								// Setup
								Player player = (Player) sender;
								PlayerInventory inventory = player.getInventory();

								// Check inventory
								if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
									if(inventory.contains(AntiShare.ANTISHARE_CUBOID_TOOL)){
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.HAVE_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name())), true);
									}else{
										ASUtils.giveTool(AntiShare.ANTISHARE_CUBOID_TOOL, player);
										ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.GET_TOOL, MaterialAPI.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name())), true);
									}
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.NEED_INV_SPACE, String.valueOf(1)), true);
								}
							}
						}else if(args[1].equalsIgnoreCase("status")){
							Cuboid cuboid = ((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).getCuboid(sender.getName());
							if(cuboid == null){
								ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.CUBOID_MISSING), false);
							}else{
								Location min = cuboid.getMinimumPoint();
								Location max = cuboid.getMaximumPoint();
								if(min != null){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "A: " + ChatColor.YELLOW + "(" + min.getBlockX() + ", " + min.getBlockY() + ", " + min.getBlockZ() + ", " + min.getWorld().getName() + ")", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "A: " + ChatColor.YELLOW + Localization.getMessage(LocaleMessage.DICT_NOT_SET), false);
								}
								if(max != null){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "B: " + ChatColor.YELLOW + "(" + max.getBlockX() + ", " + max.getBlockY() + ", " + max.getBlockZ() + ", " + max.getWorld().getName() + ")", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "B: " + ChatColor.YELLOW + Localization.getMessage(LocaleMessage.DICT_NOT_SET), false);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as cuboid <clear | tool | status>"), true);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.RED + Localization.getMessage(LocaleMessage.SYNTAX, "/as cuboid <clear | tool | status>"), true);
					}
					return true;
				}else{
					// This is for all extra commands, like /as help.
					// This is also for all "non-commands", like /as sakjdha
					return false; //Shows usage in plugin.yml
				}
			}
		}
		return false; //Shows usage in plugin.yml
	}
}
