/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
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
