package com.turt2live.antishare.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExpBottleEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Notification;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.debug.Debugger;
import com.turt2live.antishare.enums.NotificationType;

public class HazardListener implements Listener {

	private AntiShare plugin;

	public HazardListener(AntiShare plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTNTExplode(EntityExplodeEvent event){
		if(event.getEntity() instanceof TNTPrimed){
			TNTPrimed tnt = (TNTPrimed) event.getEntity();
			if(plugin.storage.getTntNoDrops(tnt, tnt.getWorld())){
				event.setYield(0); // Suppress drops
			}
		}
	}

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
				if(plugin.isBlocked(player, "AntiShare.allow.exp", player.getWorld())){
					event.setExperience(0);
					event.setShowEffect(false);
					Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
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
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.isBlocked(player, "AntiShare.allow.bedrock", player.getWorld())){
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				event.setCancelled(true);
			}else{
				Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
			}
		}else if(event.getBlock().getType().equals(Material.BEDROCK)){
			Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.isBlocked(player, "AntiShare.allow.bedrock", player.getWorld())){
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				Notification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
			}else{
				Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
			}
		}else if(event.getBlock().getType().equals(Material.BEDROCK)){
			Notification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
		if(event.isCancelled()){
			return;
		}
		if(event.getBlock().getType().equals(Material.TNT)){
			if(!plugin.config().getBoolean("hazards.allow_tnt", player.getWorld())){
				if(plugin.isBlocked(player, "AntiShare.allow.tnt", player.getWorld())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.tnt", player.getWorld()));
					Notification.sendNotification(NotificationType.ILLEGAL_TNT_PLACE, player, "TNT");
				}else{
					Notification.sendNotification(NotificationType.LEGAL_TNT_PLACE, player, "TNT");
				}
			}
		}
		if(event.isCancelled()){
			return;
		}
		if(event.getBlock().getType().equals(Material.TNT)){
			if(plugin.config().getBoolean("hazards.tnt_explosions", event.getBlock().getWorld())){
				if(plugin.isBlocked(player, "AntiShare.allow.explosions", event.getBlock().getWorld())){
					plugin.storage.setTntNoExplode(event.getBlock(), event.getBlock().getWorld());
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		boolean skip = false;
		try{
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
					if(plugin.isBlocked(player, "AntiShare.allow.eggs", player.getWorld())){
						event.setCancelled(true);
						Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "MONSTER EGG", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
					}else{
						Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "MONSTER EGG", null);
					}
				}
			}
			if(event.isCancelled()){
				return;
			}
			if(player.getItemInHand() == null){
				return;
			}
			// Fire Charges
			// Thanks go to AntiGrief for the following code (modified version)
			if(!plugin.config().getBoolean("hazards.allow_fire_charge", player.getWorld())
					&& player.getItemInHand().getTypeId() == 385
					&& event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
				if(plugin.isBlocked(player, "AntiShare.allow.firecharge", player.getWorld())){
					Notification.sendNotification(NotificationType.ILLEGAL_FIRE_CHARGE, player, "FIRE CHARGE");
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.fire_charge", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_FIRE_CHARGE, player, "FIRE CHARGE");
				}
			}
			if(event.isCancelled()){
				return;
			}
			try{
				boolean isBottle = false;
				if(player.getItemInHand() != null){
					isBottle = player.getItemInHand().getType().equals(Material.EXP_BOTTLE);
				}
				if(!plugin.config().getBoolean("hazards.allow_exp_bottle", player.getWorld()) && isBottle
						&& (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
					if(plugin.isBlocked(player, "AntiShare.allow.exp", player.getWorld())){
						event.setCancelled(true);
						Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
					}else{
						Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				Bug bug = new Bug(e, "Exp Bottle Thrown", this.getClass(), player);
				bug.setWorld(player.getWorld());
			}
			if(event.isCancelled()){
				return;
			}
			// Flint / Fire
			boolean isFire = false;
			if(player.getItemInHand() != null){
				Material item = player.getItemInHand().getType();
				isFire = item.equals(Material.FLINT_AND_STEEL) || item.equals(Material.FIRE) || item.equals(Material.FIREBALL);
			}
			if(!plugin.config().getBoolean("hazards.allow_flint", player.getWorld()) && isFire
					&& (event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK))){
				if(plugin.isBlocked(player, "AntiShare.allow.fire", player.getWorld())){
					Notification.sendNotification(NotificationType.ILLEGAL_FIRE, player, player.getItemInHand().getType().name());
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.flint", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_FIRE, player, player.getItemInHand().getType().name());
				}
			}
		}catch(Exception e){
			Bug bug = new Bug(e, e.getMessage(), this.getClass(), player);
			Debugger.sendBug(bug);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEggThrow(PlayerEggThrowEvent event){
		Player player = event.getPlayer();
		try{
			if(!plugin.config().getBoolean("hazards.allow_eggs", player.getWorld())){
				if(plugin.isBlocked(player, "AntiShare.allow.eggs", player.getWorld())){
					event.setHatching(false);
					Notification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "EGG", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "EGG", null);
				}
			}
		}catch(Exception e){
			Bug bug = new Bug(e, e.getMessage(), this.getClass(), player);
			Debugger.sendBug(bug);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onItemThrow(PlayerDropItemEvent event){
		// TODO: Permissions check
		plugin.getRegionHandler().getScanner().addToTracker(event.getItemDrop(), event.getPlayer());
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onItemPickup(PlayerPickupItemEvent event){
		// TODO: Permissions check
		if(plugin.getRegionHandler().getScanner().isInTracker(event.getItem())){
			if(plugin.getRegionHandler().getScanner().isIllegal(event.getItem())){
				event.setCancelled(true);
				ASUtils.sendToPlayer(event.getPlayer(), ChatColor.RED + "Nope!");
				// Don't move the item as the scanner will do that for us
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onBucket(PlayerBucketEmptyEvent event){
		Player player = event.getPlayer();
		Material bucket = event.getBucket();
		if(!plugin.config().getBoolean("hazards.allow_buckets", player.getWorld())){
			if(plugin.isBlocked(player, "AntiShare.allow.buckets", player.getWorld())){
				Notification.sendNotification(NotificationType.ILLEGAL_BUCKET, player, bucket.name());
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bucket", player.getWorld()));
			}else{
				Notification.sendNotification(NotificationType.LEGAL_BUCKET, player, bucket.name());
			}
		}
	}
}
