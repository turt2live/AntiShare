package com.turt2live.antishare.regions;

import org.bukkit.Location;

public class Selection {

	public Location minimum;
	public Location maximum;

	public Selection(Location min, Location max){
		this.maximum = max;
		this.minimum = min;
	}

	public Selection(com.sk89q.worldedit.bukkit.selections.Selection worldEditSelection){
		this.maximum = worldEditSelection.getMaximumPoint();
		this.minimum = worldEditSelection.getMinimumPoint();
	}

	// TODO: Add MobArena constructor
}
