package com.turt2live.antishare.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.util.FakeItemMeta;
import com.turt2live.antishare.test.util.FakeItemMetaFactory;

@RunWith (PowerMockRunner.class)
public class TestFakeItemMetaFactory {

	private FakeItemMetaFactory factory;

	@Before
	public void setup(){
		factory = new FakeItemMetaFactory();
	}

	@After
	public void tearDown(){}

	@Test
	public void testGetItemMeta(){
		assertNotNull(factory.getItemMeta(Material.STATIONARY_LAVA));
	}

	@Test
	public void testIsApplicable(){
		assertTrue(factory.isApplicable(null, (ItemStack) null));
		assertTrue(factory.isApplicable(null, (Material) null));
	}

	@Test
	public void testAsMetaFor(){
		FakeItemMeta meta = new FakeItemMeta();
		assertEquals(meta, factory.asMetaFor(meta, (ItemStack) null));
		assertEquals(meta, factory.asMetaFor(meta, (Material) null));
	}

	@Test
	public void testEquals(){
		FakeItemMeta meta = new FakeItemMeta();
		assertTrue(factory.equals(meta, meta));
	}

	@Test
	public void testDefaultLeatherColor(){
		assertEquals(Color.AQUA, factory.getDefaultLeatherColor());
	}

}
