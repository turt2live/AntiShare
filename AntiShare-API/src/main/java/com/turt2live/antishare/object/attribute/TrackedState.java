package com.turt2live.antishare.object.attribute;

/**
 * Tracking states for lists. This is also used by the can* checks
 * in {@link com.turt2live.antishare.object.ABlock} for tri-state purposes. See
 * ABlock for more information.
 *
 * @author turt2live
 */
public enum TrackedState {
    /**
     * Indicates the record was negated from the list
     */
    NEGATED,
    /**
     * Indicates the record is 'tracked'
     */
    INCLUDED,
    /**
     * Indicates the record is not present in a list
     */
    NOT_PRESENT;
}
