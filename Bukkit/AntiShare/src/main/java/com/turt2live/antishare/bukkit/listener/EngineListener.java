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

package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.abstraction.AntiShareInventoryTransferEvent;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.bukkit.events.AntiShareBlockBreakEvent;
import com.turt2live.antishare.bukkit.events.AntiShareExplodeEvent;
import com.turt2live.antishare.bukkit.events.AntiShareFadeEvent;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.bukkit.impl.BukkitPlayer;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.object.ABlock;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.attribute.ASGameMode;
import com.turt2live.antishare.object.attribute.BlockType;
import com.turt2live.antishare.object.attribute.Facing;
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
import org.bukkit.event.Event;
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

import java.lang.reflect.Method;
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
    public static final BlockFace[] TRUE_FACES = new BlockFace[] {BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST};

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
        printDebugEvent(event);

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
        printDebugEvent(event);

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
        printDebugEvent(event);

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
        printDebugEvent(event);

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
        printDebugEvent(event);

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
        printDebugEvent(event);

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
        printDebugEvent(event);

        ABlock source = new BukkitBlock(event.getLocation().getBlock());
        List<ABlock> structure = new ArrayList<ABlock>();

        for (BlockState block : event.getBlocks()) {
            structure.add(new BukkitBlock(block.getBlock()));
        }

        engine.getEngine(source.getWorld().getName()).processStructure(source, structure);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockSpread(BlockSpreadEvent event) {
        printDebugEvent(event);

        ABlock source = new BukkitBlock(event.getSource());
        ABlock child = new BukkitBlock(event.getBlock());
        engine.getEngine(source.getWorld().getName()).processBlockGrow(source, child);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBurn(BlockBurnEvent event) {
        printDebugEvent(event);

        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onFade(BlockFadeEvent event) {
        printDebugEvent(event);

        if (event instanceof AntiShareFadeEvent) return; // Don't handle ourselves

        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onDecay(LeavesDecayEvent event) {
        printDebugEvent(event);

        engine.getEngine(event.getBlock().getWorld().getName()).processFade(new BukkitBlock(event.getBlock()));
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        printDebugEvent(event);

        engine.createWorldEngine(event.getWorld().getName()); // Force-creates the world engine
    }

    @EventHandler
    public void onWorldUnload(WorldUnloadEvent event) {
        printDebugEvent(event);

        engine.unloadWorldEngine(event.getWorld().getName());
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInventoryMoveCheck(AntiShareInventoryTransferEvent event) {
        printDebugEvent(event);

        ABlock block1 = new BukkitBlock(event.getBlock1());
        ABlock block2 = new BukkitBlock(event.getBlock2());

        if (!engine.getEngine(block1.getWorld().getName()).processBlockInteraction(block1, block2)) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonExtend(BlockPistonExtendEvent event) {
        printDebugEvent(event);

        ABlock piston = new BukkitBlock(event.getBlock());
        Facing direction = BukkitUtils.getFacing(event.getDirection());
        List<ABlock> blocks = new ArrayList<ABlock>();

        // TODO: 1.8 slime blocks (may need extra handling)

        for (Block block : event.getBlocks()) {
            blocks.add(new BukkitBlock(block));
        }

        if (!engine.getEngine(piston.getWorld().getName()).processPistonMove(piston, blocks, direction, false, event.isSticky())) {
            event.setCancelled(true);
        } else {
            // Event permitted, check the +1
            Block plusOne = event.getBlock().getRelative(event.getDirection(), event.getLength() + 1);

            // Process as 'vanish' or 'drop item'
            if (VersionSelector.getMinecraft().getPistonVanish().contains(plusOne.getType())) {
                engine.getEngine(piston.getWorld().getName()).processFade(new BukkitBlock(plusOne));

                plusOne.setType(Material.AIR);
                plugin.getServer().getPluginManager().callEvent(new AntiShareFadeEvent(plusOne, plusOne.getState()));
            } else if (VersionSelector.getMinecraft().getPistonBreak().contains(plusOne.getType())) {
                if (!engine.getEngine(piston.getWorld().getName()).processBlockPhysicsBreak(new BukkitBlock(plusOne))) {
                    plusOne.setType(Material.AIR);
                    plugin.getServer().getPluginManager().callEvent(new AntiShareFadeEvent(plusOne, plusOne.getState()));
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPistonRetract(BlockPistonRetractEvent event) {
        printDebugEvent(event);

        ABlock piston = new BukkitBlock(event.getBlock());
        Facing direction = BukkitUtils.getFacing(event.getDirection()).opposite(); // Need to flip for correct processing
        List<ABlock> blocks = new ArrayList<ABlock>();

        // TODO: 1.8 slime blocks (may need extra handling)

        // Find the affected block
        Block moving = event.getRetractLocation().getBlock();
        if (moving.getType() != Material.AIR) blocks.add(new BukkitBlock(moving));

        if (!engine.getEngine(piston.getWorld().getName()).processPistonMove(piston, blocks, direction, true, event.isSticky())) {
            event.setCancelled(true);
        }
    }

    /**
     * Sends an alert
     *
     * @param langNode the alert node in the lang file
     * @param player   the player
     * @param block    the block
     */
    private void alert(String langNode, Player player, Block block) {
        if (langNode == null || player == null || block == null) return;
        alert(langNode, player.getName(), plugin.getMaterialProvider().getPlayerFriendlyName(block));
    }

    /**
     * Sends an alert
     *
     * @param langNode   the alert node in the lang file
     * @param playerName the player name
     * @param variable   a variable to replace ({@link com.turt2live.antishare.bukkit.lang.LangBuilder#SELECTOR_VARIABLE})
     */
    private void alert(String langNode, String playerName, String variable) {
        String compiled = new LangBuilder(Lang.getInstance().getFormat(langNode)).withPrefix()
                .setReplacement(LangBuilder.SELECTOR_PLAYER, playerName)
                .setReplacement(LangBuilder.SELECTOR_VARIABLE, variable)
                .build();

        if (ChatColor.stripColor(compiled).equalsIgnoreCase("disabled")) return;

        Bukkit.broadcast(compiled, APermission.GET_ALERTS);
    }

    /**
     * Prints debug information for an event to the DevEngine
     *
     * @param event the event to write
     */
    private void printDebugEvent(Event event) {
        if (event != null && DevEngine.isEnabled()) { // No event or no DevEngine - Don't bother
            DevEngine.log("[Bukkit Event] " + event.getClass().getName() + " :: EventName:" + event.getEventName());
            Method[] methods = event.getClass().getMethods();
            for (Method method : methods) {
                if (method.getParameterTypes().length == 0) {
                    try {
                        DevEngine.log("[Bukkit Event]\t\t" + method.getName() + "() = " + method.invoke(event));
                    } catch (Exception e) {
                        // Consume
                    }
                }
            }
        }
    }

}
