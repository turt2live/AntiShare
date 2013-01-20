package com.turt2live.antishare.util.generic;

import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

public class MobPattern {

	public static enum MobPatternType{
		POLE, T_SHAPE;
	}

	private final MobPatternType type;
	private final Material body, head, head2;

	public MobPattern(MobPatternType type, Material body, Material head, Material head2){
		this.type = type;
		this.head = head;
		this.body = body;
		this.head2 = head2;
	}

	public MobPattern(MobPatternType type, Material body, Material head){
		this.type = type;
		this.head = head;
		this.body = body;
		this.head2 = head;
	}

	public boolean exists(Block block){
		World world = block.getWorld();
		if(!(block.getType() == head || block.getType() == head2)){
			return false;
		}
		switch (type){
		case POLE:
			if((world.getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == body) &&
					(world.getBlockAt(block.getX(), block.getY() - 2, block.getZ()).getType() == body)){
				return true;
			}
			break;
		case T_SHAPE:
			if((world.getBlockAt(block.getX(), block.getY() - 1, block.getZ()).getType() == body) &&
					(world.getBlockAt(block.getX(), block.getY() - 2, block.getZ()).getType() == body) &&
					(((world.getBlockAt(block.getX() + 1, block.getY() - 1, block.getZ()).getType() == body) &&
					(world.getBlockAt(block.getX() - 1, block.getY() - 1, block.getZ()).getType() == body)) ||
					((world.getBlockAt(block.getX(), block.getY() - 1, block.getZ() + 1).getType() == body) &&
					(world.getBlockAt(block.getX(), block.getY() - 1, block.getZ() - 1).getType() == body)))){
				return true;
			}
			break;
		}
		return false;
	}

}
