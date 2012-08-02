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
package com.turt2live.antishare.xmail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.xmail.event.XMailPreSendEvent;
import com.turt2live.xmail.mail.ComplexMail;

public class XMailListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();

	@EventHandler
	public void onMailSend(XMailPreSendEvent event){
		if(event.isCancelled())
			return;

		boolean block = false;
		String from = event.getMail().getFrom();
		Player player = Bukkit.getPlayer(from);
		if(player != null){
			block = !plugin.getPermissions().has(player, PermissionNodes.XMAIL);
		}

		if((event.getMail() instanceof ComplexMail) && block){
			ComplexMail mail = (ComplexMail) event.getMail();
			if(!mail.hasItems() || player == null){
				return;
			}
			String gm = player.getGameMode().name().toLowerCase();
			if(!plugin.getConfig().getBoolean("xmail." + gm + "-can-send-items")){
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot send items in " + gm + " mode through xMail", true);
			}
		}
	}
}
