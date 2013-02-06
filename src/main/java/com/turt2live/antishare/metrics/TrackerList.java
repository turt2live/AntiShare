/*******************************************************************************
 * Copyright (c) 2012 turt2live (Travis Ralston).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 * 
 * Contributors:
 * turt2live (Travis Ralston) - initial API and implementation
 ******************************************************************************/
package com.turt2live.antishare.metrics;

import java.util.ArrayList;
import java.util.HashMap;

import com.turt2live.antishare.lang.Localization;
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
		SPECIAL("Unknown", "Unkown"),
		FEATURE_FINES_REWARDS("Features Used", "Fines/Rewards"),
		FEATURE_SIGNS("Features Used", "Signs"),
		FEATURE_REGIONS("Features Used", "Regions"),
		FEATURE_GM_BLOCKS("Features Used", "GameMode Blocks"),
		FEATURE_INVENTORIES("Features Used", "Inventories"),
		FEATURE_WORLD_SPLIT("Features Used", "World Split"),
		LOCALE("Locale (File Name)", Localization.getLocaleFileName());

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
	private HashMap<String, Graph> graphs = new HashMap<String, Graph>();

	/**
	 * Creates a new Tracker List
	 */
	public TrackerList(){
		for(TrackerType type : TrackerType.values()){
			switch (type){
			case FEATURE_FINES_REWARDS:
			case FEATURE_SIGNS:
			case FEATURE_REGIONS:
			case FEATURE_INVENTORIES:
			case FEATURE_GM_BLOCKS:
			case FEATURE_WORLD_SPLIT:
			case LOCALE:
				add(new NonMovingTracker(type.getName(), type));
				break;
			case SPECIAL:
				break;
			default:
				add(new Tracker(type.getName(), type));
				break;
			}
		}
	}

	/**
	 * Gets a tracker
	 * 
	 * @param type the type
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
			Graph graph = graphs.get(get(i).getGraphName());
			if(graph == null){
				graph = metrics.createGraph(get(i).getGraphName());
				graphs.put(get(i).getGraphName(), graph);
			}
			graph.addPlotter(get(i));
		}
	}

}
