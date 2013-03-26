package com.turt2live.antishare.inventory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.dumptruckman.bukkit.configuration.json.JsonConfiguration;
import com.turt2live.antishare.AntiShare;

/**
 * AntiShare Inventory
 * 
 * @author turt2live
 */
public class ASInventory implements Cloneable{

	/**
	 * Inventory type
	 * 
	 * @author turt2live
	 */
	public static enum InventoryType{
		PLAYER("players"),
		REGION("regions"),
		TEMPORARY("temporary"),
		ENDER("ender");

		private String relativeFolderName;

		private InventoryType(String relativeFolderName){
			this.relativeFolderName = relativeFolderName;
		}

		/**
		 * Gets the relative folder name
		 * 
		 * @return the folder
		 */
		public String getRelativeFolderName(){
			return relativeFolderName;
		}
	}

	public static final File DATA_FOLDER = new File(AntiShare.p.getDataFolder(), "data" + File.separator + "inventories");
	public static final int SIZE = (9 * 4) + 4;
	public static final int SIZE_HIGH_9 = (9 * 4) + 9;
	public static final ItemStack AIR = new ItemStack(Material.AIR);
	public static final String VERSION = "2";
	public static final ASInventory EMPTY = null;

	public GameMode gamemode;
	public final String owner;
	public final String world;
	public final InventoryType type;
	final Map<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();

	// TODO: Make invisible
	public ASInventory(GameMode gamemode, String owner, String world, InventoryType type){
		this.gamemode = gamemode;
		this.owner = owner;
		this.world = world;
		this.type = type;
	}

	/**
	 * Gets the contents of this Inventory. No null items will be found, only AIR. <br>
	 * Shortcut for {@link #getContents(boolean, boolean)} as (true, true)
	 * 
	 * @return the contents as an array
	 */
	public ItemStack[] getContents(){
		return getContents(true, true);
	}

	/**
	 * Gets the contents of this Inventory. No null items will be found, only AIR.
	 * 
	 * @param core set as true to include "core" slots
	 * @param armor set as true to include "armor" slots
	 * 
	 * @return the contents as an array
	 */
	public ItemStack[] getContents(boolean core, boolean armor){
		ItemStack[] array = new ItemStack[SIZE];
		if(core && armor){
			for(Integer slot : items.keySet()){
				array[slot] = items.get(slot);
			}
		}else if(core && !armor){
			array = new ItemStack[SIZE - 4];
			for(Integer slot : items.keySet()){
				if(slot >= SIZE - 4){
					continue;
				}
				array[slot] = items.get(slot);
			}
		}else if(!core && armor){
			array = new ItemStack[4];
			for(Integer slot : items.keySet()){
				if(slot < SIZE - 4){
					continue;
				}
				array[slot - (SIZE - 4)] = items.get(slot);
			}
		}
		return array;
	}

	/**
	 * Sets the contents of this inventory. This can be any size. (Extra items are ignored)
	 * 
	 * @param items the items
	 */
	public void setContents(ItemStack[] items){
		for(int i = 0; i < items.length; i++){
			set(i, items[i]);
		}
	}

	/**
	 * Sets a slot in the inventory
	 * 
	 * @param slot the slot (any number, outside of 36+4 is ignored)
	 * @param item the item
	 */
	public void set(int slot, ItemStack item){
		items.put(slot, item);
	}

	/**
	 * Clones an inventory into this inventory
	 * 
	 * @param inventory the inventory to clone
	 */
	public void clone(Inventory inventory){
		items.clear();
		if(inventory instanceof PlayerInventory){
			PlayerInventory playerInv = (PlayerInventory) inventory;
			ItemStack[] armor = playerInv.getArmorContents();
			for(int i = 0; i < armor.length; i++){
				set(36 + i, armor[i]);
			}
		}
		ItemStack[] contents = inventory.getContents();
		for(int i = 0; i < contents.length; i++){
			set(i, contents[i]);
		}
	}

