package com.turt2live.antishare.xmail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare;
import com.turt2live.xmail.event.XMailPreSendEvent;
import com.turt2live.xmail.mail.ComplexMail;

public class XMailListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();

	@EventHandler
	public void onMailSend(XMailPreSendEvent event){
		if(event.isCancelled())
			return;

		if((event.getMail() instanceof ComplexMail) && !plugin.getConfig().getBoolean("xMail.creative-can-send-items")){
			ComplexMail mail = (ComplexMail) event.getMail();
			Player player = Bukkit.getPlayer(mail.getFrom());
			if(mail.hasItems() && player != null && player.getGameMode() == GameMode.CREATIVE){
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot send items in creative mode through xMail");
			}
		}
	}
}
