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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.object.Rejectable;
import com.turt2live.antishare.object.RejectableCommand;

import java.util.List;

/**
 * Populates a list of commands.
 *
 * @author turt2live
 */
public class PopulatorCommandList<T extends Rejectable> implements Populator<T> {

    @Override
    public void populateList(BukkitList<T> list, List<String> strings) {
        if (list == null || strings == null) throw new IllegalArgumentException();

        list.included.clear();
        list.includedGeneric.clear();
        list.negated.clear();
        list.negatedGeneric.clear();

        for (String value : strings) {
            RejectableCommand command = new RejectableCommand(value);

            if (command.isNegated()) {
                if (!list.negated.contains(command)) list.negated.add(command);
            } else {
                if (!list.included.contains(command)) list.included.add(command);
            }
        }
    }

}
