package com.turt2live.antishare.bukkit.lang;

import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Language file format converter
 *
 * @author turt2live
 */
public class LangBuilder {

    /**
     * The player selector
     */
    public static final String SELECTOR_PLAYER = "@p";
    /**
     * The variable selector
     */
    public static final String SELECTOR_VARIABLE = "@v";
    /**
     * The gamemode selector
     */
    public static final String SELECTOR_GAMEMODE = "@g";

    private boolean prefix = false;
    private String format;
    private Map<String, String> replacements = new HashMap<String, String>();

    /**
     * Creates a new LangBuilder
     *
     * @param format the format to use, cannot be null
     */
    public LangBuilder(String format) {
        if (format == null) throw new IllegalArgumentException("format cannot be null");

        this.format = format;
    }

    /**
     * Sets a specified replacement
     *
     * @param replace     the value, cannot be null
     * @param replaceWith the replacement, cannot be null
     * @return this, for chaining
     */
    public LangBuilder setReplacement(String replace, String replaceWith) {
        if (replace == null) throw new IllegalArgumentException("value cannot be null");
        if (replaceWith == null) throw new IllegalArgumentException("replacement cannot be null");

        replacements.put(replace, replaceWith);
        return this;
    }

    /**
     * Sets the "with prefix" flag as true for this lang builder
     *
     * @return this, for chaining
     */
    public LangBuilder withPrefix() {
        prefix = true;
        return this;
    }

    /**
     * Completes the string by formatting it and returning it after
     * completing replacements
     *
     * @return the final string
     */
    public String build() {
        String working = format;

        for (Map.Entry<String, String> replacement : replacements.entrySet()) {
            working = working.replaceAll(Pattern.quote(replacement.getKey()), replacement.getValue());
        }

        return ChatColor.translateAlternateColorCodes('&', (prefix ? Lang.getInstance().getFormat(Lang.PREFIX) + " " : "") + working);
    }

    /**
     * Converts color codes in a list
     *
     * @param list the list to convert, null values yield null results
     * @return the colorized list
     */
    public static List<String> colorize(List<String> list) {
        if (list == null) return null;

        List<String> colored = new ArrayList<String>();

        for (String s : list) {
            colored.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        return colored;
    }

}
