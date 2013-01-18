package com.turt2live.antishare.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.money.MoneyManager;

public class MoneyListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();
	private MoneyManager manager;
	
	public MoneyListener(MoneyManager manager){
		this.manager=manager;
	}

	// ################# Player Join

	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();

		// Money (fines/rewards) status
		((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).showStatusOnLogin(player);
	}
	
}
