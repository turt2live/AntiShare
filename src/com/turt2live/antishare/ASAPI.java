package com.turt2live.antishare;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.getspout.commons.GameMode;

import com.turt2live.antishare.SQL.SQLManager;
import com.turt2live.antishare.storage.VirtualStorage;

/**
 * AntiShare API
 * 
 * @author travpc@gmail.com
 * 
 */
public class ASAPI {

	private AntiShare plugin;

	/**
	 * Creates a new instance of the API
	 */
	public ASAPI(){
		plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
	}

	/**
	 * Forces a block into the creative mode block registry
	 * 
	 * @param block the block to add
	 */
	public void addBlockToCreativeRegistry(Block block){
		ASBlockRegistry.saveCreativeBlock(block);
	}

	/**
	 * Determines if a block can be broken. This method uses the block's world
	 * 
	 * @param block the block, with a world, to check
	 * @return true if the block can be broken
	 */
	public boolean canBreakBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Determines if a block can be broken. This method uses the block's world
	 * 
	 * @param block the block, with world, to check
	 * @param player the player who woul be breaking the block
	 * @return true if the player can break the block
	 */
	public boolean canBreakBlock(Block block, Player player){
		if(isOnlyIfCreativeOn(block.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.break")){
			return true;
		}
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Determines if a block can be broken in a specified world
	 * 
	 * @param block the block to test
	 * @param player the player breaking the block
	 * @param world the world the block would be broken in
	 * @return true if the player can break that block in the world specified
	 */
	public boolean canBreakBlock(Block block, Player player, World world){
		if(isOnlyIfCreativeOn(block.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.break")){
			return true;
		}
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, world);
	}

	/**
	 * Determines if a block can be broken in a specified world
	 * 
	 * @param block the block to be broken
	 * @param world the world to test in
	 * @return true if the block can be broken in the world
	 */
	public boolean canBreakBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, world);
	}

