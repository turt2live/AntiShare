package com.turt2live.antishare.storage;

import org.bukkit.block.Block;

public class TNTMeta extends Meta {

	private Block tnt;
	private Boolean explode;

	public TNTMeta(Block tnt, Boolean explode){
		this.tnt = tnt;
		this.explode = explode;
	}

	public Block getTNT(){
		return tnt;
	}

	public boolean willExplode(){
		return explode;
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
