package com.turt2live.antishare.util.generic;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith (PowerMockRunner.class)
public class TestASEntity {

	@Before
	public void setUp(){}

	@After
	public void after(){}

	@Test
	public void testGetMethods(){
		ASEntity entity = new ASEntity("1", "2");
		assertEquals("1", entity.getGivenName());
		assertEquals("2", entity.getProperName());
	}

}
