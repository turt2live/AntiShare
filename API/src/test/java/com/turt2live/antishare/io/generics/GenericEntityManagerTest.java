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

package com.turt2live.antishare.io.generics;

import com.turt2live.antishare.object.attribute.ObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(JUnit4.class)
public class GenericEntityManagerTest {

    private static class TestManager extends GenericEntityManager {

        @Override
        public void save() {
        }

        @Override
        public void load() {
        }
    }

    @Test
    public void testSetGetClear() {
        GenericEntityManager manager = new TestManager();

        UUID uuid = UUID.randomUUID();

        assertNotNull(manager.getType(uuid));
        assertEquals(ObjectType.UNKNOWN, manager.getType(uuid));

        manager.setType(uuid, ObjectType.SPECTATOR);

        assertNotNull(manager.getType(uuid));
        assertEquals(ObjectType.SPECTATOR, manager.getType(uuid));

        manager.clear();

        assertNotNull(manager.getType(uuid));
        assertEquals(ObjectType.UNKNOWN, manager.getType(uuid));
    }

}
