package com.turt2live.antishare.storage;

import java.util.HashMap;
import java.util.Vector;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.BlockedType;

public class VirtualPerWorldStorage {

	private AntiShare plugin;
	private World world;
	private Vector<Integer> blocked_break = new Vector<Integer>();
	private Vector<Integer> blocked_place = new Vector<Integer>();
	private Vector<Integer> blocked_drop = new Vector<Integer>();
	private Vector<Integer> blocked_death = new Vector<Integer>();
	private Vector<Integer> blocked_interact = new Vector<Integer>();
	private Vector<String> blocked_commands = new Vector<String>();
	private Vector<Block> creative_blocks = new Vector<Block>();
	private Vector<Material> tracked_creative_blocks = new Vector<Material>();
	private boolean blocked_bedrock = false;
	private HashMap<String, Boolean> all_blocked = new HashMap<String, Boolean>();
	private HashMap<Player, ASVirtualInventory> inventories = new HashMap<Player, ASVirtualInventory>();
	public boolean blockDrops;

	public VirtualPerWorldStorage(World world, AntiShare plugin){
		this.plugin = plugin;
		this.world = world;
		build();
	}

	public void build(){
		blocked_break.clear();
		blocked_place.clear();
		blocked_drop.clear();
		blocked_death.clear();
		blocked_interact.clear();
		blocked_commands.clear();
		creative_blocks.clear();
		blocked_bedrock = false;
		all_blocked.clear();
		inventories.clear();
		reload();
	}

	public boolean command(String command, BlockedType type){
		return blocked_commands.contains(command);
	}

	public ASVirtualInventory getInventoryManager(Player player){
		if(!inventories.containsKey(player)){
			ASVirtualInventory inventory = new ASVirtualInventory(player, world, plugin);
			inventory.load();
			inventories.put(player, inventory);
		}
		return inventories.get(player);
	}

	public boolean isBlocked(Material material, BlockedType type){
		switch (type){
		case BEDROCK:
			return blocked_bedrock;
		case BLOCK_PLACE:
			return blocked_place.contains(material.getId());
		case BLOCK_BREAK:
			return blocked_place.contains(material.getId());
		case INTERACT:
			return blocked_interact.contains(material.getId());
		case DEATH:
			return blocked_death.contains(material.getId());
		case DROP_ITEM:
			return blocked_drop.contains(material.getId());
		}
		return false;
	}

	public boolean isCreativeBlock(Block material, BlockedType type){
		switch (type){
		case CREATIVE_BLOCK_PLACE:
			return false;
		case CREATIVE_BLOCK_BREAK:
			return creative_blocks.contains(material);
		}
		return false;
	}

	public void reload(){
		boolean flatfile = true;
		if(plugin.getSQLManager() != null){
			if(plugin.getSQLManager().isConnected()){
				flatfile = false;
				//SQLManager sql = plugin.getSQLManager();
				// TODO: Load in all vars
				String trackedBlocks[] = plugin.getConfig().getString("other.tracked-blocks").split(" ");
				boolean notNoneOrAll = false;
				if(trackedBlocks.length == 1){
					if(trackedBlocks[0].equalsIgnoreCase("*")){
						for(Material m : Material.values()){
							tracked_creative_blocks.add(m);
						}
						notNoneOrAll = false;
					}else if(trackedBlocks[0].equalsIgnoreCase("none")){
						notNoneOrAll = false;
					}
				}
				if(notNoneOrAll){
					for(String tracked : trackedBlocks){
						int id = Integer.valueOf(tracked);
						tracked_creative_blocks.add(Material.getMaterial(id));
					}
				}
			}
		}
		blocked_bedrock = !plugin.config().getBoolean("other.allow_bedrock", world);
		if(flatfile){

		}
		// Inventories
		for(Player player : inventories.keySet()){
			inventories.get(player).reload();
		}
	}

	public void removeCreativeBlock(Block block){
		if(creative_blocks.contains(block)){
			creative_blocks.remove(block);
		}
	}

	public void saveCreativeBlock(Block block){
		if(!trackBlock(block)){
			return;
		}
		if(!creative_blocks.contains(block)){
			creative_blocks.add(block);
		}
	}

	public boolean trackBlock(Block block){
		return tracked_creative_blocks.contains(block.getType());
	}
}
