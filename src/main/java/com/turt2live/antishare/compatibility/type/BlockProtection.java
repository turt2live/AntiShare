/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.compatibility.type;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public abstract class BlockProtection {

	public abstract boolean isProtected(Block block);

	public abstract boolean canAccess(Player player, Block block);

}
