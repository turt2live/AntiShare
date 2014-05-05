package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.*;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.abstraction.AntiShareInventoryTransferEvent;
import com.turt2live.antishare.bukkit.events.AntiShareBlockBreakEvent;
import com.turt2live.antishare.bukkit.events.AntiShareExplodeEvent;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.bukkit.impl.BukkitPlayer;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.utils.OutputParameter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.FallingBlock;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.metadata.FixedMetadataValue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AntiShare Bukkit Listener for the AntiShare Engine. This listener will only
 * listen to events that the engine would be interested in.
 *
 * @author turt2live
 */
public class EngineListener implements Listener {

    /**
     * Cause the BlockFace enum is weird
     */
    private static final BlockFace[] TRUE_FACES = new BlockFace[]{BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

    private AntiShare plugin = AntiShare.getInstance();
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
            String blockType = plugin.getMaterialProvider().getPlayerFriendlyName(event.getBlock());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_PLACE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
            alert(Lang.NAUGHTY_ADMIN_PLACE, event.getPlayer(), event.getBlock());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        if (event instanceof AntiShareBlockBreakEvent) return; // Don't handle ourselves

        ABlock block = new BukkitBlock(event.getBlock());
        APlayer player = new BukkitPlayer(event.getPlayer());
        ASGameMode gameMode = player.getGameMode();

        OutputParameter<List<ABlock>> additionalBreak = new OutputParameter<List<ABlock>>();
        OutputParameter<BlockType> breakAs = new OutputParameter<BlockType>();
        if (engine.getEngine(block.getWorld().getName()).processBlockBreak(player, block, gameMode, additionalBreak, breakAs)) {
            event.setCancelled(true);
            String blockType = plugin.getMaterialProvider().getPlayerFriendlyName(event.getBlock());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_BREAK)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
            alert(Lang.NAUGHTY_ADMIN_BREAK, event.getPlayer(), event.getBlock());
        } else if (additionalBreak.hasValue()) {
            if (breakAs.hasValue() && breakAs.getValue() == BlockType.CREATIVE) {
                event.getBlock().getDrops().clear(); // Yea, fuck you.
                event.setExpToDrop(0);
            }
            for (ABlock addBlock : additionalBreak.getValue()) {
                if (addBlock instanceof BukkitBlock) {
                    BukkitBlock bukkitBlock = (BukkitBlock) addBlock;
                    Block block1 = bukkitBlock.getBlock();

                    AntiShareBlockBreakEvent breakEvent = new AntiShareBlockBreakEvent(block1, event.getPlayer());
                    plugin.getServer().getPluginManager().callEvent(breakEvent);
                    if (!breakEvent.isCancelled()) {
                        engine.getEngine(event.getBlock().getWorld().getName()).processFade(bukkitBlock);
                    }
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onExplosion(EntityExplodeEvent event) {
        if (event instanceof AntiShareExplodeEvent) return; // Don't handle ourselves

        Map<ABlock, Boolean> keep = new HashMap<ABlock, Boolean>();
        for (Block block : event.blockList()) {
            keep.put(new BukkitBlock(block), true);
        }

        engine.getEngine(event.getEntity().getWorld().getName()).processExplosion(keep);

        List<Block> refire = new ArrayList<Block>();
        for (Map.Entry<ABlock, Boolean> entry : keep.entrySet()) {
            if (!entry.getValue()) {
                Block block = ((BukkitBlock) entry.getKey()).getBlock();
                refire.add(block);
                event.blockList().remove(block);
            }
        }

        if (refire.size() > 0) {
            AntiShareExplodeEvent explodeEvent = new AntiShareExplodeEvent(event.getEntity(), event.getLocation(), refire, event.getYield());
            plugin.getServer().getPluginManager().callEvent(explodeEvent);
            if (!explodeEvent.isCancelled()) {
                for (Block block : explodeEvent.blockList()) {
                    engine.getEngine(event.getEntity().getWorld().getName()).processFade(new BukkitBlock(block));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFallingBlock(EntityChangeBlockEvent event) {
        Entity eventEntity = event.getEntity();
        WorldEngine engine = this.engine.getEngine(eventEntity.getWorld().getName());
        if (eventEntity.getType() == EntityType.FALLING_BLOCK) {
            FallingBlock entity = (FallingBlock) eventEntity;
            if (event.getTo() == Material.AIR) {
                // Spawning
                OutputParameter<BlockType> current = new OutputParameter<BlockType>(BlockType.UNKNOWN);
                if (!engine.processFallingBlockSpawn(new BukkitBlock(event.getBlock()), current)) {
                    entity.setDropItem(false);
                }
                entity.setMetadata("ANTISHARE_SAND", new FixedMetadataValue(plugin, current.getValue()));
            } else {
                // Landing
                BlockType previous = BlockType.UNKNOWN;
                if (entity.hasMetadata("ANTISHARE_SAND")) {
                    try {
                        previous = (BlockType) entity.getMetadata("ANTISHARE_SAND").get(0).value();
                    } catch (Exception e) { // I'm lazy
                    }
                }
                engine.processFallingBlockLand(new BukkitBlock(event.getBlock()), previous);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onSpawn(ItemSpawnEvent event) {
        Block bkBlock = event.getLocation().getBlock();
        if (bkBlock.getType() != Material.AIR) { // TODO: Add similarity check
            ABlock block = new BukkitBlock(event.getLocation().getBlock());
            if (!engine.getEngine(block.getWorld().getName()).processBlockPhysicsBreak(block)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockGrow(BlockGrowEvent event) {
        Block source = event.getBlock().getRelative(BlockFace.DOWN);
        Block child = event.getNewState().getBlock();
        BlockState childState = event.getNewState();

        if (source.getType() == Material.CACTUS || source.getType() == Material.SUGAR_CANE_BLOCK) {
            engine.getEngine(source.getWorld().getName()).processBlockGrow(new BukkitBlock(source), new BukkitBlock(child));
        } else if (childState.getType() == Material.PUMPKIN || childState.getType() == Material.MELON_BLOCK) {
            Material stemType = childState.getType() == Material.PUMPKIN ? Material.PUMPKIN_STEM : Material.MELON_STEM;

            // At this point we have a stem that spawned a block. Now where is the stem?
            // Well, because Minecraft is a mean little game, the stem can't be determined
            // because the 'bending' behaviour of the stem is 100% client-side. So basically
            // not even the bloody server knows what damn stem spawned the block because it
            // doesn't matter. Because of this, we have to find all applicable stems (making
            // sure to not include the melons with the pumpkins) and pass it off to the
            // engine which will figure the damn thing out.

            // First, what stems can we consider?
            List<ABlock> possibleStems = new ArrayList<ABlock>();
            for (BlockFace face : TRUE_FACES) {
                Block possibleStem = child.getRelative(face);
                if (possibleStem.getType() == stemType) {
                    possibleStems.add(new BukkitBlock(possibleStem));
                }
            }

            // Now to process said stems
            if (possibleStems.size() > 0) { // If there are no stems... wtf.
                engine.getEngine(child.getWorld().getName()).processBlockStems(new BukkitBlock(child), possibleStems);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        ABlock source = new BukkitBlock(event.getLocation().getBlock());
        List<ABlock> structure = new ArrayList<ABlock>();

        for (BlockState block : event.getBlocks()) {
            structure.add(new BukkitBlock(block.getBlock()));
        }

        engine.getEngine(source.getWorld().getName()).processStructure(source, structure);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        ABlock source = new BukkitBlock(event.getSource());
        ABlock child = new BukkitBlock(event.getBlock());
        engine.getEngine(source.getWorld().getName()).processBlockGrow(source, child);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent event) {
        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        engine.createWorldEngine(event.getWorld().getName()); // Force-creates the world engine
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        engine.unloadWorldEngine(event.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveCheck(AntiShareInventoryTransferEvent event) {
        ABlock block1 = new BukkitBlock(event.getBlock1());
        ABlock block2 = new BukkitBlock(event.getBlock2());

        if (!engine.getEngine(block1.getWorld().getName()).processBlockInteraction(block1, block2)) {
            event.setCancelled(true);
        }
    }

    private void alert(String langNode, Player player, Block block) {
        if (langNode == null || player == null || block == null) return;
        alert(langNode, player.getName(), plugin.getMaterialProvider().getPlayerFriendlyName(block));
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
