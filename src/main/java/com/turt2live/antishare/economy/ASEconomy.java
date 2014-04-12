package com.turt2live.antishare.economy;

/**
 * Represents the AntiShare economy
 *
 * @author turt2live
 */
// TODO: Unit test
public interface ASEconomy {

    /**
     * Withdraws an amount from an account
     *
     * @param account the account to withdraw from
     * @param amount  the amount to withdraw
     * @return true if completed
     */
    public boolean withdraw(String account, double amount);

    /**
     * Deposits an amount into an account
     *
     * @param account the account to deposit into
     * @param amount  the amount to deposit
     * @return true if completed
     */
    public boolean deposit(String account, double amount);

    /**
     * Gets the balance of an account
     *
     * @param account the account to lookup
     * @return the account's balance, may be zero if not existing (or the economy default)
     */
    public double getBalance(String account);

    /**
     * Handles a balance modifier
     *
     * @param modifier the modifier to handle
     * @return true if handled
     */
    public boolean handleModifier(BalanceModifier modifier);

}
