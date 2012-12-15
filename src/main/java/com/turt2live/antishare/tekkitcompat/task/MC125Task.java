package com.turt2live.antishare.tekkitcompat.task;

import org.bukkit.Bukkit;

public class MC125Task extends AntiShareTask {

	private int id = -1;

	public MC125Task(int id){
		this.id = id;
	}

	@Override
	public void cancel(){
		if(id != -1){
			Bukkit.getScheduler().cancelTask(id);
		}
	}

}
