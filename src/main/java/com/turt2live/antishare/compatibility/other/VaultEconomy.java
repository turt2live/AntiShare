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
package com.turt2live.antishare.compatibility.other;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import org.bukkit.GameMode;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.money.TransactionResult;
import com.turt2live.antishare.util.MoneySaver;

/**
 * Safe-hook for Vault Economy
 * 
 * @author turt2live
 */
public class VaultEconomy {

	private Economy economy;

	/**
	 * Creates a new Vault Economy instance
	 */
	public VaultEconomy() {
		RegisteredServiceProvider<Economy> rsp = AntiShare.p.getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return;
		}
		economy = rsp.getProvider();
	}

	/**
	 * Switches the balance of a player
	 * 
	 * @param player the player
	 * @param from the gamemode to
	 * @param to the gamemode from
	 */
	public void switchBalance(String player, GameMode from, GameMode to) {
		if (economy.hasAccount(player)) {
			double current = economy.getBalance(player);
			MoneySaver.saveLevel(player, from, current);
			double balance = MoneySaver.getLevel(player, to);
			EconomyResponse r1 = economy.withdrawPlayer(player, current);
			EconomyResponse r2 = economy.depositPlayer(player, balance);
			if (!r1.transactionSuccess() || !r2.transactionSuccess()) {
				Exception e = new Exception("Cannot set balance: p=" + player + " gfrom=" + from + " gto=" + to);
				e.printStackTrace();
			}
		}
	}

	/**
	 * Adds an amount to the player's account
	 * 
	 * @param player the player
	 * @param amount the amount
	 * @return the result
	 */
	public TransactionResult add(String player, double amount) {
		if (amount < 0) {
			return subtract(player, Math.abs(amount));
		}
		EconomyResponse resp = economy.depositPlayer(player, amount);
		if (resp.type == ResponseType.FAILURE) {
			return new TransactionResult(resp.errorMessage, false);
		}
		return new TransactionResult("Completed", true);
	}

	/**
	 * Subtracts an amount from the player's account
	 * 
	 * @param player the player
	 * @param amount the amount
	 * @return the result
	 */
	public TransactionResult subtract(String player, double amount) {
		EconomyResponse resp = economy.withdrawPlayer(player, amount);
		if (resp.type == ResponseType.FAILURE) {
			return new TransactionResult(resp.errorMessage, false);
		}
		return new TransactionResult("Completed", true);
	}

	/**
	 * Determines if this player will require use of the 'tab' feature
	 * 
	 * @param player the player
	 * @return true if the tab feature will be required by the player
	 */
	public boolean requiresTab(String player) {
		return getBalance(player) <= 0;
	}

	/**
	 * Gets a player's account balance
	 * 
	 * @param player the player
	 * @return the balance
	 */
	public double getBalance(String player) {
		return economy.getBalance(player);
	}

	/**
	 * Formats an amount into a string
	 * 
	 * @param amount the amount
	 * @return the amount as a string
	 */
	public String format(double amount) {
		return economy.format(amount);
	}

}
