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

import com.turt2live.antishare.bukkit.impl.derived.DerivedEntityType;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.engine.list.RejectionList;
import com.turt2live.antishare.object.AEntity;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.DerivedRejectable;
import com.turt2live.antishare.object.attribute.TrackedState;
import org.bukkit.entity.Entity;

import java.util.UUID;

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
    public UUID getUUID() {
        return entity.getUniqueId();
    }

    @Override
    public TrackedState canPlace(APlayer player) {
        return permissionCheck(RejectionList.ListType.ENTITY_PLACE, player);
    }

    @Override
    public TrackedState canBreak(APlayer player) {
        return permissionCheck(RejectionList.ListType.ENTITY_BREAK, player);
    }

    @Override
    public TrackedState canAttack(APlayer player) {
        return permissionCheck(RejectionList.ListType.ENTITY_ATTACK, player);
    }

    @Override
    public TrackedState canInteract(APlayer player) {
        return permissionCheck(RejectionList.ListType.ENTITY_INTERACT, player);
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

    @Override
    public DerivedRejectable getGeneric() {
        return null;
    }

    @Override
    public DerivedRejectable getSpecific() {
        return new DerivedEntityType(entity.getType());
    }

    @Override
    public boolean hasGeneric() {
        return false;
    }
}
