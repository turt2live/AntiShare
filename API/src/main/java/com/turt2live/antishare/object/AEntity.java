/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.object;

import com.turt2live.antishare.object.attribute.TrackedState;

import java.util.UUID;

/**
 * Represents an AntiShare entity
 *
 * @author turt2live
 */
public interface AEntity extends Rejectable {

    /**
     * Represents a list of entity types which matter to the
     * AntiShare engine. Not all types are listed here.
     */
    public enum RelevantEntityType implements Rejectable {
        WITHER,
        IRON_GOLEM,
        SNOWMAN;

        /**
         * Attempts to match a string to an entity type
         *
         * @param s the string, null returns null
         *
         * @return the entity type, or null if not found
         */
        public static RelevantEntityType fromString(String s) {
            if (s == null) return null;

            String s2 = s.replace(' ', '_');

            for (RelevantEntityType type : values()) {
                if (type.name().equalsIgnoreCase(s)
                        || type.name().equalsIgnoreCase(s2)
                        || type.name().replace("_", "").equalsIgnoreCase(s)) return type;
            }

            return null;
        }
    }

    /**
     * Gets the current location of this entity
     *
     * @return the current location
     */
    public ASLocation getLocation();

    /**
     * Gets the unique UUID of this entity
     *
     * @return the entity's UUID
     */
    public UUID getUUID();

    /**
     * Determines if a player can place this entity. This should be
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
    public TrackedState canPlace(APlayer player);

    /**
     * Determines if a player can break this entity. This should be
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
    public TrackedState canBreak(APlayer player);

    /**
     * Determines if a player can attack this entity. This should be
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
    public TrackedState canAttack(APlayer player);

    /**
     * Determines if a player can interact with this entity. This should be
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
    public TrackedState canInteract(APlayer player);
}
