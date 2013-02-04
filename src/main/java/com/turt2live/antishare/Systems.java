package com.turt2live.antishare;

import java.util.HashMap;
import java.util.Map;

import com.turt2live.antishare.lang.LocaleMessage;
import com.turt2live.antishare.lang.Localization;
import com.turt2live.antishare.manager.AntiShareManager;
import com.turt2live.antishare.manager.BlockManager;
import com.turt2live.antishare.manager.CuboidManager;
import com.turt2live.antishare.manager.FeatureManager;
import com.turt2live.antishare.manager.FeatureManager.Feature;
import com.turt2live.antishare.manager.HookManager;
import com.turt2live.antishare.manager.InventoryManager;
import com.turt2live.antishare.manager.MoneyManager;
import com.turt2live.antishare.manager.RegionManager;
import com.turt2live.antishare.manager.WorldConfigurationManager;
import com.turt2live.antishare.metrics.TrackerList.TrackerType;

public class Systems {

	/**
	 * Represents the available managers in AntiShare's system
	 */
	public static enum Manager{
		WORLD_CONFIGS(Feature.ALWAYS_ON, WorldConfigurationManager.class, Localization.getMessage(LocaleMessage.SERVICE_WORLD_CONFIG), null),
		REGION(Feature.REGIONS, RegionManager.class, Localization.getMessage(LocaleMessage.SERVICE_REGIONS), TrackerType.FEATURE_REGIONS),
		INVENTORY(Feature.INVENTORIES, InventoryManager.class, Localization.getMessage(LocaleMessage.SERVICE_INVENTORIES), TrackerType.FEATURE_INVENTORIES),
		FEATURE(Feature.SELF, FeatureManager.class, Localization.getMessage(LocaleMessage.SERVICE_FEATURES), null),
		BLOCK(Feature.BLOCKS, BlockManager.class, Localization.getMessage(LocaleMessage.SERVICE_BLOCKS), TrackerType.FEATURE_GM_BLOCKS),
		CUBOID(Feature.REGIONS, CuboidManager.class, Localization.getMessage(LocaleMessage.SERVICE_CUBOID), null),
		MONEY(Feature.MONEY, MoneyManager.class, Localization.getMessage(LocaleMessage.SERVICE_MONEY), TrackerType.FEATURE_FINES_REWARDS),
		HOOK(Feature.ALWAYS_ON, HookManager.class, Localization.getMessage(LocaleMessage.SERVICE_HOOKS), null);

		private final Feature f;
		private final Class<? extends AntiShareManager> m;
		private final String name;
		private final TrackerType tracker;

		private Manager(Feature f, Class<? extends AntiShareManager> m, String name, TrackerType tracker){
			this.f = f;
			this.m = m;
			this.name = name;
			this.tracker = tracker;
		}

		/**
		 * Gets the tracker type associated with this manager
		 * 
		 * @return the tracker type
		 */
		public TrackerType getTrackerType(){
			return tracker;
		}

		/**
		 * Gets the name of the manager
		 * 
		 * @return the name of the manager
		 */
		public String getName(){
			return name;
		}

		/**
		 * Gets the feature enum for this manager
		 * 
		 * @return the feature enum
		 */
		public Feature getFeature(){
			return f;
		}

		protected Class<? extends AntiShareManager> getManagerClass(){
			return m;
		}
	}

	private FeatureManager features;
	private final Map<Manager, AntiShareManager> managers = new HashMap<Manager, AntiShareManager>();
	private final AntiShare plugin = AntiShare.getInstance();

	/**
	 * Loads this manager
	 * 
	 * @return true if successful
	 */
	public boolean load(){
		features = new FeatureManager();
		features.load();
		for(Manager s : Manager.values()){
			if(!features.isEnabled(s.getFeature())){
				if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
					plugin.getLogger().info(Localization.getMessage(LocaleMessage.NOT_ENABLED, s.getName()));
				}
				continue;
			}
			if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
				plugin.getLogger().info(Localization.getMessage(LocaleMessage.START_START, s.getName()));
			}
			if(s == Manager.FEATURE){
				managers.put(s, features);
			}else{
				Class<? extends AntiShareManager> m = s.getManagerClass();
				if(m == null){
					continue;
				}
				try{
					AntiShareManager man = m.newInstance();
					man.load();
					managers.put(s, man);
					if(s.getTrackerType() != null){
						plugin.getTrackers().getTracker(s.getTrackerType()).increment(1);
					}
				}catch(InstantiationException e){
					e.printStackTrace();
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	/**
	 * Saves this manager
	 * 
	 * @return true if successful
	 */
	public boolean save(){
		for(Manager s : managers.keySet()){
			if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
				plugin.getLogger().info(Localization.getMessage(LocaleMessage.STOP_SAVE, s.getName()));
			}
			managers.get(s).save();
			if(s.getFeature() == Feature.BLOCKS){
				BlockManager blocks = (BlockManager) managers.get(s);
				waitForBlockManager(blocks);
			}
		}
		managers.clear();
		return true;
	}

	/**
	 * Reloads this manager
	 * 
	 * @return true if successful
	 */
	public boolean reload(){
		boolean anyFailed = false;
		for(Manager s : managers.keySet()){
			if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
				plugin.getLogger().info(Localization.getMessage(LocaleMessage.RELOAD_RELOAD, s.getName()));
			}
			boolean done = managers.get(s).reload();
			if(!done){
				anyFailed = true;
			}
			if(s.getFeature() == Feature.BLOCKS){
				BlockManager blocks = (BlockManager) managers.get(s);
				waitForBlockManager(blocks);
			}
		}
		return anyFailed;
	}

	private void waitForBlockManager(BlockManager blocks){
		if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
			plugin.getLogger().info(Localization.getMessage(LocaleMessage.BLOCK_MAN_WAIT));
		}
		int lastPercent = 0, goal = 10;
		boolean hit100 = false;
		while (!blocks.isSaveDone()){
			if(plugin.getConfig().getBoolean("other.use-sleep")){
				try{
					Thread.sleep(50); // To avoid a higher CPU use
				}catch(InterruptedException e){
					e.printStackTrace();
				}
			}
			if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
				int percent = blocks.percentSaveDone();
				goal = lastPercent + 10;
				if(goal > 100){
					goal = 100;
				}
				if(goal <= percent && !hit100 && percent <= 100){
					plugin.getLogger().info("[Block Manager] " + Localization.getMessage(LocaleMessage.BLOCK_MAN_PERCENT, String.valueOf(percent)));
					lastPercent = percent;
					if(percent >= 100){
						hit100 = true;
					}
				}
			}
		}
	}

	/**
	 * Determines if the system requested is enabled
	 * 
	 * @param system the system
	 * @return true if enabled and loaded
	 */
	public boolean isEnabled(Manager system){
		return managers.containsKey(system);
	}

	/**
	 * Gets the AntiShare Manager associated with a system
	 * 
	 * @param system the system
	 * @return the manager, if any, requested
	 */
	public AntiShareManager getManager(Manager system){
		return managers.get(system);
	}

}
