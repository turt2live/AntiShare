package com.turt2live.antishare.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.metadata.MetadataValue;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Notification;
import com.turt2live.antishare.enums.BlockedType;
import com.turt2live.antishare.enums.NotificationType;
import com.turt2live.antishare.regions.ASRegion;

public class BlockListener implements Listener {

	private AntiShare plugin;
	private HashMap<Player, Long> blockDropTextWarnings = new HashMap<Player, Long>();

	public BlockListener(AntiShare plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(player == null){
			return;
		}
		if(event.getBlock().hasMetadata("invmirror")){
			List<MetadataValue> values = event.getBlock().getMetadata("invmirror");
			boolean remove = false;
			for(MetadataValue value : values){
				if(value.getOwningPlugin().getName().equalsIgnoreCase("AntiShare")){
					if(value.asString().equalsIgnoreCase(player.getName())){
						Chest chest = (Chest) event.getBlock().getState();
						chest.getInventory().clear();
						ASUtils.sendToPlayer(player, ChatColor.YELLOW + "Inventory chest cleared.");
					}else{
						event.setCancelled(true);
						ASUtils.sendToPlayer(player, ChatColor.RED + "That is not a normal chest, you cannot break it");
						return;
					}
					remove = true;
					break;
				}
			}
			if(remove){
				event.getBlock().removeMetadata("invmirror", plugin);
			}
		}
		ASRegion region = plugin.getRegionHandler().getRegion(player.getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You are in the wrong GameMode for this area");
					player.setGameMode(region.getGameModeSwitch());
					return;
				}
			}
		}
		region = plugin.getRegionHandler().getRegion(event.getBlock().getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot break that due to a GameMode region");
					return;
				}
			}
		}
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.bedrock", player.getWorld())
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode() == GameMode.CREATIVE){
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
					Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
					event.setCancelled(true);
				}else{
					Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}
			}else{
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				event.setCancelled(true);
			}
		}else if(event.getBlock().getType().equals(Material.BEDROCK)){
			Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
		if(event.isCancelled()){
			return;
		}
		if(plugin.storage.isBlocked(event.getBlock().getType(), BlockedType.BLOCK_BREAK, player.getWorld())){
			//System.out.println("BLOCK BREAK: ILLEGAL");
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)
						&& !plugin.getPermissions().has(player, "AntiShare.allow.break", player.getWorld())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_break", player.getWorld()));
					Notification.sendNotification(NotificationType.ILLEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}else{
					Notification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				if(!plugin.getPermissions().has(player, "AntiShare.allow.break", player.getWorld())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_break", player.getWorld()));
					Notification.sendNotification(NotificationType.ILLEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}else{
					Notification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}
		}else{
			Notification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
		if(event.isCancelled()){
			return;
		}
		if(plugin.config().getBoolean("other.track_blocks", player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.blockBypass", player.getWorld())){
			if(player.getGameMode().equals(GameMode.SURVIVAL)){
				boolean isBlocked = plugin.storage.isCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
				if(isBlocked){
					if(!plugin.config().getBoolean("other.blockDrops", player.getWorld())){
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.creativeModeBlock", player.getWorld()));
						event.setCancelled(true);
					}else{
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.creativeModeBlock", player.getWorld()));
						plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
						Block block = event.getBlock();
						event.setCancelled(true);
						block.setTypeId(0); // Fakes a break
					}
					Notification.sendNotification(NotificationType.ILLEGAL_CREATIVE_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
			}
		}else{
			Notification.sendNotification(NotificationType.LEGAL_CREATIVE_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onBlockDamage(BlockDamageEvent event){
		if(event.isCancelled() || event.getPlayer() == null){
			return;
		}
		Player player = event.getPlayer();
		if(!event.isCancelled()){
			if(plugin.config().getBoolean("other.blockDrops", player.getWorld())
					&& !plugin.getPermissions().has(player, "AntiShare.blockBypass", player.getWorld())
					&& plugin.storage.isCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld())){
				long systemTime = System.currentTimeMillis();
				if(blockDropTextWarnings.containsKey(player)){
					if((systemTime - blockDropTextWarnings.get(player)) > 1000){
						ASUtils.sendToPlayer(player, plugin.getConfig().getString("messages.noBlockDrop"));
						blockDropTextWarnings.remove(player);
						blockDropTextWarnings.put(player, systemTime);
					}
				}else{
					ASUtils.sendToPlayer(player, plugin.getConfig().getString("messages.noBlockDrop"));
					blockDropTextWarnings.put(player, systemTime);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		if(event.getPlayer() == null){
			return;
		}
		Player player = event.getPlayer();
		ASRegion region = plugin.getRegionHandler().getRegion(player.getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You are in the wrong GameMode for this area");
					player.setGameMode(region.getGameModeSwitch());
					return;
				}
			}
		}
		region = plugin.getRegionHandler().getRegion(event.getBlock().getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot place there due to a GameMode region");
					return;
				}
			}
		}
		if(plugin.storage.isBlocked(event.getBlock().getType(), BlockedType.BLOCK_PLACE, player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.allow.place", player.getWorld())){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					Notification.sendNotification(NotificationType.ILLEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_place", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				event.setCancelled(true);
				Notification.sendNotification(NotificationType.ILLEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_place", player.getWorld()));
			}
		}else{
			Notification.sendNotification(NotificationType.LEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
		if(event.isCancelled()){
			return;
		}
		//Bedrock check
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.bedrock", player.getWorld())
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
					Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}else{
					Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}
			}else{
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
			}
		}else if(event.getBlock().getType().equals(Material.BEDROCK)){
			Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
		if(event.isCancelled()){
			return;
		}
		//Creative Mode Placing
		if(plugin.config().getBoolean("other.track_blocks", player.getWorld())
				&& player.getGameMode() == GameMode.CREATIVE
				&& !plugin.getPermissions().has(player, "AntiShare.freePlace", player.getWorld())){
			plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_PLACE, event.getBlock().getWorld());
		}
		if(event.isCancelled()){
			return;
		}
		// TNT Explosions TODO: Waiting on API solution
		//		if(event.getBlock().getType().equals(Material.TNT)
		//				&& !plugin.getPermissions().has(player, "AntiShare.tnt", event.getBlock().getWorld())
		//				&& plugin.config().getBoolean("other.noTNTDrops", event.getBlock().getWorld())){
		//			if(plugin.config().onlyIfCreative(player)){
		//				if(player.getGameMode().equals(GameMode.CREATIVE)){
		//					event.getBlock().setMetadata("tnt-no-explode", new FixedMetadataValue(plugin, true));
		//					System.out.println(event.getBlock().getLocation());
		//				}
		//			}else{
		//				event.getBlock().setMetadata("tnt-no-explode", new FixedMetadataValue(plugin, true));
		//			}
		//		}
	}
}
