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

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.attribute.TrackedState;

/**
 * Represents a command which may be rejected
 *
 * @author turt2live
 */
public class RejectableCommand implements Rejectable, DerivableRejectable, DerivedRejectable {

    /**
     * Flag for "starts with". Example: <code>Does "/test command" start with "/test"?</code>
     */
    public static final byte FLAG_STARTS_WITH = 0x01;

    /**
     * Flag for "ends with". Example: <code>Does "/test command" end with "command"?</code>
     */
    public static final byte FLAG_ENDS_WITH = 0x02;

    /**
     * Flag for "exactly". Example: <code>Is "/test ComAnd" equal to "/test comand"?</code>
     */
    public static final byte FLAG_EXACTLY = 0x04;

    /**
     * Flag for "partially found". Example: <code>Does "/test command string" contain "command"?</code>
     */
    public static final byte FLAG_PARTIAL_MATCH = 0x08;

    /**
     * Flag for "not found". Example: <code>Does "/test command" contain "not in the command"</code>
     */
    public static final byte FLAG_NO_MATCH = 0x10;

    private String commandString;
    private boolean isNegated = false;

    /**
     * Creates a new rejectable command
     *
     * @param commandString the command string, cannot be null
     */
    public RejectableCommand(String commandString) {
        if (commandString == null) throw new IllegalArgumentException("command string cannot be null");
        if (commandString.startsWith("-")) {
            isNegated = true;
            commandString = commandString.substring(1);
        }
        if (!commandString.startsWith("/")) commandString = "/" + commandString;

        this.commandString = commandString;
    }

    /**
     * Determines if this is a negated command or not. Only useful for
     * list operations.
     *
     * @return the negation state of this command
     */
    public boolean isNegated() {
        return isNegated;
    }

    /**
     * Gets the command string of this rejectable command
     *
     * @return the command string
     */
    public String getCommandString() {
        return commandString;
    }

    /**
     * Determines if a specific player can execute this command. This should
     * only perform a permissions lookup and nothing more. This is generally
     * called by the internal logic of AntiShare and will be alongside other
     * checks such as list inclusion.
     * <p/>
     * This uses the tri-state enum {@link com.turt2live.antishare.object.attribute.TrackedState}
     * to represent various states, as outlined below.
     * <p/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NOT_PRESENT} - Neither allow or deny permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#INCLUDED} - Allow permission found<br/>
     * {@link com.turt2live.antishare.object.attribute.TrackedState#NEGATED} - Deny permission found
     *
     * @param player the player to check, cannot be null
     *
     * @return the state of this player's permissions as defined
     */
    public TrackedState canExecute(APlayer player) {
        boolean allow = player.hasPermission(APermission.getPermissionNode(true, RejectionList.ListType.COMMANDS));
        boolean deny = player.hasPermission(APermission.getPermissionNode(false, RejectionList.ListType.COMMANDS));

        if (allow == deny) return TrackedState.NOT_PRESENT;
        else if (allow) return TrackedState.INCLUDED;
        return TrackedState.NEGATED;
    }

    /**
     * Determines if the passed rejectable command matches this rejectable command.
     * The algorithm applied is <code>Does (this) [flag function] (other)</code>. For
     * example: <code>Does (this) start with (other)</code>.
     * <p/>
     * If multiple flags are provided, a 'vote'-like system takes place where a
     * majority of flags must match in order to have this return true. For example,
     * if there are 3 flags applied, 2 (at least) must match in order for this to
     * return true. Once a minimum has been achieved, no other flags will be checked.
     * <p/>
     * This will not consider {@link #isNegated}.
     *
     * @param other the other rejectable command, cannot be null
     * @param flags the flags applicable, unknown flags are ignored
     *
     * @return true if this rejectable command matches the other based upon the flags
     *
     * @see #FLAG_STARTS_WITH
     * @see #FLAG_ENDS_WITH
     * @see #FLAG_EXACTLY
     * @see #FLAG_PARTIAL_MATCH
     * @see #FLAG_NO_MATCH
     */
    public boolean matches(RejectableCommand other, byte flags) {
        if (other == null) throw new IllegalArgumentException("other cannot be null");

        int matches = 0;
        int minMatches = getMinMatches(flags);

        if ((flags & FLAG_STARTS_WITH) == FLAG_STARTS_WITH && matches < minMatches) {
            if (this.commandString.startsWith(other.commandString)) matches++;
        }
        if ((flags & FLAG_ENDS_WITH) == FLAG_ENDS_WITH && matches < minMatches) {
            if (this.commandString.endsWith(other.commandString.substring(1))) matches++;
        }
        if ((flags & FLAG_EXACTLY) == FLAG_EXACTLY && matches < minMatches) {
            if (this.commandString.equalsIgnoreCase(other.commandString)) matches++;
        }
        if ((flags & FLAG_PARTIAL_MATCH) == FLAG_PARTIAL_MATCH && matches < minMatches) {
            if (this.commandString.toLowerCase().contains(other.commandString.toLowerCase())) matches++;
        }
        if ((flags & FLAG_NO_MATCH) == FLAG_NO_MATCH && matches < minMatches) {
            if (!this.commandString.toLowerCase().contains(other.commandString.toLowerCase())) matches++;
        }

        return matches >= minMatches;
    }

    private int getMinMatches(byte flags) {
        int nflags = 0;

        if ((flags & FLAG_STARTS_WITH) == FLAG_STARTS_WITH) nflags++;
        if ((flags & FLAG_ENDS_WITH) == FLAG_ENDS_WITH) nflags++;
        if ((flags & FLAG_EXACTLY) == FLAG_EXACTLY) nflags++;
        if ((flags & FLAG_PARTIAL_MATCH) == FLAG_PARTIAL_MATCH) nflags++;
        if ((flags & FLAG_NO_MATCH) == FLAG_NO_MATCH) nflags++;

        return (int) Math.ceil(nflags / 2.0);
    }

    @Override
    public String toString() {
        return "RejectableCommand{" +
                "commandString='" + commandString + '\'' +
                ", isNegated=" + isNegated +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RejectableCommand)) return false;

        RejectableCommand that = (RejectableCommand) o;

        if (isNegated != that.isNegated) return false;
        return commandString.equals(that.commandString);

    }

    @Override
    public int hashCode() {
        int result = commandString.hashCode();
        result = 31 * result + (isNegated ? 1 : 0);
        return result;
    }

    @Override
    public DerivedRejectable getGeneric() {
        return null;
    }

    @Override
    public DerivedRejectable getSpecific() {
        return this;
    }

    @Override
    public boolean hasGeneric() {
        return false;
    }
}
