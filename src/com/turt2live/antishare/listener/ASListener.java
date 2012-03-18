package com.turt2live.antishare.listener;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownExpBottle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Notification;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.enums.NotificationType;

public class ASListener implements Listener {

	private AntiShare plugin;

	public ASListener(AntiShare p){
		plugin = p;
		p.getServer().getPluginManager().registerEvents(new PlayerListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new EntityListener(plugin), plugin);
		p.getServer().getPluginManager().registerEvents(new BlockListener(plugin), plugin);
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
	public void onExpBottleThrown(ProjectileLaunchEvent event){
		if(!(event.getEntity().getShooter() instanceof Player) || !(event.getEntity() instanceof ThrownExpBottle)){
			return;
		}
		Player player = (Player) event.getEntity().getShooter();
		if(player == null){
			return;
		}
		try{
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
		}catch(Exception e){
			e.printStackTrace();
			Bug bug = new Bug(e, "Exp Bottle Thrown", this.getClass(), player);
			bug.setWorld(player.getWorld());
		}
	}
}
