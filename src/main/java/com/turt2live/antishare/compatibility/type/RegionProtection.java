package com.turt2live.antishare.compatibility.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

// TODO: Document
public abstract class RegionProtection {

	public abstract boolean isRegion(Location location);

	public abstract boolean isAllowed(Player player, Block block);

}
