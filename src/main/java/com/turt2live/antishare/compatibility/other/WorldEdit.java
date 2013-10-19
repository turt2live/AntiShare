package com.turt2live.antishare.compatibility.other;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.Selection;
import com.turt2live.antishare.cuboid.Cuboid;

/**
 * Represents a WorldEdit hook
 * 
 * @author turt2live
 */
public class WorldEdit {

	private WorldEditPlugin worldedit;

	public WorldEdit(Plugin worldedit) {
		this.worldedit = (WorldEditPlugin) worldedit;
	}

	/**
	 * Gets a cuboid based on a WorldEdit selection
	 * 
	 * @param player the player to get the selection from
	 * @return the cuboid, or null if the selection is incomplete/missing
	 */
	public Cuboid getCuboid(Player player) {
		Selection selection = worldedit.getSelection(player);
		if (selection == null || selection.getMaximumPoint() == null || selection.getMinimumPoint() == null) {
			return null;
		}
		Cuboid cuboid = new Cuboid(selection.getMaximumPoint(), selection.getMinimumPoint());
		return cuboid;
	}

}
