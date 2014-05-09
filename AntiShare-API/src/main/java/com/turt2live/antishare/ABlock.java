package com.turt2live.antishare;

import com.turt2live.antishare.engine.list.Rejectable;

/**
 * An AntiShare Block.
 *
 * @author turt2live
 */
public interface ABlock extends Rejectable {

    /**
     * Represents the various chest states
     */
    public static enum ChestType {
        /**
         * A single standard chest
         */
        NORMAL,
        /**
         * A single trapped chest
         */
        TRAPPED,
        /**
         * Two standard chests merged into one large standard chest
         */
        DOUBLE_NORMAL,
        /**
         * Two trapped chests merged into one large trapped chest
         */
        DOUBLE_TRAPPED,
        /**
         * An ender chest
         */
        ENDER,
        /**
         * A locked chest (April Fool's Joke)
         *
         * @deprecated No longer used in newer versions
         */
        @Deprecated
        LOCKED,
        /**
         * Not a chest
         */
        NONE;
    }

    /**
     * Determines if this block is attached to the supplied block.
     * If this block cannot be attached to blocks, this will
     * return false.
     *
     * @param block the block to check, if null: false is returned
     * @return whether or not this block is attached to the specified block
     */
    public boolean isAttached(ABlock block);

    /**
     * Gets the facing direction of this block. May be null if not applicable
     *
     * @return the facing direction, or null
     */
    public Facing getFacingDirection();

    /**
     * Gets the applicable chest type for this block
     *
     * @return the applicable chest type, returning {@link com.turt2live.antishare.ABlock.ChestType#NONE} for 'not a chest'
     */
    public ChestType getChestType();

    /**
     * Gets the block's location
     *
     * @return the block location
     */
    public ASLocation getLocation();

    /**
     * Gets the world of this block
     *
     * @return the world
     */
    public AWorld getWorld();

    /**
     * Determines if a player can place this block. This should be
     * strictly a lookup of permissions without validating with any
     * engine components or through the rejection lists.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player, cannot be null
     * @return the appropriate tracking state as defined
     */
    public TrackedState canPlace(APlayer player);

    /**
     * Determines if a player can break this block. This should be
     * strictly a lookup of permissions without validating with any
     * engine components or through the rejection lists.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player, cannot be null
     * @return the appropriate tracking state as defined
     */
    public TrackedState canBreak(APlayer player);
}
