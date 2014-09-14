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

package com.turt2live.antishare.configuration;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class MemoryConfigurationTest {

    private static final double DELTA = 1e-15;

    @Test
    public void testGetSetDouble() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        double value = 0.34;
        double def = 0.12;
        configuration.set("double", value);

        assertEquals(value, configuration.getDouble("double"), DELTA);
        assertEquals(value, configuration.getDouble("double", def), DELTA);
        assertEquals(0.00, configuration.getDouble("not double"), DELTA);
        assertEquals(def, configuration.getDouble("not double", def), DELTA);

        assertEquals(0, configuration.getInt("double"));
        assertEquals(null, configuration.getString("double"));
        assertEquals(false, configuration.getBoolean("double"));
        assertEquals(value, configuration.getObject("double"));
        assertEquals(null, configuration.getStringList("double"));
    }

    @Test
    public void testGetSetInteger() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        int value = 4;
        int def = 12;
        configuration.set("integer", value);

        assertEquals(value, configuration.getInt("integer"));
        assertEquals(value, configuration.getInt("integer", def));
        assertEquals(0, configuration.getInt("not integer"));
        assertEquals(def, configuration.getInt("not integer", def));

        assertEquals(0.00, configuration.getDouble("integer"), DELTA);
        assertEquals(null, configuration.getString("integer"));
        assertEquals(false, configuration.getBoolean("integer"));
        assertEquals(value, configuration.getObject("integer"));
        assertEquals(null, configuration.getStringList("integer"));
    }

    @Test
    public void testGetSetString() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        String value = "test";
        String def = "def test";
        configuration.set("string", value);

        assertEquals(value, configuration.getString("string"));
        assertEquals(value, configuration.getString("string", def));
        assertEquals(null, configuration.getString("not string"));
        assertEquals(def, configuration.getString("not string", def));

        assertEquals(0.00, configuration.getDouble("string"), DELTA);
        assertEquals(0, configuration.getInt("string"));
        assertEquals(false, configuration.getBoolean("string"));
        assertEquals(value, configuration.getObject("string"));
        assertEquals(null, configuration.getStringList("string"));
    }

    @Test
    public void testGetSetStringList() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        List<String> value = new ArrayList<>();
        List<String> def = new ArrayList<>();
        configuration.set("stringlist", value);

        assertEquals(value, configuration.getStringList("stringlist"));
        assertEquals(value, configuration.getStringList("stringlist", def));
        assertEquals(null, configuration.getStringList("not stringlist"));
        assertEquals(def, configuration.getStringList("not stringlist", def));

        assertEquals(0.00, configuration.getDouble("stringlist"), DELTA);
        assertEquals(0, configuration.getInt("stringlist"));
        assertEquals(false, configuration.getBoolean("stringlist"));
        assertEquals(value, configuration.getObject("stringlist"));
        assertEquals(null, configuration.getString("stringlist"));
    }

    @Test
    public void testGetSetBoolean() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        boolean value = true;
        boolean def = false;
        configuration.set("boolean", value);

        assertEquals(value, configuration.getBoolean("boolean"));
        assertEquals(value, configuration.getBoolean("boolean", def));
        assertEquals(false, configuration.getBoolean("not boolean"));
        assertEquals(def, configuration.getBoolean("not boolean", def));

        assertEquals(0.00, configuration.getDouble("boolean"), DELTA);
        assertEquals(0, configuration.getInt("boolean"));
        assertEquals(null, configuration.getStringList("boolean"));
        assertEquals(value, configuration.getObject("boolean"));
        assertEquals(null, configuration.getString("boolean"));
    }

    @Test
    public void testGetSetObject() {
        MemoryConfiguration configuration = new MemoryConfiguration();

        Object value = configuration;
        Object def = this;
        configuration.set("object", value);

        assertEquals(value, configuration.getObject("object"));
        assertEquals(value, configuration.getObject("object", def));
        assertEquals(null, configuration.getObject("not object"));
        assertEquals(def, configuration.getObject("not object", def));

        assertEquals(0.00, configuration.getDouble("object"), DELTA);
        assertEquals(0, configuration.getInt("object"));
        assertEquals(null, configuration.getStringList("object"));
        assertEquals(false, configuration.getBoolean("object"));
        assertEquals(null, configuration.getString("object"));
    }

}
