package com.turt2live.antishare.storage;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.BlockedType;
import com.turt2live.antishare.worldedit.ASRegion;

public class VirtualStorage implements Listener {

	private AntiShare plugin;
	private HashMap<World, VirtualPerWorldStorage> worlds = new HashMap<World, VirtualPerWorldStorage>();

	public VirtualStorage(AntiShare plugin){
		this.plugin = plugin;
		build();
	}

	public boolean bedrockBlocked(World world){
		return worlds.get(world).isBlocked(Material.BEDROCK, BlockedType.BEDROCK);
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

	private void freePlayer(Player player){
		for(World world : worlds.keySet()){
			worlds.get(world).freePlayer(player);
		}
	}

	public ASVirtualInventory getInventoryManager(Player player, World world){
		return worlds.get(world).getInventoryManager(player);
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

	public void reload(final CommandSender sender){
		ASUtils.sendToPlayer(sender, ChatColor.GRAY + "[AntiShare] " + ChatColor.DARK_RED + "Reloading virtual storage. This could take a while...");
		ASUtils.sendToPlayer(sender, ChatColor.GRAY + "[AntiShare] " + ChatColor.RED + "The configuration will not work correctly until the virtual storage is reloaded.");
		new Thread(new Runnable(){
			@Override
			public void run(){
				reload();
				ASUtils.sendToPlayer(sender, ChatColor.GRAY + "[AntiShare] " + ChatColor.DARK_GREEN + "Virtual Storage Reloaded!");
			}
		}).start();
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

	public void saveRegion(ASRegion region){
		worlds.get(region.getWorld()).saveRegion(region);
	}

	// CAN BE NULL
	public ASRegion getRegion(Player player){
		return worlds.get(player.getWorld()).getRegion(player);
	}

	public int convertCreativeBlocks(){
		int total = 0;
		Set<World> worldListing = worlds.keySet();
		for(World world : worldListing){
			total += worlds.get(world).convertCreativeBlocks();
		}
		return total;
	}

	public void switchInventories(Player player, World worldFrom, GameMode gmFrom, World worldTo, GameMode gmTo){
		getInventoryManager(player, worldFrom).saveInventory(gmFrom);
		getInventoryManager(player, worldTo).loadInventory(gmTo);
	}
}
