package com.turt2live.antishare.util.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;

import org.bukkit.Material;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.signs.Sign;
import com.turt2live.antishare.signs.SignList;
import com.turt2live.antishare.test.util.FakeAntiShare;

@RunWith (PowerMockRunner.class)
public class TestItemMap {

	private FakeAntiShare fake = new FakeAntiShare();
	private ItemMap map;

	// Reload test is implied with other tests. If it fails, so will the test

	@Before
	public void setUp(){
		SignList signList = PowerMockito.mock(SignList.class);
		PowerMockito.when(signList.getSign(anyString())).thenReturn(new Sign(null, null, false, false));
		fake.signs = signList;
		fake.prepare();
		map = new ItemMap();
	}

	@After
	public void after(){
		map.reload();
	}

	@Test
	public void testGetSign(){
		assertNull(map.getSign("test"));
		map.addTemporarySign("test", new Sign("testSign", new String[] {"*", "*", "*", "*"}, true, false));
		assertNotNull(map.getSign("test"));
	}

	@Test
	public void testGetItem(){
		assertEquals(Material.STONE, map.getItem("STONE"));
		assertNull(map.getItem("sdajkdh"));
		assertNull(map.getItem(null));
		map.addTemporaryItem("test", Material.STONE);
		assertEquals(Material.STONE, map.getItem("test"));
		assertNull(map.getItem("test2"));
	}

	@Test
	public void testGetItem2(){
		assertNull(map.getItem(null, false, false));
		assertNull(map.getItem(null, true, false));
		assertNull(map.getItem(null, false, true));
		assertNull(map.getItem(null, true, true));
		assertEquals("1:0", map.getItem("stone:0", false, true));
		assertEquals("1:*", map.getItem("stone", false, true));
		assertEquals("1:test", map.getItem("stone:test", false, false));
		assertEquals("1:*", map.getItem("stone", false, false));
		assertEquals("1", map.getItem("stone:0", true, true));
		assertEquals("1", map.getItem("stone", true, true));
		assertEquals("1:test", map.getItem("stone:test", true, false));
		assertEquals("1", map.getItem("stone", true, false));
		map.addTemporaryItem("test", Material.STONE);
		assertEquals("1:0", map.getItem("test:0", false, true));
		assertEquals("1:*", map.getItem("test", false, true));
		assertEquals("1:test", map.getItem("test:test", false, false));
		assertEquals("1:*", map.getItem("test", false, false));
		assertEquals("1", map.getItem("test:0", true, true));
		assertEquals("1", map.getItem("test", true, true));
		assertEquals("1:test", map.getItem("test:test", true, false));
		assertEquals("1", map.getItem("test", true, false));
	}

}
