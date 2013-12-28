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
package com.turt2live.antishare.compatibility;

import com.turt2live.antishare.compatibility.type.BlockProtection;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * LWC hook
 *
 * @author turt2live
 */
public class LWC extends BlockProtection {

    private com.griefcraft.lwc.LWC lwc;

    public LWC() {
        lwc = com.griefcraft.lwc.LWC.getInstance();
    }

    @Override
    public boolean isProtected(Block block) {
        return lwc.findProtection(block) != null;
    }

    @Override
    public boolean canAccess(Player player, Block block) {
        return lwc.canAccessProtection(player, block);
    }

}
