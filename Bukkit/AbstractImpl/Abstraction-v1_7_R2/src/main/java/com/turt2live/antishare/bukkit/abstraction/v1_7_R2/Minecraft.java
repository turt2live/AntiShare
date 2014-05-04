package com.turt2live.antishare.bukkit.abstraction.v1_7_R2;

import com.google.common.base.Charsets;
import com.turt2live.hurtle.uuid.UUIDServiceProvider;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_7_R1.Minecraft {

    @Override
    public UUID getUUID(OfflinePlayer player) {
        if (player == null) throw new IllegalArgumentException("player cannot be null");
        UUID uuid = player.getUniqueId();
        UUID bukDef = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
        if (uuid != null && uuid.equals(bukDef)) return uuid;

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
}
