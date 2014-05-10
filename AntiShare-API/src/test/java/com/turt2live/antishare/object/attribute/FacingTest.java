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

package com.turt2live.antishare.object.attribute;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class FacingTest {

    @Test
    public void testOpposite() {
        assertEquals(Facing.SOUTH, Facing.NORTH.opposite());
        assertEquals(Facing.NORTH, Facing.SOUTH.opposite());
        assertEquals(Facing.EAST, Facing.WEST.opposite());
        assertEquals(Facing.WEST, Facing.EAST.opposite());
        assertEquals(Facing.UP, Facing.DOWN.opposite());
        assertEquals(Facing.DOWN, Facing.UP.opposite());
    }

}
