package com.turt2live.antishare.bukkit.abstraction.v1_7_R3;

import com.google.common.base.Charsets;
import com.turt2live.antishare.bukkit.abstraction.MinecraftVersion;
import com.turt2live.antishare.utils.ASGameMode;
import com.turt2live.antishare.utils.BlockType;
import com.turt2live.hurtle.uuid.UUIDServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Minecraft implements MinecraftVersion {

    private class UUIDStore {
        UUID uuid;
        long cached;

        public UUIDStore(UUID uuid, long cached) {
            this.uuid = uuid;
            this.cached = cached;
        }
    }

    private static final long CACHE_EXPIRE = 60000; // 60 seconds

    private final ConcurrentMap<String, UUIDStore> BY_NAME = new ConcurrentHashMap<String, UUIDStore>();

    @Override
    public UUID getUUID(String name) {
        return getUUID(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public UUID getUUID(OfflinePlayer player) {
        if (player == null) throw new IllegalArgumentException("player cannot be null");
        UUID uuid = player.getUniqueId();
        UUID bukDef = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
        if (uuid != null && !uuid.equals(bukDef)) return uuid;

        checkCache();
        if (BY_NAME.containsKey(player.getName())) {
            return BY_NAME.get(player.getName()).uuid;
        }
        uuid = UUIDServiceProvider.getUUID(player.getName());
        if (uuid != null) {
            BY_NAME.put(player.getName(), new UUIDStore(uuid, System.currentTimeMillis()));
        }
        return uuid;
    }

    @Override
    public String getName(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");

        OfflinePlayer player = Bukkit.getOfflinePlayer(uuid);
        if (player.getName().equalsIgnoreCase("InvalidUUID")) {
            return null; // This is Bukkit's bad code
        }
        return player.getName();
    }

    @Override
    public String getName(OfflinePlayer player) {
        checkCache();
        return player.getName();
    }

    @Override
    public ASGameMode toGameMode(GameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("gamemode cannot be null");
        switch (gamemode) {
            case ADVENTURE:
                return ASGameMode.ADVENTURE;
            case SURVIVAL:
                return ASGameMode.SURVIVAL;
            case CREATIVE:
                return ASGameMode.CREATIVE;
            default:
                return null;
        }
    }

    @Override
    public GameMode toGamemode(ASGameMode gamemode) {
        if (gamemode == null) throw new IllegalArgumentException("gamemode cannot be null");
        switch (gamemode) {
            case ADVENTURE:
                return GameMode.ADVENTURE;
            case SURVIVAL:
                return GameMode.SURVIVAL;
            case CREATIVE:
                return GameMode.CREATIVE;
            default:
                return null;
        }
    }

    @Override
    public BlockType toBlockType(GameMode gamemode) {
        if (gamemode == null) return BlockType.UNKNOWN;
        switch (gamemode) {
            case ADVENTURE:
                return BlockType.ADVENTURE;
            case SURVIVAL:
                return BlockType.SURVIVAL;
            case CREATIVE:
                return BlockType.CREATIVE;
            default:
                return BlockType.UNKNOWN;
        }
    }

    private void checkCache() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long now = System.currentTimeMillis();

                List<String> strRemove = new ArrayList<String>();
                for (Map.Entry<String, UUIDStore> entry : BY_NAME.entrySet()) {
                    if (now - entry.getValue().cached > CACHE_EXPIRE)
                        strRemove.add(entry.getKey());
                }
                for (String key : strRemove) BY_NAME.remove(key);
            }
        }).start();
    }
}
