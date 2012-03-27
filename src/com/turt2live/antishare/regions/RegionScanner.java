package com.turt2live.antishare.regions;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;

public class RegionScanner {

	private HashMap<Item, Location> items = new HashMap<Item, Location>();
	private HashMap<Item, Player> owners = new HashMap<Item, Player>();
	private RegionHandler handler;
	private AntiShare plugin;

	public RegionScanner(RegionHandler handler, AntiShare plugin){
		this.handler = handler;
		this.plugin = plugin;
		schedule();
	}

	public void addToTracker(Item item, Player owner){
		items.put(item, item.getLocation());
		owners.put(item, owner);
	}

	public boolean isInTracker(Item item){
		return items.get(item) != null;
	}

	public boolean isIllegal(Item item){
		return handler.isRegion(item.getLocation());
	}

	public void moveOutOfRegion(Item item){
		if(handler.isRegion(item.getLocation())){
			ASRegion region = handler.getRegion(item.getLocation());
			Location point = region.getPointOutside(item.getLocation(), 10);
			item.teleport(point);
		}
	}

	private void schedule(){
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				Vector<Item> remove = new Vector<Item>();
				Iterator<Item> itemList = items.keySet().iterator();
				while (itemList.hasNext()){
					Item item = itemList.next();
					if(item.isDead()){
						remove.add(item);
						continue;
					}
					if(isIllegal(item)){
						Player owner = owners.get(item);
						if(owner != null){
							moveOutOfRegion(item);
							remove.add(item);
							// TODO: Check permissions
							ASUtils.sendToPlayer(owner, ChatColor.RED + "An item of yours landed in a region.");
						}else{
							moveOutOfRegion(item);
							remove.add(item);
						}
					}
					if(!hasMoved(item.getLocation(), items.get(item))){
						items.remove(item);
					}
				}
				for(Item itemToRemove : remove){
					items.remove(itemToRemove);
				}
			}
		}, 0, 20);
	}

	private boolean hasMoved(Location to, Location original){
		if(!to.getWorld().equals(original.getWorld())){
			return true;
		}
		if(to.getBlockX() != original.getBlockX()){
			return true;
		}
		if(to.getBlockY() != original.getBlockY()){
			return true;
		}
		if(to.getBlockZ() != original.getBlockZ()){
			return true;
		}
		return false;
	}
}
