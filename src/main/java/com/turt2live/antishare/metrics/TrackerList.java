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
		FEATURE_FINES("Features Used", "Fines/Rewards"),
		FEATURE_SIGNS("Features Used", "Signs");

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
			add(new Tracker(type.getName(), type));
		}
		remove(TrackerType.SPECIAL);
	}

	// Removes a graph type, must be called before metric gets the graph
	private void remove(TrackerType type){
		for(int i = 0; i < size(); i++){
			Tracker t = get(i);
			if(t.getType() == type){
				remove(i);
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