	/**
	 * Clones an inventory into this inventory
	 * 
	 * @param inventory the inventory to clone
	 */
	public void clone(ASInventory inventory){
		items.clear();
		if(inventory instanceof PlayerInventory){
			PlayerInventory playerInv = (PlayerInventory) inventory;
			ItemStack[] armor = playerInv.getArmorContents();
			for(int i = 0; i < armor.length; i++){
				set(36 + i, armor[i]);
			}
		}
		ItemStack[] contents = inventory.getContents();
		for(int i = 0; i < contents.length; i++){
			set(i, contents[i]);
		}
	}

	/**
	 * Fills the entire inventory with the same item
	 * 
	 * @param item the item
	 */
	public void fill(ItemStack item){
		for(int i = 0; i < SIZE; i++){
			set(i, item.clone());
		}
	}

	/**
	 * Sets the inventory to a specified inventory. This can be a player inventory or another inventory. 36 slots minimum.
	 * 
	 * @param inventory the inventory to set to
	 */
	public void setTo(Inventory inventory){
		ItemStack[] armor = getContents(false, true);
		ItemStack[] contents = getContents(true, false);
		inventory.setContents(contents);
		if(inventory instanceof PlayerInventory){
			((PlayerInventory) inventory).setArmorContents(armor);
		}
	}

	/**
	 * Saves the inventory
	 */
	public void save(){
		File file = new File(DATA_FOLDER, type.getRelativeFolderName() + File.separator + owner + ".asinventory");
		JsonConfiguration yaml = new JsonConfiguration();
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			yaml.load(file);
			yaml.set(world + "." + gamemode.name(), getContents());
			yaml.set(world + "." + gamemode.name() + "_version", VERSION);
			yaml.save(file);
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidConfigurationException e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads an inventory. This will always return an inventory, however it may be empty.
	 * 
	 * @param player the player to load (player name)
	 * @param gamemode the game mode to load
	 * @param type the inventory type to load
	 * @param world the world name to load
	 * @return the loaded inventory. Will never be null.
	 */
	public static ASInventory load(String player, GameMode gamemode, InventoryType type, String world){
		File file = new File(DATA_FOLDER, type.getRelativeFolderName() + File.separator + player + ".asinventory");
		ASInventory inventory = new ASInventory(gamemode, player, world, type);
		JsonConfiguration yaml = new JsonConfiguration();
		try{
			if(!file.exists()){
				file.createNewFile();
			}
			yaml.load(file);
			String version = yaml.getString(world + "." + gamemode.name() + "_version");
			if(version.equalsIgnoreCase("2")){
				Object something = yaml.get(world + "." + gamemode.name());
				if(something instanceof List){
					List<?> objects = (List<?>) something;
					for(int i = 0; i < objects.size(); i++){
						Object entry = objects.get(i);
						if(entry instanceof ItemStack){
							inventory.set(i, (ItemStack) entry);
						}
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}catch(InvalidConfigurationException e){
			e.printStackTrace();
		}
		return inventory;
	}

	/**
	 * Gets a list of all inventories for a player of a specified type
	 * 
	 * @param playerName the player name
	 * @param type the type
	 * @return a list of inventories, never null
	 */
	public static List<ASInventory> getAll(String playerName, InventoryType type){
		List<ASInventory> invs = new ArrayList<ASInventory>();
		ASInventory i = null;
		for(World world : AntiShare.p.getServer().getWorlds()){
			for(GameMode gamemode : GameMode.values()){
				i = load(playerName, gamemode, type, world.getName());
				if(i != null){
					invs.add(i);
				}
			}
		}
		return invs;
	}

	@Override
	public String toString(){
		return "ASInventory [gamemode=" + gamemode + ", owner=" + owner + ", world=" + world + ", type=" + type + ", items=" + items + "]";
	}

	@Override
	public ASInventory clone(){
		ASInventory inventory = new ASInventory(gamemode, owner, world, type);
		inventory.setContents(getContents());
		return inventory;
	}

}
