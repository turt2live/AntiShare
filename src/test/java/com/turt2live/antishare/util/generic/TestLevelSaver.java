package com.turt2live.antishare.util.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyFloat;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.util.FakeAntiShare;
import com.turt2live.antishare.util.generic.LevelSaver.Level;

@RunWith (PowerMockRunner.class)
public class TestLevelSaver {

	private Player player;
	private FakeAntiShare fake = new FakeAntiShare();
	public static final double EPSILON = 1e-15;

	@Before
	public void setUp(){
		fake.prepare();
		player = PowerMockito.mock(Player.class);
	}

	@After
	public void after(){}

	@Test
	public void testGetSign(){
		Level test1 = LevelSaver.getLevel("turt2live", GameMode.CREATIVE);
		assertNotNull(test1);
		assertEquals(0, test1.level);
		assertEquals(0.0f, test1.percent, EPSILON);
		Level test2 = new Level(1, 0.6f);
		LevelSaver.saveLevel("turt2live", GameMode.SURVIVAL, test2);
		Level test3 = LevelSaver.getLevel("turt2live", GameMode.SURVIVAL);
		assertNotNull(test3);
		assertEquals(1, test3.level);
		assertEquals(0.6f, test3.percent, EPSILON);
	}

	@Test
	public void testLevel(){
		// Test level class
		Level testLevel = new Level(9, 0.5f);
		assertEquals(9, testLevel.level);
		assertEquals(0.5f, testLevel.percent, EPSILON);

		// Test setTo()
		verify(player, never()).setLevel(anyInt());
		verify(player, never()).setExp(anyFloat());
		testLevel.setTo(player);
		verify(player, times(1)).setLevel(anyInt());
		verify(player, times(1)).setExp(anyFloat());
	}

}
