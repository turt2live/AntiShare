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
        OutputParameter<Object> parameter = new OutputParameter<Object>();
        assertNull(parameter.getValue());

        Object obj = mock(Object.class);
        parameter = new OutputParameter<Object>(obj);
        assertNotNull(parameter.getValue());
        assertEquals(obj, parameter.getValue());
    }

    @Test
    public void testWasCalled() {
        OutputParameter<Object> parameter = new OutputParameter<Object>();
        assertFalse(parameter.wasCalled());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<Object>(mock(Object.class));
        assertFalse(parameter.wasCalled());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<Object>();
        parameter.setValue(null);
        assertTrue(parameter.wasCalled());

        parameter = new OutputParameter<Object>(mock(Object.class));
        parameter.setValue(null);
        assertTrue(parameter.wasCalled());
    }

    @Test
    public void testHasValue() {
        OutputParameter<Object> parameter = new OutputParameter<Object>();
        assertFalse(parameter.hasValue());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.hasValue());

        parameter = new OutputParameter<Object>(mock(Object.class));
        assertTrue(parameter.hasValue());
        parameter.setValue(mock(Object.class));
        assertTrue(parameter.hasValue());

        parameter = new OutputParameter<Object>();
        parameter.setValue(null);
        assertFalse(parameter.hasValue());

        parameter = new OutputParameter<Object>(mock(Object.class));
        parameter.setValue(null);
        assertFalse(parameter.hasValue());
    }

    @Test
    public void testGetValue() {
        OutputParameter<Object> parameter = new OutputParameter<Object>();
        assertNull(parameter.getValue());
        parameter.setValue(mock(Object.class));
        assertNotNull(parameter.getValue());

        parameter = new OutputParameter<Object>(mock(Object.class));
        assertNotNull(parameter.getValue());
        parameter.setValue(mock(Object.class));
        assertNotNull(parameter.getValue());

        parameter = new OutputParameter<Object>();
        parameter.setValue(null);
        assertNull(parameter.getValue());

        parameter = new OutputParameter<Object>(mock(Object.class));
        parameter.setValue(null);
        assertNull(parameter.getValue());
    }

}
