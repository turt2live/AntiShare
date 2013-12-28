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

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Abstraction for block protection plugins (like LWC)
 *
 * @author turt2live
 */
public abstract class BlockProtection {

    /**
     * Determines if a block is protected
     *
     * @param block the block to check
     * @return true if protected, false otherwise
     */
    public abstract boolean isProtected(Block block);

    /**
     * Determines if a player can access a block
     *
     * @param player the player
     * @param block  the block
     * @return true if they can access the block, false otherwise
     */
    public abstract boolean canAccess(Player player, Block block);

}
