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
	private HashMap<Item, ASRegion> original_regions = new HashMap<Item, ASRegion>();
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
		original_regions.put(item, handler.getRegion(owner.getLocation()));
	}

	public boolean isInTracker(Item item){
		return items.get(item) != null;
	}

	public boolean isIllegal(Item item, Player player){
		ASRegion playerRegion = plugin.getRegionHandler().getRegion(player.getLocation());
		ASRegion itemRegion = plugin.getRegionHandler().getRegion(item.getLocation());
		if(playerRegion == null && itemRegion == null){
			return false;
		}else if((playerRegion == null && itemRegion != null) || (playerRegion != null && itemRegion == null)){
			return true;
		}else if(!playerRegion.getUniqueID().equals(itemRegion.getUniqueID())){
			return true;
		}
		return false;
	}

	public void moveOutOfRegion(Item item){
		if(handler.isRegion(item.getLocation())){
			ASRegion region = handler.getRegion(item.getLocation());
			Location point = region.getPointOutside(item.getLocation(), 5);
			item.teleport(point);
		}
	}

	private void schedule(){
		plugin.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			@Override
			public void run(){
				Vector<Item> remove = new Vector<Item>();
				HashMap<Item, Location> updates = new HashMap<Item, Location>();
				Iterator<Item> itemList = items.keySet().iterator();
				while (itemList.hasNext()){
					Item item = itemList.next();
					if(item.isDead()){
						remove.add(item);
						continue;
					}
					Player owner = owners.get(item);
					ASRegion owner_region = original_regions.get(item);
					ASRegion item_region = handler.getRegion(item.getLocation());
					boolean removeItem = false;
					if(owner_region == null && item_region == null){
						// allowed
					}else if((owner_region != null && item_region == null) || (owner_region == null && item_region != null)){
						removeItem = true;
					}// else: allowed
					if(removeItem){
						moveOutOfRegion(item);
						remove.add(item);
						if(owner != null){
							ASUtils.sendToPlayer(owner, ChatColor.RED + "An item of yours landed in a region.");
						}
					}else{
						remove.add(item); // Just remove, it's legal
					}
					if(!hasMoved(item.getLocation(), items.get(item)) && !handler.isRegion(item.getLocation())){
						remove.add(item);
					}else{
						updates.put(item, item.getLocation());
					}
				}
				for(Item itemToRemove : remove){
					items.remove(itemToRemove);
					owners.remove(itemToRemove);
					original_regions.remove(itemToRemove);
				}
				Iterator<Item> updateList = updates.keySet().iterator();
				while (updateList.hasNext()){
					Item item = updateList.next();
					items.put(item, updates.get(item));
				}
			}
		}, 0, 20);
	}

	private boolean hasMoved(Location to, Location original){
		if(!to.getWorld().equals(original.getWorld())){
			return true;
		}
		if(to.getX() != original.getX()){
			return true;
		}
		if(to.getY() != original.getY()){
			return true;
		}
		if(to.getZ() != original.getZ()){
			return true;
		}
		return false;
	}
}
