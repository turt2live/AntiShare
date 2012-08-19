package com.turt2live.antishare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import com.turt2live.antishare.AntiShare.LogType;

public class ASTimer {

	public interface ASTimerListener {
		public void timerStart(Class<?> clazz, String extraInformation, long id);

		public void timerStop(Class<?> clazz, String extraInformation, long id);

		public String timerReport(Class<?> clazz);
	}

	private class Report {
		private String line;
		private Class<?> clazz;

		public Report(String line, Class<?> clazz){
			this.line = line;
			this.clazz = clazz;
		}

		public String debugLine(){
			return "CLASS: " + clazz.getName() + " || DATA: " + line;
		}
	}

	private Map<String, ASTimerListener> listeners = new HashMap<String, ASTimerListener>();
	private List<Report> debug = new ArrayList<Report>();
	private long id = 0;

	public String addListener(ASTimerListener listener){
		String id = UUID.randomUUID().toString();
		listeners.put(id, listener);
		if(AntiShare.getInstance().getConfig().getBoolean("other.debug")){
			AntiShare.getInstance().getMessenger().log("[AS TIMER] Listener added: " + id, Level.INFO, LogType.BYPASS);
		}
		return id;
	}

	public void removeListener(String id){
		listeners.remove(id);
		if(AntiShare.getInstance().getConfig().getBoolean("other.debug")){
			AntiShare.getInstance().getMessenger().log("[AS TIMER] Listener removed: " + id, Level.INFO, LogType.BYPASS);
		}
	}

	public long start(Class<?> clazz, String extraInformation){
		if(!AntiShare.getInstance().getConfig().getBoolean("other.timers")){
			return -1;
		}
		this.id++;
		for(String lid : listeners.keySet()){
			ASTimerListener listener = listeners.get(lid);
			listener.timerStart(clazz, extraInformation, this.id);
		}
		if(AntiShare.getInstance().getConfig().getBoolean("other.debug")){
			AntiShare.getInstance().getMessenger().log("[AS TIMER] Timer Started (" + this.id + "): " + clazz.getSimpleName(), Level.INFO, LogType.BYPASS);
		}
		return this.id;
	}

	public void stop(Class<?> clazz, String extraInformation, long id){
		if(!AntiShare.getInstance().getConfig().getBoolean("other.timers")){
			return;
		}
		for(String lid : listeners.keySet()){
			ASTimerListener listener = listeners.get(lid);
			listener.timerStop(clazz, extraInformation, id);
			Report report = new Report(listener.timerReport(clazz), clazz);
			debug.add(report);
		}
		if(AntiShare.getInstance().getConfig().getBoolean("other.debug")){
			AntiShare.getInstance().getMessenger().log("[AS TIMER] Timer Stopped (" + id + "): " + clazz.getSimpleName(), Level.INFO, LogType.BYPASS);
		}
	}

	public void debug(BufferedWriter out) throws IOException{
		if(!AntiShare.getInstance().getConfig().getBoolean("other.timers")){
			out.write("Timers are not enabled.\r\n");
			return;
		}
		Report[] reports = new Report[debug.size()];
		reports = debug.toArray(reports);
		debug.clear();
		for(Report report : reports){
			out.write(report.debugLine());
			out.newLine();
		}
	}

}
