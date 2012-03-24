package com.turt2live.antishare.log;

import javax.swing.JTextArea;

public class LogEntry {

	private String rawLine;
	private String timestamp;
	private String message;
	private LogEntryType type;
	private String listName;

	public LogEntry(String line){
		type = LogEntryType.getType(line);
		this.rawLine = line;
		analyze();
	}

	public void analyze(){
		String[] parts = rawLine.split("]");
		timestamp = parts[0].replace("[", "").trim();
		message = parts[parts.length - 1].trim();
		listName = "[" + timestamp + "] " + message;
	}

	public String getRaw(){
		return rawLine;
	}

	public String getTimestamp(){
		return timestamp;
	}

	public String getMessage(){
		return message;
	}

	public LogEntryType getType(){
		return type;
	}

	public String getListName(){
		return listName;
	}

	public void displayTo(JTextArea area){
		area.setText(type.name());
	}
}
