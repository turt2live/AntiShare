/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(JUnit4.class)
public class AColorTest {

    @Test
    public void testToString() {
        for (AColor color : AColor.values()) {
            assertThat(String.format("%c%c", AColor.COLOR_CHAR, color.code), is(color.toString()));
        }
    }

    @Test
    public void testToStringBukkit() {
        for (AColor color : AColor.values()) {
            assertThat(String.format("%c%c", AColor.COLOR_CHAR_BUKKIT, color.code), is(color.toBukkit()));
        }
    }

    @Test
    public void testTranslate() {
        String s = "&0&1&2&3&4&5&6&7&8&9&A&a&B&b&C&c&D&d&E&e&F&f&K&k & more";
        String t = AColor.translateAlternateColorCodes('&', s);
        String u = AColor.BLACK.toString() + AColor.DARK_BLUE + AColor.DARK_GREEN + AColor.DARK_AQUA
                + AColor.DARK_RED + AColor.DARK_PURPLE + AColor.GOLD + AColor.GRAY + AColor.DARK_GRAY
                + AColor.BLUE + AColor.GREEN + AColor.GREEN + AColor.AQUA + AColor.AQUA + AColor.RED
                + AColor.RED + AColor.LIGHT_PURPLE + AColor.LIGHT_PURPLE + AColor.YELLOW + AColor.YELLOW
                + AColor.WHITE + AColor.WHITE + AColor.MAGIC + AColor.MAGIC + " & more";
        assertThat(t, is(u));
    }

    @Test
    public void testBukkitTranslate() {
        String s = "&0&1&2&3&4&5&6&7&8&9&A&a&B&b&C&c&D&d&E&e&F&f&K&k & more";
        String t = AColor.toBukkit(AColor.translateAlternateColorCodes('&', s));
        String u = AColor.BLACK.toBukkit() + AColor.DARK_BLUE.toBukkit() + AColor.DARK_GREEN.toBukkit()
                + AColor.DARK_AQUA.toBukkit() + AColor.DARK_RED.toBukkit() + AColor.DARK_PURPLE.toBukkit()
                + AColor.GOLD.toBukkit() + AColor.GRAY.toBukkit() + AColor.DARK_GRAY.toBukkit()
                + AColor.BLUE.toBukkit() + AColor.GREEN.toBukkit() + AColor.GREEN.toBukkit()
                + AColor.AQUA.toBukkit() + AColor.AQUA.toBukkit() + AColor.RED.toBukkit() + AColor.RED.toBukkit()
                + AColor.LIGHT_PURPLE.toBukkit() + AColor.LIGHT_PURPLE.toBukkit() + AColor.YELLOW.toBukkit()
                + AColor.YELLOW.toBukkit() + AColor.WHITE.toBukkit() + AColor.WHITE.toBukkit()
                + AColor.MAGIC.toBukkit() + AColor.MAGIC.toBukkit() + " & more";
        assertThat(t, is(u));
    }
}
