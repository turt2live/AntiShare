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
package com.turt2live.antishare.money;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.util.ASUtils;
import com.turt2live.antishare.util.generic.ASGameMode;

/**
 * Reward for doing something
 * 
 * @author turt2live
 */
public class Reward extends Tender {

	/**
	 * Creates a new reward
	 * 
	 * @param type the type
	 * @param amount the amount (positive to add to account)
	 * @param enabled true to enable
	 * @param affect the Game Mode(s) to affect
	 */
	public Reward(TenderType type, double amount, boolean enabled, ASGameMode affect){
		super(type, amount, enabled, affect);
	}

	@Override
	public void apply(Player player){
		if(!isEnabled() || plugin.getPermissions().has(player, PermissionNodes.MONEY_NO_REWARD) || !super.affect(player.getGameMode())){
			return;
		}

		// Apply to account
		TransactionResult result = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).addToAccount(player, getAmount());
		if(!result.completed){
			// TODO: Locale
			ASUtils.sendToPlayer(player, ChatColor.RED + "Reward Failed: " + ChatColor.ITALIC + result.message, true);
			plugin.getLogger().warning("Reward Failed (" + player.getName() + "): " + result.message);
			return;
		}else{
			String formatted = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).formatAmount(getAmount());
			String balance = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).formatAmount(((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).getBalance(player));
			if(!((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).isSilent(player.getName())){
				// TODO: Locale
				ASUtils.sendToPlayer(player, ChatColor.GREEN + "You've been rewarded " + formatted + "!", true);
				ASUtils.sendToPlayer(player, "Your new balance is " + ChatColor.YELLOW + balance, true);
			}
		}
	}

}
