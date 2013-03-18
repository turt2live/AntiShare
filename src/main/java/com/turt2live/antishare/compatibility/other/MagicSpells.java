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
package com.turt2live.antishare.compatibility.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.events.SpellTargetEvent;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.util.GamemodeAbstraction;
import com.turt2live.antishare.util.PermissionNodes;

/**
 * MagicSpells hook
 * 
 * @author turt2live
 */
public class MagicSpells implements Listener{

	private boolean enabled = false;

	public MagicSpells(){
		enabled = AntiShare.p.settings().magicSpells;
	}

	@EventHandler
	public void onTarget(SpellTargetEvent event){
		if(event.isCancelled() || !enabled){
			return;
		}
		Player player = event.getCaster();
		if(!AntiShare.hasPermission(player, PermissionNodes.PLUGIN_MAGIC_SPELLS) && GamemodeAbstraction.isCreative(player.getGameMode())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTarget(SpellCastEvent event){
		if(event.isCancelled() || !enabled){
			return;
		}
		Player player = event.getCaster();
		if(!AntiShare.hasPermission(player, PermissionNodes.PLUGIN_MAGIC_SPELLS) && GamemodeAbstraction.isCreative(player.getGameMode())){
			event.setCancelled(true);
		}
	}

}
