package com.turt2live.antishare.compatibility;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.compatibility.type.BlockLogger;

import de.diddiz.LogBlock.Consumer;
import de.diddiz.LogBlock.events.BlockChangePreLogEvent;

/**
 * LogBlock hook
 * 
 * @author turt2live
 */
public class LogBlock extends BlockLogger implements Listener {

	private Consumer lb;
	private AntiShare as = AntiShare.p;

	public LogBlock(){
		Plugin logblock = as.getServer().getPluginManager().getPlugin("LogBlock");
		de.diddiz.LogBlock.LogBlock lbp = (de.diddiz.LogBlock.LogBlock) logblock;
		lb = lbp.getConsumer();
		as.getServer().getPluginManager().registerEvents(this, as);
		if(!as.settings().logBlockSpam){
			as.getLogger().warning("************************");
			as.getLogger().warning(as.getMessages().getMessage("logblock"));
			as.getLogger().warning("************************");
		}
	}

	@Override
	public void breakBlock(String playerName, Location location, Material before, byte data){
		lb.queueBlockBreak(playerName, location, before.getId(), data);
	}

	@Override
	public void placeBlock(String playerName, Location location, Material after, byte data){
		lb.queueBlockPlace(playerName, location, after.getId(), data);
	}

	@EventHandler (priority = EventPriority.HIGHEST)
	public void onBlockChange(BlockChangePreLogEvent event){
		if(event.isCancelled() || event.getOwner().startsWith(PLAYER_NAME)){
			return;
		}
		Block block = event.getLocation().getBlock();
		if(block.hasMetadata("antishare-logblock")){
			event.setCancelled(true);
			block.removeMetadata("antishare-logblock", as);
		}
	}

	@Override
	public void breakHanging(String playerName, Location location, Material before, byte data){}

	@Override
	public void placeHanging(String playerName, Location location, Material after, byte data){}

}
