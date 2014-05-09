package com.turt2live.antishare.engine.list;

import com.turt2live.antishare.TrackedState;

import java.util.ArrayList;
import java.util.List;

/**
 * A command rejection list. This works off a tri-state
 *
 * @author turt2live
 */
public class CommandRejectionList implements RejectionList<RejectableCommand> {

    private List<RejectableCommand> commands = new ArrayList<RejectableCommand>();

    /**
     * Adds a listing of commands to this rejection list. This will ensure that
     * no duplicates get added to the internal collection.
     *
     * @param commands the list of commands to add, cannot be null
     */
    public void populate(List<RejectableCommand> commands) {
        if (commands == null) throw new IllegalArgumentException("cannot add nothing");

        for (RejectableCommand command : commands) {
            if (!this.commands.contains(command)) this.commands.add(command);
        }
    }

    @Override
    public boolean isBlocked(RejectableCommand item) {
        return getState(item) == TrackedState.INCLUDED; // Does it's own null check
    }

    @Override
    public TrackedState getState(RejectableCommand item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null");

        byte flags = RejectableCommand.FLAG_STARTS_WITH | RejectableCommand.FLAG_EXACTLY;
        int included = 0, rejected = 0;

        for (RejectableCommand command : commands) {
            if (item.matches(command, flags)) {
                if (command.isNegated()) rejected++;
                else included++;
            }
        }

        if (included == rejected) return TrackedState.NOT_PRESENT;
        else if (included > rejected) return TrackedState.INCLUDED;
        return TrackedState.NEGATED;
    }

    @Override
    public ListType getType() {
        return ListType.COMMANDS;
    }
}
