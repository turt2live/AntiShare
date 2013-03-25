/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.inventory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.inventory.ASInventory.InventoryType;

/**
 * Displayable version of an AntiShare inventory
 */
public class DisplayableInventory implements InventoryHolder, Listener{

	private OASI asinventory;
	private Inventory inventory;
	private long uid = System.nanoTime();
	private AntiShare plugin = AntiShare.p;
	private String title = "AntiShare Inventory";
	private boolean useEvent = true;

	/**
	 * Creates a new displayable AntiShare inventory
	 * 
	 * @param inventory the inventory
	 */
	public DisplayableInventory(OASI inventory){
		this(inventory, "AntiShare Inventory");
	}

	/**
	 * Creates a new displayable AntiShare inventory
	 * 
	 * @param inventory the inventory
	 * @param title the inventory title to show
	 */
	public DisplayableInventory(OASI inventory, String title){
		this.asinventory = inventory;
		this.title = title;
		if(this.title == null){
			this.title = "AntiShare Inventory";
		}
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		if(this.title.length() > 31){ // Maximum title length is 31
			this.title = this.title.substring(0, 28) + "...";
		}
	}

	private void createInventory(){
		inventory = plugin.getServer().createInventory(this, asinventory.getSize(), title);
		asinventory.populateOtherInventory(inventory);
	}

	@EventHandler (priority = EventPriority.MONITOR)
	public void onClose(InventoryCloseEvent event){
		if(!useEvent || event.getInventory().getHolder() == null){
			return;
		}
		if(event.getInventory().getHolder() instanceof DisplayableInventory){
			DisplayableInventory display = (DisplayableInventory) event.getInventory().getHolder();
			if(display.uid == this.uid){
				// It's us!
				asinventory.populateSelf(event.getInventory());
				asinventory.save();
				String name = asinventory.getName();
				if(asinventory.getType() == InventoryType.PLAYER || asinventory.getType() == InventoryType.ENDER){
					Player player = plugin.getServer().getPlayerExact(name);
					if(player != null){
						if(player.getGameMode() == asinventory.getGameMode()){
							if(player.getWorld().getName().equals(asinventory.getWorld().getName())){
								asinventory.setTo(player);
							}
						}
					}
				}
				plugin.getInventoryManager().inject(asinventory); // Force-save the inventory into the manager
				event.getInventory().clear(); // Sanitary
				inventory = null; // Sanitary
				useEvent = false;
			}
		}
	}

	@Override
	public Inventory getInventory(){
		if(inventory == null){
			createInventory();
		}
		return inventory;
	}

}
