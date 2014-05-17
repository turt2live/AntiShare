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

package com.turt2live.antishare.io.flatfile;

import com.turt2live.antishare.object.attribute.ObjectType;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import java.io.File;
import java.util.Random;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FileEntityManagerTest {

    private static File folder;
    private static File testFile1;
    private static ObjectType[] expected;
    private static UUID[] uuids;

    @Test
    public void aTestWrite() {
        FileEntityManager store = new FileEntityManager(testFile1);
        Random random = new Random();
        ObjectType[] types = ObjectType.values();
        expected = new ObjectType[60];
        uuids = new UUID[60];

        for (int z = 0; z < 60; z++) {
            ObjectType type;

            do {
                type = types[random.nextInt(types.length)];
            } while (type == ObjectType.UNKNOWN);

            UUID uuid = UUID.randomUUID();

            store.setType(uuid, type);
            expected[z] = type;
            uuids[z] = uuid;
        }
        store.save();
    }

    @Test
    public void bTestRead() {
        FileEntityManager store = new FileEntityManager(testFile1);
        store.load();

        for (int i = 0; i < uuids.length; i++) {
            UUID uuid = uuids[i];
            ObjectType type = expected[i];

            assertEquals(type, store.getType(uuid));
        }
    }

    @Test
    public void hTestWriteNothing() {
        // Attempt to write nothing, the file should not be created
        File file = new File(folder, "write_nothing_test.dat");
        FileEntityManager store = new FileEntityManager(file);
        store.save();

        assertFalse(file.exists());
    }

    @BeforeClass
    public static void preTest() {
        folder = new File("test_data_3");
        if (folder.exists()) {
            delete(folder);
        }
        folder.mkdirs();
        testFile1 = new File(folder, "entities.dat");
    }

    @AfterClass
    public static void postTest() {
        if (folder != null && folder.exists()) {
            delete(folder);
        }
    }

    private static void delete(File folder) {
        File[] files = folder.listFiles();
        for (File f : files) {
            if (f.isDirectory()) delete(f);
            else f.delete();
        }
        folder.delete();
    }
}
