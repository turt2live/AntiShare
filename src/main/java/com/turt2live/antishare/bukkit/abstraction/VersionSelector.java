package com.turt2live.antishare.bukkit.abstraction;

/**
 * Used for abstracting the Minecraft version
 *
 * @author turt2live
 */
public final class VersionSelector {

    private static MinecraftVersion minecraft;

    /**
     * Gets the minecraft version abstraction layer
     *
     * @return the minecraft version abstraction layer
     */
    public static MinecraftVersion getMinecraft() {
        if (minecraft == null) initMinecraft();
        return minecraft;
    }

    private static void initMinecraft() {
        // TODO: Init minecraft layer
    }

}
