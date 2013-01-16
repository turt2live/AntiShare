package com.turt2live.antishare.tekkitcompat;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Hanging;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingBreakEvent.RemoveCause;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.blocks.BlockManager;
import com.turt2live.antishare.money.Tender.TenderType;
import com.turt2live.antishare.notification.Alert.AlertTrigger;
import com.turt2live.antishare.notification.Alert.AlertType;
import com.turt2live.antishare.notification.MessageFactory;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.regions.PerWorldConfig.ListType;
import com.turt2live.antishare.regions.Region;
import com.turt2live.antishare.regions.RegionManager;
import com.turt2live.antishare.util.ASUtils;

public class HangingListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();

	@EventHandler (priority = EventPriority.LOW)
	public void onPaintingBreak(HangingBreakEvent event){
		if(event.isCancelled() || !plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.paintings-are-attached")){
			return;
		}
		if(event.getCause() == RemoveCause.PHYSICS){
			// Removed by something
			Hanging hanging = event.getEntity();
			Location block = hanging.getLocation().getBlock().getRelative(hanging.getAttachedFace()).getLocation();
			GameMode gamemode = ((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).getRecentBreak(block);
			if(gamemode != null && gamemode == GameMode.CREATIVE){
				event.setCancelled(true);
				hanging.remove();
				((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
			}
		}
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onPaintingBreak(HangingPlaceEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		Hanging hanging = event.getEntity();
		AlertType type = AlertType.ILLEGAL;
		Material item = Material.PAINTING;
		if(ServerHas.mc14xEntities()){
			if(hanging.getType() == EntityType.ITEM_FRAME){
				item = Material.ITEM_FRAME;
			}
		}
		boolean region = false;

		// Check if they should be blocked
		if(!plugin.isBlocked(player, PermissionNodes.ALLOW_BLOCK_PLACE, PermissionNodes.DENY_BLOCK_PLACE, hanging.getWorld(), item)){
			type = AlertType.LEGAL;
		}
		Region asregion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(hanging.getLocation());
		if(asregion != null){
			if(!asregion.getConfig().isBlocked(item, ListType.BLOCK_PLACE)){
				type = AlertType.LEGAL;
			}
		}else{
			if(!plugin.getListener().getConfig(hanging.getWorld()).isBlocked(item, ListType.BLOCK_PLACE)){
				type = AlertType.LEGAL;
			}
		}

		if(!plugin.getPermissions().has(player, PermissionNodes.REGION_PLACE)){
			Region playerRegion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(player.getLocation());
			Region blockRegion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(hanging.getLocation());
			if(playerRegion != blockRegion){
				type = AlertType.ILLEGAL;
				region = true;
			}
		}

		// Handle event
		if(type == AlertType.ILLEGAL){
			event.setCancelled(plugin.shouldCancel(player, true));
		}else{
			// Handle block place for tracker
			if(!plugin.getPermissions().has(player, PermissionNodes.FREE_PLACE)){
				((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).addEntity(player.getGameMode(), hanging);
			}
		}

		// Alert
		if(region){
			if(type == AlertType.ILLEGAL){
				String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (type == AlertType.ILLEGAL ? " tried to place " : " placed ") + (type == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + item.name().replace("_", " ") + ChatColor.WHITE + " in a region.";
				String playerMessage = ChatColor.RED + "You cannot place blocks in another region!";
				plugin.getAlerts().alert(message, player, playerMessage, type, AlertTrigger.BLOCK_PLACE);
			}
		}else{
			String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (type == AlertType.ILLEGAL ? " tried to place " : " placed ") + (type == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + item.name().replace("_", " ");
			String playerMessage = plugin.getMessage("blocked-action.place-block");
			MessageFactory factory = new MessageFactory(playerMessage);
			factory.insert(item, player, hanging.getWorld(), TenderType.BLOCK_PLACE);
			playerMessage = factory.toString();
			plugin.getAlerts().alert(message, player, playerMessage, type, AlertTrigger.BLOCK_PLACE);
		}
	}

	@EventHandler (priority = EventPriority.LOW)
	public void onPaintingBreak(HangingBreakByEntityEvent event){
		if(event.isCancelled()){
			return;
		}
		Hanging hanging = event.getEntity();
		GameMode blockGamemode = ((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).getType(hanging);
		if(blockGamemode == null){
			return;
		}
		Material item = Material.PAINTING;
		if(ServerHas.mc14xEntities()){
			if(hanging.getType() == EntityType.ITEM_FRAME){
				item = Material.ITEM_FRAME;
			}
		}
		Entity remover = event.getRemover();
		if(remover instanceof Player){
			Player player = (Player) remover;
			if(player.getItemInHand() != null && plugin.getPermissions().has(player, PermissionNodes.TOOL_USE)){
				GameMode mode = ((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).getType(hanging);
				if(player.getItemInHand().getType() == AntiShare.ANTISHARE_SET_TOOL){
					if(mode != null){
						((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
					}
					((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).addEntity(player.getGameMode(), hanging);
					event.setCancelled(plugin.shouldCancel(player, true));
					ASUtils.sendToPlayer(player, ChatColor.GREEN + ASUtils.capitalize(item.name()) + " set as " + ChatColor.DARK_GREEN + player.getGameMode().name(), true);
					return;
				}else if(player.getItemInHand().getType() == AntiShare.ANTISHARE_TOOL){
					ASUtils.sendToPlayer(player, ChatColor.WHITE + "That " + ChatColor.YELLOW + ASUtils.capitalize(item.name()) + ChatColor.WHITE + " is " + ChatColor.YELLOW + (mode != null ? mode.name().toLowerCase() : "natural"), true);
					event.setCancelled(plugin.shouldCancel(player, true));
					return;
				}
			}
			AlertType type = AlertType.ILLEGAL;
			boolean special = false;
			boolean region = false;
			Boolean drops = null;
			boolean deny = false;
			AlertType specialType = AlertType.LEGAL;
			String blockGM = "Unknown";

			// Check if they should be blocked
			if(!plugin.isBlocked(player, PermissionNodes.ALLOW_BLOCK_BREAK, PermissionNodes.DENY_BLOCK_BREAK, hanging.getWorld(), item)){
				type = AlertType.LEGAL;
			}
			Region asregion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(hanging.getLocation());
			if(asregion != null){
				if(!asregion.getConfig().isBlocked(item, ListType.BLOCK_BREAK)){
					type = AlertType.LEGAL;
				}
			}else{
				if(!plugin.getListener().getConfig(hanging.getWorld()).isBlocked(item, ListType.BLOCK_BREAK)){
					type = AlertType.LEGAL;
				}
			}

			// Check creative/survival blocks
			if(!plugin.getPermissions().has(player, PermissionNodes.FREE_PLACE)){
				if(blockGamemode != null){
					blockGM = blockGamemode.name().toLowerCase();
					String oGM = player.getGameMode().name().toLowerCase();
					if(player.getGameMode() != blockGamemode){
						special = true;
						deny = plugin.getConfig().getBoolean("settings." + oGM + "-breaking-" + blockGM + "-blocks.deny");
						drops = plugin.getConfig().getBoolean("settings." + oGM + "-breaking-" + blockGM + "-blocks.block-drops");
						if(deny){
							specialType = AlertType.ILLEGAL;
						}
					}
				}
			}

			// Check regions
			if(!plugin.getPermissions().has(player, PermissionNodes.REGION_BREAK)){
				Region playerRegion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(player.getLocation());
				Region blockRegion = ((RegionManager) plugin.getSystemsManager().getManager(Manager.REGION)).getRegion(hanging.getLocation());
				if(playerRegion != blockRegion){
					special = true;
					region = true;
					specialType = AlertType.ILLEGAL;
				}
			}

			// Handle event
			if(type == AlertType.ILLEGAL || specialType == AlertType.ILLEGAL){
				event.setCancelled(plugin.shouldCancel(player, false));
			}else{
				((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
			}

			// Alert
			if(special){
				if(region){
					if(specialType == AlertType.ILLEGAL){
						String specialMessage = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (specialType == AlertType.ILLEGAL ? " tried to break " : " broke  ") + (specialType == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + item.name().replace("_", " ") + ChatColor.WHITE + " in a region.";
						String specialPlayerMessage = ChatColor.RED + "You cannot break blocks that are not in your region";
						plugin.getAlerts().alert(specialMessage, player, specialPlayerMessage, specialType, AlertTrigger.BLOCK_BREAK);
					}
				}else{
					String specialMessage = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (specialType == AlertType.ILLEGAL ? " tried to break the " + blockGM + " block " : " broke the " + blockGM + " block ") + (specialType == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + item.name().replace("_", " ");
					String specialPlayerMessage = plugin.getMessage("blocked-action." + blockGM + "-block-break");
					MessageFactory factory = new MessageFactory(specialPlayerMessage);
					factory.insert(item, player, hanging.getWorld(), blockGM.equalsIgnoreCase("creative") ? TenderType.CREATIVE_BLOCK : (blockGM.equalsIgnoreCase("survival") ? TenderType.SURVIVAL_BLOCK : TenderType.ADVENTURE_BLOCK));
					specialPlayerMessage = factory.toString();
					plugin.getAlerts().alert(specialMessage, player, specialPlayerMessage, specialType, (blockGM.equalsIgnoreCase("creative") ? AlertTrigger.CREATIVE_BLOCK : (blockGM.equalsIgnoreCase("survival") ? AlertTrigger.SURVIVAL_BLOCK : AlertTrigger.ADVENTURE_BLOCK)));
				}
			}else{
				String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (type == AlertType.ILLEGAL ? " tried to break " : " broke ") + (type == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + item.name().replace("_", " ");
				String playerMessage = plugin.getMessage("blocked-action.break-block");
				MessageFactory factory = new MessageFactory(playerMessage);
				factory.insert(item, player, hanging.getWorld(), TenderType.BLOCK_BREAK);
				playerMessage = factory.toString();
				plugin.getAlerts().alert(message, player, playerMessage, type, AlertTrigger.BLOCK_BREAK);
			}

			// Handle drops
			if(drops != null && !deny && special){
				if(drops){
					((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
					hanging.getWorld().dropItemNaturally(hanging.getLocation(), new ItemStack(item));
					hanging.remove();
				}else{
					((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
					hanging.remove();
				}
			}
		}else{
			if(blockGamemode == GameMode.CREATIVE && plugin.getConfig().getBoolean("enabled-features.no-drops-when-block-break.paintings-are-attached")){
				event.setCancelled(true);
				hanging.remove();
				((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).removeEntity(hanging);
			}
		}
	}
}
