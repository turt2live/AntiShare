package com.turt2live.antishare.bukkit.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

/**
 * AntiShare Block Break Event
 *
 * @author turt2live
 */
public class AntiShareBlockBreakEvent extends BlockBreakEvent {

    public AntiShareBlockBreakEvent(Block theBlock, Player player) {
        super(theBlock, player);
    }
}
