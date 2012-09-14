/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
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
