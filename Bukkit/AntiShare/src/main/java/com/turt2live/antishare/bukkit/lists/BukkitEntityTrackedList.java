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

import com.turt2live.antishare.bukkit.impl.BukkitEntity;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.list.TrackedTypeList;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * A list for tracking entities
 *
 * @author turt2live
 */
public class BukkitEntityTrackedList implements TrackedTypeList<AEntity> {

    private List<EntityType> included = new ArrayList<EntityType>();
    private List<EntityType> excluded = new ArrayList<EntityType>();

    /**
     * Creates a new entity tracked list
     *
     * @param restricted the list of strings to restrict, cannot be null
     */
    public BukkitEntityTrackedList(List<String> restricted) {
        if (restricted == null) throw new IllegalArgumentException("cannot have nothing");

        for (String item : restricted) {
            boolean added = false;

            if (item.equalsIgnoreCase("none")) {
                included.clear();
                excluded.clear();
                for (EntityType type : EntityType.values()) excluded.add(type);
                added = true;
            } else if (item.equalsIgnoreCase("all")) {
                included.clear();
                excluded.clear();
                for (EntityType type : EntityType.values()) included.add(type);
                added = true;
            } else {
                boolean negated = item.startsWith("-");
                if (negated) item = item.substring(1);
                String attempt1 = item.replace(' ', '_');
                String attempt2 = item.replace('_', ' ');

                for (EntityType type : EntityType.values()) {
                    if (type.name().equalsIgnoreCase(attempt1) || type.getName().equalsIgnoreCase(attempt2)) {
                        if (negated) {
                            if (!excluded.contains(type)) excluded.add(type);
                            included.remove(type);
                            added = true;
                        } else {
                            if (!included.contains(type)) included.add(type);
                            excluded.remove(type);
                            added = true;
                        }
                    }
                    if (added) break;
                }
            }
            if (!added) Engine.getInstance().getLogger().warning("Unknown entity: " + item);
        }
    }

    @Override
    public boolean isTracked(AEntity object) {
        return getState(object) == TrackedState.INCLUDED;
    }

    @Override
    public TrackedState getState(AEntity object) {
        if (object == null) throw new IllegalArgumentException("Object cannot be null");
        if (!(object instanceof BukkitEntity)) return TrackedState.NOT_PRESENT;

        BukkitEntity entity = (BukkitEntity) object;

        boolean in = included.contains(entity.getEntity().getType());
        boolean ex = excluded.contains(entity.getEntity().getType());

        if (in == ex) return TrackedState.NOT_PRESENT;
        else if (in) return TrackedState.INCLUDED;
        else return TrackedState.NEGATED;
    }
}
