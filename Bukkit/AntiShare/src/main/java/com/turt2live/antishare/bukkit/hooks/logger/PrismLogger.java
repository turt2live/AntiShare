package com.turt2live.antishare.bukkit.hooks.logger;

import com.turt2live.antishare.bukkit.hooks.BlockLogger;
import me.botsko.prism.Prism;
import org.bukkit.plugin.Plugin;

/**
 * Prism block logger hook
 *
 * @author turt2live
 */
public class PrismLogger implements BlockLogger {

    private Prism prism;

    public PrismLogger(Plugin prism) {
        if (prism instanceof Prism) {
            this.prism = (Prism) prism;
        } else throw new IllegalArgumentException("plugin not an instance of Prism");
    }
}
