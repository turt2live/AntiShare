package com.turt2live.antishare;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ASTimer {

	public abstract class ASTimerListener {
		public abstract void timerStart(Class<?> clazz, String extraInformation, long id);

		public abstract void timerStop(Class<?> clazz, String extraInformation, long id);

		public abstract String timerReport(Class<?> clazz);
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

	private List<ASTimerListener> listeners = new ArrayList<ASTimerListener>();
	private List<Report> debug = new ArrayList<Report>();
	private long id = 0;

	public long start(Class<?> clazz, String extraInformation){
		this.id++;
		for(ASTimerListener listener : listeners){
			listener.timerStart(clazz, extraInformation, this.id);
		}
		return this.id;
	}

	public void stop(Class<?> clazz, String extraInformation, long id){
		for(ASTimerListener listener : listeners){
			listener.timerStop(clazz, extraInformation, id);
			Report report = new Report(listener.timerReport(clazz), clazz);
			debug.add(report);
		}
	}

	public void debug(BufferedWriter out) throws IOException{
		Report[] reports = (Report[]) debug.toArray().clone();
		debug.clear();
		for(Report report : reports){
			out.write(report.debugLine());
			out.newLine();
		}
	}

}
