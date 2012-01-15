package me.turt2live;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.plugin.Plugin;

public class ServerBlockListener extends BlockListener {

	private Plugin	plugin;

	public ServerBlockListener(Plugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
		// System.out.println("onPlace | " + event.getPlayer() + " | " + event.getBlockPlaced().getTypeId());
		Player player = event.getPlayer();
		if (player != null && !event.isCancelled()) {
			boolean itemIsBlocked = false;
			int item = event.getBlockPlaced().getTypeId();
			itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.block_place"), item);
			if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
				if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.place") && !player.hasPermission("AntiShare.allow.place")) {
					event.setCancelled(true);
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_place")));
				}
			} else if (player.hasPermission("AntiShare.place") && !player.hasPermission("AntiShare.allow.place") && itemIsBlocked) {
				event.setCancelled(true);
				player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_place")));
			}
		}
	}

	@Override
	public void onBlockBreak(BlockBreakEvent event) {
		// System.out.println("onBreak | " + event.getPlayer() + " | " + event.getBlock().getTypeId());
		Player player = event.getPlayer();
		if (player != null && !event.isCancelled()) {
			boolean itemIsBlocked = false;
			int item = event.getBlock().getTypeId();
			itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.block_break"), item);
			if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
				if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.break") && !player.hasPermission("AntiShare.allow.break")) {
					event.setCancelled(true);
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_break")));
				}
			} else if (player.hasPermission("AntiShare.break") && !player.hasPermission("AntiShare.allow.break") && itemIsBlocked) {
				event.setCancelled(true);
				player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_break")));
			}
		}
	}

	@Override
	public void onBlockDamage(BlockDamageEvent event) {
		// System.out.println("onBreak | " + event.getPlayer() + " | " + event.getBlock().getTypeId());
		Player player = event.getPlayer();
		if (player != null && !event.isCancelled()) {
			boolean itemIsBlocked = false;
			int item = event.getBlock().getTypeId();
			itemIsBlocked = AntiShare.isBlocked(plugin.getConfig().getString("events.block_break"), item);
			if (plugin.getConfig().getBoolean("other.only_if_creative") && itemIsBlocked) {
				if (player.getGameMode() == GameMode.CREATIVE) if (player.hasPermission("AntiShare.break") && !player.hasPermission("AntiShare.allow.break")) {
					event.setCancelled(true);
					player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_break")));
				}
			} else if (player.hasPermission("AntiShare.break") && !player.hasPermission("AntiShare.allow.break") && itemIsBlocked) {
				event.setCancelled(true);
				player.sendMessage(AntiShare.addColor(plugin.getConfig().getString("messages.block_break")));
			}
		}
	}

}
