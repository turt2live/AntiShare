package com.turt2live.antishare.bukkit.listener;

import com.turt2live.antishare.bukkit.BukkitUtils;
import com.turt2live.antishare.bukkit.PermissionNode;
import com.turt2live.antishare.bukkit.lang.Lang;
import com.turt2live.antishare.bukkit.lang.LangBuilder;
import com.turt2live.antishare.utils.ASUtils;
import com.turt2live.antishare.utils.BlockType;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
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
    private static final ItemStack SET_TEMPLATE;

    static {
        CHECK_TEMPLATE = new ItemStack(Material.BLAZE_ROD);
        SET_TEMPLATE = new ItemStack(Material.BLAZE_POWDER);

        ItemMeta meta = CHECK_TEMPLATE.getItemMeta();
        meta.setDisplayName(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_CHECK_TITLE)).build());
        meta.setLore(LangBuilder.colorize(Lang.getInstance().getFormatList(Lang.TOOL_CHECK_LORE)));
        CHECK_TEMPLATE.setItemMeta(meta);

        meta = SET_TEMPLATE.getItemMeta();
        meta.setDisplayName(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_SET_TITLE)).build());
        meta.setLore(LangBuilder.colorize(Lang.getInstance().getFormatList(Lang.TOOL_SET_LORE)));
        SET_TEMPLATE.setItemMeta(meta);
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
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

        if (hand != null && player.hasPermission(PermissionNode.TOOLS)) {
            if (hand.isSimilar(CHECK_TEMPLATE)) {
                event.setCancelled(true);

                BlockType type = BukkitUtils.getBlockManager(player.getWorld()).getBlockType(BukkitUtils.toLocation(block.getLocation()));
                String strRep = ASUtils.toUpperWords(type == BlockType.UNKNOWN ? "Natural" : type.name());

                player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_CHECK))
                        .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                        .build());
            } else if (hand.isSimilar(SET_TEMPLATE)) {
                event.setCancelled(true);

                if (!rclick) {
                    BukkitUtils.getBlockManager(player.getWorld()).setBlockType(BukkitUtils.toLocation(block.getLocation()), ASUtils.toBlockType(BukkitUtils.toGameMode(player.getGameMode())));

                    String strRep = ASUtils.toUpperWords(player.getGameMode().name());

                    player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_SET))
                            .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                            .build());
                } else {
                    BlockType previous = BukkitUtils.getBlockManager(player.getWorld()).getBlockType(BukkitUtils.toLocation(block.getLocation()));
                    String strRep = ASUtils.toUpperWords(previous == BlockType.UNKNOWN ? "Natural" : previous.name());

                    BukkitUtils.getBlockManager(player.getWorld()).setBlockType(BukkitUtils.toLocation(block.getLocation()), BlockType.UNKNOWN);

                    player.sendMessage(new LangBuilder(Lang.getInstance().getFormat(Lang.TOOL_ON_UNSET))
                            .setReplacement(LangBuilder.SELECTOR_GAMEMODE, strRep)
                            .build());
                }
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

        player.updateInventory();
    }

}
