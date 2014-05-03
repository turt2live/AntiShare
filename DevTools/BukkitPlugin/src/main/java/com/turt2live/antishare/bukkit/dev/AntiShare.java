package com.turt2live.antishare.bukkit.dev;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class AntiShare extends JavaPlugin {

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
            } else
                sender.sendMessage(ChatColor.RED + "Unknown command.");
        } else
            sender.sendMessage(ChatColor.RED + "Unknown command.");
        return true;
    }
}
