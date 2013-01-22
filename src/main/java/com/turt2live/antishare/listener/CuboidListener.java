package com.turt2live.antishare.listener;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.manager.CuboidManager;
import com.turt2live.antishare.manager.CuboidManager.CuboidPoint;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.util.ASUtils;

public class CuboidListener implements Listener {

	private AntiShare plugin = AntiShare.getInstance();
	private CuboidManager manager;

	public CuboidListener(CuboidManager manager){
		this.manager = manager;
	}

	// ################# Player Interact Event (2)

	@EventHandler (priority = EventPriority.MONITOR)
	public void onInteract2(PlayerInteractEvent event){
		if(event.isCancelled()){
			return;
		}
		Player player = event.getPlayer();
		if(plugin.getPermissions().has(player, PermissionNodes.CREATE_CUBOID, player.getWorld())){
			ItemStack item = player.getItemInHand();
			if(item != null){
				if(item.getType() == AntiShare.ANTISHARE_CUBOID_TOOL){
					CuboidPoint point = null;
					switch (event.getAction()){
					case RIGHT_CLICK_BLOCK:
						point = CuboidPoint.POINT2;
						break;
					case LEFT_CLICK_BLOCK:
						point = CuboidPoint.POINT1;
						break;
					default:
						break;
					}
					if(point != null){
						Location location = event.getClickedBlock().getLocation();
						manager.updateCuboid(player.getName(), point, location);
						ASUtils.sendToPlayer(player, ChatColor.GREEN + "Point " + (point == CuboidPoint.POINT1 ? "1" : "2")
								+ " set as ("
								+ location.getBlockX() + ", "
								+ location.getBlockY() + ", "
								+ location.getBlockZ() + ", "
								+ location.getWorld().getName()
								+ "). Volume = " + ((CuboidManager) plugin.getSystemsManager().getManager(Manager.CUBOID)).getCuboid(player.getName()).getVolume(), true);
						event.setCancelled(true);
					}
				}
			}
		}
	}

}
