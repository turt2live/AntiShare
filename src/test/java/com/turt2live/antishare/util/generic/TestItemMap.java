package com.turt2live.antishare.util.generic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyString;

import java.io.File;

import org.bukkit.Material;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.signs.Sign;
import com.turt2live.antishare.signs.SignList;
import com.turt2live.antishare.test.util.FakeAntiShare;
import com.turt2live.antishare.util.WrappedEnhancedConfiguration;

@RunWith (PowerMockRunner.class)
public class TestItemMap {

	private FakeAntiShare fake = new FakeAntiShare();
	private ItemMap map;
	private WrappedEnhancedConfiguration yaml;

	// Reload test is implied with other tests. If it fails, so will the test

	@Before
	public void setUp(){
		SignList signList = PowerMockito.mock(SignList.class);
		PowerMockito.when(signList.getSign(anyString())).thenReturn(new Sign(null, null, false, false));
		fake.signs = signList;
		fake.prepare();
		map = new ItemMap();
		AntiShare plugin = fake.get();
		File file = new File(plugin.getDataFolder(), "items.yml");
		System.out.println("FILE EXISTS: " + file.exists() + " PATH = " + file.getAbsolutePath());
		yaml = new WrappedEnhancedConfiguration(file, plugin);
	}

	@After
	public void after(){
		yaml.clearFile();
		yaml.save();
	}

	@Test
	public void testGetSign(){
		assertNull(map.getSign(null));
		assertNull(map.getSign("test"));
		yaml.set("test", "test:test");
		yaml.save();
		map.reload();
		assertNotNull(map.getSign("test"));
	}

	@Test
	public void testGetItem(){
		assertEquals(Material.STONE, map.getItem("STONE"));
		assertNull(map.getItem("sdajkdh"));
		assertNull(map.getItem(null));
		yaml.set("test", "STONE");
		yaml.set("test2", "stone");
		yaml.set("test3", "1");
		yaml.save();
		map.reload();
		assertEquals(Material.STONE, map.getItem("test"));
		assertEquals(Material.STONE, map.getItem("test2"));
		assertEquals(Material.STONE, map.getItem("test3"));
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
		yaml.set("test", "stone");
		yaml.save();
		map.reload();
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
