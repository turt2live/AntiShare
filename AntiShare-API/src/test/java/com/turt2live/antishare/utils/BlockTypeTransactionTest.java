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

import com.turt2live.antishare.io.BlockManager;
import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.ObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(JUnit4.class)
public class BlockTypeTransactionTest {

    @Test
    public void testCommitAndAdd() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        BlockManager manager = mock(BlockManager.class);

        Map<ASLocation, ObjectType> typeMap = new HashMap<ASLocation, ObjectType>();

        for (int i = 0; i < 100; i++) {
            ASLocation location = mock(ASLocation.class);
            ObjectType type = ObjectType.CREATIVE;

            transaction.add(location, type);
            typeMap.put(location, type);
        }

        transaction.commit(manager);
        verify(manager, times(typeMap.size())).setBlockType(any(ASLocation.class), any(ObjectType.class));

        // Safe add begin

        transaction = new BlockTypeTransaction();
        manager = mock(BlockManager.class);
        typeMap.clear();

        for (int i = 0; i < 100; i++) {
            ASLocation location = mock(ASLocation.class);
            ObjectType type = ObjectType.CREATIVE;

            ObjectType existing = typeMap.containsKey(location) ? typeMap.get(location) : ObjectType.UNKNOWN;

            transaction.add(location, type);
            if (existing == ObjectType.UNKNOWN) typeMap.put(location, type);
        }

        transaction.commit(manager);
        verify(manager, times(typeMap.size())).setBlockType(any(ASLocation.class), any(ObjectType.class));
    }

    @Test
    public void testDoubleCommit() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        BlockManager manager = mock(BlockManager.class);

        for (int i = 0; i < 100; i++) {
            ASLocation location = mock(ASLocation.class);
            ObjectType type = ObjectType.CREATIVE;
            transaction.add(location, type);
        }

        transaction.commit(manager);
        verify(manager, times(100)).setBlockType(any(ASLocation.class), any(ObjectType.class));
        transaction.commit(manager);
        verifyNoMoreInteractions(manager);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommitNull() {
        new BlockTypeTransaction().commit(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNull1() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        transaction.add(null, ObjectType.CREATIVE);
    }

    @Test
    public void testAddNull2() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        BlockManager manager = mock(BlockManager.class);

        transaction.add(mock(ASLocation.class), null);

        transaction.commit(manager);
        verify(manager, times(1)).setBlockType(any(ASLocation.class), eq(ObjectType.UNKNOWN));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSafeAddNull1() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        transaction.safeAdd(null, ObjectType.CREATIVE);
    }

    @Test
    public void testSafeAddNull2() {
        BlockTypeTransaction transaction = new BlockTypeTransaction();
        BlockManager manager = mock(BlockManager.class);

        transaction.safeAdd(mock(ASLocation.class), null);

        transaction.commit(manager);
        verify(manager, times(1)).setBlockType(any(ASLocation.class), eq(ObjectType.UNKNOWN));
    }

}
