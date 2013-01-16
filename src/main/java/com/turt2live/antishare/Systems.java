package com.turt2live.antishare;

import java.util.HashMap;
import java.util.Map;

import com.turt2live.antishare.blocks.BlockManager;
import com.turt2live.antishare.cuboid.CuboidManager;
import com.turt2live.antishare.inventory.InventoryManager;
import com.turt2live.antishare.manager.AntiShareManager;
import com.turt2live.antishare.manager.FeatureManager;
import com.turt2live.antishare.manager.FeatureManager.Feature;
import com.turt2live.antishare.regions.RegionManager;

public class Systems extends AntiShareManager {

	public static enum Manager{
		REGION(Feature.REGIONS, RegionManager.class, "region manager"),
		INVENTORY(Feature.INVENTORIES, InventoryManager.class, "inventory manager"),
		FEATURES(Feature.SELF, FeatureManager.class, "feature manager"),
		BLOCKS(Feature.BLOCKS, BlockManager.class, "block manager"),
		CUBOIDS(Feature.ALWAYS_ON, CuboidManager.class, "cuboid manager");

		private Feature f;
		private Class<? extends AntiShareManager> m;
		private String name;

		private Manager(Feature f, Class<? extends AntiShareManager> m, String name){
			this.f = f;
			this.m = m;
			this.name = name;
		}

		public String getName(){
			return name;
		}

		public Feature getFeature(){
			return f;
		}

		protected Class<? extends AntiShareManager> getManagerClass(){
			return m;
		}
	}

	private FeatureManager features;
	private Map<Manager, AntiShareManager> managers = new HashMap<Manager, AntiShareManager>();

	@Override
	public boolean load(){
		features = new FeatureManager();
		features.load();
		for(Manager s : Manager.values()){
			if(!features.isEnabled(s.getFeature())){
				if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
					plugin.getLogger().info("Feature not enabled: " + s.getName() + ", skipping...");
				}
				continue;
			}
			if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
				plugin.getLogger().info("Starting " + s.getName() + "...");
			}
			if(s == Manager.FEATURES){
				managers.put(s, features);
			}else{
				Class<? extends AntiShareManager> m = s.getManagerClass();
				if(m == null){
					continue;
				}
				try{
					AntiShareManager man = m.newInstance();
					managers.put(s, man);
				}catch(InstantiationException e){
					e.printStackTrace();
				}catch(IllegalAccessException e){
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	@Override
	public boolean save(){
		for(Manager s : managers.keySet()){
			if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
				plugin.getLogger().info("Saving " + s.getName() + "...");
			}
			managers.get(s).save();
			if(s.getFeature() == Feature.BLOCKS){
				BlockManager blocks = (BlockManager) managers.get(s);
				if(!plugin.getConfig().getBoolean("other.more-quiet-shutdown")){
					plugin.getLogger().info("Waiting for block manager to be done...");
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
							plugin.getLogger().info("[Block Manager] Percent Done: " + percent + "%");
							lastPercent = percent;
							if(percent >= 100){
								hit100 = true;
							}
						}
					}
				}
			}
		}
		managers.clear();
		return true;
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
