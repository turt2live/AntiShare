package com.turt2live.antishare.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.lang.Locale;

@RunWith (PowerMockRunner.class)
public class TestWrappedEnhancedConfiguration {

	private WrappedEnhancedConfiguration wrapper;

	@Before
	public void setUp(){
		File dataFolder = new File("src" + File.separator + "main" + File.separator + "resources");
		File file = new File(dataFolder, "locale" + File.separator + Locale.EN_US.getFileName());
		wrapper = new WrappedEnhancedConfiguration(file);
	}

	@After
	public void after(){}

	@Test
	public void testClear(){
		assertTrue(wrapper.getKeys(true).size() > 0);
		wrapper.clearFile();
		assertEquals(0, wrapper.getKeys(true).size());
	}

}
