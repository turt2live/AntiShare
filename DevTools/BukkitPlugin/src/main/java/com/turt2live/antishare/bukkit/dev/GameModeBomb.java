package com.turt2live.antishare.bukkit.dev;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Random;

public class GameModeBomb {

    private static Random random = new Random();
    private AntiShare plugin;
    private ASLocation start;
    private BlockType[] values = new BlockType[]{BlockType.CREATIVE, BlockType.SURVIVAL, BlockType.ADVENTURE, BlockType.SPECTATOR};

    public GameModeBomb(AntiShare plugin, ASLocation start) {
        this.plugin = plugin;
        this.start = start;
    }

    public void begin() {
        String world = "world";
        int creative = 0, survival = 0, adventure = 0, spectator = 0;
        int bombRadius = 50;
        for (int x = -bombRadius; x < bombRadius; x++) {
            for (int z = -bombRadius; z < bombRadius; z++) {
                for (int y = 0; y < 256; y++) {
                    ASLocation offset = new ASLocation(start.X + x, y, start.Z + z);
                    BlockType type = values[random.nextInt(values.length)];
                    Engine.getInstance().getEngine(world).getBlockManager().setBlockType(offset, type);

                    switch (type) {
                        case CREATIVE:
                            creative++;
                            break;
                        case SURVIVAL:
                            survival++;
                            break;
                        case ADVENTURE:
                            adventure++;
                            break;
                        case SPECTATOR:
                            spectator++;
                            break;
                    }
                }
            }
        }
        Bukkit.broadcastMessage(ChatColor.GREEN + "Done (" + bombRadius + "r): " + creative + "c " + survival + "s " + adventure + "a " + spectator + "sp = " + (creative + survival + adventure + spectator));
    }

}
