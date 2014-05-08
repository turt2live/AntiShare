package com.turt2live.antishare.bukkit.dev.check;

import com.turt2live.antishare.bukkit.dev.AntiShare;
import com.turt2live.antishare.bukkit.dev.CheckBase;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.conversations.*;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Finds blocks that will fall because of the block under them being broken
public class SupportCheck extends CheckBase implements Prompt {

    private Player player;
    private int nextMaterial = -1;
    private Material[] materials = Material.values();
    private List<Material> breaks = new ArrayList<Material>();

    public SupportCheck(AntiShare plugin, Player player) {
        super(plugin);
        this.player = player;
    }

    public void begin() {
        Bukkit.broadcastMessage(ChatColor.BLUE + "Running support sequence...");
        prepareCube(10, new Location(player.getWorld(), 0, 70, 0));
        player.teleport(new Location(player.getWorld(), 0, 72, 0));
        player.beginConversation(new ConversationFactory(plugin)
                .withFirstPrompt(this)
                .withTimeout(60)
                .withLocalEcho(false)
                .buildConversation(player));
    }

    // Return true for 'done'
    private boolean advance() {
        final Location spawn = new Location(player.getWorld(), 0, 70, 0);
        Material test;
        do {
            nextMaterial++; // Go to next material
            if (nextMaterial >= materials.length) {
                plugin.getServer().broadcastMessage(ChatColor.GREEN + "Done");
                writeResults();
                return true;
            }
            test = materials[nextMaterial];
        } while (!test.isBlock() || invalid(test));
        prepareCube(10, spawn);
        player.sendRawMessage(ChatColor.BLUE + "Testing " + test.name());
        spawn.add(0, 5, 0).getBlock().setType(test);
        spawn.add(0, -1, 0).getBlock().setType(Material.STONE);
        return false;
    }

    private void writeResults() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(new File(plugin.getDataFolder(), "unsupportedMaterials.txt"), false));
            writer.write("List<Material> list = super.getBrokenOnTop();\n");

            for (Material material : this.breaks) {
                writer.write("list.add(Material." + material.name() + ");");
                writer.newLine();
            }

            writer.write("return list;");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean invalid(Material material) {
        switch (material) {
            case PISTON_EXTENSION:
            case PISTON_MOVING_PIECE:
            case ENDER_PORTAL:
            case PORTAL:
                return true;
        }
        return false;
    }

    @Override
    public String getPromptText(ConversationContext conversationContext) {
        return nextMaterial < 0 ? ChatColor.AQUA + "Enter anything to begin." : ChatColor.AQUA + "Break the stone. Does the top drop? [Y/N]";
    }

    @Override
    public boolean blocksForInput(ConversationContext conversationContext) {
        return true; // Need input
    }

    @Override
    public Prompt acceptInput(ConversationContext conversationContext, String s) {
        if (nextMaterial >= 0) {
            if (s.equalsIgnoreCase("yes") || s.equalsIgnoreCase("y")) {
                this.breaks.add(this.materials[nextMaterial]);
                player.sendRawMessage(ChatColor.GREEN + "Added " + this.materials[nextMaterial].name());
            } else {
                player.sendRawMessage(ChatColor.RED + "Not added " + this.materials[nextMaterial].name());
            }
        }
        boolean done = advance();
        if (done) {
            player.sendRawMessage(ChatColor.AQUA + "Done!");
        }
        return done ? null : this;
    }

    private void prepareCube(int radius, Location location) {
        World world = location.getWorld();
        for (int x = 0; x < radius; x++) {
            for (int y = radius; y >= 0; y--) {
                for (int z = 0; z < radius; z++) {
                    // Set ceiling
                    if (y == radius) {
                        Location ceilingBlock = new Location(world, x + location.getBlockX() - 5, y + location.getBlockY() + 1, z + location.getBlockZ() - 5);
                        Block block = ceilingBlock.getBlock();
                        block.setType(Material.STONE);
                    }

                    // Clear area
                    Location possibleBlock = new Location(world, x + location.getBlockX() - 5, y + location.getBlockY(), z + location.getBlockZ() - 5);
                    Block block = possibleBlock.getBlock();
                    block.setType(Material.AIR);

                    // Set floor
                    if (y == 0) {
                        Location floorBlock = new Location(world, x + location.getBlockX() - 5, location.getBlockY() - 1, z + location.getBlockZ() - 5);
                        block = floorBlock.getBlock();
                        block.setType(Material.STONE);
                    }
                }
            }
        }
    }
}
