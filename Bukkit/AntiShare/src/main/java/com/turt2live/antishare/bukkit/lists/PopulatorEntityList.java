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

import com.turt2live.antishare.bukkit.impl.derived.DerivedEntityType;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.object.Rejectable;
import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * Populates a list of entities
 *
 * @author turt2live
 */
public class PopulatorEntityList<T extends Rejectable> implements Populator<T> {

    @Override
    public void populateList(BukkitList<T> list, List<String> strings) {
        if (list == null || strings == null) throw new IllegalArgumentException();

        list.included.clear();
        list.includedGeneric.clear();
        list.negated.clear();
        list.negatedGeneric.clear();

        for (String item : strings) {
            boolean added = false;

            if (item.equalsIgnoreCase("none")) {
                list.included.clear();
                list.negated.clear();
                for (EntityType type : EntityType.values()) list.negated.add(new DerivedEntityType(type));
                added = true;
            } else if (item.equalsIgnoreCase("all")) {
                list.included.clear();
                list.negated.clear();
                for (EntityType type : EntityType.values()) list.included.add(new DerivedEntityType(type));
                added = true;
            } else {
                boolean negated = item.startsWith("-");
                if (negated) item = item.substring(1);
                String attempt1 = item.replace(' ', '_');
                String attempt2 = item.replace('_', ' ');

                for (EntityType type : EntityType.values()) {
                    if (type.name().equalsIgnoreCase(attempt1) || type.getName().equalsIgnoreCase(attempt2)) {
                        if (negated) {
                            if (!list.negated.contains(type)) list.negated.add(new DerivedEntityType(type));
                            list.included.remove(type);
                            added = true;
                        } else {
                            if (!list.included.contains(type)) list.included.add(new DerivedEntityType(type));
                            list.negated.remove(type);
                            added = true;
                        }
                    }
                    if (added) break;
                }
            }
            if (!added) Engine.getInstance().getLogger().warning("Unknown entity: " + item);
        }
    }

}
