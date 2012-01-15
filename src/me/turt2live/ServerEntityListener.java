package me.turt2live;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ServerEntityListener extends EntityListener {

	private Plugin	plugin;

	public ServerEntityListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onEntityDeath(EntityDeathEvent event) {
		// System.out.println("onDeath | " + event.getEntity());
		if (event.getEntity() instanceof Player) {
			Player player = (Player) event.getEntity();
			if (player != null) for (ItemStack item : event.getDrops()) {
				boolean itemIsBlocked = false;
				itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.death"), item.getTypeId());
				if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
					if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.death") && !player.hasPermission("AntiShare.allow.death")) {
						player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.death")));
						item.setAmount(0);
					}
				} else if (player.hasPermission("AntiShare.death") && !player.hasPermission("AntiShare.allow.death") && itemIsBlocked) {
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.death")));
					item.setAmount(0);
				}
			}
		}
	}

}
