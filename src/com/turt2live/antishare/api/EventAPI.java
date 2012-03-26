package com.turt2live.antishare.api;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.PoweredMinecart;
import org.bukkit.entity.StorageMinecart;
import org.bukkit.inventory.ItemStack;

import com.turt2live.antishare.MultiWorld;
import com.turt2live.antishare.enums.BlockedType;
import com.turt2live.antishare.storage.VirtualStorage;

public class EventAPI extends APIBase {

	// TODO: JavaDocs

	public boolean canPlaceBlock(Player player, Block block, World world){
		if(player == null || block == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.place", world)){
			VirtualStorage store = getPlugin().storage;
			return !store.isBlocked(block.getType(), BlockedType.BLOCK_PLACE, world);
		}
		return true;
	}

	public boolean canBreakBlock(Player player, Block block, World world){
		if(player == null || block == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.break", world)){
			VirtualStorage store = getPlugin().storage;
			return !store.isBlocked(block.getType(), BlockedType.BLOCK_BREAK, world);
		}
		return true;
	}

	public boolean canDieWithItem(Player player, ItemStack item, World world){
		if(player == null || item == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.death", world)){
			VirtualStorage store = getPlugin().storage;
			return !store.isBlocked(item, BlockedType.DEATH, world);
		}
		return true;
	}

	public boolean canDropItem(Player player, ItemStack item, World world){
		if(player == null || item == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.drop", world)){
			VirtualStorage store = getPlugin().storage;
			return !store.isBlocked(item, BlockedType.DROP_ITEM, world);
		}
		return true;
	}

	public boolean canInteractWith(Player player, Material object, World world){
		if(player == null || object == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.interact", world)){
			VirtualStorage store = getPlugin().storage;
			return !store.isBlocked(object, BlockedType.INTERACT, world);
		}
		return true;
	}

	public boolean canInteractWith(Player player, Entity entity, World world){
		if(player == null || entity == null || world == null){
			return false;
		}
		Material item = Material.AIR;
		if(entity instanceof StorageMinecart){
			item = Material.STORAGE_MINECART;
		}else if(entity instanceof PoweredMinecart){
			item = Material.POWERED_MINECART;
		}
		return canInteractWith(player, item, world);
	}

	public boolean canThrowEgg(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.eggs", world)){
			return getPlugin().config().getBoolean("hazards.allow_eggs", world);
		}
		return true;
	}

	public boolean canThrowExpBottle(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.exp", world)){
			return getPlugin().config().getBoolean("hazards.allow_exp_bottle", world);
		}
		return true;
	}

	public boolean canPlayWithBedrock(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.bedrock", world)){
			return getPlugin().config().getBoolean("hazards.allow_bedrock", world);
		}
		return true;
	}

	public boolean canUseBuckets(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.buckets", world)){
			return getPlugin().config().getBoolean("hazards.allow_buckets", world);
		}
		return true;
	}

	public boolean canUseFireCharge(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.firecharge", world)){
			return getPlugin().config().getBoolean("hazards.allow_fire_charge", world);
		}
		return true;
	}

	public boolean canUseFire(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.fire", world)){
			return getPlugin().config().getBoolean("hazards.allow_flint", world);
		}
		return true;
	}

	public boolean canPlaceTNT(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.tnt", world)){
			return getPlugin().config().getBoolean("hazards.allow_tnt", world);
		}
		return true;
	}

	public boolean canHitPlayers(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.pvp", world)){
			return getPlugin().config().getBoolean("other.pvp", world);
		}
		return true;
	}

	public boolean canHitMobs(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.mobpvp", world)){
			return getPlugin().config().getBoolean("other.pvp-mobs", world);
		}
		return true;
	}

	public boolean isOnlyIfCreativeOn(Player player){
		if(player == null){
			return false;
		}
		return getPlugin().config().onlyIfCreative(player);
	}

	public boolean canTransferToWorld(Player player, World to, World from){
		if(player == null || to == null || from == null){
			return false;
		}
		return MultiWorld.worldSwap(getPlugin(), player, from, to);
	}

	public boolean canUseCommand(Player player, String command, World world){
		if(player == null || command == null || world == null){
			return false;
		}
		if(!command.startsWith("/")){
			command = "/" + command;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.commands", world)){
			return !getPlugin().storage.commandBlocked(command, player.getWorld());
		}
		return true;
	}
}
