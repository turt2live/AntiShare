package com.turt2live.antishare.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Vector;

public class AntiShareConfiguration {

	private File file;
	private boolean valid = false;
	private Socket socket;
	@SuppressWarnings ("unused")
	private PrintStream toPlugin;
	@SuppressWarnings ("unused")
	private BufferedReader fromPlugin;

	public AntiShareConfiguration(File file){
		if(!file.exists()){
			valid = false;
			return;
		}
		this.file = file;
		analyze();
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

	public void load(String ip, int port){
		try{
			socket = new Socket(ip, port);
			toPlugin = new PrintStream(socket.getOutputStream());
			fromPlugin = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public boolean isConnected(){
		return socket != null && (socket != null ? socket.isConnected() : false);
	}

	private void analyze(){
		Vector<String> defaults = new Vector<String>();
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
}
