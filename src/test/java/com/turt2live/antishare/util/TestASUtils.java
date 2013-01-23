package com.turt2live.antishare.util;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.turt2live.antishare.util.ASUtils;

public class TestASUtils {

	@Before
	public void setUp(){
		// Runs before @Test
	}

	@After
	public void tearDown(){
		// Runs after @Test
	}

	@Test
	public void testGetBoolean(){
		// True
		assertTrue(ASUtils.getBoolean("true"));
		assertTrue(ASUtils.getBoolean("t"));
		assertTrue(ASUtils.getBoolean("on"));
		assertTrue(ASUtils.getBoolean("active"));
		assertTrue(ASUtils.getBoolean("1"));

		// False
		assertFalse(ASUtils.getBoolean("false"));
		assertFalse(ASUtils.getBoolean("f"));
		assertFalse(ASUtils.getBoolean("off"));
		assertFalse(ASUtils.getBoolean("inactive"));
		assertFalse(ASUtils.getBoolean("0"));

		// Invalid
		assertNull(ASUtils.getBoolean("thisShouldBeNull"));
		assertNull(ASUtils.getBoolean("not-a-boolean"));
		assertNull(ASUtils.getBoolean(null));
		assertNull(ASUtils.getBoolean(""));
		assertNull(ASUtils.getBoolean(" "));
		assertNull(ASUtils.getBoolean("		")); // Has a tab character in it
	}

}
