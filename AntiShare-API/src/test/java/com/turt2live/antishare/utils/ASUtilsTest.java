package com.turt2live.antishare.utils;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

@RunWith(JUnit4.class)
public class ASUtilsTest {

    @Test
    public void testToBlock() {
        assertEquals(BlockType.ADVENTURE, ASUtils.toBlockType(ASGameMode.ADVENTURE));
        assertEquals(BlockType.SURVIVAL, ASUtils.toBlockType(ASGameMode.SURVIVAL));
        assertEquals(BlockType.CREATIVE, ASUtils.toBlockType(ASGameMode.CREATIVE));
        assertEquals(BlockType.SPECTATOR, ASUtils.toBlockType(ASGameMode.SPECTATOR));
        assertEquals(BlockType.UNKNOWN, ASUtils.toBlockType(null));
    }

    @Test
    public void testToGamemode() {
        assertEquals(ASGameMode.ADVENTURE, ASUtils.toGamemode(BlockType.ADVENTURE));
        assertEquals(ASGameMode.SURVIVAL, ASUtils.toGamemode(BlockType.SURVIVAL));
        assertEquals(ASGameMode.CREATIVE, ASUtils.toGamemode(BlockType.CREATIVE));
        assertEquals(ASGameMode.SPECTATOR, ASUtils.toGamemode(BlockType.SPECTATOR));
        assertNull(ASUtils.toGamemode(BlockType.UNKNOWN));
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
