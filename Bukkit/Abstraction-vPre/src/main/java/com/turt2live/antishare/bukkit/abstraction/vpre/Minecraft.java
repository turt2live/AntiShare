package com.turt2live.antishare.bukkit.abstraction.vpre;

import com.turt2live.antishare.bukkit.abstraction.MinecraftVersion;
import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.utils.BlockType;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Minecraft implements MinecraftVersion {
    @Override
    public UUID getUUID(String name) {
        return UUID.randomUUID(); // TODO
    }

    @Override
    public UUID getUUID(OfflinePlayer player) {
        return UUID.randomUUID(); // TODO
    }

    @Override
    public String getName(UUID uuid) {
        return uuid.toString(); // TODO
    }

    @Override
    public String getName(OfflinePlayer player) {
        return "name"; // TODO
    }

    @Override
    public ASGameMode toGameMode(GameMode gamemode) {
        return ASGameMode.ADVENTURE; // TODO
    }

    @Override
    public GameMode toGamemode(ASGameMode gamemode) {
        return GameMode.SURVIVAL; // TODO
    }

    @Override
    public BlockType toBlockType(GameMode gamemode) {
        return BlockType.ADVENTURE; // TODO
    }
}
