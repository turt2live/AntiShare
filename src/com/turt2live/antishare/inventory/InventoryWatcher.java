package com.turt2live.antishare.inventory;

import org.bukkit.entity.Player;

import com.turt2live.antishare.AntiShare;

/**
 * Used to update inventories after a delay
 * 
 * @author turt2live
 */
public class InventoryWatcher implements Runnable {

	private Player player;
	private AntiShare plugin = AntiShare.getInstance();

	public InventoryWatcher(Player player){
		this.player = player;
	}

	@Override
	public void run(){
		plugin.getInventoryManager().refreshInventories(player);
	}

}
