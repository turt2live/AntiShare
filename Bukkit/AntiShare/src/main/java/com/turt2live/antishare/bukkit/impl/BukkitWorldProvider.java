package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.engine.WorldProvider;
import com.turt2live.antishare.object.AWorld;
import org.bukkit.World;

/**
 * Represents a Bukkit world provider
 *
 * @author turt2live
 */
public class BukkitWorldProvider implements WorldProvider {

    @Override
    public AWorld getWorld(String name) {
        if (name == null) throw new IllegalArgumentException();

        AntiShare plugin = AntiShare.getInstance();
        World world = plugin.getServer().getWorld(name);
        if (world == null) return null;

        return new BukkitWorld(world);
    }
}
