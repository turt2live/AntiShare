package com.turt2live.antishare;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.PotionSplashEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.sk89q.worldedit.EntityType;
import com.turt2live.antishare.enums.NotificationType;
import com.turt2live.antishare.listener.BlockListener;
import com.turt2live.antishare.listener.EntityListener;
import com.turt2live.antishare.listener.PlayerListener;

public class ASListener implements Listener {

	private AntiShare plugin;

	public ASListener(AntiShare p){
		plugin = p;
		p.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new EntityListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new BlockListener(plugin), plugin);
	}

	@EventHandler
	public void onPotionSplash(PotionSplashEvent event){
		if(AntiShare.DEBUG_MODE){
			Bukkit.broadcastMessage(event.getEntityType().toString() + " | " + event.getPotion().toString() + " | " + event.getEntity().toString());
		}
	}

	// TODO
	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTNTPrime(ExplosionPrimeEvent event){
		if(event.getEntityType().equals(EntityType.TNT)){
			TNTPrimed tnt = (TNTPrimed) event.getEntity();
			Block potentialTNT = tnt.getLocation().getWorld().getBlockAt(tnt.getLocation());
			if(potentialTNT.hasMetadata("tnt-no-explode")){
				System.out.println("TNT");
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onTNTExplode(EntityExplodeEvent event){
		if(event.getEntityType().equals(EntityType.TNT)){
			TNTPrimed tnt = (TNTPrimed) event.getEntity();
			if(tnt.hasMetadata("tnt-no-explode")){
				System.out.println("TNT2");
			}
		}
		//event.setYield(0);
	}

	@EventHandler (priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void onExpBottleThrown(ProjectileLaunchEvent event){
		if(!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof ThrownExpBottle)){
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(player == null){
			return;
		}
		if(plugin.config().getBoolean("other.allow_exp_bottle", player.getWorld()) == false){
			if(plugin.getPermissions().has(player, "AntiShare.allow.exp", player.getWorld())){
				Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
				return;
			}
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
				}else{
					Notification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
				}
			}else{
				event.setCancelled(true);
				Notification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
			}
		}
	}
}
