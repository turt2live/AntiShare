package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.*;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.bukkit.impl.BukkitPlayer;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

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
            String blockType = AntiShare.getInstance().getMaterialProvider().getPlayerFriendlyName(event.getBlock());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_PLACE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
            alert(Lang.NAUGHTY_ADMIN_PLACE, event.getPlayer(), event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFallingBlock(EntityChangeBlockEvent event) {
        Entity eventEntity = event.getEntity();
        WorldEngine engine = this.engine.getEngine(eventEntity.getWorld().getName());
        ASLocation location = BukkitUtils.toLocation(event.getBlock().getLocation());
        if (eventEntity.getType() == EntityType.FALLING_BLOCK) {
            FallingBlock entity = (FallingBlock) eventEntity;
            if (event.getTo() == Material.AIR) {
                // Spawning
                BlockType current = engine.getBlockManager().getBlockType(location);
                if (Engine.getInstance().isPhysicsBreakAsGamemode() && current == BlockType.CREATIVE) {
                    entity.setDropItem(false); // For if it hits something
                }
                entity.setMetadata("ANTISHARE_SAND", new FixedMetadataValue(AntiShare.getInstance(), current));
            } else {
                // Landing
                if (entity.hasMetadata("ANTISHARE_SAND")) {
                    try {
                        BlockType previous = (BlockType) entity.getMetadata("ANTISHARE_SAND").get(0).value();
                        engine.getBlockManager().setBlockType(location, previous);
                    } catch (Exception e) { // I'm lazy
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(ItemSpawnEvent event) {
        // TODO: Better detection...
        ABlock block = new BukkitBlock(event.getLocation().getBlock());
        if (!engine.getEngine(block.getWorld().getName()).processBlockPhysicsBreak(block)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        Block source = event.getBlock().getRelative(BlockFace.DOWN);
        Block child = event.getNewState().getBlock();

        // TODO: Handle crops (pumpkins)

        if (source.getType() == Material.CACTUS || source.getType() == Material.SUGAR_CANE_BLOCK) {
            engine.getEngine(source.getWorld().getName()).processBlockGrow(new BukkitBlock(source), new BukkitBlock(child));
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        ABlock source = new BukkitBlock(event.getSource());
        ABlock child = new BukkitBlock(event.getBlock());
        engine.getEngine(source.getWorld().getName()).processBlockGrow(source, child);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        ABlock block = new BukkitBlock(event.getBlock());
        engine.getEngine(block.getWorld().getName()).getBlockManager().setBlockType(block.getLocation(), BlockType.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        ABlock block = new BukkitBlock(event.getBlock());
        engine.getEngine(block.getWorld().getName()).getBlockManager().setBlockType(block.getLocation(), BlockType.UNKNOWN);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent event) {
        ABlock block = new BukkitBlock(event.getBlock());
        engine.getEngine(block.getWorld().getName()).getBlockManager().setBlockType(block.getLocation(), BlockType.UNKNOWN);
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        engine.createWorldEngine(event.getWorld().getName()); // Force-creates the world engine
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        engine.unloadWorldEngine(event.getWorld().getName());
    }

    private void alert(String langNode, Player player, Block block) {
        if (langNode == null || player == null || block == null) return;
        alert(langNode, player.getName(), AntiShare.getInstance().getMaterialProvider().getPlayerFriendlyName(block));
    }

    private void alert(String langNode, String playerName, String variable) {
        String compiled = new LangBuilder(Lang.getInstance().getFormat(langNode)).withPrefix()
                .setReplacement(LangBuilder.SELECTOR_PLAYER, playerName)
                .setReplacement(LangBuilder.SELECTOR_VARIABLE, variable)
                .build();

        if (ChatColor.stripColor(compiled).equalsIgnoreCase("disabled")) return;

        Bukkit.broadcast(compiled, PermissionNodes.GET_ALERTS);
    }

}
