/*******************************************************************************
 * Copyright (C) 2014 Travis Ralston (turt2live)
 *
 * This software is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/

package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.APermission;
import com.turt2live.antishare.bukkit.abstraction.VersionSelector;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.bukkit.util.BukkitUtils;
import com.turt2live.antishare.engine.DevEngine;
import com.turt2live.antishare.object.attribute.BlockType;
import com.turt2live.antishare.utils.ASUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The toolkit listener for AntiShare
 *
 * @author turt2live
 */
public class ToolListener implements Listener {

    /*
    Tools are not protected from the server because the only people that should have tools
    are administrators/staff of the server - not random players. If the staff start spreading
    the tools around the server, that's out of scope of this plugin.
     */

    private static final ItemStack CHECK_TEMPLATE;
    private static final ItemStack CHECK_TEMPLATE_BLOCK;
    private static final ItemStack SET_TEMPLATE;

    static {
        CHECK_TEMPLATE = new ItemStack(Material.BLAZE_ROD);
        SET_TEMPLATE = new ItemStack(Material.BLAZE_POWDER);
        CHECK_TEMPLATE_BLOCK = new ItemStack(Material.LAPIS_BLOCK);

        ItemMeta meta = CHECK_TEMPLATE.getItemMeta();
        meta.setDisplayName(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_CHECK_TITLE)).build());
        meta.setLore(LangBuilder.colorize(Lang.getInstance().getFormatList(Lang.TOOL_CHECK_LORE)));
        CHECK_TEMPLATE.setItemMeta(meta);

        meta = CHECK_TEMPLATE_BLOCK.getItemMeta();
        meta.setDisplayName(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_CHECK_TITLE)).build());
        meta.setLore(LangBuilder.colorize(Lang.getInstance().getFormatList(Lang.TOOL_CHECK_LORE)));
        CHECK_TEMPLATE_BLOCK.setItemMeta(meta);

        meta = SET_TEMPLATE.getItemMeta();
        meta.setDisplayName(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_SET_TITLE)).build());
        meta.setLore(LangBuilder.colorize(Lang.getInstance().getFormatList(Lang.TOOL_SET_LORE)));
        SET_TEMPLATE.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onInteract(PlayerInteractEvent event) {
        switch (event.getAction()) {
            case RIGHT_CLICK_AIR:
            case LEFT_CLICK_AIR:
            case PHYSICAL:
                return;
        }

        Block block = event.getClickedBlock();
        boolean rclick = event.getAction() == Action.RIGHT_CLICK_BLOCK;
        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();

        if (hand != null && player.hasPermission(APermission.TOOLS)) {
            if (hand.isSimilar(CHECK_TEMPLATE)) {
                event.setCancelled(true);

                BlockType type = BukkitUtils.getBlockManager(player.getWorld()).getBlockType(BukkitUtils.toLocation(block.getLocation()));
                String strRep = ASUtils.toUpperWords(type == BlockType.UNKNOWN ? "Natural" : type.name());

                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_CHECK))
                        .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                        .withPrefix()
                        .build());

                DevEngine.log("[Tools] Check completed (" + player.getName() + ")",
                        "[Tools] \t\tCheck on: " + block,
                        "[Tools] \t\tWith: " + hand,
                        "[Tools] \t\tRClick: " + rclick,
                        "[Tools] \t\tResult: " + type + " (" + strRep + ")");
            } else if (hand.isSimilar(SET_TEMPLATE)) {
                event.setCancelled(true);

                if (!rclick) {
                    BukkitUtils.getBlockManager(player.getWorld()).setBlockType(BukkitUtils.toLocation(block.getLocation()), ASUtils.toBlockType(VersionSelector.getMinecraft().toGameMode(player.getGameMode())));

                    String strRep = ASUtils.toUpperWords(player.getGameMode().name());

                    player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_SET))
                            .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                            .withPrefix()
                            .build());

                    DevEngine.log("[Tools] Set completed (" + player.getName() + ")",
                            "[Tools] \t\tCheck on: " + block,
                            "[Tools] \t\tWith: " + hand,
                            "[Tools] \t\tRClick: " + rclick,
                            "[Tools] \t\tResult: to " + player.getGameMode() + " (" + strRep + ")");
                } else {
                    BlockType previous = BukkitUtils.getBlockManager(player.getWorld()).getBlockType(BukkitUtils.toLocation(block.getLocation()));
                    String strRep = ASUtils.toUpperWords(previous == BlockType.UNKNOWN ? "Natural" : previous.name());

                    BukkitUtils.getBlockManager(player.getWorld()).setBlockType(BukkitUtils.toLocation(block.getLocation()), BlockType.UNKNOWN);

                    player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_UNSET))
                            .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                            .withPrefix()
                            .build());

                    DevEngine.log("[Tools] Unset completed (" + player.getName() + ")",
                            "[Tools] \t\tCheck on: " + block,
                            "[Tools] \t\tWith: " + hand,
                            "[Tools] \t\tRClick: " + rclick,
                            "[Tools] \t\tResult: was " + previous + " (" + strRep + ")");
                }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void onBlockPlace(BlockPlaceEvent event) {
        if (!DevEngine.isEnabled()) return; // Ignore if not debugging

        Block block = event.getBlock();
        Player player = event.getPlayer();
        ItemStack hand = player.getItemInHand();

        if (hand != null && player.hasPermission(APermission.TOOLS)) {
            if (hand.isSimilar(CHECK_TEMPLATE_BLOCK)) {
                event.setCancelled(true);

                BlockType type = BukkitUtils.getBlockManager(player.getWorld()).getBlockType(BukkitUtils.toLocation(block.getLocation()));
                String strRep = ASUtils.toUpperWords(type == BlockType.UNKNOWN ? "Natural" : type.name());

                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_CHECK))
                        .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                        .withPrefix()
                        .build());

                DevEngine.log("[Tools] Check completed (" + player.getName() + ")",
                        "[Tools] \t\tCheck on: " + block,
                        "[Tools] \t\tWith: " + hand,
                        "[Tools] \t\tResult: " + type + " (" + strRep + ")");
            }
        }
    }

    /**
     * Gives the toolkit to a specified player. This does not validate the
     * player's permissions
     *
     * @param player the player to give the tools to, cannot be null
     */
    public static void giveTools(Player player) {
        ItemStack checkTool = CHECK_TEMPLATE.clone();
        ItemStack setTool = SET_TEMPLATE.clone();

        PlayerInventory inventory = player.getInventory();
        ItemStack slot1 = inventory.getItem(0);
        ItemStack slot2 = inventory.getItem(1);
        inventory.setItem(0, checkTool);
        inventory.setItem(1, setTool);
        if (slot1 != null) inventory.addItem(slot1);
        if (slot2 != null) inventory.addItem(slot2);

        // Debugging tools :D
        if (DevEngine.isEnabled()) {
            ItemStack checkBlockTool = CHECK_TEMPLATE_BLOCK.clone();
            ItemStack slot3 = inventory.getItem(2);
            inventory.setItem(2, checkBlockTool);
            if (slot3 != null) inventory.addItem(slot3);
        }

        player.updateInventory();
    }

}
