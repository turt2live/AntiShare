package com.turt2live.antishare.listener;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Notification;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.enums.NotificationType;

public class HazardListener implements Listener {

	private AntiShare plugin;

	/*
	 * TODO: Remaining hazards
	 * - buckets
	 * - fire
	 * - fire charge
	 * - TNT
	 */

	public HazardListener(AntiShare plugin){
		this.plugin = plugin;
	}

	// TODO Waiting on API solution
	//	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	//	public void onTNTExplode(EntityExplodeEvent event){
	//		if(event.getEntity() instanceof TNTPrimed){
	//			TNTPrimed tnt = (TNTPrimed) event.getEntity();
	//			System.out.println("TNT2 " + tnt.getLocation());
	//			if(tnt.hasMetadata("tnt-no-explode")){
	//				System.out.println("TNT2 " + tnt.getLocation());
	//			}
	//		}
	//		//event.setYield(0);
	//	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onExpBottleThrown(ExpBottleEvent event){
		if(!(event.getEntity().getShooter() instanceof Player)){
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(player == null){
			return;
		}
		try{
			if(plugin.config().getBoolean("hazards.allow_exp_bottle", player.getWorld()) == false){
				if(plugin.getPermissions().has(player, "AntiShare.allow.exp", player.getWorld())){
					Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					return;
				}
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						event.setExperience(0);
						event.setShowEffect(false);
						Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
					}else{
						Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					}
				}else{
					event.setExperience(0);
					event.setShowEffect(false);
					Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			Bug bug = new Bug(e, "Exp Bottle Thrown", this.getClass(), player);
			bug.setWorld(player.getWorld());
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockBreak(BlockBreakEvent event){
		Player player = event.getPlayer();
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.allow.bedrock", player.getWorld())
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
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.allow.bedrock", player.getWorld())
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

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		boolean skip = false;
		if(plugin.config().getBoolean("hazards.allow_eggs", player.getWorld()) == false){
			ItemStack possibleEgg = event.getItem();
			if(possibleEgg != null){
				if(possibleEgg.getTypeId() != 383){
					skip = true;
				}
			}else{
				skip = true;
			}
			if(!skip){
				if(plugin.getPermissions().has(player, "AntiShare.allow.eggs", player.getWorld())){
					Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "MONSTER EGG", null);
					return;
				}
				// At this point the player is not allowed to use eggs, and we are dealing with an egg
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						event.setCancelled(true);
						Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "MONSTER EGG", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
					}else{
						Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "MONSTER EGG", null);
					}
				}else{
					event.setCancelled(true);
					Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "MONSTER EGG", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEggThrow(PlayerEggThrowEvent event){
		Player player = event.getPlayer();
		try{
			if(plugin.config().getBoolean("hazards.allow_eggs", player.getWorld()) == false){
				if(plugin.getPermissions().has(player, "AntiShare.allow.eggs", player.getWorld())){
					Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "EGG", null);
					return;
				}
				// At this point the player is not allowed to use eggs, and we are dealing with an egg
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						event.setHatching(false);
						Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "EGG", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
					}else{
						Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "EGG", null);
					}
				}else{
					event.setHatching(false);
					Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "EGG", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
				}
			}
		}catch(Exception e){
			Bug bug = new Bug(e, e.getMessage(), this.getClass(), player);
			plugin.getDebugger().sendBug(bug);
			e.printStackTrace();
		}
	}
}
