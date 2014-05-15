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

import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.object.attribute.ObjectType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ASUtilsTest {

    @Test
    public void testToBlock() {
        assertEquals(ObjectType.ADVENTURE, ASUtils.toBlockType(ASGameMode.ADVENTURE));
        assertEquals(ObjectType.SURVIVAL, ASUtils.toBlockType(ASGameMode.SURVIVAL));
        assertEquals(ObjectType.CREATIVE, ASUtils.toBlockType(ASGameMode.CREATIVE));
        assertEquals(ObjectType.SPECTATOR, ASUtils.toBlockType(ASGameMode.SPECTATOR));
        assertEquals(ObjectType.UNKNOWN, ASUtils.toBlockType(null));
    }

    @Test
    public void testToGamemode() {
        assertEquals(ASGameMode.ADVENTURE, ASUtils.toGamemode(ObjectType.ADVENTURE));
        assertEquals(ASGameMode.SURVIVAL, ASUtils.toGamemode(ObjectType.SURVIVAL));
        assertEquals(ASGameMode.CREATIVE, ASUtils.toGamemode(ObjectType.CREATIVE));
        assertEquals(ASGameMode.SPECTATOR, ASUtils.toGamemode(ObjectType.SPECTATOR));
        assertNull(ASUtils.toGamemode(ObjectType.UNKNOWN));
        assertNull(ASUtils.toGamemode(null));
    }

    @Test
    public void testWords() {
        assertEquals("Test", ASUtils.toUpperWords("test"));
        assertEquals("Test", ASUtils.toUpperWords("Test"));
        assertEquals("Test", ASUtils.toUpperWords("test "));
        assertEquals("Test", ASUtils.toUpperWords("test_"));
        assertEquals("Test", ASUtils.toUpperWords("Test "));
        assertEquals("Test", ASUtils.toUpperWords("Test_"));

        assertEquals("Test Test", ASUtils.toUpperWords("test test"));
        assertEquals("Test Test", ASUtils.toUpperWords("test Test"));
        assertEquals("Test Test", ASUtils.toUpperWords("test_test"));
        assertEquals("Test Test", ASUtils.toUpperWords("test_Test"));
        assertEquals("Test Test", ASUtils.toUpperWords("test test "));
        assertEquals("Test Test", ASUtils.toUpperWords("test Test "));
        assertEquals("Test Test", ASUtils.toUpperWords("test_test "));
        assertEquals("Test Test", ASUtils.toUpperWords("test_Test "));
        assertEquals("Test Test", ASUtils.toUpperWords("test test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("test Test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("test_test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("test_Test_"));

        assertEquals("Test Test", ASUtils.toUpperWords("Test test"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test Test"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_test"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_Test"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test test "));
        assertEquals("Test Test", ASUtils.toUpperWords("Test Test "));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_test "));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_Test "));
        assertEquals("Test Test", ASUtils.toUpperWords("Test test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test Test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_test_"));
        assertEquals("Test Test", ASUtils.toUpperWords("Test_Test_"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWords() {
        ASUtils.toUpperWords(null);
    }

}
