/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
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

import com.turt2live.antishare.compatibility.type.BlockProtection;

/**
 * Lockette hook
 * 
 * @author turt2live
 */
public class Lockette extends BlockProtection {

	@Override
	public boolean isProtected(Block block){
		return org.yi.acru.bukkit.Lockette.Lockette.isProtected(block);
	}

	@Override
	public boolean canAccess(Player player, Block block){
		if(!isProtected(block)){
			return true;
		}
		if(org.yi.acru.bukkit.Lockette.Lockette.isEveryone(block)){
			return true;
		}else{
			if(org.yi.acru.bukkit.Lockette.Lockette.isOwner(block, player.getName())){
				return true;
			}else if(org.yi.acru.bukkit.Lockette.Lockette.isUser(block, player.getName(), true)){
				return true;
			}
		}
		return false;
	}

}
