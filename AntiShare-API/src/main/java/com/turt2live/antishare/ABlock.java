package com.turt2live.antishare;

/**
 * An AntiShare Block.
 *
 * @author turt2live
 */
public interface ABlock {

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
}
