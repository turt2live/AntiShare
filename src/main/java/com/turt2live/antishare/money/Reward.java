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
package com.turt2live.antishare.money;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.turt2live.antishare.util.ASGameMode;
import com.turt2live.antishare.util.Action;
import com.turt2live.antishare.util.PermissionNodes;

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
	public Reward(Action type, double amount, boolean enabled, ASGameMode affect) {
		super(type, amount, enabled, affect);
	}

	@Override
	public void apply(Player player) {
		if (!isEnabled() || player.hasPermission(PermissionNodes.MONEY_NO_REWARD) || !super.affect(player.getGameMode())) {
			return;
		}

		// Apply to account
		TransactionResult result = plugin.getMoneyManager().addToAccount(player, getAmount());
		if (!result.completed) {
			plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("reward-failed", result.message), true);
			plugin.getLogger().warning(plugin.getMessages().getMessage("reward-failed", result.message) + "  (" + player.getName() + ")");
			return;
		} else {
			String formatted = plugin.getMoneyManager().formatAmount(getAmount());
			String balance = plugin.getMoneyManager().formatAmount(plugin.getMoneyManager().getBalance(player));
			if (!plugin.getMoneyManager().isSilent(player.getName())) {
				plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("reward-success", formatted), true);
				plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("new-balance", balance), true);
			}
		}
	}

}