	/**
	 * Interaction, determines if a block can be left or right clicked
	 * 
	 * @param block the block, with world, to test
	 * @return true if the block can be interacted with
	 */
	public boolean canClickBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.INTERACT, block.getWorld());
	}

	/**
	 * Determines if a player can interact with a block
	 * 
	 * @param block the block, with world, to test
	 * @param player the player beating the block
	 * @return true if the player can interact with the block
	 */
	public boolean canClickBlock(Block block, Player player){
		if(isOnlyIfCreativeOn(block.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.interact")){
			return true;
		}
		return !plugin.storage.isBlocked(block.getType(), BlockedType.INTERACT, block.getWorld());
	}

	/**
	 * Determines if a block can be interacted with in a world
	 * 
	 * @param block the block to test
	 * @param world the world to test in
	 * @return true if the block can be interacted with in the world
	 */
	public boolean canClickBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.INTERACT, world);
	}

	/**
	 * Determines if a player can die with a specific item in their inventory. Based on the player's world
	 * 
	 * @param item the item to die with
	 * @param player the player dying
	 * @return true if the item would be dropped on death
	 */
	public boolean canDieWithItem(ItemStack item, Player player){
		if(isOnlyIfCreativeOn(player.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.death")){
			return true;
		}
		return !plugin.storage.isBlocked(item, BlockedType.DEATH, player.getWorld());
	}

	/**
	 * Determines if a player can die with an item in a specific world
	 * 
	 * @param item the item to die with
	 * @param player the player dying
	 * @param world the world to test in
	 * @return true if the item would be dropped on death if the player died in the world specified
	 */
	public boolean canDieWithItem(ItemStack item, Player player, World world){
		if(isOnlyIfCreativeOn(world)
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.death")){
			return true;
		}
		return !plugin.storage.isBlocked(item, BlockedType.DEATH, world);
	}

	/**
	 * Determines if an item would be dropped on death in a world
	 * 
	 * @param item the item to be killed with
	 * @param world the world to test in
	 * @return true if the item would be dropped off a player in the world
	 */
	public boolean canDieWithItem(ItemStack item, World world){
		return !plugin.storage.isBlocked(item, BlockedType.DEATH, world);
	}

	/**
	 * Determines if a block can be placed. Based off the block's world
	 * 
	 * @param block the block, with world, to test
	 * @return true if the block can be placed
	 */
	public boolean canPlaceBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, block.getWorld());
	}

	/**
	 * Determines if a player can place a block. Based off the block's world
	 * 
	 * @param block the block, with world, to test
	 * @param player the player who would place this block
	 * @return true if the player can place the block
	 */
	public boolean canPlaceBlock(Block block, Player player){
		if(isOnlyIfCreativeOn(block.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.place")){
			return true;
		}
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, block.getWorld());
	}

	/**
	 * Determines if a player can place a block in a specified world
	 * 
	 * @param block the block to test
	 * @param player the player to test
	 * @param world the world to test in
	 * @return true if the block can be placed by the player in the world specified
	 */
	public boolean canPlaceBlock(Block block, Player player, World world){
		if(isOnlyIfCreativeOn(block.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.place")){
			return true;
		}
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, world);
	}

	/**
	 * Determines if a block can be placed in a specific world
	 * 
	 * @param block the block to test
	 * @param world the world to test in
	 * @return true if the block can be placed in the world
	 */
	public boolean canPlaceBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, world);
	}

	/**
	 * Determines if the player can hit other players. Based off the player's world
	 * 
	 * @param player the player, with world, to test
	 * @return true if the player can hit other players
	 */
	public boolean canPVP(Player player){
		if(player.hasPermission("AntiShare.pvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(player.getWorld()) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp", player.getWorld());
	}

	/**
	 * Determines if a player can hit other players in a specific world
	 * 
	 * @param player the player to test
	 * @param world the world to test in
	 * @return true if the player can hit other players in the specified world
	 */
	public boolean canPVP(Player player, World world){
		if(player.hasPermission("AntiShare.pvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp", world);
	}

	/**
	 * Determines if players can hit other players in the specified world
	 * 
	 * @param world the world to test
	 * @return true if PVP is allowed in the specified world
	 */
	public boolean canPVP(World world){
		return plugin.config().getBoolean("other.pvp", world);
	}

	/**
	 * Determines if the player can hit mobs. Based on the player's world
	 * 
	 * @param player the player, with world, to test
	 * @return true if the player can hit mobs
	 */
	public boolean canPVPAgainstMobs(Player player){
		if(player.hasPermission("AntiShare.mobpvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(player.getWorld()) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp-mobs", player.getWorld());
	}

	/**
	 * Determines if a player can hit mobs in a specific world
	 * 
	 * @param player the player to test
	 * @param world the world to test in
	 * @return true if the player can hit mobs in the specified world
	 */
	public boolean canPVPAgainstMobs(Player player, World world){
		if(player.hasPermission("AntiShare.mobpvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp-mobs", world);
	}

	/**
	 * Determines if PVP against mobs is allowed in a world
	 * 
	 * @param world the world to test
	 * @return true if players can hit mobs in the specified world
	 */
	public boolean canPVPAgainstMobs(World world){
		return plugin.config().getBoolean("other.pvp-mobs", world);
	}

	/**
	 * Determines if a player can throw an item. Based off the player's worl
	 * 
	 * @param item the item to be thrown
	 * @param player the player, with world, throwing
	 * @return true if the item can be thrown by the player
	 */
	public boolean canThrowItem(ItemStack item, Player player){
		if(isOnlyIfCreativeOn(player.getWorld())
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.drop")){
			return true;
		}
		return !plugin.storage.isBlocked(item, BlockedType.DROP_ITEM, player.getWorld());
	}

	/**
	 * Determines if a player can throw an item in a specific world
	 * 
	 * @param item the item to be tested
	 * @param player the player to be throwing
	 * @param world the world to be tested in
	 * @return true if the item can be thrown by the player in the specified world
	 */
	public boolean canThrowItem(ItemStack item, Player player, World world){
		if(isOnlyIfCreativeOn(world)
				&& !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.allow.drop")){
			return true;
		}
		return !plugin.storage.isBlocked(item, BlockedType.DROP_ITEM, world);
	}

	/**
	 * Determines if an item can be thrown in a world
	 * 
	 * @param item the item to test
	 * @param world the world to test in
	 * @return true if the item can be thrown in the specified world
	 */
	public boolean canThrowItem(ItemStack item, World world){
		return !plugin.storage.isBlocked(item, BlockedType.DROP_ITEM, world);
	}

	/**
	 * Determines if a player can travel to a world
	 * 
	 * @param player the player travelling
	 * @param world the world to travel to
	 * @return true if the player can travel to the world
	 */
	public boolean canTransferToWorld(Player player, World world){
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.worlds")){
			return true;
		}
		return plugin.config().getBoolean("other.worldTransfer", world);
	}

	/**
	 * Determines if transfers to a world are allowed
	 * 
	 * @param world the world to test
	 * @return true if transfers are allowed to the world
	 */
	public boolean canTransferToWorld(World world){
		return plugin.config().getBoolean("other.worldTransfer", world);
	}

	/**
	 * Determines if a player can place/break bedrock. Based off the player's world
	 * 
	 * @param player the player, with world, to test
	 * @return true if the player is allowed to place/break bedrock
	 */
	public boolean canUseBedrock(Player player){
		if(plugin.config().getBoolean("other.allow_bedrock", player.getWorld())){
			return true;
		}
		if(player.hasPermission("AntiShare.bedrock")){
			return true;
		}
		return false;
	}

	/**
	 * Determines if a player is allowed to place/break bedrock in a specific world
	 * 
	 * @param player the player with the bedrock
	 * @param world the world to test in
	 * @return true if the player can place bedrock in the specified world
	 */
	public boolean canUseBedrock(Player player, World world){
		if(plugin.config().getBoolean("other.allow_bedrock", world)){
			return true;
		}
		if(player.hasPermission("AntiShare.bedrock")){
			return true;
		}
		return false;
	}

	/**
	 * Determines if a player can use a command in their current world
	 * 
	 * @param command the command to test (the '/' is not required)
	 * @param player the player, with world, to test against
	 * @return true if the player can use the command in their current world
	 */
	public boolean canUseCommand(String command, Player player){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(player.hasPermission("AntiShare.allow.commands")){
			return true;
		}
		return !plugin.storage.commandBlocked(command, player.getWorld());
	}

	/**
	 * Determines if a player can use a command in a specific world
	 * 
	 * @param command the command to test (the '/' is not required)
	 * @param player the player to test
	 * @param world the world to test in
	 * @return true if the player can use the command in the specified world
	 */
	public boolean canUseCommand(String command, Player player, World world){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(player.hasPermission("AntiShare.allow.commands")){
			return true;
		}
		return !plugin.storage.commandBlocked(command, world);
	}

	/**
	 * Determines if a command can be used in a specific world
	 * 
	 * @param command the command to test (the '/' is not required)
	 * @param world the world to test in
	 * @return true if the command can be used in the world
	 */
	public boolean canUseCommand(String command, World world){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		return !plugin.storage.commandBlocked(command, world);
	}

	/**
	 * Determines if a player can use the monster eggs. Based on the player's world
	 * 
	 * @param player the player, with world, to test
	 * @return true if the player can use monster eggs in their current world
	 */
	public boolean canUseEgg(Player player){
		if(player.hasPermission("AntiShare.allow.eggs")){
			return true;
		}
		if(plugin.config().getBoolean("other.allow_eggs", player.getWorld()) == false){
			if(isOnlyIfCreativeOn(player.getWorld())){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines if a player can use the monster eggs in a specific world
	 * 
	 * @param player the player to test
	 * @param world the world to test in
	 * @return true if the player is allowed to use monster eggs in the specified world
	 */
	public boolean canUseEgg(Player player, World world){
		if(player.hasPermission("AntiShare.allow.eggs")){
			return true;
		}
		if(plugin.config().getBoolean("other.allow_eggs", world) == false){
			if(isOnlyIfCreativeOn(world)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					return false;
				}else{
					return true;
				}
			}else{
				return false;
			}
		}
		return true;
	}

	/**
	 * The AntiShare plugin
	 * 
	 * @return the AntiShare instance used within the server
	 */
	public AntiShare getPlugin(){
		return plugin;
	}

	/**
	 * Gets a saved inventory for a player (from the virtual storage)
	 * 
	 * @param player the player to get from
	 * @param world the world to use
	 * @param gamemode the gamemode to get
	 * @return a HashMap of the player's saved inventory. (HashMap's key is 'slot', value is the item)
	 */
	public HashMap<Integer, ItemStack> getSavedInventory(Player player, World world, GameMode gamemode){
		if(gamemode.equals(GameMode.CREATIVE)){
			return plugin.storage.getInventoryManager(player, world).getCreativeInventory();
		}else if(gamemode.equals(GameMode.SURVIVAL)){
			return plugin.storage.getInventoryManager(player, world).getSurvivalInventory();
		}
		return null;
	}

	/**
	 * Gets the SQL Manager being used
	 * 
	 * @return the SQL Manager instance
	 */
	public SQLManager getSQLManager(){
		return plugin.getSQLManager();
	}

	/**
	 * Gets the virtual storage system
	 * 
	 * @return the virtual storage
	 */
	public VirtualStorage getStorage(){
		return plugin.storage;
	}

	/**
	 * Determines if a block is a creative block. Based on the block's world
	 * 
	 * @param block the block, with world, to test
	 * @return true if the specified block is a 'creative mode' block
	 */
	public boolean isCreativeBlock(Block block){
		return plugin.storage.isCreativeBlock(block, BlockedType.CREATIVE_BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Determines if 'only_if_creative' is on in a specific world
	 * 
	 * @param world the world to test
	 * @return true if creative-mode only is active in the world
	 */
	public boolean isOnlyIfCreativeOn(World world){
		return plugin.config().getBoolean("other.only_if_creative", world);
	}

	/**
	 * Determines if a player is subject to "only if creative" mode
	 * 
	 * @param player the player to test
	 * @return true if the player has to be in creative to be blocked an action, false for denial regardless of game mode
	 */
	public boolean isOnlyIfCreativeOn(Player player){
		return plugin.config().onlyIfCreative(player);
	}

	/**
	 * Checks to see if the SQL Manager is connected
	 * 
	 * @return true if a connection is active
	 */
	public boolean isSQLConnected(){
		if(plugin.getSQLManager() == null){
			return false;
		}
		return plugin.getSQLManager().isConnected();
	}

	/**
	 * Checks to see if the configuration allows SQL
	 * 
	 * @return true if SQL is set to 'true' in the configuration
	 */
	public boolean isSQLEnabled(){
		return plugin.getConfig().getBoolean("SQL.use");
	}

	/**
	 * Determines if an inventory swap will occur when switching Game Modes in a world
	 * 
	 * @param world the world to test
	 * @return true if players will find their inventory changed in a world
	 */
	public boolean isSwapInventoriesOn(World world){
		return plugin.config().getBoolean("other.inventory_swap", world);
	}

	/**
	 * Reconnects the SQL Manager
	 * 
	 * @return true if the reconnect worked
	 */
	public boolean reconnectToSQL(){
		plugin.getSQLManager().disconnect();
		return plugin.getSQLManager().attemptConnectFromConfig();
	}

	/**
	 * Reloads the plugin, saving everything to disk/to the database
	 */
	public void reloadPlugin(){
		plugin.reloadConfig();
		new Thread(new Runnable(){
			@Override
			public void run(){
				ASMultiWorld.detectWorlds(plugin);
			}
		});
		plugin.storage.reload();
	}

	/**
	 * Reloads the plugin, saving everything to disk/to the database. This alerts a CommandSender of status and does <b>NOT</b> check permissions
	 * 
	 * @param sender the CommandSender to be alerted, regardless of permissions
	 */
	public void reloadPlugin(CommandSender sender){
		plugin.reloadConfig();
		AntiShare.log.info("AntiShare Reloaded.");
		if(sender instanceof Player){
			ASUtils.sendToPlayer(sender, ChatColor.GREEN + "AntiShare Reloaded.");
		}
		new Thread(new Runnable(){
			@Override
			public void run(){
				ASMultiWorld.detectWorlds(plugin);
			}
		});
		plugin.storage.reload(sender);
	}

	/**
	 * Force remove a block from the creative-mode registry. Based on the block's world
	 * 
	 * @param block the block, with world, to remove
	 */
	public void removeBlockFromCreativeRegistry(Block block){
		ASBlockRegistry.unregisterCreativeBlock(block);
	}

	/**
	 * Sends a notification through the AntiShare notification system
	 * 
	 * @param type the notification type
	 * @param player the player who is involved
	 * @param variable the variable. This is the red or green portion at the end of the notification
	 */
	public void sendNotification(NotificationType type, Player player, String variable){
		ASNotification.sendNotification(type, player, variable);
	}
}
