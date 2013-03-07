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
package com.turt2live.antishare.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.compatibility.other.VaultEconomy;
import com.turt2live.antishare.money.Fine;
import com.turt2live.antishare.money.Reward;
import com.turt2live.antishare.money.TransactionResult;
import com.turt2live.antishare.util.ASGameMode;
import com.turt2live.antishare.util.Action;

/**
 * Manages rewards and fines
 * 
 * @author turt2live
 */
public class MoneyManager {

	private final List<Reward> rewards = new ArrayList<Reward>();
	private final List<Fine> fines = new ArrayList<Fine>();
	private boolean doRewards = false, doFines = false, tab = false, showStatusOnLogin = false;
	private VaultEconomy vaultEconomy;
	private final List<String> silentTo = new ArrayList<String>();
	private String finesTo = "nowhere", rewardsFrom = "nowhere";
	private AntiShare plugin = AntiShare.p;

	/**
	 * Saves the Money Manager
	 */
	public void save(){
		File silent = new File(plugin.getDataFolder() + File.separator + "data", "money-silent.txt");
		try{
			silent.getParentFile().mkdirs();
			BufferedWriter out = new BufferedWriter(new FileWriter(silent, false));
			for(String player : silentTo){
				out.write(player + "\r\n");
			}
			out.close();
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	/**
	 * Loads the Money Manager
	 */
	public void load(){
		silentTo.clear();
		File silent = new File(plugin.getDataFolder() + File.separator + "data", "money-silent.txt");
		try{
			if(silent.exists()){
				BufferedReader in = new BufferedReader(new FileReader(silent));
				String line;
				while ((line = in.readLine()) != null){
					silentTo.add(line);
				}
				in.close();
			}
		}catch(IOException e){
			e.printStackTrace();
		}

		// Quit if we have to
		if(!plugin.settings().features.fines){
			return;
		}

		// Load config
		EnhancedConfiguration money = new EnhancedConfiguration(new File(plugin.getDataFolder(), "fines.yml"), plugin);
		money.loadDefaults(plugin.getResource("fines.yml"));
		if(money.needsUpdate()){
			money.saveDefaults();
		}
		money.load();

		// Set settings
		doRewards = money.getBoolean("rewards-enabled");
		doFines = money.getBoolean("fines-enabled");
		tab = money.getBoolean("keep-tab");
		showStatusOnLogin = money.getBoolean("show-status-on-login");
		finesTo = money.getString("send-collections-to");
		rewardsFrom = money.getString("get-collections-from");

		// Prepare
		rewards.clear();
		fines.clear();

		// Load tender
		int finesLoaded = 0;
		int rewardsLoaded = 0;
		for(Action type : Action.values()){
			String path = type.name();
			if(money.getConfigurationSection(path) == null){
				continue;
			}
			boolean doFine = money.getBoolean(path + ".do-fine", false);
			boolean doReward = money.getBoolean(path + ".do-reward", false);
			double fine = money.getDouble(path + ".fine", 0);
			double reward = money.getDouble(path + ".reward", 0);
			double noMoney = money.getString(path + ".no-money", "default").equalsIgnoreCase("default") ? fine : money.getDouble(path + ".no-money");
			ASGameMode affect = ASGameMode.match(money.getString(path + ".give-to", "none"));

			// Sanity
			if(affect == null){
				plugin.getLogger().warning(plugin.getMessages().getMessage("unknown-fine-reward", money.getString(path + ".give-to"), "NONE"));
				affect = ASGameMode.NONE;
			}

			// Check enabled state
			if(!doRewards && doReward){
				doReward = false;
			}
			if(!doFines && doFine){
				doFine = false;
			}
			if(affect == ASGameMode.NONE){
				doFine = false;
				doReward = false;
			}

			// Add fine/reward
			Reward a = new Reward(type, reward, doReward, affect);
			Fine f = new Fine(type, fine, doFine, noMoney, affect);
			rewards.add(a);
			fines.add(f);

			// Record stats
			if(doFine){
				finesLoaded++;
			}
			if(doReward){
				rewardsLoaded++;
			}
		}

		// Check load state
		Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		if(vault != null){
			vaultEconomy = new VaultEconomy();
		}else{
			plugin.getLogger().info(plugin.getMessages().getMessage("cannot-load-fines-rewards"));
			return;
		}

		// Spam console
		if(finesLoaded > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("fines-loaded", String.valueOf(finesLoaded)));
		}
		if(rewardsLoaded > 0){
			plugin.getLogger().info(plugin.getMessages().getMessage("rewards-loaded", String.valueOf(rewardsLoaded)));
		}
	}

	/**
	 * Tells the player (if required) that they have silenced us.
	 * 
	 * @param player the player
	 */
	public void showStatusOnLogin(Player player){
		if(isSilent(player.getName()) && showStatusOnLogin){
			player.performCommand("as money status");
		}
	}

	/**
	 * Adds an amount to a player's account
	 * 
	 * @param player the player
	 * @param amount the amount
	 * @return the result
	 */
	public TransactionResult addToAccount(Player player, double amount){
		if(vaultEconomy == null){
			return TransactionResult.NO_VAULT;
		}
		if(!rewardsFrom.equalsIgnoreCase("nowhere")){
			vaultEconomy.subtract(rewardsFrom, amount);
		}
		return vaultEconomy.add(player.getName(), amount);
	}

	/**
	 * Subtracts an amount from a player's account
	 * 
	 * @param player the player
	 * @param amount the amount
	 * @return the result
	 */
	public TransactionResult subtractFromAccount(Player player, double amount){
		if(vaultEconomy == null){
			return TransactionResult.NO_VAULT;
		}
		if(vaultEconomy.requiresTab(player.getName()) && !tab){
			return TransactionResult.NO_TAB;
		}
		if(!finesTo.equalsIgnoreCase("nowhere")){
			vaultEconomy.add(finesTo, amount);
		}
		return vaultEconomy.subtract(player.getName(), amount);
	}

	/**
	 * Gets the player's account balance
	 * 
	 * @param player the player
	 * @return the balance
	 */
	public double getBalance(Player player){
		if(vaultEconomy == null){
			return 0.0;
		}
		return vaultEconomy.getBalance(player.getName());
	}

	/**
	 * Formats an amount into a String
	 * 
	 * @param amount the amount
	 * @return the string of the amount
	 */
	public String formatAmount(double amount){
		if(vaultEconomy == null){
			return String.valueOf(amount);
		}
		return vaultEconomy.format(amount);
	}

	/**
	 * Gets the raw VaultEconomy hook
	 * 
	 * @return the hook
	 */
	public VaultEconomy getRawEconomyHook(){
		return vaultEconomy;
	}

	/**
	 * Adds a player to the silent list
	 * 
	 * @param playername the player name
	 */
	public void addToSilentList(String playername){
		silentTo.add(playername);
	}

	/**
	 * Removes a player from the silent list
	 * 
	 * @param playername the player name
	 */
	public void removeFromSilentList(String playername){
		silentTo.remove(playername);
	}

	/**
	 * Determines if balance messages should be sent to the player
	 * 
	 * @param playername the player name
	 * @return true if the Money Manager should be silent to this player
	 */
	public boolean isSilent(String playername){
		return silentTo.contains(playername);
	}

	/**
	 * Gets a fine from a type
	 * 
	 * @param type the type
	 * @return the fine, or null if not found
	 */
	public Fine getFine(Action type){
		for(Fine fine : fines){
			if(fine.getType() == type){
				return fine;
			}
		}
		return null;
	}

	/**
	 * Gets an reward from a type
	 * 
	 * @param type the type
	 * @return the reward, or null if not found
	 */
	public Reward getReward(Action type){
		for(Reward reward : rewards){
			if(reward.getType() == type){
				return reward;
			}
		}
		return null;
	}

	/**
	 * Fires for an reward or fine. <br>
	 * Permission checks are done internally from here on in.
	 * 
	 * @param action the action to fine/reward
	 * @param illegal true if the action is illegal
	 * @param player the person to apply this to
	 */
	public void fire(Action action, boolean illegal, Player player){
		if(vaultEconomy == null){
			return;
		}
		// Apply reward/fine
		if(illegal){
			getFine(action).apply(player);
		}else{
			getReward(action).apply(player);
		}
	}

}
