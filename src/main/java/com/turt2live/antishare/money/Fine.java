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

import com.turt2live.antishare.PermissionNodes;
import com.turt2live.antishare.util.ASGameMode;
import com.turt2live.antishare.util.Action;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

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
     * @param type       the type
     * @param amount     the amount (positive to remove from account)
     * @param enabled    true to enable
     * @param overcharge the amount to charge if the account has less than or equal to zero
     * @param affect     the Game Mode(s) to affect
     */
    public Fine(Action type, double amount, boolean enabled, double overcharge, ASGameMode affect) {
        super(type, amount, enabled, affect);
        this.overcharge = overcharge;
    }

    /**
     * Gets the overcharge value for this fine
     *
     * @return the overcharge value
     */
    public double getOverCharge() {
        return overcharge;
    }

    @Override
    public void apply(Player player) {
        if (!isEnabled() || player.hasPermission(PermissionNodes.MONEY_NO_FINE) || !super.affect(player.getGameMode())) {
            return;
        }

        // Apply to account
        double amount = getAmount();
        if (plugin.getMoneyManager().getRawEconomyHook().requiresTab(player.getName())) {
            amount = overcharge;
        }
        TransactionResult result = plugin.getMoneyManager().subtractFromAccount(player, amount);
        if (!result.completed) {
            plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("fine-failed", result.message), true);
            plugin.getLogger().warning(plugin.getMessages().getMessage("fine-failed", result.message) + "  (" + player.getName() + ")");
            return;
        } else {
            String formatted = plugin.getMoneyManager().formatAmount(getAmount());
            String balance = plugin.getMoneyManager().formatAmount(plugin.getMoneyManager().getBalance(player));
            if (!plugin.getMoneyManager().isSilent(player.getName())) {
                plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("fine-success", formatted), true);
                plugin.getMessages().sendTo(player, ChatColor.RED + plugin.getMessages().getMessage("new-balance", balance), true);
            }
        }
    }

}
