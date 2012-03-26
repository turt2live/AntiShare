package com.turt2live.antishare.api;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.enums.BlockedType;
import com.turt2live.antishare.storage.VirtualInventory;

public class SettingsAPI extends APIBase {

	/**
	 * Determines if an inventory swap will occur when switching Game Modes in a world
	 * 
	 * @param world the world to test
	 * @return true if players will find their inventory changed in a world
	 */
	public boolean isSwapInventoriesOn(World world){
		return getPlugin().config().getBoolean("other.inventory_swap", world);
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
			return getPlugin().storage.getInventoryManager(player, world).getCreativeInventory();
		}else if(gamemode.equals(GameMode.SURVIVAL)){
			return getPlugin().storage.getInventoryManager(player, world).getSurvivalInventory();
		}
		return null;
	}

	/**
	 * Gets the inventory of a player in HashMap form
	 * 
	 * @param player the player to get the inventory from
	 * @return the inventory
	 */
	public HashMap<Integer, ItemStack> getInventory(Player player){
		return VirtualInventory.getInventoryFromPlayer(player);
	}

	/**
	 * Forces a block into the creative mode block registry
	 * 
	 * @param block the block to add
	 */
	public void addBlockToCreativeRegistry(Block block){
		getPlugin().storage.saveCreativeBlock(block, BlockedType.CREATIVE_BLOCK_PLACE, block.getWorld());
	}

	/**
	 * Force remove a block from the creative-mode registry. Based on the block's world
	 * 
	 * @param block the block, with world, to remove
	 */
	public void removeBlockFromCreativeRegistry(Block block){
		getPlugin().storage.saveCreativeBlock(block, BlockedType.CREATIVE_BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Determines if a block is a creative block. Based on the block's world
	 * 
	 * @param block the block, with world, to test
	 * @return true if the specified block is a 'creative mode' block
	 */
	public boolean isCreativeBlock(Block block){
		return getPlugin().storage.isCreativeBlock(block, BlockedType.CREATIVE_BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Forces a block into the survival mode block registry
	 * 
	 * @param block the block to add
	 */
	public void addBlockToSurivalRegistry(Block block){
		getPlugin().storage.saveSurvivalBlock(block, BlockedType.SURVIVAL_BLOCK_PLACE, block.getWorld());
	}

	/**
	 * Force remove a block from the survival-mode registry. Based on the block's world
	 * 
	 * @param block the block, with world, to remove
	 */
	public void removeBlockFromSurvivalRegistry(Block block){
		getPlugin().storage.saveSurvivalBlock(block, BlockedType.SURVIVAL_BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Determines if a block is a survival block. Based on the block's world
	 * 
	 * @param block the block, with world, to test
	 * @return true if the specified block is a 'survival mode' block
	 */
	public boolean isSurvivalBlock(Block block){
		return getPlugin().storage.isSurvivalBlock(block, BlockedType.SURVIVAL_BLOCK_BREAK, block.getWorld());
	}

	/**
	 * Checks to see if the SQL Manager is connected
	 * 
	 * @return true if a connection is active
	 */
	public boolean isSQLConnected(){
		if(getPlugin().getSQLManager() == null){
			return false;
		}
		return getPlugin().getSQLManager().isConnected();
	}

	/**
	 * Checks to see if the configuration allows SQL
	 * 
	 * @return true if SQL is set to 'true' in the configuration
	 */
	public boolean isSQLEnabled(){
		return getPlugin().getConfig().getBoolean("SQL.use");
	}

	/**
	 * Reconnects the SQL Manager
	 * 
	 * @return true if the reconnect worked
	 */
	public boolean reconnectToSQL(){
		getPlugin().getSQLManager().disconnect();
		return getPlugin().getSQLManager().attemptConnectFromConfig();
	}

	/**
	 * Reloads the plugin, saving everything to disk/to the database
	 */
	public void reloadPlugin(){
		reloadPlugin(Bukkit.getConsoleSender());
	}

	/**
	 * Reloads the plugin, saving everything to disk/to the database. This alerts a CommandSender of status.
	 * 
	 * @param sender the CommandSender to be alerted, regardless of permissions
	 */
	public void reloadPlugin(CommandSender sender){
		getPlugin().getServer().dispatchCommand(sender, "as rl");
	}
}
