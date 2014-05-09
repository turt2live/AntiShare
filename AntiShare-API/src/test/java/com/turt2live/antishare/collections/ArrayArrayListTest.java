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

package com.turt2live.antishare.collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class ArrayArrayListTest {

    @Test
    public void testCreate1() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>();
        assertEquals(0, numbers.size());
    }

    @Test
    public void testCreate2() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(new Integer[] {9, 10, 11});
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate3() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(9, 10, 11);
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate4() {
        ArrayArrayList<Integer> numbers = new ArrayArrayList<Integer>(Arrays.asList(9, 10, 11));
        assertEquals(3, numbers.size());
        assertEquals(new Integer(9), numbers.get(0));
        assertEquals(new Integer(10), numbers.get(1));
        assertEquals(new Integer(11), numbers.get(2));
    }

    @Test
    public void testCreate5() {
        new ArrayArrayList<Integer>((List<Integer>) null);
        new ArrayArrayList<Integer>((Integer[]) null);
    }

}
