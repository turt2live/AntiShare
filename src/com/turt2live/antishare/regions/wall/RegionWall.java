package com.turt2live.antishare.regions.wall;

import org.bukkit.Location;

public class RegionWall {

	private Wall location;
	private Location point;

	public RegionWall(Wall location, Location point){
		this.location = location;
		this.point = point;
	}

	public RegionWall add(double amount){
		switch (location){
		case NORTH:
			point.setX(point.getX() - (point.getX() < 0 ? amount * +1 : amount * -1));
			break;
		case SOUTH:
			point.setX(point.getX() + (point.getX() < 0 ? amount * +1 : amount * -1));
			break;
		case EAST:
			point.setZ(point.getZ() - (point.getZ() < 0 ? amount * +1 : amount * -1));
			break;
		case WEST:
			point.setZ(point.getZ() + (point.getZ() < 0 ? amount * +1 : amount * -1));
			break;
		case CEILING:
			point.setY(point.getY() - (point.getY() < 0 ? amount * +1 : amount * -1));
			break;
		case FLOOR:
			point.setY(point.getY() + (point.getY() < 0 ? amount * +1 : amount * -1));
			break;
		}
		return this;
	}

	public Location getPoint(){
		return point;
	}

	public Wall getType(){
		return location;
	}
}
