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

package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.bukkit.abstraction.event.AntiShareInventoryTransferEvent;
import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import com.turt2live.antishare.bukkit.dev.util.FakeWorld;
import com.turt2live.antishare.bukkit.listener.EngineListener;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityChangeBlockEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.world.StructureGrowEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("deprecation")
public class StressTest extends CheckBase implements Runnable {

    public StressTest(AntiShare plugin) {
        super(plugin);
    }

    @Override
    public void begin() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Starting stress test...");
        // 30 seconds of solid lag :D
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings reset");
        final BukkitTask task = plugin.getServer().getScheduler().runTaskTimer(plugin, this, 0L, 20L);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            public void run() {
                task.cancel();
                Bukkit.broadcastMessage(ChatColor.GREEN + "DONE");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "timings merged");
            }
        }, 20L * 30);
    }

    @Override
    public void run() {
        PluginManager man = plugin.getServer().getPluginManager();
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "FIRING");
        for (int i = 0; i < 50; i++) {
            player().setGameMode(gamemode());
            man.callEvent(new BlockBreakEvent(block(), player()));
            man.callEvent(new BlockPlaceEvent(block(), block().getState(), block(), player().getItemInHand(), player(), true));
            man.callEvent(new EntityExplodeEvent(player(), block().getLocation(), blocks(), 4.0f));
            man.callEvent(new EntityChangeBlockEvent(fallingBlock(), block(), Material.AIR, (byte) 0x0));
            man.callEvent(new EntityChangeBlockEvent(fallingBlock(), block(), Material.SAND, (byte) 0x0));
            Block cacti = growCactus();
            man.callEvent(new BlockGrowEvent(cacti, cacti.getState()));
            Block crop = growStem();
            man.callEvent(new BlockGrowEvent(crop, crop.getState()));
            man.callEvent(new StructureGrowEvent(block().getLocation(), TreeType.TREE, true, player(), states()));
            man.callEvent(new BlockSpreadEvent(block(), block(), block().getState()));
            man.callEvent(new BlockBurnEvent(block()));
            man.callEvent(new BlockFadeEvent(block(), block().getState()));
            man.callEvent(new LeavesDecayEvent(block()));
            man.callEvent(new AntiShareInventoryTransferEvent(block(), block()));
            man.callEvent(new WorldLoadEvent(new FakeWorld("testWorld" + (i % 2 == 0 ? "1" : "2"))));
            man.callEvent(new WorldUnloadEvent(new FakeWorld("testWorld" + (i % 2 == 0 ? "2" : "1"))));
        }
    }

    private Entity fallingBlock() {
        Block block = block();
        return block.getWorld().spawnFallingBlock(block.getLocation().add(0, 20, 0), Material.SAND, (byte) 0x0);
    }

    private Block growStem() {
        Material crop = AntiShare.RANDOM.nextBoolean() ? Material.PUMPKIN : Material.MELON_BLOCK;
        Material stemType = crop == Material.PUMPKIN ? Material.PUMPKIN_STEM : Material.MELON_STEM;

        Block block = block();
        block.getRelative(BlockFace.EAST).setType(Material.WATER);
        block.getRelative(BlockFace.UP).setType(stemType);
        block.setType(Material.SOIL);
        block.getRelative(BlockFace.WEST).setType(Material.GRASS);
        block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP).setType(crop);
        return block.getRelative(BlockFace.WEST).getRelative(BlockFace.UP);
    }

    private Block growCactus() {
        Block block = block();
        for (BlockFace face : EngineListener.TRUE_FACES) {
            Block rel = block.getRelative(face);
            rel.getRelative(BlockFace.UP).setType(Material.AIR);
            rel.setType(Material.AIR);
        }
        block.getRelative(BlockFace.DOWN).setType(Material.SAND);
        block.setType(Material.CACTUS);
        block.getRelative(BlockFace.UP).setType(Material.CACTUS);
        return block.getRelative(BlockFace.UP);
    }

    private Item item() {
        ItemStack stack = itemStack();
        final Item item = block().getWorld().dropItemNaturally(block().getLocation(), stack);
        plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                if (!item.isDead()) item.remove();
            }
        }, 5L);
        return item;
    }

    private GameMode gamemode() {
        GameMode[] modes = GameMode.values();
        return modes[AntiShare.RANDOM.nextInt(modes.length)];
    }

    private ItemStack itemStack() {
        ItemStack itemStack = new ItemStack(Material.DIAMOND, AntiShare.RANDOM.nextInt(10) + 2);
        return itemStack;
    }

    private List<BlockState> states() {
        List<BlockState> states = new ArrayList<BlockState>();
        List<Block> blocks = blocks();
        for (Block block : blocks) {
            states.add(block.getState());
        }
        return states;
    }

    private List<Block> blocks() {
        List<Block> blocks = new ArrayList<Block>();
        for (int i = 0; i < AntiShare.RANDOM.nextInt(20) + 5; i++) {
            blocks.add(block());
        }
        return blocks;
    }

    private Player player() {
        Player[] players = plugin.getServer().getOnlinePlayers();
        return players[AntiShare.RANDOM.nextInt(players.length)];
    }

    private Block block() {
        Location location = player().getLocation();
        double dx = (AntiShare.RANDOM.nextDouble() * 1000) * (AntiShare.RANDOM.nextBoolean() ? -1 : 1);
        double dy = (AntiShare.RANDOM.nextDouble() * 10) * (AntiShare.RANDOM.nextBoolean() ? -1 : 1);
        double dz = (AntiShare.RANDOM.nextDouble() * 1000) * (AntiShare.RANDOM.nextBoolean() ? -1 : 1);
        location.add(dx, dy, dz);
        return location.getBlock();
    }
}
