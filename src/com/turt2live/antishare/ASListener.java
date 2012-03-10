package com.turt2live.antishare;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Monster;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.enums.BlockedType;
import com.turt2live.antishare.enums.NotificationType;
import com.turt2live.antishare.regions.ASRegion;
import com.turt2live.antishare.storage.ASVirtualInventory;

public class ASListener implements Listener {

	private AntiShare plugin;
	private HashMap<Player, Long> blockDropTextWarnings = new HashMap<Player, Long>();

	public ASListener(AntiShare p){
		plugin = p;
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onBlockBreak(BlockBreakEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		if(player == null){
			return;
		}
		ASRegion region = plugin.getRegionHandler().getRegion(player.getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You are in the wrong GameMode for this area");
					player.setGameMode(region.getGameModeSwitch());
					return;
				}
			}
		}
		region = plugin.getRegionHandler().getRegion(event.getBlock().getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot break that due to a GameMode region");
					return;
				}
			}
		}
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.bedrock", player.getWorld())
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode() == GameMode.CREATIVE){
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
					event.setCancelled(true);
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}
			}else{
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				ASNotification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				event.setCancelled(true);
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
		if(event.isCancelled()){
			return;
		}
		if(plugin.storage.isBlocked(event.getBlock().getType(), BlockedType.BLOCK_BREAK, player.getWorld())){
			//System.out.println("BLOCK BREAK: ILLEGAL");
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)
						&& !plugin.getPermissions().has(player, "AntiShare.allow.break", player.getWorld())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_break", player.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				if(!plugin.getPermissions().has(player, "AntiShare.allow.break", player.getWorld())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_break", player.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
		if(event.isCancelled()){
			return;
		}
		if(plugin.config().getBoolean("other.track_blocks", player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.blockBypass", player.getWorld())){
			if(player.getGameMode().equals(GameMode.SURVIVAL)){
				boolean isBlocked = plugin.storage.isCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
				if(isBlocked){
					if(!plugin.config().getBoolean("other.blockDrops", player.getWorld())){
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.creativeModeBlock", player.getWorld()));
						event.setCancelled(true);
					}else{
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.creativeModeBlock", player.getWorld()));
						plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
						Block block = event.getBlock();
						event.setCancelled(true);
						block.setTypeId(0); // Fakes a break
					}
					ASNotification.sendNotification(NotificationType.ILLEGAL_CREATIVE_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld());
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_CREATIVE_BLOCK_BREAK, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onBlockDamage(BlockDamageEvent event){
		if(event.isCancelled() || event.getPlayer() == null){
			return;
		}
		Player player = event.getPlayer();
		if(!event.isCancelled()){
			if(plugin.config().getBoolean("other.blockDrops", player.getWorld())
					&& !plugin.getPermissions().has(player, "AntiShare.blockBypass", player.getWorld())
					&& plugin.storage.isCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_BREAK, event.getBlock().getWorld())){
				long systemTime = System.currentTimeMillis();
				if(blockDropTextWarnings.containsKey(player)){
					if((systemTime - blockDropTextWarnings.get(player)) > 1000){
						ASUtils.sendToPlayer(player, plugin.getConfig().getString("messages.noBlockDrop"));
						blockDropTextWarnings.remove(player);
						blockDropTextWarnings.put(player, systemTime);
					}
				}else{
					ASUtils.sendToPlayer(player, plugin.getConfig().getString("messages.noBlockDrop"));
					blockDropTextWarnings.put(player, systemTime);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onBlockPlace(BlockPlaceEvent event){
		if(event.isCancelled() || event.getPlayer() == null){
			return;
		}
		Player player = event.getPlayer();
		ASRegion region = plugin.getRegionHandler().getRegion(player.getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You are in the wrong GameMode for this area");
					player.setGameMode(region.getGameModeSwitch());
					return;
				}
			}
		}
		region = plugin.getRegionHandler().getRegion(event.getBlock().getLocation());
		if(region != null){
			if(!plugin.getPermissions().has(player, "AntiShare.roam", player.getWorld())){
				if(!player.getGameMode().equals(region.getGameModeSwitch())){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, ChatColor.RED + "You cannot place there due to a GameMode region");
					return;
				}
			}
		}
		if(plugin.storage.isBlocked(event.getBlock().getType(), BlockedType.BLOCK_PLACE, player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.allow.place", player.getWorld())){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					ASNotification.sendNotification(NotificationType.ILLEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_place", player.getWorld()));
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				}
			}else{
				event.setCancelled(true);
				ASNotification.sendNotification(NotificationType.ILLEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.block_place", player.getWorld()));
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_BLOCK_PLACE, plugin, player, event.getBlock().getType().name(), event.getBlock().getType());
		}
		if(event.isCancelled()){
			return;
		}
		//Bedrock check
		if(plugin.storage.bedrockBlocked(player.getWorld())
				&& !plugin.getPermissions().has(player, "AntiShare.bedrock", player.getWorld())
				&& event.getBlock().getType().equals(Material.BEDROCK)){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
				}
			}else{
				event.setCancelled(true);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.bedrock", player.getWorld()));
				ASNotification.sendNotification(NotificationType.ILLEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_BEDROCK, plugin, player, "BEDROCK", Material.BEDROCK);
		}
		if(event.isCancelled()){
			return;
		}
		//Creative Mode Placing
		if(plugin.config().getBoolean("other.track_blocks", player.getWorld())
				&& player.getGameMode() == GameMode.CREATIVE
				&& !plugin.getPermissions().has(player, "AntiShare.freePlace", player.getWorld())){
			plugin.storage.saveCreativeBlock(event.getBlock(), BlockedType.CREATIVE_BLOCK_PLACE, event.getBlock().getWorld());
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onEntityDamage(EntityDamageEvent event){
		if(!(event instanceof EntityDamageByEntityEvent)
				|| event.isCancelled()){
			return;
		}
		String entityName = event.getEntity().getClass().getName().replace("Craft", "").replace("org.bukkit.craftbukkit.entity.", "");
		Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
		if(damager instanceof Player){
			Player dealer = (Player) damager;
			if(!dealer.getGameMode().equals(GameMode.CREATIVE) && !plugin.config().onlyIfCreative(dealer)){
				return;
			}else if(!dealer.getGameMode().equals(GameMode.CREATIVE)){
				return;
			}
			//System.out.println("GM: " + dealer.getGameMode().toString());
			if(event.getEntity() instanceof Player){
				String targetName = ((Player) event.getEntity()).getName();
				if(plugin.config().getBoolean("other.pvp", dealer.getWorld())){
					ASNotification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, targetName, null);
					return;
				}
				if(!plugin.getPermissions().has(dealer, "AntiShare.pvp", dealer.getWorld())){
					ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.pvp", event.getEntity().getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_PLAYER_PVP, plugin, dealer, targetName, null);
					event.setCancelled(true);
				}
			}else{
				if(plugin.config().getBoolean("other.pvp-mobs", dealer.getWorld())){
					ASNotification.sendNotification(NotificationType.LEGAL_MOB_PVP, plugin, dealer, entityName, null);
					return;
				}
				if(!plugin.getPermissions().has(dealer, "AntiShare.mobpvp", dealer.getWorld())){
					ASNotification.sendNotification(NotificationType.ILLEGAL_MOB_PVP, plugin, dealer, entityName, null);
					ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.mobpvp", event.getEntity().getWorld()));
					event.setCancelled(true);
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_MOB_PVP, plugin, dealer, entityName, null);
				}
			}
		}else if(damager instanceof Projectile){
			LivingEntity shooter = ((Projectile) damager).getShooter();
			if(shooter instanceof Player){
				Player dealer = ((Player) shooter);
				if(!dealer.getGameMode().equals(GameMode.CREATIVE) && !plugin.config().onlyIfCreative(dealer)){
					ASNotification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
					return;
				}else if(!dealer.getGameMode().equals(GameMode.CREATIVE)){
					ASNotification.sendNotification(NotificationType.LEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
					return;
				}
				if(!plugin.getPermissions().has(dealer, "AntiShare.pvp", dealer.getWorld())){
					ASUtils.sendToPlayer(dealer, plugin.config().getString("messages.pvp", dealer.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_PLAYER_PVP, plugin, dealer, entityName, null);
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onEntityDeath(EntityDeathEvent event){
		// System.out.println("onDeath | " + event.getEntity());
		if(event.getEntity() instanceof Player){
			Player player = (Player) event.getEntity();
			if(player == null){
				return;
			}
			boolean illegal = false;
			if(!plugin.getPermissions().has(player, "AntiShare.allow.death", player.getWorld())){
				boolean doCheck = false;
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						doCheck = true;
					}
				}else{
					doCheck = true;
				}
				if(doCheck){
					for(ItemStack item : event.getDrops()){
						if(plugin.storage.isBlocked(item, BlockedType.DEATH, player.getWorld())){
							illegal = true;
							item.setAmount(0);
						}
					}
				}
			}
			if(illegal){
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.death", player.getWorld()));
				ASNotification.sendNotification(NotificationType.ILLEGAL_DEATH, plugin, player, player.getGameMode().toString(), null);
			}else{
				ASNotification.sendNotification(NotificationType.LEGAL_DEATH, plugin, player, player.getGameMode().toString(), null);
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onEntityTarget(EntityTargetEvent event){
		if(event.isCancelled()){
			return;
		}

		Entity targetEntity = event.getTarget();
		if(event.getEntity() instanceof Monster
				&& targetEntity != null
				&& targetEntity instanceof Player){
			Player player = (Player) targetEntity;
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					if(!plugin.getPermissions().has(player, "AntiShare.mobpvp", player.getWorld())
							&& !plugin.config().getBoolean("other.pvp-mobs", player.getWorld())){
						event.setCancelled(true);
					}
				}
			}else{
				if(!plugin.getPermissions().has(player, "AntiShare.mobpvp", player.getWorld())
						&& !plugin.config().getBoolean("other.pvp-mobs", player.getWorld())){
					event.setCancelled(true);
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerCommand(PlayerCommandPreprocessEvent event){
		String commandSent = event.getMessage();
		if(event.isCancelled()){
			return;
		}
		Player sender = event.getPlayer();
		if(plugin.getPermissions().has(sender, "AntiShare.allow.commands", sender.getWorld())){
			ASNotification.sendNotification(NotificationType.LEGAL_COMMAND, plugin, sender, commandSent, null);
			return;
		}
		if(plugin.config().onlyIfCreative(sender)){
			if(sender.getGameMode().equals(GameMode.SURVIVAL)){
				ASNotification.sendNotification(NotificationType.LEGAL_COMMAND, plugin, sender, commandSent, null);
				return;
			}
		}
		if(plugin.storage.commandBlocked(commandSent, sender.getWorld())){
			ASUtils.sendToPlayer(sender, plugin.getConfig().getString("messages.illegalCommand"));
			ASNotification.sendNotification(NotificationType.ILLEGAL_COMMAND, plugin, sender, commandSent, null);
			event.setCancelled(true);
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerDropItem(PlayerDropItemEvent event){
		if(event.isCancelled() || event.getPlayer() == null){
			return;
		}
		Player player = event.getPlayer();
		if(plugin.getPermissions().has(player, "AntiShare.allow.drop", player.getWorld())){
			ASNotification.sendNotification(NotificationType.LEGAL_DROP_ITEM, plugin, player, event.getItemDrop().getItemStack().getType().name(), event.getItemDrop().getItemStack().getType());
			return;
		}
		if(plugin.storage.isBlocked(event.getItemDrop().getItemStack(), BlockedType.DROP_ITEM, player.getWorld())){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setCancelled(true);
					ASNotification.sendNotification(NotificationType.ILLEGAL_DROP_ITEM, plugin, player, event.getItemDrop().getItemStack().getType().name(), event.getItemDrop().getItemStack().getType());
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.drop_item", player.getWorld()));
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_DROP_ITEM, plugin, player, event.getItemDrop().getItemStack().getType().name(), event.getItemDrop().getItemStack().getType());
				}
			}else{
				event.setCancelled(true);
				ASNotification.sendNotification(NotificationType.ILLEGAL_DROP_ITEM, plugin, player, event.getItemDrop().getItemStack().getType().name(), event.getItemDrop().getItemStack().getType());
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.drop_item", player.getWorld()));
			}
		}
		if(event.isCancelled()){
			return;
		}
		// TODO Fix this
		if(plugin.config().getBoolean("other.cannot_throw_into_regions", player.getWorld())){
			Item item = event.getItemDrop();
			ASRegion region = plugin.getRegionHandler().getRegion(item.getLocation());
			if(!plugin.getPermissions().has(player, "AntiShare.allow.throwIntoRegions")){
				if(plugin.getRegionHandler().isRegion(item.getLocation())){
					event.setCancelled(true);
					ASNotification.sendNotification(NotificationType.ILLEGAL_ITEM_THROW_INTO_REGION, player, region.getName());
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.throwItemIntoRegion", player.getWorld()));
				}else{
					if(region != null){
						ASNotification.sendNotification(NotificationType.LEGAL_ITEM_THROW_INTO_REGION, player, region.getName());
					}
				}
			}else{
				if(region != null){
					ASNotification.sendNotification(NotificationType.LEGAL_ITEM_THROW_INTO_REGION, player, region.getName());
				}
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerGameModeChange(PlayerGameModeChangeEvent event){
		Player player = event.getPlayer();
		if(plugin.config().getBoolean("other.inventory_swap", event.getPlayer().getWorld())
				&& !plugin.getConflicts().INVENTORY_CONFLICT_PRESENT){
			if(player != null){
				if(!plugin.getPermissions().has(player, "AntiShare.noswap", player.getWorld())){
					plugin.storage.getInventoryManager(player, player.getWorld()).switchInventories(player.getGameMode(), event.getNewGameMode());
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.inventory_swap", player.getWorld()));
				}
			}
		}
		ASNotification.sendNotification(NotificationType.GAMEMODE_CHANGE, plugin, player, event.getNewGameMode().toString(), null);
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerInteract(PlayerInteractEvent event){
		Player player = event.getPlayer();
		if(event.isCancelled() || player == null){
			return;
		}
		//		// Exp Bottle Check (stuck here to avoid the null check)
		//		if(plugin.config().getBoolean("other.allow_exp_bottle", player.getWorld()) == false){
		//			boolean skip = false;
		//			ItemStack possibleBottle = event.getItem();
		//			if(possibleBottle != null){
		//				if(!possibleBottle.getType().equals(Material.EXP_BOTTLE)){
		//					skip = true;
		//				}
		//			}else{
		//				skip = true;
		//			}
		//			possibleBottle = player.getItemInHand();
		//			if(possibleBottle != null){
		//				if(!possibleBottle.getType().equals(Material.EXP_BOTTLE)){
		//					skip = true;
		//				}
		//			}else{
		//				skip = true;
		//			}
		//			if(!skip){
		//				if(plugin.getPermissions().has(player, "AntiShare.allow.exp", player.getWorld())){
		//					ASNotification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
		//					return;
		//				}
		//				if(plugin.config().onlyIfCreative(player)){
		//					if(player.getGameMode().equals(GameMode.CREATIVE)){
		//						event.setCancelled(true);
		//						ASNotification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
		//						ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
		//					}else{
		//						ASNotification.sendNotification(NotificationType.LEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
		//					}
		//				}else{
		//					event.setCancelled(true);
		//					ASNotification.sendNotification(NotificationType.ILLEGAL_EXP_BOTTLE, plugin, player, "EXPERIENCE BOTTLE", null);
		//					ASUtils.sendToPlayer(player, plugin.config().getString("messages.exp_bottle", player.getWorld()));
		//				}
		//			}
		//		}
		if(event.getClickedBlock() == null || event.isCancelled()){
			return;
		}
		if(!plugin.getPermissions().has(player, "AntiShare.allow.interact", player.getWorld())
				&& plugin.storage.isBlocked(event.getClickedBlock().getType(), BlockedType.INTERACT, player.getWorld())){
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
						event.setCancelled(true);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.interact", player.getWorld()));
						ASNotification.sendNotification(NotificationType.ILLEGAL_INTERACTION, plugin, player, event.getClickedBlock().getType().name(), event.getClickedBlock().getType());
					}
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_INTERACTION, plugin, player, event.getClickedBlock().getType().name(), event.getClickedBlock().getType());
				}
			}else{
				if(event.getAction().equals(Action.RIGHT_CLICK_AIR) || event.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
					event.setCancelled(true);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.interact", player.getWorld()));
					ASNotification.sendNotification(NotificationType.ILLEGAL_INTERACTION, plugin, player, event.getClickedBlock().getType().name(), event.getClickedBlock().getType());
				}
			}
		}else{
			ASNotification.sendNotification(NotificationType.LEGAL_INTERACTION, plugin, player, event.getClickedBlock().getType().name(), event.getClickedBlock().getType());
		}
		if(event.isCancelled()){
			return;
		}
		boolean skip = false;
		//Egg check
		if(plugin.config().getBoolean("other.allow_eggs", player.getWorld()) == false){
			ItemStack possibleEgg = event.getItem();
			if(possibleEgg != null){
				if(possibleEgg.getTypeId() != 383){
					skip = true;
				}
			}else{
				skip = true;
			}
			if(!skip){
				if(plugin.getPermissions().has(player, "AntiShare.allow.eggs", player.getWorld())){
					ASNotification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "MONSTER EGG", null);
					return;
				}
				// At this point the player is not allowed to use eggs, and we are dealing with an egg
				if(plugin.config().onlyIfCreative(player)){
					if(player.getGameMode().equals(GameMode.CREATIVE)){
						event.setCancelled(true);
						ASNotification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "MONSTER EGG", null);
						ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
					}else{
						ASNotification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "MONSTER EGG", null);
					}
				}else{
					event.setCancelled(true);
					ASNotification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "MONSTER EGG", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
				}
			}
		}
		if(event.isCancelled()){
			return;
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onEggThrow(PlayerEggThrowEvent event){
		Player player = event.getPlayer();
		if(plugin.config().getBoolean("other.allow_eggs", player.getWorld()) == false){
			if(plugin.getPermissions().has(player, "AntiShare.allow.eggs", player.getWorld())){
				ASNotification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "EGG", null);
				return;
			}
			// At this point the player is not allowed to use eggs, and we are dealing with an egg
			if(plugin.config().onlyIfCreative(player)){
				if(player.getGameMode().equals(GameMode.CREATIVE)){
					event.setHatching(false);
					ASNotification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "EGG", null);
					ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
				}else{
					ASNotification.sendNotification(NotificationType.LEGAL_EGG, plugin, player, "EGG", null);
				}
			}else{
				event.setHatching(false);
				ASNotification.sendNotification(NotificationType.ILLEGAL_EGG, plugin, player, "EGG", null);
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.eggs", player.getWorld()));
			}
		}
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerPortal(PlayerPortalEvent event){
		onPlayerTeleport(event);
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerTeleport(PlayerTeleportEvent event){
		if(plugin.getConflicts().INVENTORY_CONFLICT_PRESENT
				|| plugin.getConflicts().WORLD_MANAGER_CONFLICT_PRESENT){
			return;
		}
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		if(!event.getFrom().getWorld().equals(event.getTo().getWorld())){
			boolean cancel = !ASMultiWorld.worldSwap(plugin, player, event.getFrom(), event.getTo());
			if(cancel){
				ASUtils.sendToPlayer(player, plugin.config().getString("messages.worldSwap", event.getTo().getWorld()));
				ASNotification.sendNotification(NotificationType.ILLEGAL_WORLD_CHANGE, plugin, player, event.getTo().getWorld().getName(), null);
				event.setCancelled(true);
			}else{
				scheduleInventoryChange(player, event);
			}
		}
	}

	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event){
		if(event.isCancelled()){
			return;
		}
		plugin.getRegionHandler().checkRegion(event.getPlayer(), event.getTo(), event.getFrom());
	}

	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerJoin(PlayerJoinEvent event){
		Player player = event.getPlayer();
		ASVirtualInventory manager = plugin.storage.getInventoryManager(player, player.getWorld());
		manager.makeMatch();
	}

	public void scheduleInventoryChange(final Player player, final PlayerTeleportEvent event){
		new Thread(new Runnable(){
			@Override
			public void run(){
				long time = System.currentTimeMillis();
				while (player.getLocation().getWorld() != event.getTo().getWorld()){
					if((System.currentTimeMillis() - time) >= 10000){
						AntiShare.log.severe("[" + plugin.getDescription().getFullName() + "] ERROR: World transfer inventory change took longer than 10 seconds!");
						AntiShare.log.severe("[" + plugin.getDescription().getFullName() + "] Please report this to turt2live! http://mc.turt2live.com/plugins/bug.php?simple&plugin=AntiShare");
						AntiShare.log.severe("[" + plugin.getDescription().getFullName() + "] Please provide this next line in your bug report:");
						AntiShare.log.severe("[" + plugin.getDescription().getFullName() + "] FROM: " + event.getFrom().getWorld().getName() + " TO: " + event.getTo().getWorld().getName() + " LOC: " + player.getLocation().getWorld().getName());
						return;
					}
				}
				if(!plugin.getPermissions().has(player, "AntiShare.worlds", event.getTo().getWorld())){
					plugin.storage.switchInventories(player, event.getFrom().getWorld(), player.getGameMode(), event.getTo().getWorld(), player.getGameMode());
				}
				ASNotification.sendNotification(NotificationType.LEGAL_WORLD_CHANGE, plugin, player, event.getTo().getWorld().getName(), null);
			}
		}).start();
	}
}
