/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.object;

import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * Represents an item
 *
 * @author turt2live
 */
public interface AItem extends Rejectable {

    /**
     * Determines if a player can use this item. This should be
     * strictly a lookup of permissions without validating with any
     * engine components or through the rejection lists.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.object.attribute.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player, cannot be null
     *
     * @return the appropriate tracking state as defined
     */
    public TrackedState canUse(APlayer player);

    /**
     * Determines if a player can drop this item. This should be
     * strictly a lookup of permissions without validating with any
     * engine components or through the rejection lists.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.object.attribute.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player, cannot be null
     *
     * @return the appropriate tracking state as defined
     */
    public TrackedState canDrop(APlayer player);

    /**
     * Determines if a player can pickup this item. This should be
     * strictly a lookup of permissions without validating with any
     * engine components or through the rejection lists.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.object.attribute.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player, cannot be null
     *
     * @return the appropriate tracking state as defined
     */
    public TrackedState canPickup(APlayer player);

}
