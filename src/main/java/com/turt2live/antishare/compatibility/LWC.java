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
package com.turt2live.antishare.compatibility;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.turt2live.antishare.compatibility.type.BlockProtection;

/**
 * LWC hook
 * 
 * @author turt2live
 */
public class LWC extends BlockProtection {

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
