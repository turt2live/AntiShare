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

	public static enum System{
		REGION(Feature.REGIONS, RegionManager.class, "region manager"),
		INVENTORY(Feature.INVENTORIES, InventoryManager.class, "inventory manager"),
		FEATURES(Feature.SELF, FeatureManager.class, "feature manager"),
		BLOCKS(Feature.BLOCKS, BlockManager.class, "block manager"),
		CUBOIDS(Feature.ALWAYS_ON, CuboidManager.class, "cuboid manager");

		private Feature f;
		private Class<? extends AntiShareManager> m;
		private String name;

		private System(Feature f, Class<? extends AntiShareManager> m, String name){
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
	private Map<System, AntiShareManager> managers = new HashMap<System, AntiShareManager>();

	@Override
	public boolean load(){
		features = new FeatureManager();
		features.load();
		for(System s : System.values()){
			if(!features.isEnabled(s.getFeature())){
				if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
					plugin.getLogger().info("Feature not enabled: " + s.getName() + ", skipping...");
				}
				continue;
			}
			if(!plugin.getConfig().getBoolean("other.more-quiet-startup")){
				plugin.getLogger().info("Starting " + s.getName() + "...");
			}
			if(s == System.FEATURES){
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
		for(System s : managers.keySet()){
			managers.get(s).save();
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
	public boolean isEnabled(System system){
		return managers.containsKey(system);
	}

	/**
	 * Gets the AntiShare Manager associated with a system
	 * 
	 * @param system the system
	 * @return the manager, if any, requested
	 */
	public AntiShareManager getManager(System system){
		return managers.get(system);
	}

}
