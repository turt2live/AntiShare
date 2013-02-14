package com.turt2live.antishare.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.bukkit.entity.Player;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith (PowerMockRunner.class)
public class TestErrorAndStringList {

	private Player player;

	@Before
	public void setUp(){
		player = mock(Player.class);
	}

	@After
	public void after(){}

	@Test
	public void testGetMethods(){
		StringList blankList = new StringList();
		StringList filledList = new StringList("test1", "test2");
		assertEquals(0, blankList.get().length);
		assertEquals(2, filledList.get().length);
		assertEquals(0, blankList.getList().size());
		assertEquals(2, filledList.getList().size());
		blankList = new ErrorStringList("error");
		filledList = new ErrorStringList("error", "test1", "test2");
		assertEquals(0, blankList.get().length);
		assertEquals(2, filledList.get().length);
		assertEquals(0, blankList.getList().size());
		assertEquals(2, filledList.getList().size());
	}

	@Test
	public void testIsError(){
		StringList stringList = new StringList();
		ErrorStringList errorList = new ErrorStringList("error");
		assertFalse(stringList.isError());
		assertTrue(errorList.isError());
	}

	@Test
	public void testSendToPlayer(){
		StringList stringList = new StringList();
		ErrorStringList errorList = new ErrorStringList("error");
		stringList.print(player);
		verify(player, never()).sendMessage(anyString());
		errorList.print(player);
		verify(player, times(1)).sendMessage(anyString());
	}

}
