package com.turt2live.antishare.economy;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertEquals;

@RunWith(JUnit4.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BalanceModifierTest {

    @Test(expected = IllegalArgumentException.class)
    public void aTestNull() {
        new BalanceModifier(null, "", 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void bTestNull() {
        new BalanceModifier(BalanceModifier.ModifierType.FINE, null, 12);
    }

    @Test(expected = IllegalArgumentException.class)
    public void cTestSmall() {
        new BalanceModifier(BalanceModifier.ModifierType.FINE, "", -10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void dTestSmall() {
        new BalanceModifier(BalanceModifier.ModifierType.FINE, "", 0);
    }

    @Test
    public void eTestConstructor() {
        BalanceModifier modifier = new BalanceModifier(BalanceModifier.ModifierType.FINE, "account", 10);
        assertEquals("account", modifier.getAccount());
        assertEquals(BalanceModifier.ModifierType.FINE, modifier.getType());
        assertEquals(10.0, modifier.getAmount(), 0);
    }

    @Test
    public void fTestScaled(){
        BalanceModifier modifier = new BalanceModifier(BalanceModifier.ModifierType.FINE,"",10);
        assertEquals(-10.0, modifier.getScaledAmount(), 0);

        modifier=new BalanceModifier(BalanceModifier.ModifierType.REWARD,"",10);
        assertEquals(10.0, modifier.getScaledAmount(), 0);
    }

}
