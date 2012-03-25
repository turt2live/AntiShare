package com.turt2live.antishare.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Vector;

public class AntiShareConfiguration {

	private File file;
	private boolean valid = false;
	private Vector<String> defaults = new Vector<String>();

	public AntiShareConfiguration(File file){
		if(!file.exists()){
			valid = false;
			return;
		}
		this.file = file;
		analyze();
	}

	private void analyze(){
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("resources/config.yml")));
			String line;
			while ((line = in.readLine()) != null){
				if(!line.startsWith("#")){
					defaults.add(line.split(":")[0].trim());
				}
			}
			in.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			int lines = 0;
			Vector<String> current = new Vector<String>();
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line;
			while ((line = in.readLine()) != null){
				if(!line.startsWith("#")){
					lines++;
					current.add(line.split(":")[0].trim());
				}
			}
			in.close();
			valid = lines == defaults.size() && Utils.containsCount(defaults, current);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void set(String path, Object value){

	}

	public Object get(String path){
		return null;
	}

	public boolean isValid(){
		return valid;
	}

	public File getFile(){
		return file;
	}
}
