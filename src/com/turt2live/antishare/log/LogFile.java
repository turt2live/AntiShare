package com.turt2live.antishare.log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.JTextArea;

public class LogFile {

	public File file;
	private Vector<LogEntry> entries = new Vector<LogEntry>();
	private boolean invalid = false;

	public LogFile(File file){
		this.file = file;
		analyze();
	}

	public void analyze(){
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null){
				entries.add(new LogEntry(line));
			}
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean invalid(){
		return invalid;
	}

	public void displayTo(JList list){
		Vector<String> values = new Vector<String>();
		for(LogEntry entry : entries){
			values.add(entry.getListName());
		}
		list.setListData(values);
	}

	public void display(int index, JTextArea area){
		if(index >= 0){
			entries.get(index).displayTo(area);
		}
	}
}
