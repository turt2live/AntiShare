package com.turt2live.antishare.io;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.feildmaster.lib.configuration.EnhancedConfiguration;
import com.turt2live.antishare.tekkitcompat.ScheduleLayer;
import com.turt2live.antishare.tekkitcompat.task.AntiShareTask;

public class IOManager implements Runnable {

	private volatile boolean operate = true, wait = false, shifted = false;
	private ConcurrentLinkedQueue<Data> queue = new ConcurrentLinkedQueue<Data>();
	private AntiShare plugin = AntiShare.getInstance();
	private AntiShareTask task;

	public IOManager(){
		task = ScheduleLayer.runTaskTimerAsynchronously(plugin, this, 0, 15); // Check every 3/4 seconds
	}

	@Override
	public void run(){
		do{
			Data data;
			while ((data = queue.poll()) != null){
				process(data);
				System.out.println("QUEUE");
			}
		}while (shifted ? operate : false);
	}

	public void shift(){
		if(task != null){
			task.cancel();
			task = null;
		}
		shifted = true;
		Thread thread = new Thread(this);
		thread.setName("ANTISHARE IO MANAGER");
		thread.start();
	}

	protected void process(Data data){
		File file = data.getFile();
		EnhancedConfiguration config = new EnhancedConfiguration(file, plugin);
		config.load();
		config.set(data.getKey(), data.getValue());
		System.out.println(data.getFile().getAbsolutePath() + " " + data.getKey() + " " + data.getValue());
		boolean s = config.save();
		if(!s){
			config.getLastException().printStackTrace();
		}
	}

	public void setWait(boolean wait){
		this.wait = wait;
	}

	public void stop(){
		operate = false;
	}

	public boolean isDone(){
		return queue.size() <= 0 && !wait && task == null;
	}

	public void insertValue(String key, Object value, File file){
		Data data = new Data(key, value, file);
		insert(data);
		System.out.println("INSERT");
	}

	public boolean isStopped(){
		return !operate;
	}

	public void insert(Data data){
		if(!operate){
			throw new IllegalArgumentException("Cannot insert value after manager told to stop");
		}
		if(data != null){
			boolean a = queue.add(data);
			if(!a){
				throw new AssertionError("Failed to insert data");
			}
		}else{
			throw new NullPointerException("Data is null");
		}
	}

	public int getRemaining(){
		return queue.size();
	}

}
