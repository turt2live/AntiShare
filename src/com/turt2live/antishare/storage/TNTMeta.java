package com.turt2live.antishare.storage;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class TNTMeta extends Meta {

	private Block tnt;
	private Boolean explode;
	private Player owner;

	public TNTMeta(Block tnt, Boolean explode, Player owner){
		this.tnt = tnt;
		this.explode = explode;
	}

	public Block getTNT(){
		return tnt;
	}

	public boolean willExplode(){
		return explode;
	}

	public Player getOwner(){
		return owner;
	}

	public void setExplode(Boolean explode){
		this.explode = explode;
	}

	@Override
	@Deprecated
	public void add(String misc, Object explode){
		if(explode instanceof Boolean){
			this.explode = (Boolean) explode;
		}
	}

	@Override
	@Deprecated
	public Boolean get(String misc){
		return explode;
	}

	@Override
	@Deprecated
	public void unset(String misc){
		explode = false;
	}
}
