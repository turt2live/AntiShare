package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.engine.Engine;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

import java.util.Random;

/**
 * AntiShare Bukkit Listener for the AntiShare Engine. This listener will only
 * listen to events that the engine would be interested in.
 *
 * @author turt2live
 */
public class EngineListener implements Listener {

    private Engine engine;

    EngineListener() {
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

    // TODO: Debug code
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (player.getItemInHand() != null) {
            if (player.getItemInHand().getType() == Material.BLAZE_ROD) {
                Block block = event.getClickedBlock();
                if (block != null) {
                    event.setCancelled(true);

                    BlockType type = engine.getEngine(event.getPlayer().getWorld().getName())
                            .getBlockManager().getBlockType(BukkitUtils.toLocation(block.getLocation()));
                    player.sendMessage(ChatColor.YELLOW + "Block Type: " + ChatColor.GOLD + type.name());
                }
            } else if (player.getItemInHand().getType() == Material.DIAMOND) {
                int iterations = 10000000; // 10 million
                Random random = new Random();
                long iter = System.nanoTime();
                double last = -1;
                for (int i = 0; i < iterations; i++) {
                    int modx = random.nextInt(300) * (random.nextBoolean() ? -1 : 1);
                    int mody = random.nextInt(300) * (random.nextBoolean() ? -1 : 1);
                    int modz = random.nextInt(300) * (random.nextBoolean() ? -1 : 1);

                    Engine.getInstance().getEngine(player.getWorld().getName()).getBlockManager().setBlockType(
                            player.getLocation().getBlockX() + modx,
                            player.getLocation().getBlockY() + mody,
                            player.getLocation().getBlockZ() + modz,
                            BlockType.ADVENTURE
                    );

                    double now = Math.round(((double) i / (double) iterations) * 100);
                    if (now != last) {
                        last = now;
                        System.out.println(i + "/" + iterations + " (" + now + "%)");
                    }
                }

                System.out.println("saving");
                long start = System.nanoTime();
                Engine.getInstance().getEngine(player.getWorld().getName()).getBlockManager().saveAll();
                long read = System.nanoTime();
                System.out.println("loading");
                Engine.getInstance().getEngine(player.getWorld().getName()).getBlockManager().loadAll();
                long done = System.nanoTime();

                System.out.println("Iteration:    " + (start - iter) + "ns \t(" + (start - iter) / 1000000 + "ms)");
                System.out.println("Save:         " + (read - start) + "ns \t(" + (read - start) / 1000000 + "ms)");
                System.out.println("Load:         " + (done - read) + "ns \t(" + (done - read) / 1000000 + "ms)");
                System.out.println("Total IO:     " + (done - start) + "ns \t(" + (done - start) / 1000000 + "ms)");
                System.out.println("Total:        " + (done - iter) + "ns \t(" + (done - iter) / 1000000 + "ms)");
            }
        }
    }

}
