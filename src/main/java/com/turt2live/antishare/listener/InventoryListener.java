package com.turt2live.antishare.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.inventory.InventoryManager;
import com.turt2live.antishare.notification.Alert.AlertTrigger;
import com.turt2live.antishare.notification.Alert.AlertType;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.tekkitcompat.ServerHas;

public class InventoryListener implements Listener{

	private AntiShare plugin = AntiShare.getInstance();
	private InventoryManager manager;
	
	public InventoryListener(InventoryManager manager){
		this.manager=manager;
	}

	// ################# Player World Change

	@EventHandler (priority = EventPriority.MONITOR)
	public void onWorldChange(PlayerChangedWorldEvent event){
		Player player = event.getPlayer();
		World to = player.getWorld();
		World from = event.getFrom();
		boolean ignore = true;

		// Check to see if we should even bother checking
		if(!plugin.getConfig().getBoolean("handled-actions.world-transfers")){
			// Fix up inventories
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).fixInventory(player, event.getFrom());
			return;
		}

		// Check temp
		if(((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).isInTemporary(player)){
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).removeFromTemporary(player);
		}

		// Inventory check
		if(!plugin.getPermissions().has(player, PermissionNodes.NO_SWAP)){
			// Save from
			switch (player.getGameMode()){
			case CREATIVE:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveCreativeInventory(player, from);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderCreativeInventory(player, from);
				break;
			case SURVIVAL:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveSurvivalInventory(player, from);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderSurvivalInventory(player, from);
				break;
			default:
				if(ServerHas.adventureMode()){
					if(player.getGameMode() == GameMode.ADVENTURE){
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveAdventureInventory(player, from);
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderAdventureInventory(player, from);
					}
				}
				break;
			}

			// Check for linked inventories
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).checkLinks(player, to, from);

			// Update the inventories (check for merges)
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).refreshInventories(player, true);

			// Set to
			switch (player.getGameMode()){
			case CREATIVE:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getCreativeInventory(player, to).setTo(player);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderCreativeInventory(player, to).setTo(player); // Sets to the ender chest, not the player
				break;
			case SURVIVAL:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getSurvivalInventory(player, to).setTo(player);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderSurvivalInventory(player, to).setTo(player); // Sets to the ender chest, not the player
				break;
			default:
				if(ServerHas.adventureMode()){
					if(player.getGameMode() == GameMode.ADVENTURE){
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getAdventureInventory(player, to).setTo(player);
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderAdventureInventory(player, to).setTo(player); // Sets to the ender chest, not the player
					}
				}
				break;
			}

			// For alerts
			ignore = false;
		}

		// Alerts
		String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " changed to world " + ChatColor.YELLOW + to.getName();
		String playerMessage = ignore ? "no message" : "Your inventory has been changed to " + ChatColor.YELLOW + to.getName();
		plugin.getAlerts().alert(message, player, playerMessage, AlertType.GENERAL, AlertTrigger.GENERAL);
	}

	// ################# Player Quit

	@EventHandler (priority = EventPriority.MONITOR)
	public void onQuit(PlayerQuitEvent event){
		Player player = event.getPlayer();

		// Tell the inventory manager to release this player
		((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).releasePlayer(player);
	}

	// ################# Player Join

	@EventHandler (priority = EventPriority.MONITOR)
	public void onJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		
		// Tell the inventory manager to prepare this player
		((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).loadPlayer(player);
	}

	// ################# Player Game Mode Change

	@EventHandler (priority = EventPriority.LOW)
	public void onGameModeChange(PlayerGameModeChangeEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		GameMode from = player.getGameMode();
		GameMode to = event.getNewGameMode();

		// Check to see if we should even bother
		if(!plugin.getConfig().getBoolean("handled-actions.gamemode-inventories")){
			return;
		}

		// Check temp
		if(((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).isInTemporary(player)){
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).removeFromTemporary(player);
		}

		if(!plugin.getPermissions().has(player, PermissionNodes.NO_SWAP)){
			// Save from
			switch (from){
			case CREATIVE:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveCreativeInventory(player, player.getWorld());
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderCreativeInventory(player, player.getWorld());
				break;
			case SURVIVAL:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveSurvivalInventory(player, player.getWorld());
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderSurvivalInventory(player, player.getWorld());
				break;
			default:
				if(ServerHas.adventureMode()){
					if(from == GameMode.ADVENTURE){
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveAdventureInventory(player, player.getWorld());
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).saveEnderAdventureInventory(player, player.getWorld());
					}
				}
				break;
			}

			// Update inventories
			((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).refreshInventories(player, true);

			// Set to
			switch (to){
			case CREATIVE:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getCreativeInventory(player, player.getWorld()).setTo(player);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderCreativeInventory(player, player.getWorld()).setTo(player);
				break;
			case SURVIVAL:
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getSurvivalInventory(player, player.getWorld()).setTo(player);
				((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderSurvivalInventory(player, player.getWorld()).setTo(player);
				break;
			default:
				if(ServerHas.adventureMode()){
					if(from == GameMode.ADVENTURE){
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getAdventureInventory(player, player.getWorld()).setTo(player);
						((InventoryManager) plugin.getSystemsManager().getManager(Manager.INVENTORY)).getEnderAdventureInventory(player, player.getWorld()).setTo(player);
					}
				}
				break;
			}

			// Check for open inventories and stuff
			player.closeInventory();
		}else{
			return;
		}

		// Alerts
		String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + " changed to Game Mode " + ChatColor.YELLOW + to.name();
		String playerMessage = "Your inventory has been changed to " + ChatColor.YELLOW + to.name();
		if(!plugin.getConfig().getBoolean("other.send-gamemode-change-message")){
			playerMessage = "no message";
		}
		plugin.getAlerts().alert(message, player, playerMessage, AlertType.GENERAL, AlertTrigger.GENERAL);
	}
	
}
