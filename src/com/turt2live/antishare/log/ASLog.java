package com.turt2live.antishare.log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.logging.Logger;

import org.bukkit.ChatColor;

import com.turt2live.antishare.AntiShare;
import com.turt2live.antishare.debug.Bug;
import com.turt2live.antishare.debug.Debugger;

public class ASLog {

	private AntiShare plugin;
	private Logger log;

	private String logDir = "Logs";
	private String logFilename = "log.txt";
	private String logFullFilename = "log-all.txt";
	private String eventLogFilename = "events-log.txt";
	private String technicalLogFilename = "antishare-log.txt";

	private Vector<String> logLines = new Vector<String>();
	private Vector<String> logFullLines = new Vector<String>();
	private Vector<String> logTechnicalLines = new Vector<String>();
	private Vector<String> logEventLines = new Vector<String>();

	public ASLog(AntiShare plugin, Logger logger){
		this.plugin = plugin;
		this.log = logger;
		new File(plugin.getDataFolder(), logDir).mkdirs();
	}

	// Used to log minor events
	public void log(String line){
		logLines.add(line);
	}

	// Used to log ALL events (Notification)
	public void logForce(String line){
		logFullLines.add(line);
	}

	// Main console log, but for AntiShare exclusively
	public void logTechnical(String line){
		logTechnicalLines.add(line);
	}

	// Event log
	public void logEvent(String event){
		logEventLines.add(event);
	}

	// Main things used by other parts of the code
	public void info(String line){
		line = ChatColor.stripColor(line);
		log.info(line);
		logTechnical("[INFO] " + line);
	}

	public void warning(String line){
		line = ChatColor.stripColor(line);
		log.warning(line);
		logTechnical("[WARNING] " + line);
	}

	public void severe(String line){
		line = ChatColor.stripColor(line);
		log.severe(line);
		logTechnical("[SEVERE] " + line);
	}

	public void save(){
		// Save Log
		for(String line : logLines){
			line = ChatColor.stripColor(line);
			if(!line.startsWith("[AntiShare]")){
				line = "[AntiShare] " + line;
			}
			File logFile = new File(plugin.getDataFolder(), logDir + File.separator + logFilename);
			try{
				if(!logFile.exists()){
					logFile.getParentFile().mkdirs();
					try{
						logFile.createNewFile();
					}catch(Exception e){
						Bug bug = new Bug(e, "Log create error: " + logFile.getName(), this.getClass(), null);
						Debugger.sendBug(bug);
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
				out.write("[" + timestamp(false) + "] " + line + "\r\n");
				out.close();
			}catch(Exception e){
				Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
				Debugger.sendBug(bug);
			}
		}
		// Save Full Log
		for(String line : logFullLines){
			line = ChatColor.stripColor(line);
			if(!line.startsWith("[AntiShare]")){
				line = "[AntiShare] " + line;
			}
			File logFile = new File(plugin.getDataFolder(), logDir + File.separator + logFullFilename);
			try{
				if(!logFile.exists()){
					logFile.getParentFile().mkdirs();
					try{
						logFile.createNewFile();
					}catch(Exception e){
						Bug bug = new Bug(e, "Log create error: " + logFile.getName(), this.getClass(), null);
						Debugger.sendBug(bug);
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
				out.write("[" + timestamp(false) + "] " + line + "\r\n");
				out.close();
			}catch(Exception e){
				Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
				Debugger.sendBug(bug);
			}
		}
		// Save Technical Log
		for(String line : logTechnicalLines){
			line = ChatColor.stripColor(line);
			if(!line.startsWith("[AntiShare]")){
				line = "[AntiShare] " + line;
			}
			File logFile = new File(plugin.getDataFolder(), logDir + File.separator + technicalLogFilename);
			try{
				if(!logFile.exists()){
					logFile.getParentFile().mkdirs();
					try{
						logFile.createNewFile();
					}catch(Exception e){
						Bug bug = new Bug(e, "Log create error: " + logFile.getName(), this.getClass(), null);
						Debugger.sendBug(bug);
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
				out.write("[" + timestamp(false) + "] " + line + "\r\n");
				out.close();
			}catch(Exception e){
				Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
				Debugger.sendBug(bug);
			}
		}
		// Save Event Log
		for(String event : logEventLines){
			event = ChatColor.stripColor(event);
			File logFile = new File(plugin.getDataFolder(), logDir + File.separator + eventLogFilename);
			try{
				if(!logFile.exists()){
					logFile.getParentFile().mkdirs();
					try{
						logFile.createNewFile();
					}catch(Exception e){
						Bug bug = new Bug(e, "Log create error: " + logFile.getName(), this.getClass(), null);
						Debugger.sendBug(bug);
					}
				}
				BufferedWriter out = new BufferedWriter(new FileWriter(logFile, true));
				out.write("[" + timestamp(false) + "] " + event + "\r\n");
				out.close();
			}catch(Exception e){
				Bug bug = new Bug(e, "Log all save error", this.getClass(), null);
				Debugger.sendBug(bug);
			}
		}
	}

	// Timestamp
	public static String timestamp(boolean filemode){
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss MMM d");
		Date date = new Date();
		return (filemode) ? (dateFormat.format(date)).replaceAll(" ", "-").replaceAll("\\:", "").replaceAll("\\,", "") : dateFormat.format(date);
	}
}
