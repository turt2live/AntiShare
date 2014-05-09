package com.turt2live.antishare;

import com.turt2live.antishare.engine.list.RejectionList;

/**
 * AntiShare Permission Nodes
 *
 * @author turt2live
 */
public final class PermissionNodes {

    /**
     * Allows the use of the AntiShare tools
     */
    public static final String TOOLS = "antishare.tools";
    /**
     * Ignores block placement tracking for players with this permission node
     */
    public static final String FREE_PLACE = "antishare.freeplace";
    /**
     * If enabled, the player will get notifications of illegal actions performed by players
     */
    public static final String GET_ALERTS = "antishare.alerts";

    private PermissionNodes() {
    }

    /**
     * Gets the permission node for the specified settings. This does not include the
     * final period character.
     *
     * @param allow true for generating an "allow" node, false otherwise
     * @param list  the list to use, cannot be null
     * @return the generated node
     */
    public static String getPermissionNode(boolean allow, RejectionList.ListType list) {
        if (list == null) throw new IllegalArgumentException();

        StringBuilder permission = new StringBuilder("antishare.");
        permission.append(allow ? "allow." : "deny.");
        permission.append(list.name().toLowerCase());

        return permission.toString();
    }

}