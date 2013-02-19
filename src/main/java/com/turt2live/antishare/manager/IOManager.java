package com.turt2live.antishare.manager;

import java.util.concurrent.ArrayBlockingQueue;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.io.SaveWrapper;
import com.turt2live.antishare.io.Saveable;
import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;

// TODO: Document
public class IOManager extends AntiShareManager implements Runnable {

	private AntiShare plugin = AntiShare.getInstance();
	private ArrayBlockingQueue<Saveable> pendingSaves;
	private boolean run = false;

	@Override
	public boolean load(){
		int value = plugin.getConfig().getInt("other.save-capacity", 100);
		if(value < 1){
			value = 100;
		}
		pendingSaves = new ArrayBlockingQueue<Saveable>(value);
		run = true;
		return true;
	}

	@Override
	public boolean save(){
		run = false;
		parse();
		return pendingSaves.size() == 0;
	}

	@Override
	public void run(){
		if(run){
			parse();
		}
	}

	private void parse(){
		Saveable save;
		while ((save = pendingSaves.poll()) != null){
			SaveWrapper wrapper = save.getSaveLocation();
			boolean saved = wrapper.save();
			if(saved){
				save.onSave();
			}else{
				if(run){
					plugin.getLogger().warning(Localization.getMessage(LocaleMessage.SAVE_FAILED, save.toString()));
					queueSave(save);
				}else{
					plugin.getLogger().warning(Localization.getMessage(LocaleMessage.SAVE_FAILED_NO_REQUEUE, save.toString()));
				}
			}
		}
	}

	public boolean queueSave(Saveable object){
		if(object == null || object.getSaveLocation() == null){
			throw new IllegalArgumentException("Invalid save object");
		}
		if(!run){
			throw new IllegalArgumentException("IO Manager is shutting down");
		}
		boolean added = pendingSaves.offer(object);
		if(!added){
			plugin.getLogger().severe(Localization.getMessage(LocaleMessage.SAVE_QUEUE_FULL));
		}
		return added;
	}

}
