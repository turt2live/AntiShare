package com.turt2live.antishare;

import org.bukkit.Location;
import org.bukkit.World;

import com.sk89q.worldedit.bukkit.selections.CuboidSelection;

/**
 * Compatibility for WorldEdit (ClassNotFoundException)
 * 
 * @author turt2live
 */
public class Selection {

	private World world;
	private Location minimum, maximum;

	/**
	 * Creates a new Selection
	 * 
	 * @param world the world
	 * @param minimum the minimum
	 * @param maximum the maximum
	 */
	public Selection(World world, Location minimum, Location maximum){
		this.world = world;
		this.maximum = maximum;
		this.minimum = minimum;
	}

	/**
	 * Compatibility for WorldEdit and AntiShare
	 * 
	 * @param selection the WorldEdit selection
	 */
	public Selection(com.sk89q.worldedit.bukkit.selections.Selection selection){
		this.world = selection.getWorld();
		this.minimum = selection.getMinimumPoint();
		this.maximum = selection.getMaximumPoint();
	}

	/**
	 * Gets the WorldEdit selection
	 * 
	 * @return the selection
	 */
	public com.sk89q.worldedit.bukkit.selections.Selection getWorldEditSelection(){
		return new CuboidSelection(world, minimum, maximum);
	}

}
