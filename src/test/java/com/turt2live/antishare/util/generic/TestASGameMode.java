package com.turt2live.antishare.util.generic;

import static org.junit.Assert.assertEquals;

import org.bukkit.GameMode;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import com.turt2live.antishare.test.util.FakeAntiShare;

@RunWith (PowerMockRunner.class)
public class TestASGameMode {

	private FakeAntiShare fake = new FakeAntiShare();

	@Before
	public void setUp(){
		fake.prepare();
	}

	@After
	public void after(){}

	@Test
	public void testMatch(){
		GameMode creative = GameMode.CREATIVE;
		GameMode survival = GameMode.SURVIVAL;
		GameMode adventure = GameMode.ADVENTURE;

		// Expected results
		boolean[][] expected = {
				// Test creative, survival, adventure
				new boolean[] {true, false, false},
				new boolean[] {false, true, false},
				new boolean[] {false, false, true},
				new boolean[] {true, true, true},
				new boolean[] {true, true, true},
				new boolean[] {false, false, false}
		};

		ASGameMode[] values = ASGameMode.values();

		for(int i = 0; i < values.length; i++){
			boolean[] expectedResults = expected[i];
			ASGameMode value = values[i];
			assertEquals(expectedResults[0], value.matches(creative));
			assertEquals(expectedResults[1], value.matches(survival));
			assertEquals(expectedResults[2], value.matches(adventure));
		}

		// PHASE 2: Test configuration change
		boolean original = fake.get().getConfig().getBoolean("other.adventure-is-creative");
		fake.get().getConfig().set("other.adventure-is-creative", !original);

		// Expected results
		expected = new boolean[][] {
				// Test creative, survival, adventure
				new boolean[] {true, false, false},
				new boolean[] {false, true, false},
				new boolean[] {false, false, true},
				new boolean[] {true, true, true},
				new boolean[] {true, true, false},
				new boolean[] {false, false, false}
		};

		for(int i = 0; i < values.length; i++){
			boolean[] expectedResults = expected[i];
			ASGameMode value = values[i];
			assertEquals(expectedResults[0], value.matches(creative));
			assertEquals(expectedResults[1], value.matches(survival));
			assertEquals(expectedResults[2], value.matches(adventure));
		}

		// Revert 
		fake.get().getConfig().set("other.adventure-is-creative", original);
	}

}
