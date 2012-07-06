package com.turt2live.antishare.money;

import java.util.logging.Level;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.AntiShare.LogType;
import com.turt2live.antishare.api.ASGameMode;
import com.turt2live.antishare.permissions.PermissionNodes;

/**
 * Fine for doing something
 * 
 * @author turt2live
 */
public class Fine extends Tender {

	private double overcharge;

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
		if(plugin.getMoneyManager().getRawEconomyHook().requiresTab(player.getName())){
			amount = overcharge;
		}
		TransactionResult result = plugin.getMoneyManager().subtractFromAccount(player, amount);
		if(!result.completed){
			ASUtils.sendToPlayer(player, ChatColor.RED + "Fine Failed: " + ChatColor.ITALIC + result.message, true);
			plugin.getMessenger().log("Fine Failed (" + player.getName() + "): " + result.message, Level.WARNING, LogType.BYPASS);
			return;
		}else{
			String formatted = plugin.getMoneyManager().formatAmount(getAmount());
			String balance = plugin.getMoneyManager().formatAmount(plugin.getMoneyManager().getBalance(player));
			if(!plugin.getMoneyManager().isSilent(player.getName())){
				ASUtils.sendToPlayer(player, ChatColor.RED + "You've been fined " + formatted + "!", true);
				ASUtils.sendToPlayer(player, "Your new balance is " + ChatColor.YELLOW + balance, true);
			}
		}
	}

}
