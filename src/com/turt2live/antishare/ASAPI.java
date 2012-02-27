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

public class ASAPI {

	private AntiShare plugin;

	public ASAPI(){
		plugin = (AntiShare) Bukkit.getServer().getPluginManager().getPlugin("AntiShare");
	}

	public void addBlockToCreativeRegistry(Block block){
		ASBlockRegistry.saveCreativeBlock(block);
	}

	public boolean canBreakBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, block.getWorld());
	}

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

	public boolean canBreakBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, world);
	}

	public boolean canClickBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.INTERACT, block.getWorld());
	}

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

	public boolean canClickBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.INTERACT, world);
	}

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

	public boolean canDieWithItem(ItemStack item, World world){
		return !plugin.storage.isBlocked(item, BlockedType.DEATH, world);
	}

	public boolean canPlaceBlock(Block block){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, block.getWorld());
	}

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

	public boolean canPlaceBlock(Block block, World world){
		return !plugin.storage.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, world);
	}

	public boolean canPVP(Player player){
		if(player.hasPermission("AntiShare.pvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(player.getWorld()) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp", player.getWorld());
	}

	public boolean canPVP(Player player, World world){
		if(player.hasPermission("AntiShare.pvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp", world);
	}

	public boolean canPVP(World world){
		return plugin.config().getBoolean("other.pvp", world);
	}

	public boolean canPVPAgainstMobs(Player player){
		if(player.hasPermission("AntiShare.mobpvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(player.getWorld()) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp-mobs", player.getWorld());
	}

	public boolean canPVPAgainstMobs(Player player, World world){
		if(player.hasPermission("AntiShare.mobpvp")){
			return true;
		}
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		return plugin.config().getBoolean("other.pvp-mobs", world);
	}

	public boolean canPVPAgainstMobs(World world){
		return plugin.config().getBoolean("other.pvp-mobs", world);
	}

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

	public boolean canThrowItem(ItemStack item, World world){
		return !plugin.storage.isBlocked(item, BlockedType.DROP_ITEM, world);
	}

	public boolean canTransferToWorld(Player player, World world){
		if(isOnlyIfCreativeOn(world) && !player.getGameMode().equals(GameMode.CREATIVE)){
			return true;
		}
		if(player.hasPermission("AntiShare.worlds")){
			return true;
		}
		return plugin.config().getBoolean("other.worldTransfer", world);
	}

	public boolean canTransferToWorld(World world){
		return plugin.config().getBoolean("other.worldTransfer", world);
	}

	public boolean canUseBedrock(Player player){
		if(plugin.config().getBoolean("other.allow_bedrock", player.getWorld())){
			return true;
		}
		if(player.hasPermission("AntiShare.bedrock")){
			return true;
		}
		return false;
	}

	public boolean canUseBedrock(Player player, World world){
		if(plugin.config().getBoolean("other.allow_bedrock", world)){
			return true;
		}
		if(player.hasPermission("AntiShare.bedrock")){
			return true;
		}
		return false;
	}

	public boolean canUseCommand(String command, Player player){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(player.hasPermission("AntiShare.allow.commands")){
			return true;
		}
		return !plugin.storage.commandBlocked(command, player.getWorld());
	}

	public boolean canUseCommand(String command, Player player, World world){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(player.hasPermission("AntiShare.allow.commands")){
			return true;
		}
		return !plugin.storage.commandBlocked(command, world);
	}

	public boolean canUseCommand(String command, World world){
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		return !plugin.storage.commandBlocked(command, world);
	}

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

	public AntiShare getPlugin(){
		return plugin;
	}

	public HashMap<Integer, ItemStack> getSavedInventory(Player player, World world, GameMode gamemode){
		if(gamemode.equals(GameMode.CREATIVE)){
			return plugin.storage.getInventoryManager(player, world).getCreativeInventory();
		}else if(gamemode.equals(GameMode.SURVIVAL)){
			return plugin.storage.getInventoryManager(player, world).getSurvivalInventory();
		}
		return null;
	}

	public SQLManager getSQLManager(){
		return plugin.getSQLManager();
	}

	public VirtualStorage getStorage(){
		return plugin.storage;
	}

	public boolean isCreativeBlock(Block block){
		return plugin.storage.isCreativeBlock(block, BlockedType.CREATIVE_BLOCK_BREAK, block.getWorld());
	}

	public boolean isOnlyIfCreativeOn(World world){
		return plugin.config().getBoolean("other.only_if_creative", world);
	}

	public boolean isSQLConnected(){
		if(plugin.getSQLManager() == null){
			return false;
		}
		return plugin.getSQLManager().isConnected();
	}

	public boolean isSQLEnabled(){
		return plugin.getConfig().getBoolean("SQL.use");
	}

	public boolean isSwapInventoriesOn(World world){
		return plugin.config().getBoolean("other.inventory_swap", world);
	}

	public boolean reconnectToSQL(){
		plugin.getSQLManager().disconnect();
		return plugin.getSQLManager().attemptConnectFromConfig();
	}

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

	public void removeBlockFromCreativeRegistry(Block block){
		ASBlockRegistry.unregisterCreativeBlock(block);
	}

	public void sendNotification(NotificationType type, Player player, String variable){
		ASNotification.sendNotification(type, player, variable);
	}
}
