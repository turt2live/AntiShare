package com.turt2live.antishare.bukkit.dev;

public abstract class CheckBase {

    protected AntiShare plugin;

    public CheckBase(AntiShare plugin) {
        this.plugin = plugin;
    }

    public abstract void begin();

}
