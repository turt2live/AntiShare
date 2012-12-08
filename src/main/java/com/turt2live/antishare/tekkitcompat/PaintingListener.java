package com.turt2live.antishare.tekkitcompat;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Painting;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingBreakEvent.RemoveCause;

import com.turt2live.antishare.AntiShare;

@SuppressWarnings ("deprecation")
public class PaintingListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();

	@EventHandler (priority = EventPriority.LOW)
	public void onPaintingBreak(PaintingBreakEvent event){
		if(event.isCancelled() || !plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.paintings-are-attached")){
			return;
		}
		if(event.getCause() == RemoveCause.PHYSICS){
			// Removed by something
			Painting hanging = event.getPainting();
			Location block = hanging.getLocation().getBlock().getRelative(hanging.getAttachedFace()).getLocation();
			GameMode gamemode = plugin.getBlockManager().getRecentBreak(block);
			if(gamemode != null && gamemode == GameMode.CREATIVE){
				event.setCancelled(true);
				hanging.remove();
			}
		}
	}

}
