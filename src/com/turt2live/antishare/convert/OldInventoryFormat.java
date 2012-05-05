package com.turt2live.antishare.convert;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;

/**
 * Inventory format for prior to 3.2.X.<br>
 * This converts from the 3.1.3 (and prior) format to the
 * 3.2.0a format (the format used in development). Although this
 * format is VERY similar to the 3.2.0b format, it does not match
 * the storage requirements outlined in the new Inventory Manager.<br>
 * <br>
 * This class was directly imported from the 3.2.0a version and modified to
 * meet the new code introduced in 3.2.0b.
 * 
 * @author turt2live
 */
public class OldInventoryFormat {

	// Versions prior to 3.2.0 use this format

	private AntiShare plugin;
	private File saveFile;
	private String playername;
	private World world;
	private GameMode gamemode;

	/**
	 * Creates a general inventory format
	 */
	public OldInventoryFormat(){
		this.plugin = AntiShare.getInstance();
	}

	/**
	 * Creates a general inventory format
	 * 
	 * @param file the inventory file
	 */
	public OldInventoryFormat(File file){
		this.plugin = AntiShare.getInstance();
		this.saveFile = file;
	}

	/**
	 * Checks for validity of the file (name only)
	 * 
	 * @return true if valid
	 */
	public boolean isValid(){
		return saveFile.getName().replace(".yml", "").split("_").length >= 3;
	}

	/**
	 * Splits the file, preparing the parts it needs
	 */
	public void split(){
		String[] parts = saveFile.getName().replace(".yml", "").split("_");
		String worldname = parts[2];
		if(parts.length > 3){
			StringBuilder world = new StringBuilder(worldname);
			for(int i = 3; i < parts.length; i++){
				world.append("_" + parts[i]);
			}
			worldname = world.toString();
		}
		playername = parts[0];
		world = plugin.getServer().getWorld(worldname);
		gamemode = GameMode.valueOf(parts[1]);
	}

	/**
	 * Gets the player name
	 * 
	 * @return the player name
	 */
	public String getPlayerName(){
		return playername;
	}

	/**
	 * Gets the world
	 * 
	 * @return the world
	 */
	public World getWorld(){
		return world;
	}

	/**
	 * Gets the Game Mode
	 * 
	 * @return the Game Mode
	 */
	public GameMode getGameMode(){
		return gamemode;
	}

	/**
	 * Gets a 3.2.0a inventory
	 * 
	 * @param playername the playername
	 * @param world the world
	 * @param gamemode the Game Mode
	 * @return the 3.2.0a inventory
	 */
	public ConcurrentHashMap<Integer, ItemStack> getInventory(String playername, World world, GameMode gamemode){
		File sdir = new File(plugin.getDataFolder(), "inventories");
		sdir.mkdirs();
		File saveFile = new File(sdir, playername + "_" + gamemode.toString() + "_" + world.getName() + ".yml");
		if(!saveFile.exists()){
			try{
				saveFile.createNewFile();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		this.saveFile = saveFile;
		return getInventory();
	}

	/**
	 * Gets the 3.2.0a inventory
	 * 
	 * @return the 3.2.0a inventory
	 */
	public ConcurrentHashMap<Integer, ItemStack> getInventory(){
		ConcurrentHashMap<Integer, ItemStack> inventoryMap = new ConcurrentHashMap<Integer, ItemStack>();
		inventoryMap.clear();
		try{
			File sdir = new File(plugin.getDataFolder(), "inventories");
			sdir.mkdirs();
			if(!saveFile.exists()){
				saveFile.createNewFile();
			}
			EnhancedConfiguration config = new EnhancedConfiguration("inventories/" + saveFile.getName(), plugin);
			config.load();
			Integer i = 0;
			Integer size = 37; //1 more because of loop
			for(i = 0; i < size; i++){
				if(config.getItemStack(String.valueOf(i)) != null){
					ItemStack item = config.getItemStack(i.toString());
					inventoryMap.put(i, item);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return inventoryMap;
	}

	// There is no save method for a reason: We want to use the new format.

}
