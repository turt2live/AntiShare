package com.turt2live.antishare;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TestAntiShare {

	int val = 0;

	@Before
	public void before(){
		val = 1;
	}

	@After
	public void after(){
		System.out.println("VALUE = " + val);
	}

	@Test
	public void test(){
		val = 2;
	}

}
