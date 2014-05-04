package com.turt2live.antishare.bukkit.abstraction.vpre;

import com.turt2live.antishare.ABlock;
import com.turt2live.antishare.ASGameMode;
import com.turt2live.antishare.BlockType;
import com.turt2live.antishare.bukkit.abstraction.MinecraftVersion;
import com.turt2live.hurtle.uuid.UUIDServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.Chest;
import org.bukkit.inventory.DoubleChestInventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class Minecraft implements MinecraftVersion {

    protected class UUIDStore {
        public UUID uuid;
        public long cached;

        public UUIDStore(UUID uuid, long cached) {
            this.uuid = uuid;
            this.cached = cached;
        }
    }

    protected class NameStore {
        String name;
        long cached;

        public NameStore(String name, long cached) {
            this.name = name;
            this.cached = cached;
        }
    }

    protected static final long CACHE_EXPIRE = 60000; // 60 seconds

    protected final ConcurrentMap<String, UUIDStore> BY_NAME = new ConcurrentHashMap<String, UUIDStore>();
    protected final ConcurrentMap<UUID, NameStore> BY_UUID = new ConcurrentHashMap<UUID, NameStore>();

    @Override
    public ABlock.ChestType getChestType(Block block) {
        if (block == null) throw new IllegalArgumentException();

        if (block.getType() == Material.CHEST) {
            BlockState state = block.getState();
            if (state instanceof Chest && ((Chest) state).getInventory() instanceof DoubleChestInventory) {
                return ABlock.ChestType.DOUBLE_NORMAL;
            }
            return ABlock.ChestType.NORMAL;
        } else if (block.getType() == Material.ENDER_CHEST) {
            return ABlock.ChestType.ENDER;
        } else if (block.getType() == Material.LOCKED_CHEST) {
            return ABlock.ChestType.LOCKED;
        }

        return ABlock.ChestType.NONE;
    }

    @Override
    public UUID getUUID(String name) {
        return getUUID(Bukkit.getOfflinePlayer(name));
    }

    @Override
    public UUID getUUID(OfflinePlayer player) {
        if (player == null) throw new IllegalArgumentException("player cannot be null");
        checkCache();
        if (BY_NAME.containsKey(player.getName())) {
            return BY_NAME.get(player.getName()).uuid;
        }
        UUID uuid = UUIDServiceProvider.getUUID(player.getName());
        if (uuid != null) {
            BY_NAME.put(player.getName(), new UUIDStore(uuid, System.currentTimeMillis()));
            BY_UUID.put(uuid, new NameStore(player.getName(), System.currentTimeMillis()));
        }
        return uuid;
    }

    @Override
    public String getName(UUID uuid) {
        if (uuid == null) throw new IllegalArgumentException("uuid cannot be null");
        checkCache();
        if (BY_UUID.containsKey(uuid)) {
            return BY_UUID.get(uuid).name;
        }
        String name = UUIDServiceProvider.getName(uuid);
        if (name != null) {
            BY_NAME.put(name, new UUIDStore(uuid, System.currentTimeMillis()));
            BY_UUID.put(uuid, new NameStore(name, System.currentTimeMillis()));
        }
        return name;
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

    protected void checkCache() {
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

                List<UUID> uidRemove = new ArrayList<UUID>();
                for (Map.Entry<UUID, NameStore> entry : BY_UUID.entrySet()) {
                    if (now - entry.getValue().cached > CACHE_EXPIRE)
                        uidRemove.add(entry.getKey());
                }
                for (UUID key : uidRemove) BY_UUID.remove(key);
            }
        }).start();
    }
}
