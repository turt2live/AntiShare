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

package com.turt2live.antishare.bukkit.dev;

import com.turt2live.antishare.bukkit.dev.check.*;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.events.EventDispatcher;
import com.turt2live.antishare.object.ASLocation;
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

        DevListener listener = new DevListener();
        getServer().getPluginManager().registerEvents(listener, this);
        EventDispatcher.register(listener);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args != null && args.length > 0) {
            CheckBase check = null;
            if (args[0].equalsIgnoreCase("sand")) {
                check = new FallingSandCheck(this);
            } else if (args[0].equalsIgnoreCase("items")) {
                check = new EssentialsToAntiShare(this);
            } else if (args[0].equalsIgnoreCase("supports")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    check = new SupportCheck(this, player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Not for console.");
                }
            } else if (args[0].equalsIgnoreCase("stress")) {
                check = new StressTest(this);
            } else if (args[0].equalsIgnoreCase("bomb")) {
                ASLocation start = new ASLocation(RANDOM.nextInt(2048) * (RANDOM.nextBoolean() ? -1 : 1), 0, RANDOM.nextInt(2048) * (RANDOM.nextBoolean() ? -1 : 1));
                if (sender instanceof Player) {
                    start = BukkitUtils.toLocation(((Player) sender).getLocation());
                }
                check = new GameModeBomb(this, start);
            } else if (args[0].equalsIgnoreCase("devengine")) {
                if (getServer().getPluginManager().getPlugin("AntiShare") != null) {
                    if (args.length >= 2) {
                        boolean state = args[1].equalsIgnoreCase("on") || args[1].equalsIgnoreCase("true");
                        DevEngine.setEnabled(state);
                        sender.sendMessage(ChatColor.AQUA + "DevEngine is now " + (state ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    } else {
                        sender.sendMessage(ChatColor.AQUA + "DevEngine is " + (DevEngine.isEnabled() ? ChatColor.GREEN + "ENABLED" : ChatColor.RED + "DISABLED"));
                    }
                } else {
                    sender.sendMessage(ChatColor.RED + "No DevEngine to access");
                }
            } else if (args[0].equalsIgnoreCase("piston")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    check = new PistonCheck(this, player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Not for console.");
                }
            } else if (args[0].equalsIgnoreCase("itemtest")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    check = new ItemStackTest(this, player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Not for console.");
                }
            } else if (args[0].equalsIgnoreCase("itemtest2")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    check = new ItemStackTest2(this, player);
                } else {
                    sender.sendMessage(ChatColor.RED + "Not for console.");
                }
            } else if (args[0].equalsIgnoreCase("matstrings")) {
                check = new MaterialStringsCheck(this);
            } else
                sender.sendMessage(ChatColor.RED + "Unknown command.");

            if (check != null)
                check.begin();
        } else
            sender.sendMessage(ChatColor.RED + "Unknown command.");
        return true;
    }
}
