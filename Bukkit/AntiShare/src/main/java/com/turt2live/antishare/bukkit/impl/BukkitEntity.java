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

package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.ASLocation;
import org.bukkit.entity.Entity;

/**
 * A Bukkit entity
 *
 * @author turt2live
 */
public class BukkitEntity extends BukkitObject implements AEntity {

    private Entity entity;

    /**
     * Creates a new Bukkit Entity
     *
     * @param entity the entity, cannot be null
     */
    public BukkitEntity(Entity entity) {
        if (entity == null) throw new IllegalArgumentException("cannot have a null argument");

        this.entity = entity;
    }

    @Override
    public ASLocation getLocation() {
        return BukkitUtils.toLocation(entity.getLocation());
    }

    @Override
    protected String getFriendlyName() {
        return entity.getType().getName();
    }

    /**
     * Gets the Bukkit entity
     *
     * @return the bukkit entity
     */
    public Entity getEntity() {
        return entity;
    }
}
