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

	/**
	 * Determines if a player can place a block in the world
	 * 
	 * @param player the player
	 * @param block the block
	 * @param world the world
	 * @return true if the player can place the block in the world
	 */
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

	/**
	 * Determines if a player can break a block in the world
	 * 
	 * @param player the player
	 * @param block the block
	 * @param world the world
	 * @return true if the player can break the block in the world
	 */
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

	/**
	 * Determines if a player can die with an item in their inventory in a world
	 * 
	 * @param player the player
	 * @param item the item to die with
	 * @param world the world
	 * @return true if the item would drop if they died with it in the world
	 */
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

	/**
	 * Determines if a player can throw an item in a world
	 * 
	 * @param player the player
	 * @param item the item
	 * @param world the world
	 * @return true if the item would be thrown by the player in the world
	 */
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

	/**
	 * Determines if a player can beat on a block in a world <br>
	 * Note: Storage/Powered carts are checked as well, pass in the ITEM version of them.
	 * 
	 * @param player the player
	 * @param object the Material
	 * @param world the world
	 * @return true if the player can play whack-a-mole in the world
	 */
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

	/**
	 * Determines if a player can beat on a Minecart in a world
	 * 
	 * @param player the player
	 * @param entity the minecart (storage or powered)
	 * @param world the world
	 * @return true if the player can hit the minecart
	 */
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

	/**
	 * Determines if a player can throw an egg in the world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if an egg (spawning or chicken) would work in the world
	 */
	public boolean canThrowEgg(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.eggs", world)){
			return getPlugin().config().getBoolean("hazards.allow_eggs", world);
		}
		return true;
	}

	/**
	 * Determines if a player can throw the experience bottle in the world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if the experience bottle would land with expected results
	 */
	public boolean canThrowExpBottle(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.exp", world)){
			return getPlugin().config().getBoolean("hazards.allow_exp_bottle", world);
		}
		return true;
	}

	/**
	 * Determines if a player can place, or break, bedrock in a world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if the player can place, or break, bedrock in the world
	 */
	public boolean canPlayWithBedrock(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.bedrock", world)){
			return getPlugin().config().getBoolean("hazards.allow_bedrock", world);
		}
		return true;
	}

	/**
	 * Determines if the player can use buckets in the world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if the player would be allowed to flood/burn a house with buckets
	 */
	public boolean canUseBuckets(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.buckets", world)){
			return getPlugin().config().getBoolean("hazards.allow_buckets", world);
		}
		return true;
	}

	/**
	 * Determines if a player can use fire charges in a world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if a fire block would be spawned by the use of the fire charge
	 */
	public boolean canUseFireCharge(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.firecharge", world)){
			return getPlugin().config().getBoolean("hazards.allow_fire_charge", world);
		}
		return true;
	}

	/**
	 * Determines if a player can use the lighter and/or fire blocks in a world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if fire would be spawned
	 */
	public boolean canUseFire(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.fire", world)){
			return getPlugin().config().getBoolean("hazards.allow_flint", world);
		}
		return true;
	}

	/**
	 * Determines if a player can place TNT in a world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if item 46 would appear
	 */
	public boolean canPlaceTNT(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.tnt", world)){
			return getPlugin().config().getBoolean("hazards.allow_tnt", world);
		}
		return true;
	}

	/**
	 * Determines if a player can hit other players in a world
	 * 
	 * @param player the player with the stick
	 * @param world the world
	 * @return true if damage would occur
	 */
	public boolean canHitPlayers(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.pvp", world)){
			return getPlugin().config().getBoolean("other.pvp", world);
		}
		return true;
	}

	/**
	 * Determines if a player would be allowed to hit mobs in a world
	 * 
	 * @param player the player
	 * @param world the world
	 * @return true if the sheep would cry
	 */
	public boolean canHitMobs(Player player, World world){
		if(player == null || world == null){
			return false;
		}
		if(getPlugin().isBlocked(player, "AntiShare.allow.mobpvp", world)){
			return getPlugin().config().getBoolean("other.pvp-mobs", world);
		}
		return true;
	}

	/**
	 * Determines if the "only if creative" flag is on for a player
	 * 
	 * @param player the player
	 * @return true if "only if creative" is enabled for the player
	 */
	public boolean isOnlyIfCreativeOn(Player player){
		if(player == null){
			return false;
		}
		return getPlugin().config().onlyIfCreative(player);
	}

	/**
	 * Determines if a player can teleport to a world
	 * 
	 * @param player the player
	 * @param to the destination
	 * @param from the departure world
	 * @return true if the player would reach his/her destination
	 */
	public boolean canTransferToWorld(Player player, World to, World from){
		if(player == null || to == null || from == null){
			return false;
		}
		return MultiWorld.worldSwap(getPlugin(), player, from, to);
	}

	/**
	 * Determines if a player can use a command in a world
	 * 
	 * @param player the player
	 * @param command the command
	 * @param world the world
	 * @return true if the command would be allowed to be typed
	 */
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
