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

package com.turt2live.antishare.bukkit.lists;

import com.turt2live.antishare.bukkit.impl.derived.DerivedBukkitObject;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.Rejectable;

import java.util.List;

public class PopulatorRelevantMobList<T extends Rejectable> implements Populator<T> {

    @Override
    public void populateList(BukkitList<T> list, List<String> strings) {
        if (list == null || strings == null) throw new IllegalArgumentException();

        for (String s : strings) {
            boolean negated = false;
            if (s.startsWith("-")) {
                negated = true;
                s = s.substring(1);
            }
            AEntity.RelevantEntityType entityType = AEntity.RelevantEntityType.fromString(s);
            if (entityType != null) {
                (negated ? list.negated : list.included).add(new DerivedBukkitObject(entityType));
            } else {
                Engine.getInstance().getLogger().warning("Unknown entity type: " + s);
            }
        }
    }
}
