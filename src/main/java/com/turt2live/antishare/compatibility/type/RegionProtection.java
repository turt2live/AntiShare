/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.compatibility.type;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Abstraction for region protection plguins (like Towny)
 * 
 * @author turt2live
 */
public abstract class RegionProtection {

	/**
	 * Determines if there is a region at the specified location
	 * 
	 * @param location the location
	 * @return true if a region was found, of any sort. False otherwise
	 */
	public abstract boolean isRegion(Location location);

	/**
	 * Determines if the player is allowed to access a block
	 * 
	 * @param player the player
	 * @param block the block
	 * @return true if the player can access the block, false otherwise.
	 */
	public abstract boolean isAllowed(Player player, Block block);

}
