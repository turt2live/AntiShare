package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.utils.ASLocation;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.BlockType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

/**
 * AntiShare Bukkit Listener for the AntiShare Engine. This listener will only
 * listen to events that the engine would be interested in.
 *
 * @author turt2live
 */
public class EngineListener implements Listener {

    private Engine engine;

    /**
     * Creates a new Bukkit engine listener
     */
    public EngineListener() {
        engine = Engine.getInstance();
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ASLocation location = BukkitUtils.toLocation(event.getBlock().getLocation());
        BlockType type = ASUtils.toBlockType(BukkitUtils.toGameMode(event.getPlayer().getGameMode()));

        engine.getEngine(event.getPlayer().getWorld().getName()).processBlockPlace(location, type);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        engine.createWorldEngine(event.getWorld().getName()); // Force-creates the world engine
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        engine.unloadWorldEngine(event.getWorld().getName());
    }

}
