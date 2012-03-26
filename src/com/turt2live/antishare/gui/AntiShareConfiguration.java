package com.turt2live.antishare.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class AntiShareConfiguration {

	private File file;
	private boolean valid = false;

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

	public void load(){
		// We NEED the CraftBukkit jar before we can do anything else.
		boolean validJar = false;
		JOptionPane.showMessageDialog(null, "Hello!\nI have to ask you where your CraftBukkit JAR is, please lead me to it!");
		while (!validJar){
			JFileChooser open = new JFileChooser();
			open.setApproveButtonText("Open");
			open.setDialogTitle("Locate CraftBukkit");
			open.setCurrentDirectory(new File(System.getProperty("user.dir")));
			open.setFileSelectionMode(JFileChooser.FILES_ONLY);
			int result = open.showOpenDialog(null);
			if(result == JFileChooser.CANCEL_OPTION){
				int exit = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "Confirm Exit", JOptionPane.YES_NO_OPTION);
				if(exit == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}else{
				// Try to load
				// TODO: Lo
				boolean loaded = false;
				if(!loaded){
					JOptionPane.showMessageDialog(null, "Sorry! That JAR didn't work for me.", "Invalid JAR", JOptionPane.ERROR_MESSAGE);
				}else{
					validJar = true; // Break
				}
			}
		}
		// Continue on
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
