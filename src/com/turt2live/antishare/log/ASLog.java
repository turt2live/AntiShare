package com.turt2live.antishare.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.debug.Bug;

public class ASLog {

	private AntiShare plugin;
	private Logger log;

	public ASLog(AntiShare plugin, Logger logger){
		this.plugin = plugin;
		this.log = logger;
	}

	// Used to log minor events
	public void logEvent(String line){
		if(!line.startsWith("[AntiShare]")){
			line = "[AntiShare] " + line;
		}
		File logFile = new File(plugin.getDataFolder(), "log.txt");
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
			out.write("[" + timestamp(false) + "] " + line + "\r\n");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
			plugin.getDebugger().sendBug(bug);
		}
	}

	// Used to log ALL events (Notification)
	public void logEventForce(String line){
		if(!line.startsWith("[AntiShare]")){
			line = "[AntiShare] " + line;
		}
		File logFile = new File(plugin.getDataFolder(), "log-all.txt");
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
			out.write("[" + timestamp(false) + "] " + line + "\r\n");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
			plugin.getDebugger().sendBug(bug);
		}
	}

	// Main console log, but for AntiShare exclusively
	public void logTechnical(String line){
		if(!line.startsWith("[AntiShare]")){
			line = "[AntiShare] " + line;
		}
		File logFile = new File(plugin.getDataFolder(), "log-antishare.txt");
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
			out.write("[" + timestamp(false) + "] " + line + "\r\n");
			out.close();
		}catch(Exception e){
			e.printStackTrace();
			Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
			plugin.getDebugger().sendBug(bug);
		}
	}

	// Main things used by other parts of the code
	public void info(String line){
		log.info(line);
		logTechnical("[INFO] " + line);
	}

	public void warning(String line){
		log.warning(line);
		logTechnical("[WARNING] " + line);
	}

	public void severe(String line){
		log.severe(line);
		logTechnical("[SEVERE] " + line);
	}

	// Timestamp
	public static String timestamp(boolean filemode){
		DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Date date = new Date();
		return (filemode) ? (dateFormat.format(date)).replaceAll(" ", "-").replaceAll("\\:", "").replaceAll("\\,", "") : dateFormat.format(date);
	}
}
