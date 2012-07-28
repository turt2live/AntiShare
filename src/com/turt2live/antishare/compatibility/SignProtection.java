package com.turt2live.antishare.compatibility;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class SignProtection {

	public abstract boolean isProtected(Block block);

	public abstract boolean canAccess(Player player, Block block);

}
