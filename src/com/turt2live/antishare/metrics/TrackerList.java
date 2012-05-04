package com.turt2live.antishare.metrics;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

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
		COMMAND_LEGAL("Legal Actions", "Command");

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
