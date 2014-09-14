/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare;

/**
 * AntiShare Color Class
 */
public enum AColor {
    /**
     * Represents black
     */
    BLACK('0'),
    /**
     * Represents dark blue
     */
    DARK_BLUE('1'),
    /**
     * Represents dark green
     */
    DARK_GREEN('2'),
    /**
     * Represents dark blue (aqua)
     */
    DARK_AQUA('3'),
    /**
     * Represents dark red
     */
    DARK_RED('4'),
    /**
     * Represents dark purple
     */
    DARK_PURPLE('5'),
    /**
     * Represents gold
     */
    GOLD('6'),
    /**
     * Represents gray
     */
    GRAY('7'),
    /**
     * Represents dark gray
     */
    DARK_GRAY('8'),
    /**
     * Represents blue
     */
    BLUE('9'),
    /**
     * Represents green
     */
    GREEN('a'),
    /**
     * Represents aqua
     */
    AQUA('b'),
    /**
     * Represents red
     */
    RED('c'),
    /**
     * Represents light purple
     */
    LIGHT_PURPLE('d'),
    /**
     * Represents yellow
     */
    YELLOW('e'),
    /**
     * Represents white
     */
    WHITE('f'),
    /**
     * Represents magical characters that change around randomly
     */
    MAGIC('k'),
    /**
     * Makes the text bold.
     */
    BOLD('l'),
    /**
     * Makes a line appear through the text.
     */
    STRIKETHROUGH('m'),
    /**
     * Makes the text appear underlined.
     */
    UNDERLINE('n'),
    /**
     * Makes the text italic.
     */
    ITALIC('o'),
    /**
     * Resets all previous chat colors or formats.
     */
    RESET('r');

    public static final char COLOR_CHAR = '\u00B7';
    public static final char COLOR_CHAR_BUKKIT = '\u00A7';

    final char code;
    private final String toString, toStringBukkit;

    private AColor(char code) {
        this.code = code;
        this.toString = new String(new char[] {COLOR_CHAR, code});
        this.toStringBukkit = new String(new char[] {COLOR_CHAR_BUKKIT, code});
    }

    /**
     * Converts this color code to Bukkit format
     *
     * @return the Bukkit format
     */
    public String toBukkit() {
        return toStringBukkit;
    }

    @Override
    public String toString() {
        return toString;
    }

    /**
     * Translates a string using an alternate color code character into a
     * string that uses the internal {@link #COLOR_CHAR} color code
     * character. The alternate color code character will only be replaced if
     * it is immediately followed by 0-9, A-F, a-f, K-O, k-o, R or r.
     *
     * @param altColorChar    The alternate color code character to replace. Ex: &
     * @param textToTranslate Text containing the alternate color code character.
     *
     * @return Text containing the {@link #COLOR_CHAR} color code character.
     */
    public static String translateAlternateColorCodes(char altColorChar, String textToTranslate) {
        if (textToTranslate == null) return null;
        char[] b = textToTranslate.toCharArray();
        for (int i = 0; i < b.length - 1; i++) {
            if (b[i] == altColorChar && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(b[i + 1]) > -1) {
                b[i] = AColor.COLOR_CHAR;
                b[i + 1] = Character.toLowerCase(b[i + 1]);
            }
        }
        return new String(b);
    }

    /**
     * Converts colors in a string to Bukkit format
     *
     * @param input the input string
     *
     * @return the Bukkit-colored string, or null on null input
     */
    public static String toBukkit(String input) {
        if (input == null) return null;
        char[] s = input.toCharArray();
        for (int i = 0; i < s.length - 1; i++) {
            if (s[i] == COLOR_CHAR && "0123456789AaBbCcDdEeFfKkLlMmNnOoRr".indexOf(s[i + 1]) > -1) {
                s[i] = AColor.COLOR_CHAR_BUKKIT;
                s[i + 1] = Character.toLowerCase(s[i + 1]);
            }
        }
        return new String(s);
    }
}
