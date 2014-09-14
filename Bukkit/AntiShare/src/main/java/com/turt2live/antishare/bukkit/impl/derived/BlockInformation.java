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

package com.turt2live.antishare.bukkit.impl.derived;

import com.turt2live.antishare.object.DerivedRejectable;
import org.bukkit.Material;

/**
 * Block information class
 *
 * @author turt2live
 */
public class BlockInformation implements DerivedRejectable {

    final Material material;
    final short damage;

    public BlockInformation(Material m, short d) {
        if (m == null) throw new IllegalArgumentException();

        material = m;
        damage = d;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BlockInformation)) return false;

        BlockInformation that = (BlockInformation) o;

        if (damage != that.damage) return false;
        return material == that.material;

    }

    @Override
    public int hashCode() {
        int result = material.hashCode();
        result = 31 * result + (int) damage;
        return result;
    }

}
