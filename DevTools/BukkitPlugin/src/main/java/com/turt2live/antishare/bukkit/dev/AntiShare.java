package com.turt2live.antishare.bukkit.dev;

import com.turt2live.antishare.ASLocation;
import com.turt2live.antishare.bukkit.BukkitUtils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Random;

public class AntiShare extends JavaPlugin {

    public static final Random RANDOM = new Random();

    @Override
    public void onEnable() {
        getLogger().warning("============= ANTISHARE =============");
        getLogger().warning("   -- DEVELOPMENT TOOLS ENABLED --");
        getLogger().warning(" This means that your server is using tools designed for AntiShare development.");
        getLogger().warning(" These tools have NO permission checks and CAN damage a server! ");
        getLogger().warning(" USE THESE TOOLS AT YOUR OWN RISK. THERE IS NO SUPPORT FOR THESE TOOLS.");
        getLogger().warning("============= ANTISHARE =============");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args != null && args.length > 0) {
            if (args[0].equalsIgnoreCase("sand")) {
                sender.sendMessage(ChatColor.GREEN + "Running falling sand check...");
                FallingSandCheck check = new FallingSandCheck(this);
                check.begin();
            } else if (args[0].equalsIgnoreCase("items")) {
                sender.sendMessage(ChatColor.GREEN + "Translating items.csv from Essentials format...");
                EssentialsToAntiShare check = new EssentialsToAntiShare(this);
                check.begin();
            } else if (args[0].equalsIgnoreCase("supports")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    player.sendMessage(ChatColor.BLUE + "Running support sequence...");
                    SupportCheck check = new SupportCheck(player, this);
                    check.begin();
                } else {
                    sender.sendMessage(ChatColor.RED + "Not for console.");
                }
            } else if (args[0].equalsIgnoreCase("stress")) {
                sender.sendMessage(ChatColor.GREEN + "Starting stress test...");
                StressTest check = new StressTest(this);
                check.begin();
            } else if (args[0].equalsIgnoreCase("bomb")) {
                ASLocation start = new ASLocation(RANDOM.nextInt(2048) * (RANDOM.nextBoolean() ? -1 : 1), 0, RANDOM.nextInt(2048) * (RANDOM.nextBoolean() ? -1 : 1));
                if (sender instanceof Player) {
                    start = BukkitUtils.toLocation(((Player) sender).getLocation());
                }
                sender.sendMessage(ChatColor.GREEN + "Starting: " + start);
                GameModeBomb check = new GameModeBomb(this, start);
                check.begin();
            } else
                sender.sendMessage(ChatColor.RED + "Unknown command.");
        } else
            sender.sendMessage(ChatColor.RED + "Unknown command.");
        return true;
    }
}
