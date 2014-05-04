package com.turt2live.antishare.bukkit.hooks.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.List;

/**
 * AntiShare Explode Event
 *
 * @author turt2live
 */
public class AntiShareExplodeEvent extends EntityExplodeEvent {

    public AntiShareExplodeEvent(Entity what, Location location, List<Block> blocks, float yield) {
        super(what, location, blocks, yield);
    }

}
