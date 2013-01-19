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
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.cuboid.Cuboid;
import com.turt2live.antishare.inventory.ASInventory;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;
import com.turt2live.antishare.inventory.DisplayableInventory;
import com.turt2live.antishare.manager.CuboidManager;
import com.turt2live.antishare.manager.InventoryManager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.manager.RegionManager;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.regions.Region;
import com.turt2live.antishare.regions.RegionKey;
import com.turt2live.antishare.tekkitcompat.CommandBlockLayer;
import com.turt2live.antishare.tekkitcompat.ServerHas;
import com.turt2live.antishare.util.ASUtils;

/**
 * Command Handler
 * 
 * @author turt2live
 */
public class CommandHandler implements CommandExecutor {

	private AntiShare plugin = AntiShare.getInstance();

	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, String[] args){
		if(ServerHas.commandBlock()){
			if(CommandBlockLayer.isCommandBlock(sender)){
				return false;
			}
		}
		if(command.getName().equalsIgnoreCase("AntiShare")){
			if(args.length > 0){
				if(args[0].equalsIgnoreCase("version")){
					ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "Version: " + ChatColor.GOLD + plugin.getDescription().getVersion() + ChatColor.YELLOW + " Build: " + ChatColor.GOLD + plugin.getBuild(), false);
					return true;
				}else if(args[0].equalsIgnoreCase("reload") || args[0].equalsIgnoreCase("rl")){
					if(plugin.getPermissions().has(sender, PermissionNodes.RELOAD)){
						ASUtils.sendToPlayer(sender, "Reloading...", true);
						plugin.reload();
						ASUtils.sendToPlayer(sender, ChatColor.GREEN + "AntiShare Reloaded.", true);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("mirror")){
					// Sanity Check
					if(!(sender instanceof Player)){
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You are not a player, and therefore cannot view inventories. Sorry!", true);
					}else{
						if(plugin.getPermissions().has(sender, PermissionNodes.MIRROR)){
							if(!plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have inventories enabled." + ChatColor.RED + " You will not be able to view inventories without inventory tracking on.", true);
								return true;
							}
							if(args.length < 2){
								ASUtils.sendToPlayer(sender, ChatColor.RED + "No player name provided! Try /as mirror <player> [enderchest/normal] [gamemode] [world]", true);
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
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Player '" + playername + "' could not be found, sorry!", true);
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
										ASUtils.sendToPlayer(sender, ChatColor.RED + "I don't know what inventory '" + args[2] + "' is, so I assumed 'normal'.", true);
									}
								}

								// Per specific game mode
								GameMode gamemode = player.isOnline() ? ((Player) player).getGameMode() : GameMode.SURVIVAL;
								if(args.length > 3){
									GameMode temp = ASUtils.getGameMode(args[3]);
									if(temp != null){
										gamemode = temp;
									}else{
										ASUtils.sendToPlayer(sender, ChatColor.RED + "I don't know what Game Mode '" + args[3] + "' is, so I assumed the Game Mode (" + gamemode.name().toLowerCase() + ")", true);
									}
								}

								// World check
								World world = player.isOnline() ? ((Player) player).getWorld() : plugin.getServer().getWorlds().get(0);
								if(args.length > 4){
									World temp = Bukkit.getWorld(args[4]);
									if(temp == null){
										ASUtils.sendToPlayer(sender, ChatColor.RED + "Unknown world '" + args[4] + "' using " + world.getName(), true);
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
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Inventory not found! Maybe it's not created yet?", true);
									return true;
								}

								// Create title
								String title = player.getName() + " | " + ASUtils.gamemodeAbbreviation(gamemode, false) + " | " + world.getName();

								// Create displayable inventory
								DisplayableInventory display = new DisplayableInventory(chosen, title);

								// Show inventory
								if(isEnder){
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Welcome to " + player.getName() + "'s enderchest inventory.", true);
									ASUtils.sendToPlayer(sender, "You are able to edit their inventory as you please.", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Welcome to " + player.getName() + "'s inventory.", true);
									ASUtils.sendToPlayer(sender, "You are able to edit their inventory as you please.", true);
								}
								((Player) sender).openInventory(display.getInventory()); // Creates the "live editing" window
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
						}
					}
					return true;
				}else if(args[0].equalsIgnoreCase("region")){
					if(plugin.getPermissions().has(sender, PermissionNodes.REGION_CREATE)){
						// Sanity Check
						if(sender instanceof Player){
							Player player = (Player) sender;
							if(args.length < 3){
								ASUtils.sendToPlayer(sender, ChatColor.RED + "Not enough arguments! " + ChatColor.WHITE + "Try /as region <gamemode> <name>", true);
							}else{
								String regionName = args[2];
								GameMode gamemode = ASUtils.getGameMode(args[1]);
								if(gamemode != null){
									if(!((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).isRegionNameTaken(regionName)){
										if(((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).isCuboidComplete(player.getName())){
											Cuboid cuboid = ((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).getCuboid(player.getName());
											((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).addRegion(cuboid, player.getName(), regionName, gamemode);
											ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region created", true);
										}else{
											ASUtils.sendToPlayer(sender, ChatColor.RED + "You need to use the Cuboid tool to create a cuboid.", true);
										}
									}else{
										ASUtils.sendToPlayer(sender, ChatColor.RED + "Region name already in use!", true);
									}
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Unknown gamemode: " + args[1], true);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + "You must be a player to create regions.", true);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
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
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "No region where you are standing!", true);
								}
							}else{
								String regionName = args[1];
								Region region = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(regionName);
								if(region != null){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).removeRegion(region.getName());
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Region not found!", true);
								}
							}
						}else{
							// Remove region
							if(args.length > 1){
								String regionName = args[1];
								Region region = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(regionName);
								if(region != null){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).removeRegion(region.getName());
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Region removed", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Region not found!", true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + "You must supply a region name when removing regions from the console. " + ChatColor.WHITE + "Try: /as rmregion <name>", true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
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
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "/as editregion <name> <key> <value>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "name " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<any name>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowEnterMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ShowExitMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "true/false", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "EnterMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<enter message>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "ExitMessage " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "<exit message>", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "inventory " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "'none'/'set'", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "gamemode " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "survival/creative", false);
									ASUtils.sendToPlayer(sender, ChatColor.AQUA + "Key: " + ChatColor.WHITE + "area " + ChatColor.AQUA + "Value: " + ChatColor.WHITE + "No Value", false);
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'Show____Message'" + ChatColor.WHITE + " - True to show the message", false);
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'____Message'" + ChatColor.WHITE + " - Use {name} to input the region name.", false);
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'inventory'" + ChatColor.WHITE + " - Sets the region's inventory. 'none' to not have a default inventory, 'set' to mirror yours", false);
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + "'area'" + ChatColor.WHITE + " - Sets the area based on your WorldEdit selection", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "Incorrect syntax, try: /as editregion <name> <key> <value>", true);
									ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help", true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "Incorrect syntax, try: /as editregion <name> <key> <value>", true);
								ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help", true);
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
								ASUtils.sendToPlayer(sender, ChatColor.RED + "That region does not exist!", true);
							}else{
								// Update region if needed
								if(RegionKey.isKey(key)){
									((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).updateRegion(((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(name), RegionKey.getKey(key), value, sender);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "That is not a valid region key", true);
									ASUtils.sendToPlayer(sender, ChatColor.RED + "For keys and values type /as editregion help", true);
								}
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
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
								ASUtils.sendToPlayer(sender, ChatColor.RED + "'" + args[1] + "' is not a number!", true);
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
							ASUtils.sendToPlayer(sender, ChatColor.RED + "No regions to list!", true);
							return true;
						}

						// Math
						Double maxPagesD = Math.ceil(regions.size() / resultsPerPage);
						if(maxPagesD < 1){
							maxPagesD = 1.0;
						}
						int maxPages = maxPagesD.intValue();
						if(maxPagesD < page){
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Page " + page + " does not exist! The last page is " + maxPages, true);
							return true;
						}

						// Generate pages
						String pagenation = ChatColor.DARK_GREEN + "=======[ " + ChatColor.GREEN + "AntiShare Regions " + ChatColor.DARK_GREEN + "|" + ChatColor.GREEN + " Page " + page + "/" + maxPages + ChatColor.DARK_GREEN + " ]=======";
						ASUtils.sendToPlayer(sender, pagenation, false);
						for(int i = ((page - 1) * resultsPerPage); i < (resultsPerPage < regions.size() ? (resultsPerPage * page) : regions.size()); i++){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_AQUA + "#" + (i + 1) + " " + ChatColor.GOLD + regions.get(i).getName()
									+ ChatColor.YELLOW + " Creator: " + ChatColor.AQUA + regions.get(i).getOwner()
									+ ChatColor.YELLOW + " World: " + ChatColor.AQUA + regions.get(i).getWorldName(), false);
						}
						ASUtils.sendToPlayer(sender, pagenation, false);
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("tool")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You must be a player to use the tool!", true);
						}else{
							// Setup
							Player player = (Player) sender;
							PlayerInventory inventory = player.getInventory();

							// Check inventory
							if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
								if(inventory.contains(AntiShare.ANTISHARE_TOOL)){
									ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_TOOL.name()) + ")", true);
								}else{
									ASUtils.giveTool(AntiShare.ANTISHARE_TOOL, player);
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_TOOL.name()) + ")", true);
								}
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + "You must have at least 1 free spot in your inventory!", true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("settool")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You must be a player to use the set block tool!", true);
						}else{
							// Setup
							Player player = (Player) sender;
							PlayerInventory inventory = player.getInventory();

							// Check inventory
							if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
								if(inventory.contains(AntiShare.ANTISHARE_SET_TOOL)){
									ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the set block tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_SET_TOOL.name()) + ")", true);
								}else{
									ASUtils.giveTool(AntiShare.ANTISHARE_SET_TOOL, player);
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the set block tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_SET_TOOL.name()) + ")", true);
								}
								ASUtils.sendToPlayer(player, ChatColor.AQUA + "" + ChatColor.ITALIC + "LEFT" + ChatColor.RESET + ChatColor.AQUA + " click to set the block type", true);
								ASUtils.sendToPlayer(player, ChatColor.AQUA + "" + ChatColor.ITALIC + "RIGHT" + ChatColor.RESET + ChatColor.AQUA + " click to remove the block type", true);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + "You must have at least 1 free spot in your inventory!", true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("toolbox")){
					if(plugin.getPermissions().has(sender, PermissionNodes.TOOL_GET)){
						// Sanity check
						if(!(sender instanceof Player)){
							ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You must be a player to use the toolbox!", true);
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
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_TOOL.name()) + ")", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_TOOL.name()) + ")", true);
								}
								if(!inventory.contains(AntiShare.ANTISHARE_SET_TOOL)){
									ASUtils.giveTool(AntiShare.ANTISHARE_SET_TOOL, player, 2);
									ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the set block tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_SET_TOOL.name()) + ")", true);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the set block tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_SET_TOOL.name()) + ")", true);
								}
								if(plugin.getPermissions().has(sender, PermissionNodes.CREATE_CUBOID)){
									if(!inventory.contains(AntiShare.ANTISHARE_CUBOID_TOOL)){
										ASUtils.giveTool(AntiShare.ANTISHARE_CUBOID_TOOL, player, 3);
										ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the cuboid tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name()) + ")", true);
									}else{
										ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the cuboid tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name()) + ")", true);
									}
								}else{
									ASUtils.sendToPlayer(player, ChatColor.RED + "You are missing the cuboid tool: You do not have permission.", true);
								}
								ASUtils.sendToPlayer(player, ChatColor.YELLOW + "With the " + ASUtils.capitalize(AntiShare.ANTISHARE_TOOL.name()) + " simply left or right click a block to see what type it is.", false);
								ASUtils.sendToPlayer(player, ChatColor.GOLD + "With the " + ASUtils.capitalize(AntiShare.ANTISHARE_SET_TOOL.name()) + " simply left click to set the block type, and right click to remove the type.", false);
								ASUtils.sendToPlayer(player, ChatColor.YELLOW + "With the " + ASUtils.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name()) + " simply left click to set point 1 and right click to set point 2.", false);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + "You must have at least 3 free spots in your inventory!", true);
							}
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("money")){
					if(args.length < 2){
						ASUtils.sendToPlayer(sender, ChatColor.RED + "Syntax Error, try /as money on/off/status", true);
					}else{
						if(args[1].equalsIgnoreCase("status") || args[1].equalsIgnoreCase("state")){
							String state = !((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).isSilent(sender.getName()) ? ChatColor.GREEN + "getting" : ChatColor.RED + "not getting";
							state = state + ChatColor.WHITE;
							ASUtils.sendToPlayer(sender, "You are " + state + " fine/reward messages", true);
							return true;
						}
						if(ASUtils.getBoolean(args[1]) == null){
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Syntax Error, try /as money on/off/status", true);
							return true;
						}
						boolean silent = !ASUtils.getBoolean(args[1]);
						if(silent){
							((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).addToSilentList(sender.getName());
						}else{
							((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).removeFromSilentList(sender.getName());
						}
						String message = "You are now " + (silent ? ChatColor.RED + "not getting" : ChatColor.GREEN + "getting") + ChatColor.WHITE + " fine/reward messages";
						ASUtils.sendToPlayer(sender, message, true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("simplenotice") || args[0].equalsIgnoreCase("sn")){
					if(sender instanceof Player){
						Player player = (Player) sender;
						if(player.getListeningPluginChannels().contains("SimpleNotice")){
							if(plugin.isSimpleNoticeEnabled(player.getName())){
								plugin.disableSimpleNotice(player.getName());
								ASUtils.sendToPlayer(player, ChatColor.RED + "SimpleNotice is now NOT being used to send you messages", false);
							}else{
								plugin.enableSimpleNotice(player.getName());
								ASUtils.sendToPlayer(player, ChatColor.GREEN + "SimpleNotice is now being used to send you messages", false);
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + "You do not have SimpleNotice", false);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.RED + "You are not a player and do not have SimpleNotice", true);
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
									ASUtils.sendToPlayer(sender, ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " is in " + ChatColor.GOLD + player.getGameMode().name(), false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "Unknown Game Mode!", true);
								}
								return true;
							}
						}
						if(gm == null){
							for(GameMode gamemode : GameMode.values()){
								if(ASUtils.findGameModePlayers(gamemode).size() > 0){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + gamemode.name() + ": " + ChatColor.YELLOW + ASUtils.commas(ASUtils.findGameModePlayers(gamemode)), false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + gamemode.name() + ": " + ChatColor.YELLOW + "no one", false);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.GOLD + gm.name() + ": " + ChatColor.YELLOW + ASUtils.commas(ASUtils.findGameModePlayers(gm)), false);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
					}
					return true;
				}else if(args[0].equalsIgnoreCase("cuboid")){
					if(!plugin.getPermissions().has(sender, PermissionNodes.CREATE_CUBOID)){
						ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You do not have permission!", true);
						return true;
					}
					if(args.length > 1){
						if(args[1].equalsIgnoreCase("clear")){
							if(((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).isCuboidComplete(sender.getName())){
								((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).removeCuboid(sender.getName());
								ASUtils.sendToPlayer(sender, ChatColor.GREEN + "Your cuboid save was removed.", true);
							}else{
								ASUtils.sendToPlayer(sender, ChatColor.RED + "You have no saved cuboid!", true);
							}
						}else if(args[1].equalsIgnoreCase("tool")){
							if(!(sender instanceof Player)){
								ASUtils.sendToPlayer(sender, ChatColor.DARK_RED + "You must be a player to use the tool!", true);
							}else{
								// Setup
								Player player = (Player) sender;
								PlayerInventory inventory = player.getInventory();

								// Check inventory
								if(inventory.firstEmpty() != -1 && inventory.firstEmpty() <= inventory.getSize()){
									if(inventory.contains(AntiShare.ANTISHARE_CUBOID_TOOL)){
										ASUtils.sendToPlayer(sender, ChatColor.RED + "You already have the cuboid tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name()) + ")", true);
									}else{
										ASUtils.giveTool(AntiShare.ANTISHARE_CUBOID_TOOL, player);
										ASUtils.sendToPlayer(sender, ChatColor.GREEN + "You now have the cuboid tool! (" + ASUtils.capitalize(AntiShare.ANTISHARE_CUBOID_TOOL.name()) + ")", true);
									}
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.RED + "You must have at least 1 free spot in your inventory!", true);
								}
							}
						}else if(args[1].equalsIgnoreCase("status")){
							Cuboid cuboid = ((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).getCuboid(sender.getName());
							if(cuboid == null){
								ASUtils.sendToPlayer(sender, ChatColor.RED + "No saved cuboid", false);
							}else{
								Location min = cuboid.getMinimumPoint();
								Location max = cuboid.getMaximumPoint();
								if(min != null){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "Point A: " + ChatColor.YELLOW + "("
											+ min.getBlockX() + ", "
											+ min.getBlockY() + ", "
											+ min.getBlockZ() + ", "
											+ min.getWorld().getName()
											+ ")", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "Point A: " + ChatColor.YELLOW + "Not set", false);
								}
								if(max != null){
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "Point B: " + ChatColor.YELLOW + "("
											+ max.getBlockX() + ", "
											+ max.getBlockY() + ", "
											+ max.getBlockZ() + ", "
											+ max.getWorld().getName()
											+ ")", false);
								}else{
									ASUtils.sendToPlayer(sender, ChatColor.GOLD + "Point B: " + ChatColor.YELLOW + "Not set", false);
								}
							}
						}else{
							ASUtils.sendToPlayer(sender, ChatColor.RED + "Unknown argument. Try /as cuboid <clear | tool | status>", true);
						}
					}else{
						ASUtils.sendToPlayer(sender, ChatColor.RED + "Unknown argument. Try /as cuboid <clear | tool | status>", true);
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
