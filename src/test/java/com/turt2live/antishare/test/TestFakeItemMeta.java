package com.turt2live.antishare.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.util.FakeItemMeta;

@RunWith (PowerMockRunner.class)
public class TestFakeItemMeta {

	private FakeItemMeta meta;

	@Before
	public void setup(){
		meta = new FakeItemMeta();
	}

	@After
	public void tearDown(){}

	@Test
	public void testDisplayName(){
		assertFalse(meta.hasDisplayName());
		meta.setDisplayName("test");
		assertTrue(meta.hasDisplayName());
		assertEquals("test", meta.getDisplayName());
	}

	@Test
	public void testLore(){
		assertFalse(meta.hasLore());
		List<String> lore = new ArrayList<String>();
		lore.add("test");
		meta.setLore(lore);
		assertTrue(meta.hasLore());
		List<String> lore2 = meta.getLore();
		for(int i = 0; i < lore2.size(); i++){
			assertEquals(lore.get(i), lore2.get(i));
		}
	}

	@Test
	public void testClone(){
		assertEquals(meta, meta.clone());
	}

	@Test
	public void testOtherMethods(){
		try{
			meta.serialize();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.hasEnchants();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.hasEnchant(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.getEnchantLevel(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.getEnchants();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.addEnchant(null, 0, false);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			meta.removeEnchant(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
	}

}
