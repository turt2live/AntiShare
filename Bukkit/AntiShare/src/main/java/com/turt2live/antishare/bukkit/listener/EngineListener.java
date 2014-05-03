package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.APlayer;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.AlertManager;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.bukkit.impl.BukkitPlayer;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.events.general.AlertEvent;
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

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockPlace(BlockPlaceEvent event) {
        ABlock block = new BukkitBlock(event.getBlock());
        APlayer player = new BukkitPlayer(event.getPlayer());
        ASGameMode gamemode = player.getGameMode();

        if (engine.getEngine(block.getWorld().getName()).processBlockPlace(player, block, gamemode)) {
            event.setCancelled(true);
            String blockType = event.getBlock().getType().name().toLowerCase(); // TODO: PLAYER-FRIENDLY
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_PLACE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
            AlertManager.triggerAlert(AlertEvent.AlertType.ADMIN_BLOCK_PLACE, player, block);
        }
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
