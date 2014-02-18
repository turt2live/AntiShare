package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.economy.ASEconomy;
import com.turt2live.antishare.economy.BalanceModifier;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * Vault integration
 *
 * @author turt2live
 */
public class VaultEconomy implements ASEconomy {

    private Economy economy;

    public VaultEconomy() {
        RegisteredServiceProvider<Economy> rsp = AntiShare.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null)
            economy = rsp.getProvider();
    }

    @Override
    public boolean withdraw(String account, double amount) {
        if (economy == null) return false;
        return economy.withdrawPlayer(account, amount).transactionSuccess();
    }

    @Override
    public boolean deposit(String account, double amount) {
        if (economy == null) return false;
        return economy.depositPlayer(account, amount).transactionSuccess();
    }

    @Override
    public double getBalance(String account) {
        if (economy == null) return 0;
        return economy.getBalance(account);
    }

    @Override
    public boolean handleModifier(BalanceModifier modifier) {
        switch (modifier.getType()) {
            case REWARD:
                return deposit(modifier.getAccount(), modifier.getAmount());
            case FINE:
                return withdraw(modifier.getAccount(), modifier.getAmount());
            default:
                return false;
        }
    }
}
