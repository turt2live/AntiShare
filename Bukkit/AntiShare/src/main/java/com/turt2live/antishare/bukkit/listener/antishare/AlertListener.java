package com.turt2live.antishare.bukkit.listener.antishare;

import com.turt2live.antishare.PermissionNodes;
import com.turt2live.antishare.bukkit.impl.BukkitBlock;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.events.EventListener;
import com.turt2live.antishare.events.general.AlertEvent;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Listens for AntiShare alerts
 *
 * @author turt2live
 */
public class AlertListener {

    private static Map<AlertEvent.AlertType, String> LANG_NODES = new HashMap<AlertEvent.AlertType, String>();

    static {
        for (AlertEvent.AlertType type : AlertEvent.AlertType.values()) {
            switch (type) {
                case ADMIN_BLOCK_PLACE:
                    LANG_NODES.put(type, Lang.NAUGHTY_ADMIN_PLACE);
                    break;
                default:
                    LANG_NODES.put(type, Lang.NAUGHTY_ADMIN_DEFAULT);
                    break;
            }
        }
    }

    @EventListener
    public void onAlert(AlertEvent event) {
        String langNode = LANG_NODES.get(event.getType());
        String playerName = event.getPlayer().getName();
        String block = null;
        if (event.getBlock() instanceof BukkitBlock)
            block = ((BukkitBlock) event.getBlock()).getBlock().getType().name().toLowerCase(); // TODO: Player-friendly

        String variable = block == null ? "" : block;

        String compiled = new LangBuilder(Lang.getInstance().getFormat(langNode)).withPrefix()
                .setReplacement(LangBuilder.SELECTOR_PLAYER, playerName)
                .setReplacement(LangBuilder.SELECTOR_VARIABLE, variable)
                .build();

        Bukkit.broadcast(compiled, PermissionNodes.GET_ALERTS);
    }

}
