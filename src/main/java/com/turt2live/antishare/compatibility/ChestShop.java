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
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;

import com.Acrobot.ChestShop.Signs.ChestShopSign;
import com.Acrobot.ChestShop.Utils.uBlock;
import com.turt2live.antishare.compatibility.type.SignProtection;

public class ChestShop extends SignProtection {

	@Override
	public boolean isProtected(Block block){
		Sign protection = getSign(block);
		return protection != null && ChestShopSign.isValid(protection);
	}

	@Override
	public boolean canAccess(Player player, Block block){
		return hasSign(block) ? isProtected(block) ? ChestShopSign.canAccess(player, getSign(block)) : true : true;
	}

	private boolean hasSign(Block block){
		return getSign(block) != null;
	}

	private Sign getSign(Block block){
		return uBlock.findAnyNearbyShopSign(block);
	}

}
