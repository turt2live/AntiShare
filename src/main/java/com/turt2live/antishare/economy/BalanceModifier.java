package com.turt2live.antishare.economy;

/**
 * Represents an object to be used to modify the balance of an account
 *
 * @author turt2live
 */
// TODO: Unit test
public final class BalanceModifier {

    /**
     * Represents a modifier type
     */
    public enum ModifierType {
        /**
         * A fine
         */
        FINE,
        /**
         * A reward
         */
        REWARD;
    }
    private String account;
    private ModifierType type;
    private double amount;

    /**
     * Creates a new balance modifier
     *
     * @param type    the modifier type, cannot be null
     * @param account the account to modify, cannot be null
     * @param amount  the amount to modify, cannot be null or negative
     */
    public BalanceModifier(ModifierType type, String account, double amount) {
        if (type == null || account == null || amount <= 0) throw new IllegalArgumentException();

        this.type = type;
        this.account = account;
        this.amount = amount;
    }

    /**
     * Gets the type of this modifier
     *
     * @return the modifier type
     */
    public ModifierType getType() {
        return type;
    }

    /**
     * Gets the account for this modification
     *
     * @return the account to modify
     */
    public String getAccount() {
        return account;
    }

    /**
     * Gets the amount to modify the account by
     *
     * @return the positive amount
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Gets the scaled amount to modify the account by. This may be a negative number
     * depending upon the ModifierType used. This would be the difference in the balance
     * for the account after this modification has been applied.
     *
     * @return the difference in balance
     */
    public double getScaledAmount() {
        return type == ModifierType.FINE ? -amount : amount;
    }

}
