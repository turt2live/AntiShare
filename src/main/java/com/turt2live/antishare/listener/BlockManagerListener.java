package com.turt2live.antishare.listener;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.blocks.BlockManager;
import com.turt2live.antishare.money.Tender.TenderType;
import com.turt2live.antishare.notification.MessageFactory;
import com.turt2live.antishare.notification.Alert.AlertTrigger;
import com.turt2live.antishare.notification.Alert.AlertType;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.util.ASUtils;

public class BlockManagerListener implements Listener {

	private BlockManager blocks;
	private AntiShare plugin = AntiShare.getInstance();
	
	public BlockManagerListener(BlockManager manager){
		blocks = manager;
	}

	// ################# Chunk Load

	@EventHandler
	public void onChunkLoad(ChunkLoadEvent event){
		blocks.loadChunk(event.getChunk());
	}

	// ################# Chunk Unload

	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event){
		blocks.unloadChunk(event.getChunk());
	}

	// ################# GameMode Block Break

	@EventHandler (priority = EventPriority.MONITOR)
	public void onGameModeBlockBreak(BlockBreakEvent event){
		if(!event.isCancelled()){
			blocks.removeBlock(event.getBlock());
		}
	}

	// ################# Block Place

	@EventHandler(priority=EventPriority.NORMAL)
	public void onBlockPlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		GameMode existing=null;
		AlertType type = AlertType.LEGAL;
		boolean handle = false;
		if(!event.isCancelled() && plugin.getConfig().getBoolean("enabled-features.attached-blocks-settings.disable-placing-mixed-gamemode")){
			Block source = event.getBlockAgainst();
			Block relative = event.getBlockPlaced();
			if(!plugin.getPermissions().has(player, PermissionNodes.FREE_PLACE)){
				GameMode potentialNewGM = player.getGameMode();
				if(ASUtils.isDroppedOnBreak(relative, source)){
					handle=true;
					 existing = ((BlockManager) plugin.getSystemsManager().getManager(Manager.BLOCK)).getType(source);
					if(existing != null){
						if(existing != potentialNewGM){
							event.setCancelled(plugin.shouldCancel(player, true));
							type=AlertType.ILLEGAL;
						}
					}
				}
			}
		}
		if(!handle){
			return;
		}
		Block block=event.getBlock();
		String message = ChatColor.YELLOW + player.getName() + ChatColor.WHITE + (type == AlertType.ILLEGAL ? " tried to attach " : " attached ") + (type == AlertType.ILLEGAL ? ChatColor.RED : ChatColor.GREEN) + block.getType().name().replace("_", " ") + ChatColor.WHITE + " onto a " + (existing!=null?existing.name().toLowerCase():"natural") + " block";
		String playerMessage = plugin.getMessage("blocked-action.attach-block");
		MessageFactory factory = new MessageFactory(playerMessage);
		factory.insert(block, player, block.getWorld(), TenderType.BLOCK_PLACE);
		playerMessage = factory.toString();
		plugin.getAlerts().alert(message, player, playerMessage, type, AlertTrigger.BLOCK_PLACE);
	}

	// ################# GameMode Block Place
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onGameModePlace(BlockPlaceEvent event){
		Player player = event.getPlayer();
		if(!event.isCancelled() &&!plugin.getPermissions().has(player, PermissionNodes.FREE_PLACE)){
			blocks.addBlock(player.getGameMode(), event.getBlock());
		}
	}
	
}
