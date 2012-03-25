package com.turt2live.antishare.log;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.swing.JLabel;

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
		if(rawLine.contains("[ILLEGAL]")){
			message = "[ILLEGAL] " + message;
		}else if(rawLine.contains("[LEGAL]")){
			message = "[LEGAL] " + message;
		}
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

	public void displayTo(final JLabel area){
		area.setText("Loading...");
		area.setBackground(Color.LIGHT_GRAY);
		new Thread(new Runnable(){
			@Override
			public void run(){
				StringBuilder text = new StringBuilder();
				try{
					URL statsURL = new URL("http://antishare.turt2live.com/log/?type=" + type.name());
					BufferedReader in = new BufferedReader(new InputStreamReader(statsURL.openConnection().getInputStream()));
					String line;
					while ((line = in.readLine()) != null){
						text.append(line);
					}
					in.close();
				}catch(Exception e){
					e.printStackTrace();
				}
				String contents = text.toString();
				String parts[] = message.split("Conflict: ");
				if(parts.length > 1){
					contents = contents.replace("-CONFLICT-", parts[parts.length - 1]);
				}
				if(rawLine.contains("[LEGAL]")){
					contents = contents.replace("-TYPE-", "<font color='green'>legal</font>");
				}else if(rawLine.contains("[ILLEGAL]")){
					contents = contents.replace("-TYPE-", "<font color='red'>illegal</font>");
				}
				contents = contents.replace("-ORIG-", rawLine);
				contents = contents.replace("-MESSAGE-", message);
				area.setText(contents);
				area.setBackground(Color.WHITE);
			}
		}).start();
	}

}
