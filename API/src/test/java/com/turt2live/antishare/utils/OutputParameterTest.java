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

package com.turt2live.antishare.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(JUnit4.class)
public class OutputParameterTest {

    @Test
    public void testDefault() {
        OutputParameter<Object> parameter = new OutputParameter<>();
        assertNull(parameter.getValue());

        Object obj = mock(Object.class);
        parameter = new OutputParameter<>(obj);
        assertNotNull(parameter.getValue());
        assertEquals(obj, parameter.getValue());
    }

    @Test
    public void testWasCalled() {
        OutputParameter<Object> parameter = new OutputParameter<>();
        assertFalse(parameter.wasCalled());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<>(mock(Object.class));
        assertFalse(parameter.wasCalled());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<>();
        parameter.setValue(null);
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<>(mock(Object.class));
        parameter.setValue(null);
        assertTrue(parameter.wasCalled());
    }

    @Test
    public void testHasValue() {
        OutputParameter<Object> parameter = new OutputParameter<>();
        assertFalse(parameter.hasValue());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.hasValue());

        parameter = new OutputParameter<>(mock(Object.class));
        assertTrue(parameter.hasValue());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.hasValue());

        parameter = new OutputParameter<>();
        parameter.setValue(null);
        assertFalse(parameter.hasValue());

        parameter = new OutputParameter<>(mock(Object.class));
        parameter.setValue(null);
        assertFalse(parameter.hasValue());
    }

    @Test
    public void testGetValue() {
        OutputParameter<Object> parameter = new OutputParameter<>();
        assertNull(parameter.getValue());
        parameter.setValue(mock(Object.class));
        assertNotNull(parameter.getValue());

        parameter = new OutputParameter<>(mock(Object.class));
        assertNotNull(parameter.getValue());
        parameter.setValue(mock(Object.class));
        assertNotNull(parameter.getValue());

        parameter = new OutputParameter<>();
        parameter.setValue(null);
        assertNull(parameter.getValue());

        parameter = new OutputParameter<>(mock(Object.class));
        parameter.setValue(null);
        assertNull(parameter.getValue());
    }

}
