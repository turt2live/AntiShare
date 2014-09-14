/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.bukkit.abstraction.event.AntiShareEatEvent;
import com.turt2live.antishare.bukkit.abstraction.event.AntiShareInventoryTransferEvent;
import com.turt2live.antishare.bukkit.events.AntiShareBlockBreakEvent;
import com.turt2live.antishare.bukkit.events.AntiShareExplodeEvent;
import com.turt2live.antishare.bukkit.events.AntiShareFadeEvent;
import com.turt2live.antishare.bukkit.impl.*;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.engine.WorldEngine;
import com.turt2live.antishare.object.*;
import com.turt2live.antishare.object.attribute.Facing;
import com.turt2live.antishare.object.attribute.ObjectType;
import com.turt2live.antishare.object.pattern.MobPattern;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.OutputParameter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.hanging.HangingBreakByEntityEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.hanging.HangingPlaceEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
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
    public void onBlockPlace(final BlockPlaceEvent event) {
        printDebugEvent(event);

        final ABlock block = new BukkitBlock(event.getBlock());
        APlayer player = new BukkitPlayer(event.getPlayer());
        ASGameMode gamemode = player.getGameMode();
        final OutputParameter<MobPattern> matchedPattern = new OutputParameter<>();

        if (engine.getEngine(block.getWorld().getName()).processBlockPlace(player, block, gamemode, matchedPattern)) {
            event.setCancelled(true);

            if (matchedPattern.hasValue()) {
                String pattern = ASUtils.toUpperWords(matchedPattern.getValue().getEntityType().name().replace('_', ' '));
                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_MOB_CREATE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, pattern).build());
                alert(Lang.NAUGHTY_ADMIN_MOB_CREATE, event.getPlayer().getName(), pattern);

                // Due to a Bukkit bug, we need to send block updates 2 ticks later as well
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
                    @Override
                    public void run() {
                        List<ABlock> blocks = matchedPattern.getValue().getInvolvedBlocks(block);
                        for (ABlock block : blocks) {
                            if (block instanceof BukkitBlock) {
                                Block bk = ((BukkitBlock) block).getBlock();
                                World world = bk.getWorld();
                                world.refreshChunk(bk.getChunk().getX(), bk.getChunk().getZ());
                            }
                        }
                        World world = event.getBlock().getWorld();
                        world.refreshChunk(event.getBlock().getChunk().getX(), event.getBlock().getChunk().getZ());
                    }
                }, 2);
            } else {
                String blockType = plugin.getMaterialProvider().getPlayerFriendlyName(event.getBlock());
                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_PLACE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
                alert(Lang.NAUGHTY_ADMIN_PLACE, event.getPlayer().getName(), blockType);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        printDebugEvent(event);

        if (event instanceof AntiShareBlockBreakEvent) return; // Don't handle ourselves

        ABlock block = new BukkitBlock(event.getBlock());
        APlayer player = new BukkitPlayer(event.getPlayer());
        ASGameMode gameMode = player.getGameMode();

        OutputParameter<List<ABlock>> additionalBreak = new OutputParameter<>();
        OutputParameter<ObjectType> breakAs = new OutputParameter<>();
        if (engine.getEngine(block.getWorld().getName()).processBlockBreak(player, block, gameMode, additionalBreak, breakAs)) {
            event.setCancelled(true);

            String blockType = plugin.getMaterialProvider().getPlayerFriendlyName(event.getBlock());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_BREAK)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, blockType).build());
            alert(Lang.NAUGHTY_ADMIN_BREAK, event.getPlayer().getName(), blockType);
        } else if (additionalBreak.hasValue()) {
            if (breakAs.hasValue() && breakAs.getValue() == ObjectType.CREATIVE) {
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

        Map<ABlock, Boolean> keep = new HashMap<>();
        for (Block block : event.blockList()) {
            keep.put(new BukkitBlock(block), true);
        }

        engine.getEngine(event.getEntity().getWorld().getName()).processExplosion(keep);

        List<Block> refire = new ArrayList<>();
        for (Map.Entry<ABlock, Boolean> entry : keep.entrySet()) {
            if (!entry.getValue()) {
                Block block = ((BukkitBlock) entry.getKey()).getBlock();
                refire.add(block);
                event.blockList().remove(block);
            }
        }

        if (!refire.isEmpty()) {
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
                OutputParameter<ObjectType> current = new OutputParameter<>(ObjectType.UNKNOWN);
                if (!engine.processFallingBlockSpawn(new BukkitBlock(event.getBlock()), current)) {
                    entity.setDropItem(false);
                }
                entity.setMetadata("ANTISHARE_SAND", new FixedMetadataValue(plugin, current.getValue()));
            } else {
                // Landing
                ObjectType previous = ObjectType.UNKNOWN;
                if (entity.hasMetadata("ANTISHARE_SAND")) {
                    try {
                        previous = (ObjectType) entity.getMetadata("ANTISHARE_SAND").get(0).value();
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
            List<ABlock> possibleStems = new ArrayList<>();
            for (BlockFace face : TRUE_FACES) {
                Block possibleStem = child.getRelative(face);
                if (possibleStem.getType() == stemType) {
                    possibleStems.add(new BukkitBlock(possibleStem));
                }
            }

            // Now to process said stems
            if (!possibleStems.isEmpty()) { // If there are no stems... wtf.
                engine.getEngine(child.getWorld().getName()).processBlockStems(new BukkitBlock(child), possibleStems);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onStructureGrow(StructureGrowEvent event) {
        printDebugEvent(event);

        ABlock source = new BukkitBlock(event.getLocation().getBlock());
        List<ABlock> structure = new ArrayList<>();

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
        List<ABlock> blocks = new ArrayList<>();

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
        List<ABlock> blocks = new ArrayList<>();

        // TODO: 1.8 slime blocks (may need extra handling)

        // Find the affected block
        Block moving = event.getRetractLocation().getBlock();
        if (moving.getType() != Material.AIR) blocks.add(new BukkitBlock(moving));

        if (!engine.getEngine(piston.getWorld().getName()).processPistonMove(piston, blocks, direction, true, event.isSticky())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        RejectableCommand command = new RejectableCommand(event.getMessage());

        if (engine.getEngine(event.getPlayer().getWorld().getName()).processCommandExecution(player, command)) {
            event.setCancelled(true);

            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_COMMAND)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, command.getCommandString()).build());
            alert(Lang.NAUGHTY_ADMIN_COMMAND, event.getPlayer().getName(), command.getCommandString());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onInteract(PlayerInteractEvent event) {
        printDebugEvent(event);

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        APlayer player = new BukkitPlayer(event.getPlayer());
        ABlock interaction = new BukkitBlock(event.getClickedBlock());

        if (engine.getEngine(interaction.getWorld().getName()).processInteraction(player, interaction)) {
            event.setCancelled(true);

            String name = plugin.getMaterialProvider().getPlayerFriendlyName(event.getClickedBlock());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_INTERACTION)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
            alert(Lang.NAUGHTY_ADMIN_INTERACTION, player.getName(), name);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onContainerOpen(PlayerInteractEvent event) {
        printDebugEvent(event);

        if (event.getAction() != Action.RIGHT_CLICK_BLOCK)
            return;

        APlayer player = new BukkitPlayer(event.getPlayer());
        ABlock block = new BukkitBlock(event.getClickedBlock());

        if (block.isContainer()) {
            engine.getEngine(block.getWorld().getName()).processContainerOpen(player, block);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerUse(PlayerInteractEvent event) {
        printDebugEvent(event);

        if (event.getItem() == null) return;

        if (processGenericUse(event.getPlayer(), event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onExpBottleUse(ExpBottleEvent event) {
        printDebugEvent(event);

        Player shooter = VersionSelector.getMinecraft().getPlayerAttacker(event.getEntity());
        if (shooter != null) {
            if (processGenericUse(shooter, new ItemStack(Material.EXP_BOTTLE))) {
                event.setExperience(0);
                event.setShowEffect(false);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEggUse(PlayerEggThrowEvent event) {
        printDebugEvent(event);

        if (processGenericUse(event.getPlayer(), new ItemStack(Material.EGG))) {
            event.setHatching(false);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onProjectileUse(ProjectileLaunchEvent event) {
        printDebugEvent(event);

        Player shooter = VersionSelector.getMinecraft().getPlayerAttacker(event.getEntity());
        if (shooter != null) {
            Projectile projectile = event.getEntity();
            Material item = null;
            if (projectile instanceof EnderPearl) {
                item = Material.ENDER_PEARL;
            } else if (projectile instanceof EnderSignal) {
                item = Material.EYE_OF_ENDER;
            } else if (projectile instanceof Snowball) {
                item = Material.SNOW_BALL;
            } else if (projectile instanceof WitherSkull) {
                item = Material.SKULL_ITEM;
            } else if (projectile instanceof Arrow) {
                item = Material.ARROW;
            } else if (projectile instanceof Fireball) {
                item = Material.FIREBALL;
            } else if (projectile instanceof ThrownExpBottle) {
                item = Material.EXP_BOTTLE;
            } else if (projectile instanceof ThrownPotion) {
                item = Material.POTION;
            } else if (projectile instanceof Egg) {
                item = Material.EGG;
            }

            // Entities we ignore
            if (projectile instanceof Fish) {
                return;
            }

            if (item != null) {
                ItemStack stack = new ItemStack(item);

                if (processGenericUse(shooter, stack)) {
                    event.setCancelled(true);
                }
            } else
                throw new IllegalStateException("Unknown projectile, cannot be mapped! Entity Type: " + projectile.getType());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPotionSplashUse(PotionSplashEvent event) {
        printDebugEvent(event);

        Player shooter = VersionSelector.getMinecraft().getPlayerAttacker(event.getPotion());
        if (shooter != null) {
            ItemStack item = new ItemStack(Material.POTION);

            if (processGenericUse(shooter, item)) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
    public void onPossibleWaterUse(PlayerInteractEvent event) {
        printDebugEvent(event);

        if (event.isCancelled() && event.getAction() == org.bukkit.event.block.Action.RIGHT_CLICK_AIR
                && event.getClickedBlock() == null && event.getItem() != null) {
            // It is almost certainly water.
            Player player = event.getPlayer();
            List<Block> blocks = player.getLineOfSight(null, 10); // TODO: Better method?
            boolean water = false;

            for (Block block : blocks) {
                if (block.getType() == Material.WATER || block.getType() == Material.STATIONARY_WATER) {
                    water = true;
                    break;
                }
            }

            // It was a water use!
            if (water) {
                if (processGenericUse(player, event.getItem())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEatUse(AntiShareEatEvent event) {
        printDebugEvent(event);

        if (processGenericUse(event.getPlayer(), event.getItem())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        AItem item = new BukkitItem(event.getItemDrop().getItemStack());

        if (engine.getEngine(player.getWorld().getName()).processItemDrop(player, item)) {
            event.setCancelled(true);

            String name = plugin.getMaterialProvider().getPlayerFriendlyName(event.getItemDrop().getItemStack());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_DROP)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
            alert(Lang.NAUGHTY_ADMIN_DROP, player.getName(), name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onItemPickup(PlayerPickupItemEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        AItem item = new BukkitItem(event.getItem().getItemStack());

        if (engine.getEngine(player.getWorld().getName()).processItemPickup(player, item)) {
            event.setCancelled(true);

            Player pl = event.getPlayer();
            boolean alert = true;
            if (pl.hasMetadata("ANTISHARE-PICKUP-SPAM") && pl.hasMetadata("ANTISHARE-PICKUP-SPAM-TS")) {
                Material last = (Material) pl.getMetadata("ANTISHARE-PICKUP-SPAM").get(0).value();
                long timestamp = pl.getMetadata("ANTISHARE-PICKUP-SPAM-TS").get(0).asLong();

                if (last == event.getItem().getItemStack().getType() && System.currentTimeMillis() - timestamp < 1000)
                    alert = false;
            }

            if (alert) {
                pl.setMetadata("ANTISHARE-PICKUP-SPAM", new FixedMetadataValue(plugin, event.getItem().getItemStack().getType()));
                pl.setMetadata("ANTISHARE-PICKUP-SPAM-TS", new FixedMetadataValue(plugin, System.currentTimeMillis()));

                String name = plugin.getMaterialProvider().getPlayerFriendlyName(event.getItem().getItemStack());
                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_PICKUP)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
                alert(Lang.NAUGHTY_ADMIN_PICKUP, player.getName(), name);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDeath(EntityDeathEvent event) {
        printDebugEvent(event);

        AEntity entity = new BukkitEntity(event.getEntity());

        engine.getEngine(event.getEntity().getWorld().getName()).processEntityDeath(entity);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onHangingPlace(HangingPlaceEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        AEntity hanging = new BukkitEntity(event.getEntity());

        if (engine.getEngine(event.getPlayer().getWorld().getName()).processEntityPlace(hanging, player)) {
            event.setCancelled(true);

            String name = BukkitUtils.getPlayerFriendlyName(event.getEntity().getType());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_ENTITY_PLACE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
            alert(Lang.NAUGHTY_ADMIN_ENTITY_PLACE, player.getName(), name);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHangingBreakEntity(HangingBreakByEntityEvent event) {
        printDebugEvent(event);

        if (event.getRemover() instanceof Player) {
            APlayer player = new BukkitPlayer((Player) event.getRemover());
            AEntity entity = new BukkitEntity(event.getEntity());

            if (engine.getEngine(event.getEntity().getWorld().getName()).processEntityBreak(player, entity)) {
                event.setCancelled(true);

                String name = BukkitUtils.getPlayerFriendlyName(event.getEntity().getType());
                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_ENTITY_BREAK)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
                alert(Lang.NAUGHTY_ADMIN_ENTITY_BREAK, player.getName(), name);
            }
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onHangingBreak(HangingBreakEvent event) {
        printDebugEvent(event);

        AEntity entity = new BukkitEntity(event.getEntity());
        engine.getEngine(event.getEntity().getWorld().getName()).processEntityDeath(entity);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerAttackEntity(EntityDamageByEntityEvent event) {
        printDebugEvent(event);

        Player attacker = VersionSelector.getMinecraft().getPlayerAttacker(event.getDamager());

        if (attacker != null) {
            APlayer player = new BukkitPlayer(attacker);
            AEntity attacked = new BukkitEntity(event.getEntity());

            if (engine.getEngine(attacker.getWorld().getName()).processEntityAttack(player, attacked)) {
                event.setCancelled(true);

                String name = BukkitUtils.getPlayerFriendlyName(event.getEntity().getType());
                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_ENTITY_ATTACK)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
                alert(Lang.NAUGHTY_ADMIN_ENTITY_ATTACK, player.getName(), name);
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        AEntity entity = new BukkitEntity(event.getRightClicked());

        if (engine.getEngine(event.getPlayer().getWorld().getName()).processEntityInteract(player, entity)) {
            event.setCancelled(true);

            String name = BukkitUtils.getPlayerFriendlyName(event.getRightClicked().getType());
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_ENTITY_INTERACT)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
            alert(Lang.NAUGHTY_ADMIN_ENTITY_INTERACT, player.getName(), name);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDeath(PlayerDeathEvent event) {
        APlayer player = new BukkitPlayer(event.getEntity());
        List<AItem> items = new ArrayList<>();

        for (ItemStack item : event.getDrops()) {
            items.add(new BukkitItem(item));
        }

        engine.getEngine(event.getEntity().getWorld().getName()).processPlayerDeath(player, items);

        if (items.size() != event.getDrops().size()) {
            int stacks = event.getDrops().size() - items.size(); // stacks
            int actual = 0;

            for (ItemStack item : event.getDrops()) {
                AItem bk = new BukkitItem(item);
                if (!items.contains(bk)) actual += item.getAmount();
            }

            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_DEATH)).withPrefix()
                    .setReplacement(LangBuilder.SELECTOR_VARIABLE + "1", actual + "")
                    .setReplacement(LangBuilder.SELECTOR_VARIABLE + "2", stacks + "")
                    .build());
            alert(Lang.NAUGHTY_ADMIN_DEATH, player.getName(), actual + "", stacks + "");
        }

        event.getDrops().clear();
        for (AItem item : items) {
            if (!(item instanceof BukkitItem)) continue;
            event.getDrops().add(((BukkitItem) item).getStack());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        printDebugEvent(event);

        // Filter the event, we don't want to spam the engine
        Location from = event.getFrom();
        Location to = event.getTo();

        int x1 = from.getBlockX();
        int y1 = from.getBlockY();
        int z1 = from.getBlockZ();

        int x2 = to.getBlockX();
        int y2 = to.getBlockY();
        int z2 = to.getBlockZ();

        if (x1 != x2 || y1 != y2 || z1 != z2) {
            // Filtered to per-block

            ASLocation aFrom = BukkitUtils.toLocation(from);
            ASLocation aTo = BukkitUtils.toLocation(to);
            APlayer player = new BukkitPlayer(event.getPlayer());

            OutputParameter<Integer> approaching = new OutputParameter<>();
            OutputParameter<Boolean> crossed = new OutputParameter<>();

            engine.getEngine(event.getPlayer().getWorld().getName()).processPlayerMove(player, aFrom, aTo, approaching, crossed);

            // World split handling
            if (approaching.wasCalled() && crossed.wasCalled()) {
                Player bkPlayer = event.getPlayer();
                if (crossed.getValue()) {
                    bkPlayer.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.GENERAL_WORLDSPLIT_CROSSED)).withPrefix().build());
                } else if (approaching.getValue() > 0) {
                    int distance = approaching.getValue();

                    if ((distance <= 16 && distance % 4 == 0) || distance <= 5)
                        bkPlayer.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.GENERAL_WORLDSPLIT_APPROACH)).withPrefix()
                                .setReplacement(LangBuilder.SELECTOR_VARIABLE, distance + "").build());
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerGamemodeChange(PlayerGameModeChangeEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        ASGameMode from = VersionSelector.getMinecraft().toGameMode(event.getPlayer().getGameMode());
        ASGameMode to = VersionSelector.getMinecraft().toGameMode(event.getNewGameMode());

        int seconds = engine.getEngine(event.getPlayer().getWorld().getName()).processGameModeChange(player, from, to);
        if (seconds > -1) { // 0 still counts, -1 indicates "no cooldown"
            event.setCancelled(true);

            Player bkPlayer = event.getPlayer();
            bkPlayer.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.GENERAL_GAMEMODE_COOLDOWN)).withPrefix()
                    .setReplacement(LangBuilder.SELECTOR_VARIABLE, seconds + "").build());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWorldChange(PlayerChangedWorldEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());
        AWorld from = new BukkitWorld(event.getFrom());
        AWorld to = new BukkitWorld(event.getPlayer().getWorld());

        engine.processWorldChange(player, from, to);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());

        engine.processPlayerJoin(player);
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerQuit(PlayerQuitEvent event) {
        printDebugEvent(event);

        APlayer player = new BukkitPlayer(event.getPlayer());

        engine.processPlayerQuit(player);
    }

    /**
     * Used for processing of generic ITEM_USE events (like exp bottles, eggs, etc).
     * This will send the applicable alerts as well
     *
     * @param player the player
     * @param stack  the item (representation if needed)
     *
     * @return true if denied, false otherwise
     */
    private boolean processGenericUse(Player player, ItemStack stack) {
        if (engine.getEngine(player.getWorld().getName()).processItemUse(new BukkitPlayer(player), new BukkitItem(stack))) {
            String name = plugin.getMaterialProvider().getPlayerFriendlyName(stack);
            player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.NAUGHTY_USE)).withPrefix().setReplacement(LangBuilder.SELECTOR_VARIABLE, name).build());
            alert(Lang.NAUGHTY_ADMIN_USE, player.getName(), name);

            return true;
        }
        return false;
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

        if (Lang.getInstance().getFormat(langNode).trim().equalsIgnoreCase("disabled")) return;

        Bukkit.broadcast(compiled, APermission.GET_ALERTS);
    }

    /**
     * Sends an alert
     *
     * @param langNode   the alert node in the lang file
     * @param playerName the player name
     * @param variable1  a variable to replace ({@link com.turt2live.antishare.bukkit.lang.LangBuilder#SELECTOR_VARIABLE})
     * @param variable2  a variable to replace ({@link com.turt2live.antishare.bukkit.lang.LangBuilder#SELECTOR_VARIABLE})
     */
    private void alert(String langNode, String playerName, String variable1, String variable2) {
        String compiled = new LangBuilder(Lang.getInstance().getFormat(langNode)).withPrefix()
                .setReplacement(LangBuilder.SELECTOR_PLAYER, playerName)
                .setReplacement(LangBuilder.SELECTOR_VARIABLE + "1", variable1)
                .setReplacement(LangBuilder.SELECTOR_VARIABLE + "2", variable2)
                .build();

        if (Lang.getInstance().getFormat(langNode).trim().equalsIgnoreCase("disabled")) return;

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
