package com.turt2live.antishare.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Notification;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.enums.NotificationType;

public class EntityListener implements Listener {

	private AntiShare plugin;

	public EntityListener(AntiShare plugin){
		this.plugin = plugin;
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityDamage(EntityDamageEvent event){
		if(!(event instanceof EntityDamageByEntityEvent)
				|| event.isCancelled()){
			return;
		}
		try{
			String entityName = event.getEntity().getClass().getName().replace("Craft", "").replace("org.bukkit.craftbukkit.entity.", "");
			Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
			if(damager instanceof Player){
				Player dealer = (Player) damager;
				if(!dealer.getGameMode().equals(GameMode.CREATIVE) && !plugin.config().onlyIfCreative(dealer)){
					return;
				}else if(!dealer.getGameMode().equals(GameMode.CREATIVE)){
					return;
				}
				//System.out.println("GM: " + dealer.getGameMode().toString());
				if(event.getEntity() instanceof Player){
					String targetName = ((Player) event.getEntity()).getName();
					if(plugin.config().getBoolean("other.pvp", dealer.getWorld())){
						Notification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, targetName, null);
						return;
					}
					if(!plugin.getPermissions().has(dealer, "AntiShare.pvp", dealer.getWorld())){
						ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.pvp", event.getEntity().getWorld()));
						Notification.sendNotification(NotificationType.ILLEGAL_PLAYER_PVP, plugin, dealer, targetName, null);
						event.setCancelled(true);
					}
				}else{
					if(plugin.config().getBoolean("other.pvp-mobs", dealer.getWorld())){
						Notification.sendNotification(NotificationType.LEGAL_MOB_PVP, plugin, dealer, entityName, null);
						return;
					}
					if(!plugin.getPermissions().has(dealer, "AntiShare.mobpvp", dealer.getWorld())){
						Notification.sendNotification(NotificationType.ILLEGAL_MOB_PVP, plugin, dealer, entityName, null);
						ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.mobpvp", event.getEntity().getWorld()));
						event.setCancelled(true);
					}else{
						Notification.sendNotification(NotificationType.LEGAL_MOB_PVP, plugin, dealer, entityName, null);
					}
				}
			}else if(damager instanceof Projectile){
				LivingEntity shooter = ((Projectile) damager).getShooter();
				if(shooter instanceof Player){
					Player dealer = ((Player) shooter);
					if(!dealer.getGameMode().equals(GameMode.CREATIVE) && !plugin.config().onlyIfCreative(dealer)){
						Notification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
						return;
					}else if(!dealer.getGameMode().equals(GameMode.CREATIVE)){
						Notification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
						return;
					}
					if(!plugin.getPermissions().has(dealer, "AntiShare.pvp", dealer.getWorld())){
						ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.pvp", dealer.getWorld()));
						Notification.sendNotification(NotificationType.ILLEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
						event.setCancelled(true);
					}
				}
			}
		}catch(Exception e){
			Bug bug = new Bug(e, e.getMessage(), this.getClass(), null);
			plugin.getDebugger().sendBug(bug);
			e.printStackTrace();
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onEntityTarget(EntityTargetEvent event){
		if(!(event.getTarget() instanceof Player)){
			return;
		}
		try{
			Entity targetEntity = event.getTarget();
			if(event.getEntity() instanceof Monster
					&& targetEntity != null
					&& targetEntity instanceof Player){
				Player player = (Player) targetEntity;
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						if(!plugin.getPermissions().has(player, "AntiShare.mobpvp", player.getWorld())
								&& !plugin.config().getBoolean("other.pvp-mobs", player.getWorld())){
							event.setCancelled(true);
						}
					}
				}else{
					if(!plugin.getPermissions().has(player, "AntiShare.mobpvp", player.getWorld())
							&& !plugin.config().getBoolean("other.pvp-mobs", player.getWorld())){
						event.setCancelled(true);
					}
				}
			}
		}catch(Exception e){
			Bug bug = new Bug(e, e.getMessage(), this.getClass(), (Player) event.getTarget());
			plugin.getDebugger().sendBug(bug);
			e.printStackTrace();
		}
	}
}
