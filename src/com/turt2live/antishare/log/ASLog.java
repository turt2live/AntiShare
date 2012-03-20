package com.turt2live.antishare.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.debug.Bug;

public class ASLog {

	private AntiShare plugin;
	private Logger log;

	private String logDir = "Logs";
	private String logFilename = "log.txt";
	private String logFullFilename = "log-all.txt";
	private String eventLogFilename = "events-log.txt";
	private String technicalLogFilename = "antishare-log.txt";

	public ASLog(AntiShare plugin, Logger logger){
		this.plugin = plugin;
		this.log = logger;
		new File(plugin.getDataFolder(), logDir).mkdirs();
	}

	// Used to log minor events
	public void log(String line){
		if(!line.startsWith("[AntiShare]")){
			line = "[AntiShare] " + line;
		}
		line = ChatColor.stripColor(line);
		File logFile = new File(plugin.getDataFolder(), logDir + File.separator + logFilename);
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
	public void logForce(String line){
		if(!line.startsWith("[AntiShare]")){
			line = "[AntiShare] " + line;
		}
		line = ChatColor.stripColor(line);
		File logFile = new File(plugin.getDataFolder(), logDir + File.separator + logFullFilename);
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
		line = ChatColor.stripColor(line);
		File logFile = new File(plugin.getDataFolder(), logDir + File.separator + technicalLogFilename);
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

	// Event log
	public void logEvent(String event){
		event = ChatColor.stripColor(event);
		File logFile = new File(plugin.getDataFolder(), logDir + File.separator + eventLogFilename);
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(logFile));
			out.write("[" + timestamp(false) + "] " + event + "\r\n");
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
		logTechnical("[" + timestamp(false) + "] [INFO] " + line);
	}

	public void warning(String line){
		log.warning(line);
		logTechnical("[" + timestamp(false) + "] [WARNING] " + line);
	}

	public void severe(String line){
		log.severe(line);
		logTechnical("[" + timestamp(false) + "] [SEVERE] " + line);
	}

	// Timestamp
	public static String timestamp(boolean filemode){
		DateFormat dateFormat = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss Z");
		Date date = new Date();
		return (filemode) ? (dateFormat.format(date)).replaceAll(" ", "-").replaceAll("\\:", "").replaceAll("\\,", "") : dateFormat.format(date);
	}
}
