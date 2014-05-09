package com.turt2live.antishare.configuration;

import com.turt2live.antishare.object.attribute.ASGameMode;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
public class BreakSettingsTest {

    @Test
    public void testCreate() {
        for (ASGameMode gameMode : ASGameMode.values()) {
            BreakSettings settings = new BreakSettings(true, gameMode);
            assertEquals(true, settings.denyAction);
            assertEquals(gameMode, settings.breakAs);

            settings = new BreakSettings(false, gameMode);
            assertEquals(false, settings.denyAction);
            assertEquals(gameMode, settings.breakAs);
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateNull() {
        new BreakSettings(false, null);
    }
}
