package com.turt2live.antishare.bukkit.abstraction.v1_7_R3;

import com.google.common.base.Charsets;
import com.turt2live.hurtle.uuid.UUIDServiceProvider;
import org.bukkit.OfflinePlayer;

import java.util.UUID;

public class Minecraft extends com.turt2live.antishare.bukkit.abstraction.v1_7_R2.Minecraft {

    @Override
    public UUID getUUID(OfflinePlayer player) {
        if (player == null) throw new IllegalArgumentException("player cannot be null");
        UUID uuid = player.getUniqueId();
        UUID badUUID = UUID.nameUUIDFromBytes(("OfflinePlayer:" + player.getName()).getBytes(Charsets.UTF_8));
        UUID badName = UUID.nameUUIDFromBytes("InvalidUsername".getBytes(Charsets.UTF_8));
        if (uuid != null && (uuid.equals(badUUID) || uuid.equals(badName))) return uuid;

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
}
