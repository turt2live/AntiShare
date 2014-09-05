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

package com.turt2live.antishare.bukkit.impl.derived;

import com.turt2live.antishare.object.DerivedRejectable;
import org.bukkit.entity.EntityType;

/**
 * A bukkit entity type derivation
 *
 * @author turt2live
 */
public class DerivedEntityType implements DerivedRejectable {

    private EntityType type;

    public DerivedEntityType(EntityType type) {
        if (type == null) throw new IllegalArgumentException();

        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DerivedEntityType)) return false;

        DerivedEntityType that = (DerivedEntityType) o;

        return type == that.type;

    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return "DerivedEntityType{" +
                "type=" + type +
                '}';
    }
}
