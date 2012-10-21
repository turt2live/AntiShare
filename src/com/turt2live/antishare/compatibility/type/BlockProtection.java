package com.turt2live.antishare.compatibility.type;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class BlockProtection {

	public abstract boolean isProtected(Block block);

	public abstract boolean canAccess(Player player, Block block);

}
