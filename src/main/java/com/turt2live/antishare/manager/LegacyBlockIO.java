package com.turt2live.antishare.manager;

import org.bukkit.Location;
import org.bukkit.World;

// TODO: Document
public class LegacyBlockIO {

	public static Location locationFromString(World world, String string){
		String[] parts = string.split(";");
		return new Location(world, Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]));
	}

}
