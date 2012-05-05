package com.turt2live.antishare.metrics;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.GameMode;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.metrics.Metrics.Graph;

/**
 * Tracker list
 * 
 * @author turt2live
 */
public class TrackerList extends ArrayList<Tracker> {

	/**
	 * Tracker type enum
	 * 
	 * @author turt2live
	 */
	public static enum TrackerType{
		BLOCK_BREAK_ILLEGAL("Illegal Actions", "Block Break"),
		BLOCK_BREAK_LEGAL("Legal Actions", "Block Break"),
		BLOCK_PLACE_ILLEGAL("Illegal Actions", "Block Place"),
		BLOCK_PLACE_LEGAL("Legal Actions", "Block Place"),
		DEATH_ILLEGAL("Illegal Actions", "Player Death"),
		DEATH_LEGAL("Legal Actions", "Player Death"),
		DROP_ILLEGAL("Illegal Actions", "Item Drop"),
		DROP_LEGAL("Legal Actions", "Item Drop"),
		PICKUP_ILLEGAL("Illegal Actions", "Item Pickup"),
		PICKUP_LEGAL("Legal Actions", "Item Pickup"),
		RIGHT_CLICK_ILLEGAL("Illegal Actions", "Right Click"),
		RIGHT_CLICK_LEGAL("Legal Actions", "Right Click"),
		USE_ILLEGAL("Illegal Actions", "Use"),
		USE_LEGAL("Legal Actions", "Use"),
		CREATIVE_BLOCK_ILLEGAL("Illegal Actions", "Creative Block"),
		CREATIVE_BLOCK_LEGAL("Legal Actions", "Creative Block"),
		SURVIVAL_BLOCK_ILLEGAL("Illegal Actions", "Survival Block"),
		SURVIVAL_BLOCK_LEGAL("Legal Actions", "Survival Block"),
		HIT_PLAYER_ILLEGAL("Illegal Actions", "Hit Player"),
		HIT_PLAYER_LEGAL("Legal Actions", "Hit Player"),
		HIT_MOB_ILLEGAL("Illegal Actions", "Hit Mob"),
		HIT_MOB_LEGAL("Legal Actions", "Hit Mob"),
		COMMAND_ILLEGAL("Illegal Actions", "Command"),
		COMMAND_LEGAL("Legal Actions", "Command"),
		CREATIVE_REGIONS("Region Types", "Creative"),
		SURVIVAL_REGIONS("Region Types", "Survival"),
		FLAT_FILE("Storage System", "Flat-File (YAML)"),
		SQL("Storage System", "SQL");

		private String graphname = "DEFAULT";
		private String name = "DEFAULT";

		private TrackerType(String graphname, String name){
			this.graphname = graphname;
			this.name = name;
		}

		/**
		 * Returns the graph name
		 * 
		 * @return the graph name
		 */
		public String getGraphName(){
			return graphname;
		}

		/**
		 * Gets the name of the tracker
		 * 
		 * @return the name
		 */
		public String getName(){
			return name;
		}
	}

	private static final long serialVersionUID = 8386186678486064850L;
	private ConcurrentHashMap<String, Graph> graphs = new ConcurrentHashMap<String, Graph>();

	/**
	 * Creates a new Tracker List
	 */
	public TrackerList(){
		for(TrackerType type : TrackerType.values()){
			add(new Tracker(type.getName(), type));
		}

		// Fix the SQL/Flat File graphs
		getTracker(TrackerType.SQL).increment(AntiShare.getInstance().useSQL() ? 1 : 0);
		getTracker(TrackerType.FLAT_FILE).increment(AntiShare.getInstance().useSQL() ? 0 : 1);

		// Fix region graphs
		remove(TrackerType.CREATIVE_REGIONS);
		remove(TrackerType.SURVIVAL_REGIONS);
		RegionTracker creative = new RegionTracker(TrackerType.CREATIVE_REGIONS.getName(), TrackerType.CREATIVE_REGIONS, GameMode.CREATIVE);
		RegionTracker survival = new RegionTracker(TrackerType.SURVIVAL_REGIONS.getName(), TrackerType.SURVIVAL_REGIONS, GameMode.SURVIVAL);
		add(creative);
		add(survival);
	}

	// Removes a graph type, must be called before metric gets the graph
	private void remove(TrackerType type){
		for(int i = 0; i < size(); i++){
			Tracker t = get(i);
			if(t.getType() == type){
				remove(t);
				break;
			}
		}
	}

	/**
	 * Gets a tracker
	 * 
	 * @return the tracker
	 */
	public Tracker getTracker(TrackerType type){
		for(int i = 0; i < size(); i++){
			Tracker t = get(i);
			if(t.getType() == type){
				return t;
			}
		}
		return null;
	}

	/**
	 * Adds all the trackers to the metrics
	 * 
	 * @param metrics the metrics
	 */
	public void addTo(Metrics metrics){
		for(int i = 0; i < size(); i++){
			Graph graph = graphs.get(get(i).getType().getGraphName());
			if(graph == null){
				graph = metrics.createGraph(get(i).getType().getGraphName());
				graphs.put(get(i).getType().getGraphName(), graph);
			}
			graph.addPlotter(get(i));
		}
	}

}
