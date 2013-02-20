package com.turt2live.antishare.compatibility.other;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.nisovin.magicspells.events.SpellCastEvent;
import com.nisovin.magicspells.events.SpellTargetEvent;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.GamemodeAbstraction;
import com.turt2live.antishare.permissions.PermissionNodes;

/**
 * MagicSpells hook
 * 
 * @author turt2live
 */
public class MagicSpells implements Listener {

	private boolean enabled = false;
	private AntiShare plugin = AntiShare.getInstance();

	public MagicSpells(){
		enabled = plugin.getConfig().getBoolean("magicspells.block-creative");
	}

	@EventHandler
	public void onTarget(SpellTargetEvent event){
		if(event.isCancelled() || !enabled){
			return;
		}
		Player player = event.getCaster();
		if(!plugin.getPermissions().has(player, PermissionNodes.PLUGIN_MAGIC_SPELLS) && GamemodeAbstraction.isCreative(player.getGameMode())){
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void onTarget(SpellCastEvent event){
		if(event.isCancelled() || !enabled){
			return;
		}
		Player player = event.getCaster();
		if(!plugin.getPermissions().has(player, PermissionNodes.PLUGIN_MAGIC_SPELLS) && GamemodeAbstraction.isCreative(player.getGameMode())){
			event.setCancelled(true);
		}
	}

}
