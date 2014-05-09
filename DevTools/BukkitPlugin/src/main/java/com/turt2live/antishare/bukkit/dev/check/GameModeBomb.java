package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.object.ASLocation;
import com.turt2live.antishare.object.attribute.BlockType;
import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import com.turt2live.antishare.engine.Engine;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class GameModeBomb extends CheckBase {

    private ASLocation start;
    private BlockType[] values = new BlockType[]{BlockType.CREATIVE, BlockType.SURVIVAL, BlockType.ADVENTURE, BlockType.SPECTATOR};

    public GameModeBomb(AntiShare plugin, ASLocation start) {
        super(plugin);
        this.start = start;
    }

    @Override
    public void begin() {
        Bukkit.broadcastMessage(ChatColor.GREEN + "Starting: " + start);
        String world = "world";
        int creative = 0, survival = 0, adventure = 0, spectator = 0;
        int bombRadius = 50;
        for (int x = -bombRadius; x < bombRadius; x++) {
            for (int z = -bombRadius; z < bombRadius; z++) {
                for (int y = 0; y < 256; y++) {
                    ASLocation offset = new ASLocation(start.X + x, y, start.Z + z);
                    BlockType type = values[AntiShare.RANDOM.nextInt(values.length)];
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
