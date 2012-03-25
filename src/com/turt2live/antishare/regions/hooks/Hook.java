package com.turt2live.antishare.regions.hooks;

import org.bukkit.entity.Player;

import com.turt2live.antishare.regions.Selection;

public interface Hook {

	public boolean inRegion(Player player);

	public boolean hasRegion(Selection location);

	public boolean exists();

	public String getName();
}
