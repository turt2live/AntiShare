package com.turt2live.antishare.storage;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.BlockedType;

public class VirtualStorage implements Listener {

	private AntiShare plugin;
	private HashMap<World, VirtualPerWorldStorage> worlds = new HashMap<World, VirtualPerWorldStorage>();

	public VirtualStorage(AntiShare plugin){
		this.plugin = plugin;
		build();
	}

	public boolean bedrockBlocked(World world){
		return worlds.get(world).isBlocked(null, BlockedType.BEDROCK);
	}

	public boolean blockDrops(World world){
		return worlds.get(world).blockDrops;
	}

	public void build(){
		for(World world : Bukkit.getServer().getWorlds()){
			worlds.put(world, new VirtualPerWorldStorage(world, plugin));
		}
	}

	public boolean commandBlocked(String command, World world){
		return worlds.get(world).command(command, BlockedType.COMMAND);
	}

	private void freePlayer(Player player){
		for(World world : worlds.keySet()){
			worlds.get(world).freePlayer(player);
		}
	}

	public ASVirtualInventory getInventoryManager(Player player, World world){
		return worlds.get(world).getInventoryManager(player);
	}

	public boolean isBlocked(int itemID, BlockedType type, World world){
		return worlds.get(world).isBlocked(Material.getMaterial(itemID), type);
	}

	public boolean isBlocked(ItemStack item, BlockedType type, World world){
		return worlds.get(world).isBlocked(item.getType(), type);
	}

	public boolean isBlocked(Material material, BlockedType type, World world){
		return worlds.get(world).isBlocked(material, type);
	}

	public boolean isCreativeBlock(Block block, BlockedType type, World world){
		return worlds.get(world).isCreativeBlock(block, type);
	}

	@EventHandler
	public void playerKickedEvent(PlayerKickEvent event){
		if(event.isCancelled()){
			return;
		}
		freePlayer(event.getPlayer());
	}

	@EventHandler
	public void playerQuitEvent(PlayerQuitEvent event){
		freePlayer(event.getPlayer());
	}

	public void reload(){
		build();
		Set<World> worldListing = worlds.keySet();
		for(World world : worldListing){
			worlds.get(world).reload();
		}
	}

	public void reload(World world){
		if(worlds.get(world) == null){
			worlds.put(world, new VirtualPerWorldStorage(world, plugin));
		}else{
			worlds.get(world).reload();
		}
	}

	public void saveCreativeBlock(Block block, BlockedType type, World world){
		switch (type){
		case CREATIVE_BLOCK_PLACE:
			worlds.get(world).saveCreativeBlock(block);
			break;
		case CREATIVE_BLOCK_BREAK:
			worlds.get(world).removeCreativeBlock(block);
			break;
		}
	}

	public void saveToDisk(){
		Set<World> worldListing = worlds.keySet();
		for(World world : worldListing){
			worlds.get(world).saveToDisk();
		}
	}
}
