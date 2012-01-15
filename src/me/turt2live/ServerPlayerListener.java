package me.turt2live;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public class ServerPlayerListener extends PlayerListener {

	private Plugin	plugin;

	public ServerPlayerListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event) {
		// System.out.println("onDrop | " + event.getPlayer() + " | " + event.getItemDrop().getItemStack().getTypeId());
		Player player = event.getPlayer();
		if (player != null && !event.isCancelled()) {
			boolean itemIsBlocked = false;
			ItemStack item = event.getItemDrop().getItemStack();
			itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.drop_item"), item.getTypeId());
			if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
				if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.drop") && !player.hasPermission("AntiShare.allow.drop")) {
					event.setCancelled(true);
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.drop_item")));
				}
			} else if (player.hasPermission("AntiShare.drop") && !player.hasPermission("AntiShare.allow.drop") && itemIsBlocked) {
				event.setCancelled(true);
				player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.drop_item")));
			}
		}
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		// System.out.println("onInteract | " + event.getPlayer() + " | " + event.getClickedBlock().getTypeId());
		Player player = event.getPlayer();
		if (player != null && !event.isCancelled() && event.getClickedBlock() != null) {
			boolean itemIsBlocked = false;
			int item = event.getClickedBlock().getTypeId();
			itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.interact"), item);
			if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
				if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.interact") && !player.hasPermission("AntiShare.allow.interact")) {
					event.setCancelled(true);
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.interact")));
				}
			} else if (player.hasPermission("AntiShare.interact") && !player.hasPermission("AntiShare.allow.interact") && itemIsBlocked) {
				event.setCancelled(true);
				player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.interact")));
			}
		}
	}

}
