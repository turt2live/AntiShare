package com.turt2live.antishare.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith (PowerMockRunner.class)
public class TestFakeInventory {

	private FakeInventory inventory = new FakeInventory();

	@Before
	public void setUp(){}

	@After
	public void tearDown(){
		inventory.clear();
	}

	@Test
	public void testGetSize(){
		assertEquals(36, inventory.getSize());
	}

	@Test
	public void testGetAndSetItem(){
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemStack item2 = new ItemStack(Material.STONE);
		inventory.setItem(5, item);
		inventory.setItem(9, item2);
		assertEquals(Material.STONE, inventory.getItem(9).getType());
		assertEquals(Material.ANVIL, inventory.getItem(5).getType());
		assertNull(inventory.getItem(4));
	}

	@Test
	public void testAddItem(){
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemStack item2 = new ItemStack(Material.STONE);
		HashMap<Integer, ItemStack> left = inventory.addItem(item, item2);
		assertEquals(0, left.size());
		assertEquals(Material.STONE, inventory.getItem(2).getType());
		assertEquals(Material.ANVIL, inventory.getItem(1).getType());
		assertNull(inventory.getItem(4));
	}

	@Test
	public void testGetAndSetContents(){
		ItemStack item = new ItemStack(Material.ANVIL);
		ItemStack item2 = new ItemStack(Material.STONE);
		ItemStack[] set = new ItemStack[] {item, item2};
		inventory.setContents(set);
		ItemStack[] stacks = inventory.getContents();
		assertNotNull(stacks);
		assertEquals(2, stacks.length);
		for(int i = 0; i < stacks.length; i++){
			assertEquals(set[i], stacks[i]);
		}
	}

	@Test
	public void testContainsMaterial(){
		ItemStack item = new ItemStack(Material.ANVIL);
		inventory.addItem(item);
		assertTrue(inventory.contains(Material.ANVIL));
		assertFalse(inventory.contains(Material.STONE));
	}

	@Test
	public void testFirstEmpty(){
		ItemStack item = new ItemStack(Material.ANVIL);
		assertEquals(1, inventory.firstEmpty());
		inventory.addItem(item);
		assertEquals(2, inventory.firstEmpty());
		for(int i = 0; i <= inventory.getSize(); i++){
			inventory.setItem(i, item);
		}
		assertEquals(-1, inventory.firstEmpty());
	}

	@Test
	public void testClear(){
		ItemStack item = new ItemStack(Material.ANVIL);
		inventory.addItem(item);
		inventory.clear(4);
		assertEquals(2, inventory.firstEmpty());
		inventory.clear(1);
		assertEquals(1, inventory.firstEmpty());
		inventory.addItem(item);
		inventory.clear();
		assertEquals(1, inventory.firstEmpty());
	}

	@Test
	public void testGetType(){
		assertEquals(InventoryType.PLAYER, inventory.getType());
	}

	@Test
	public void testOtherMethods(){
		try{
			inventory.getMaxStackSize();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setMaxStackSize(0);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getName();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.removeItem();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.contains(0);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.contains((ItemStack) null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.contains(0, 0);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.containsAtLeast(null, 1);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.all((ItemStack) null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.all((Material) null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.all(1);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.first(1);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.first(Material.STONE);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.first((ItemStack) null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.remove(1);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.remove(Material.STONE);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.remove((ItemStack) null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getViewers();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getTitle();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.iterator();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.iterator(1);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getChestplate();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getBoots();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getHeldItemSlot();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getArmorContents();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getHelmet();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getLeggings();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setArmorContents(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setHelmet(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setBoots(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setLeggings(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setChestplate(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.setItemInHand(null);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getItemInHand();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.getHolder();
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
		try{
			inventory.clear(0, 0);
			throw new UnknownError("Call passed");
		}catch(UnsupportedOperationException e){}
	}

}
