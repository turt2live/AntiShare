/*******************************************************************************
 * Copyright (c) 2013 Travis Ralston.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Lesser Public License v2.1
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/old-licenses/gpl-2.0.html
 *
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.util;

import com.turt2live.antishare.AntiShare;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;

public class MobPattern {

    /**
     * Mob pattern type, representing shape
     */
    public static enum MobPatternType {
        I_SHAPE, T_SHAPE;
    }

    public final String name;
    public final EntityType entityType;
    private final MobPatternType type;
    private final Material body, head, head2;

    /**
     * Creates a new mob pattern containing 2 potential heads
     *
     * @param type  shape of mob
     * @param body  body material (eg: IRON_BLOCK)
     * @param head  first possible head type (eg: PUMPKIN)
     * @param head2 second possible head type (eg: JACK_O_LANTERN)
     */
    public MobPattern(MobPatternType type, Material body, Material head, Material head2) {
        this.type = type;
        this.head = head;
        this.body = body;
        this.head2 = head2;
        switch (body) {
            case SNOW:
                name = "Snow Golem";
                entityType = EntityType.SNOWMAN;
                break;
            case IRON_BLOCK:
                name = "Iron Golem";
                entityType = EntityType.IRON_GOLEM;
                break;
            case SOUL_SAND:
                name = "Wither";
                entityType = EntityType.WITHER;
                break;
            default:
                name = "Unknown";
                entityType = EntityType.UNKNOWN;
        }
    }

    /**
     * Creates a mob pattern containing a single head type
     *
     * @param type the mob shape
     * @param body the body material (eg: SOUL_SAND)
     * @param head the single head type (eg: SKULL)
     */
    public MobPattern(MobPatternType type, Material body, Material head) {
        this(type, body, head, head);
    }

    /**
     * Determines if the block passed is involved with this mob pattern
     *
     * @param block the block to use as a source
     * @return true if the block forms a complete mob, false otherwise
     */
    public boolean exists(Block block) {
        World world = block.getWorld();
        if (!(block.getType() == head || block.getType() == head2)) {
            return false;
        }
        switch (type) {
            case I_SHAPE:
                if (world.getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == body
                        && world.getBlockAt(block.getX(), block.getY() - 2, block.getZ()).getType() == body) {
                    return true;
                }
                break;
            case T_SHAPE:
                if (world.getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == body
                        && world.getBlockAt(block.getX(), block.getY() - 2, block.getZ()).getType() == body
                        && (world.getBlockAt(block.getX() + 1, block.getY() - 1, block.getZ()).getType() == body
                        && world.getBlockAt(block.getX() - 1, block.getY() - 1, block.getZ()).getType() == body
                        || world.getBlockAt(block.getX(), block.getY() - 1, block.getZ() + 1).getType() == body
                        && world.getBlockAt(block.getX(), block.getY() - 1, block.getZ() - 1).getType() == body)) {
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Schedules block updates to correct false information
     *
     * @param block The block (source) to update
     */
    public void scheduleUpdate(final Block block) {
        AntiShare plugin = AntiShare.p;
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

            @Override
            public void run() {
                World world = block.getWorld();
                world.refreshChunk(block.getChunk().getX(), block.getChunk().getZ());
            }
        }, 1);
    }
}
