package com.turt2live.antishare.money;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;

import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

import com.turt2live.antishare.AntiShare;

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
	public VaultEconomy(){
		RegisteredServiceProvider<Economy> rsp = AntiShare.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp == null){
			return;
		}
		economy = rsp.getProvider();
	}

	/**
	 * Adds an amount to the player's account
	 * 
	 * @param player the player
	 * @param amount the amount
	 * @return the result
	 */
	public TransactionResult add(Player player, double amount){
		if(amount < 0){
			return subtract(player, Math.abs(amount));
		}
		EconomyResponse resp = economy.depositPlayer(player.getName(), amount);
		if(resp.type == ResponseType.FAILURE){
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
	public TransactionResult subtract(Player player, double amount){
		EconomyResponse resp = economy.withdrawPlayer(player.getName(), amount);
		if(resp.type == ResponseType.FAILURE){
			return new TransactionResult(resp.errorMessage, false);
		}
		return new TransactionResult("Completed", true);
	}

	/**
	 * Determines if a player's account is negative
	 * 
	 * @param player the player
	 * @return true if the current balance is negative
	 */
	public boolean isNegative(Player player){
		return getBalance(player) < 0;
	}

	/**
	 * Determines if this player will require use of the 'tab' feature
	 * 
	 * @param player the player
	 * @return true if the tab feature will be required by the player
	 */
	public boolean requiresTab(Player player){
		return getBalance(player) <= 0;
	}

	/**
	 * Gets a player's account balance
	 * 
	 * @param player the player
	 * @return the balance
	 */
	public double getBalance(Player player){
		return economy.getBalance(player.getName());
	}

	/**
	 * Formats an amount into a string
	 * 
	 * @param amount the amount
	 * @return the amount as a string
	 */
	public String format(double amount){
		return economy.format(amount);
	}

}
