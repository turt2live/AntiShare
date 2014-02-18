package com.turt2live.antishare.bukkit;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.ASUtils;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.engine.Engine;
import com.turt2live.antishare.io.flatfile.FileBlockStore;
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

    // TODO: Unload world engines

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
                event.setCancelled(true);
                FileBlockStore store = (FileBlockStore) engine.getEngine(player.getWorld().getName()).getBlockManager().getStore(BukkitUtils.toLocation(player.getLocation()));
                store.clear(); // Remove any bad data
                int[] header = store.getHeader();

                System.out.println("filling");
                for (int x = 0; x < header[3]; x++) {
                    for (int y = 0; y < header[3]; y++) {
                        for (int z = 0; z < header[3]; z++) {
                            store.setType((header[0] * header[3]) + x,
                                    (header[1] * header[3]) + y,
                                    (header[2] * header[3]) + z,
                                    BlockType.SPECTATOR);
                        }
                    }
                }

                System.out.println("save");
                long save = System.nanoTime();
                store.save();
                System.out.println("load");
                long load = System.nanoTime();
                store.load();
                long done = System.nanoTime();

                long tsave = load - save;
                long tload = done - load;
                long tt = done - save;
                System.out.println("Save: " + tsave + " ns (" + (tsave / 1000000) + "ms)");
                System.out.println("Load: " + tload + " ns (" + (tload / 1000000) + "ms)");
                System.out.println("Total: " + tt + " ns (" + (tt / 1000000) + "ms)");
            }
        }
    }

}
