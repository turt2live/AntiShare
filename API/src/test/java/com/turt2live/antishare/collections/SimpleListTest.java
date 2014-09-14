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

package com.turt2live.antishare.collections;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class SimpleListTest {

    @Test
    public void testAdd() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.addItem(19);

        assertTrue(numbers.hasItem(19));
        assertFalse(numbers.hasItem(20));
    }

    @Test
    public void testRemove() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        numbers.removeItem(10);
        numbers.removeItem(20);

        assertTrue(numbers.hasItem(19));
        assertTrue(numbers.hasItem(21));
        assertFalse(numbers.hasItem(10));
        assertFalse(numbers.hasItem(20));
    }

    @Test
    public void testConvert() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        List<Integer> list = numbers.getListing();
        assertTrue(list.contains(19));
        assertTrue(list.contains(20));
        assertTrue(list.contains(21));
        assertEquals(3, list.size());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testModify() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.addItem(19);
        numbers.addItem(20);
        numbers.addItem(21);

        List<Integer> list = numbers.getListing();
        list.add(12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull1() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.addItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull2() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.removeItem(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNull3() {
        SimpleList<Integer> numbers = new SimpleList<>();
        numbers.hasItem(null);
    }

}
