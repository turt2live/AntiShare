package com.turt2live.antishare.util.generic;

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
