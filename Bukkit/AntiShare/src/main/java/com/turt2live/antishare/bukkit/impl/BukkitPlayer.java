package com.turt2live.antishare.bukkit.impl;

import com.turt2live.antishare.AColor;
import com.turt2live.antishare.bukkit.AntiShare;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.object.APlayer;
import com.turt2live.antishare.object.attribute.ASGameMode;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Bukkit player
 *
 * @author turt2live
 */
public class BukkitPlayer implements APlayer {

    private final Player player;

    public BukkitPlayer(Player player) {
        if (player == null) throw new IllegalArgumentException();

        this.player = player;
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public UUID getUUID() {
        return VersionSelector.getMinecraft().getUUID(player);
    }

    @Override
    public ASGameMode getGameMode() {
        return VersionSelector.getMinecraft().toGameMode(player.getGameMode());
    }

    @Override
    public void setGameMode(ASGameMode gameMode) {
        GameMode gameMode1 = VersionSelector.getMinecraft().toGamemode(gameMode);
        player.setGameMode(gameMode1);
    }

    @Override
    public boolean hasPermission(String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendMessage(String message) {
        message = AColor.toBukkit(message);
        message = ChatColor.translateAlternateColorCodes(AntiShare.COLOR_REPLACE_CHAR, message);
        player.sendMessage(message);
    }
}
