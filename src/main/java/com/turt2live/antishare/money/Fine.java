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

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.turt2live.antishare.Systems.Manager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.permissions.PermissionNodes;
import com.turt2live.antishare.util.ASUtils;
import com.turt2live.antishare.util.generic.ASGameMode;

/**
 * Fine for doing something
 * 
 * @author turt2live
 */
public class Fine extends Tender {

	private final double overcharge;

	/**
	 * Creates a new fine
	 * 
	 * @param type the type
	 * @param amount the amount (positive to remove from account)
	 * @param enabled true to enable
	 * @param overcharge the amount to charge if the account has less than or equal to zero
	 * @param affect the Game Mode(s) to affect
	 */
	public Fine(TenderType type, double amount, boolean enabled, double overcharge, ASGameMode affect){
		super(type, amount, enabled, affect);
		this.overcharge = overcharge;
	}

	/**
	 * Gets the overcharge value for this fine
	 * 
	 * @return the overcharge value
	 */
	public double getOverCharge(){
		return overcharge;
	}

	@Override
	public void apply(Player player){
		if(!isEnabled() || plugin.getPermissions().has(player, PermissionNodes.MONEY_NO_FINE) || !super.affect(player.getGameMode())){
			return;
		}

		// Apply to account
		double amount = getAmount();
		if(((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).getRawEconomyHook().requiresTab(player.getName())){
			amount = overcharge;
		}
		TransactionResult result = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).subtractFromAccount(player, amount);
		if(!result.completed){
			// TODO: Locale
			ASUtils.sendToPlayer(player, ChatColor.RED + "Fine Failed: " + ChatColor.ITALIC + result.message, true);
			plugin.log("Fine Failed (" + player.getName() + "): " + result.message, Level.WARNING);
			return;
		}else{
			String formatted = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).formatAmount(getAmount());
			String balance = ((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).formatAmount(((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).getBalance(player));
			if(!((MoneyManager) plugin.getSystemsManager().getManager(Manager.MONEY)).isSilent(player.getName())){
				// TODO: Locale
				ASUtils.sendToPlayer(player, ChatColor.RED + "You've been fined " + formatted + "!", true);
				ASUtils.sendToPlayer(player, "Your new balance is " + ChatColor.YELLOW + balance, true);
			}
		}
	}

}
