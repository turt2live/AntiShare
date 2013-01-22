package com.turt2live.antishare.tekkitcompat.task;

import org.bukkit.scheduler.BukkitTask;

public class CB145Task extends AntiShareTask {

	private BukkitTask task;

	/**
	 * Creates a new CraftBukkit 1.4.5+ task
	 * @param task the scheduler's task
	 */
	public CB145Task(BukkitTask task){
		this.task = task;
	}

	@Override
	public void cancel(){
		task.cancel();
	}

}
