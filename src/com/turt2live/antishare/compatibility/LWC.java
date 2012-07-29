/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 * 
 * Contributors:
 *     turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.compatibility;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class LWC extends SignProtection {

	private com.griefcraft.lwc.LWC lwc;

	public LWC(){
		lwc = com.griefcraft.lwc.LWC.getInstance();
	}

	@Override
	public boolean isProtected(Block block){
		return lwc.findProtection(block) != null;
	}

	@Override
	public boolean canAccess(Player player, Block block){
		return lwc.canAccessProtection(player, block);
	}

}
